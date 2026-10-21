package com.holidaydessert.controller.admin;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.constant.TicketVenue;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.model.TicketType;
import com.holidaydessert.service.TicketOrderService;
import com.holidaydessert.service.TicketService;
import com.holidaydessert.service.TicketSimulateService;
import com.holidaydessert.service.TicketTypeService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/ticket")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class TicketManagement {

	private static final String LIST_PATH = "/holidayDessert/admin/ticket/list";

	@Autowired
	private TicketService ticketService;

	@Autowired
	private TicketTypeService ticketTypeService;

	@Autowired
	private TicketOrderService ticketOrderService;

	@Autowired
	private TicketSimulateService ticketSimulateService;

	// =============================================
	// 活動列表 / 訂單列表
	// =============================================
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) {
		return "admin/ticket/list";
	}

	@ResponseBody
	@GetMapping("/ticketTables")
	public Map<String, Object> ticketTables(@RequestParam Map<String, String> params) {
		return ticketService.list(params);
	}

	@ResponseBody
	@GetMapping("/orderTables")
	public Map<String, Object> orderTables(@RequestParam Map<String, String> params) {
		return ticketOrderService.list(params);
	}

	// =============================================
	// 活動新增 / 修改
	// =============================================
	@RequestMapping(value = "/addTicket", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTicket(@SessionAttribute Employee employeeSession, Model model) {
		Ticket ticket = new Ticket();
		ticket.setTicketVenue(TicketVenue.TAIPEI_ARENA.name());
		ticket.setTicketStatus(0);
		model.addAttribute("ticket", ticket);
		model.addAttribute("venueList", venueList());
		return "admin/ticket/ticketForm";
	}

	@RequestMapping(value = "/updateTicket", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateTicket(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId, Model model) {
		Ticket ticket = ticketService.getData(ticketId);
		if (ticket == null) {
			model.addAttribute("MESSAGE", "查無此活動");
			model.addAttribute("PATH", LIST_PATH);
			return "admin/toPath";
		}
		model.addAttribute("ticket", ticket);
		model.addAttribute("venueList", venueList());
		return "admin/ticket/ticketForm";
	}

	@PostMapping("/ticketAddSubmit")
	public String ticketAddSubmit(@SessionAttribute Employee employeeSession, @ModelAttribute Ticket ticket, Model model) {
		String error = ticketService.add(ticket);
		model.addAttribute("MESSAGE", error != null ? error : "資料新增成功，已依場地自動配置座位與票數");
		model.addAttribute("PATH", error != null ? "/holidayDessert/admin/ticket/addTicket" : LIST_PATH);
		return "admin/toPath";
	}

	@PostMapping("/ticketUpdateSubmit")
	public String ticketUpdateSubmit(@SessionAttribute Employee employeeSession, @ModelAttribute Ticket ticket, Model model) {
		String error = ticketService.update(ticket);
		model.addAttribute("MESSAGE", error != null ? error : "資料修改成功");
		model.addAttribute("PATH", error != null ? "/holidayDessert/admin/ticket/updateTicket?ticketId=" + ticket.getTicketId() : LIST_PATH);
		return "admin/toPath";
	}

	// =============================================
	// 座位配置 / 搶票測試
	// =============================================
	@RequestMapping(value = "/seatMap", method = { RequestMethod.GET, RequestMethod.POST })
	public String seatMap(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId, Model model) {
		Ticket ticket = ticketService.getData(ticketId);
		if (ticket == null) {
			model.addAttribute("MESSAGE", "查無此活動");
			model.addAttribute("PATH", LIST_PATH);
			return "admin/toPath";
		}
		model.addAttribute("ticket", ticket);
		model.addAttribute("venueName", TicketVenue.of(ticket.getTicketVenue()).getVenueName());
		model.addAttribute("maxUsers", TicketSimulateService.MAX_USERS);
		return "admin/ticket/seatMap";
	}

	@ResponseBody
	@GetMapping("/zones")
	public ApiReturnObject zones(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId) {
		return ApiReturnObject.success("查詢成功", ticketTypeService.listZones(ticketId));
	}

	@ResponseBody
	@GetMapping("/zoneSeats")
	public ApiReturnObject zoneSeats(@SessionAttribute Employee employeeSession, @RequestParam Integer typeId) {
		return ApiReturnObject.success("查詢成功", ticketTypeService.zoneSeats(typeId));
	}

	@ResponseBody
	@PostMapping("/zoneSave")
	public ApiReturnObject zoneSave(@SessionAttribute Employee employeeSession,
			@RequestParam Integer ticketId, @ModelAttribute TicketType type) {
		Ticket ticket = ticketService.getData(ticketId);
		if (ticket == null) return ApiReturnObject.notFound("查無此活動");

		String error = ticketTypeService.saveZone(type, ticket);
		if (error != null) return ApiReturnObject.badRequest(error);
		if (type.getTypeId() != null) {
			ticketService.evictType(type.getTypeId()); // 分區異動後清除快取，需重新預熱
		}
		ticketService.refreshQuantity(ticketId);
		return ApiReturnObject.success("儲存成功，請重新預熱後再進行搶票", null);
	}

	@ResponseBody
	@PostMapping("/zoneDelete")
	public ApiReturnObject zoneDelete(@SessionAttribute Employee employeeSession,
			@RequestParam Integer ticketId, @RequestParam Integer typeId) {
		String error = ticketTypeService.deleteZone(typeId);
		if (error != null) return ApiReturnObject.conflict(error);
		ticketService.evictType(typeId);
		ticketService.refreshQuantity(ticketId);
		return ApiReturnObject.success("刪除成功", null);
	}

	@ResponseBody
	@PostMapping("/init")
	public ApiReturnObject init(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId) {
		Ticket ticket = ticketService.getData(ticketId);
		if (ticket == null) return ApiReturnObject.notFound("查無此活動");
		String msg = ticketService.initTickets(ticket.getTicketName());
		return msg.contains("預熱完成") ? ApiReturnObject.success(msg, null) : ApiReturnObject.conflict(msg);
	}

	@ResponseBody
	@PostMapping("/close")
	public ApiReturnObject close(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId) {
		String msg = ticketService.closeWarmUp(ticketId, "管理員手動關閉");
		return msg.contains("查無此活動") ? ApiReturnObject.notFound(msg) : ApiReturnObject.success(msg, null);
	}

	@ResponseBody
	@PostMapping("/reset")
	public ApiReturnObject reset(@SessionAttribute Employee employeeSession, @RequestParam Integer ticketId) {
		try {
			return ApiReturnObject.success(ticketService.resetTicket(ticketId), null);
		} catch (Exception e) {
			log.error("[Admin] 重置失敗 ticketId={}", ticketId, e);
			return ApiReturnObject.serverError("重置失敗，請稍後再試");
		}
	}

	@ResponseBody
	@PostMapping("/simulate")
	public ApiReturnObject simulate(@SessionAttribute Employee employeeSession,
			@RequestParam Integer ticketId,
			@RequestParam(defaultValue = "0") Integer typeId,
			@RequestParam(defaultValue = "500") Integer users,
			@RequestParam(defaultValue = "0") Integer quantity) {
		try {
			Map<String, Object> result = ticketSimulateService.simulate(ticketId, typeId, users, quantity);
			if (result.containsKey("error")) return ApiReturnObject.badRequest(String.valueOf(result.get("error")));
			return ApiReturnObject.success("搶票測試完成", result);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return ApiReturnObject.serverError("搶票測試中斷");
		} catch (Exception e) {
			log.error("[Admin] 搶票測試失敗 ticketId={}", ticketId, e);
			return ApiReturnObject.serverError("搶票測試失敗：" + e.getMessage());
		}
	}

	private List<Map<String, Object>> venueList() {
		return Arrays.stream(TicketVenue.values()).map(venue -> {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("code", venue.name());
			map.put("name", venue.getVenueName());
			map.put("zoneCount", venue.getZones().size());
			map.put("capacity", venue.getCapacity());
			return map;
		}).toList();
	}

}
