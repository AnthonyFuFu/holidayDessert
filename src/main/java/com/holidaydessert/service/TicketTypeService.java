package com.holidaydessert.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.constant.TicketVenue;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.model.TicketType;
import com.holidaydessert.repository.TicketOrderRepository;
import com.holidaydessert.repository.TicketTypeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 票種（分區）管理：場地模板產生分區、座位圖資料、座位池計算
 */
@Slf4j
@Service
public class TicketTypeService {

	public static final int DEFAULT_MAX_PER_PERSON = 4;

	@Autowired
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	private TicketOrderRepository ticketOrderRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/** 依場地模板建立分區（尚未儲存） */
	public List<TicketType> buildZones(Ticket ticket, TicketVenue venue) {
		List<TicketType> types = new ArrayList<>();
		int sort = 0;
		for (TicketVenue.Zone zone : venue.getZones()) {
			TicketType type = new TicketType();
			type.setTicket(ticket);
			type.setTypeArea(zone.area());
			type.setTypeName(zone.name());
			type.setTypePrice(zone.price());
			type.setTypeRows(zone.rows());
			type.setTypeSeatsPerRow(zone.seatsPerRow());
			type.setTypeQuantity(zone.quantity());
			type.setTypeRemaining(zone.quantity());
			type.setTypeColor(zone.color());
			type.setTypeMaxPerPerson(DEFAULT_MAX_PER_PERSON);
			type.setTypeSort(++sort);
			types.add(type);
		}
		return types;
	}

	public List<TicketType> saveAll(List<TicketType> types) {
		return ticketTypeRepository.saveAll(types);
	}

	public TicketType getData(Integer typeId) {
		return ticketTypeRepository.findById(typeId).orElse(null);
	}

	/**
	 * 座位圖分區清單：Redis 已預熱則顯示即時剩餘票數，否則顯示 DB 剩餘票數
	 */
	public List<Map<String, Object>> listZones(Integer ticketId) {
		List<Map<String, Object>> zones = new ArrayList<>();
		for (TicketType type : ticketTypeRepository.findByTicketId(ticketId)) {
			String redisRemaining = stringRedisTemplate.opsForValue().get(String.format(TicketConstant.TICKET_REMAINING_KEY, type.getTypeId()));
			int remaining = redisRemaining != null ? Integer.parseInt(redisRemaining) : type.getTypeRemaining();

			Map<String, Object> zone = new LinkedHashMap<>();
			zone.put("typeId", type.getTypeId());
			zone.put("typeArea", type.getTypeArea() != null ? type.getTypeArea() : "一般區");
			zone.put("typeName", type.getTypeName());
			zone.put("typePrice", type.getTypePrice());
			zone.put("typeRows", type.getTypeRows());
			zone.put("typeSeatsPerRow", type.getTypeSeatsPerRow());
			zone.put("typeQuantity", type.getTypeQuantity());
			zone.put("typeRemaining", remaining);
			zone.put("typeSold", type.getTypeQuantity() - remaining);
			zone.put("typeMaxPerPerson", type.getTypeMaxPerPerson());
			zone.put("typeColor", type.getTypeColor() != null ? type.getTypeColor() : "#9e9e9e");
			zone.put("typeSort", type.getTypeSort());
			zone.put("initialized", redisRemaining != null);
			zones.add(zone);
		}
		return zones;
	}

	/**
	 * 新增或修改分區；回傳錯誤訊息，成功回傳 null
	 * 已售出張數不可大於新的座位數，修改後剩餘票數 = 新座位數 - 已售出
	 */
	public String saveZone(TicketType form, Ticket ticket) {
		if (form.getTypeName() == null || form.getTypeName().isBlank()) return "請輸入分區名稱";
		if (form.getTypePrice() == null || form.getTypePrice() < 0) return "票價不可小於 0";
		if (form.getTypeMaxPerPerson() == null || form.getTypeMaxPerPerson() < 1) return "每人購買上限至少 1 張";

		boolean seatMap = form.getTypeRows() != null && form.getTypeRows() > 0;
		int quantity;
		if (seatMap) {
			if (form.getTypeSeatsPerRow() == null || form.getTypeSeatsPerRow() < 1) return "請輸入每排座位數";
			quantity = form.getTypeRows() * form.getTypeSeatsPerRow();
		} else {
			// 自由座：不配位，直接使用輸入的張數
			if (form.getTypeQuantity() == null || form.getTypeQuantity() < 1) return "請輸入可售張數";
			quantity = form.getTypeQuantity();
			form.setTypeRows(null);
			form.setTypeSeatsPerRow(null);
		}

		TicketType type;
		int sold = 0;
		if (form.getTypeId() != null) {
			type = getData(form.getTypeId());
			if (type == null || !type.getTicket().getTicketId().equals(ticket.getTicketId())) return "查無此分區";
			sold = type.getTypeQuantity() - type.getTypeRemaining();
			if (quantity < sold) return "座位數不可少於已售出張數（" + sold + "）";
			if (sold > 0 && seatMap && type.hasSeatMap()
					&& (form.getTypeRows() < type.getTypeRows() || form.getTypeSeatsPerRow() < type.getTypeSeatsPerRow())) {
				return "已有售出座位，排數 / 每排座位數只能增加";
			}
		} else {
			type = new TicketType();
			type.setTicket(ticket);
			type.setTypeSort(ticketTypeRepository.findMaxSort(ticket.getTicketId()) + 1);
		}

		type.setTypeArea(form.getTypeArea() == null || form.getTypeArea().isBlank() ? "一般區" : form.getTypeArea().trim());
		type.setTypeName(form.getTypeName().trim());
		type.setTypePrice(form.getTypePrice());
		type.setTypeRows(form.getTypeRows());
		type.setTypeSeatsPerRow(form.getTypeSeatsPerRow());
		type.setTypeQuantity(quantity);
		type.setTypeRemaining(quantity - sold);
		type.setTypeMaxPerPerson(form.getTypeMaxPerPerson());
		// 顏色會輸出到座位圖 style，只接受 #RRGGBB
		type.setTypeColor(form.getTypeColor() != null && form.getTypeColor().matches("^#[0-9a-fA-F]{6}$") ? form.getTypeColor() : "#9e9e9e");
		ticketTypeRepository.save(type);
		return null;
	}

	/** 刪除分區（已有訂單不可刪除）；回傳錯誤訊息，成功回傳 null */
	public String deleteZone(Integer typeId) {
		if (ticketOrderRepository.existsByTicketType_TypeId(typeId)) {
			return "此分區已有訂單，無法刪除";
		}
		ticketTypeRepository.deleteById(typeId);
		return null;
	}

	/**
	 * 單一分區座位明細（座位圖點擊分區時顯示）
	 * Redis 已預熱：已售 = 全部座位 - 座位池；未預熱：以 DB 訂單座位為準
	 */
	public Map<String, Object> zoneSeats(Integer typeId) {
		TicketType type = getData(typeId);
		Map<String, Object> result = new LinkedHashMap<>();
		if (type == null) return result;

		result.put("typeId", typeId);
		result.put("typeName", type.getTypeName());
		result.put("typeRows", type.getTypeRows());
		result.put("typeSeatsPerRow", type.getTypeSeatsPerRow());
		result.put("seatMap", type.hasSeatMap());
		if (!type.hasSeatMap()) return result;

		boolean initialized = Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(TicketConstant.TICKET_REMAINING_KEY, typeId)));
		Set<String> sold;
		if (initialized) {
			List<String> pool = stringRedisTemplate.opsForList().range(String.format(TicketConstant.TICKET_SEATS_KEY, typeId), 0, -1);
			Set<String> available = pool != null ? new HashSet<>(pool) : Set.of();
			sold = new HashSet<>(allSeats(type));
			sold.removeAll(available);
		} else {
			sold = soldSeatsFromDb(typeId);
		}
		result.put("initialized", initialized);
		result.put("soldSeats", sold);
		return result;
	}

	/** 可售座位（前排優先、同排由左至右），排除 DB 已售座位 */
	public List<String> availableSeats(TicketType type) {
		Set<String> sold = soldSeatsFromDb(type.getTypeId());
		return allSeats(type).stream().filter(seat -> !sold.contains(seat)).toList();
	}

	private List<String> allSeats(TicketType type) {
		List<String> seats = new ArrayList<>(type.getTypeRows() * type.getTypeSeatsPerRow());
		for (int row = 1; row <= type.getTypeRows(); row++) {
			for (int seat = 1; seat <= type.getTypeSeatsPerRow(); seat++) {
				seats.add(row + "-" + seat);
			}
		}
		return seats;
	}

	private Set<String> soldSeatsFromDb(Integer typeId) {
		Set<String> sold = new HashSet<>();
		for (String seats : ticketOrderRepository.findSoldSeats(typeId)) {
			sold.addAll(Arrays.asList(seats.split(",")));
		}
		return sold;
	}

	/** 「3-5,3-6」→「3排5號、3排6號」 */
	public static String formatSeats(String seats) {
		if (seats == null || seats.isBlank()) return "";
		return Arrays.stream(seats.split(","))
				.map(seat -> seat.split("-"))
				.map(parts -> parts.length == 2 ? parts[0] + "排" + parts[1] + "號" : String.join("-", parts))
				.reduce((a, b) -> a + "、" + b)
				.orElse("");
	}

}
