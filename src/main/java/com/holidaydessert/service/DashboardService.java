package com.holidaydessert.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.TicketVenue;

import lombok.extern.slf4j.Slf4j;

/**
 * 後台首頁儀表板：彙整營收、訂單、會員、票務、商品內容、排班與系統狀態
 * 搶票測試建立的會員（帳號 tkt_sim_ 開頭）不列入會員統計
 */
@Slf4j
@Service
public class DashboardService {

	private static final String TEST_MEMBER_FILTER = "MEM_ACCOUNT NOT LIKE 'tkt\\_sim\\_%'";
	private static final int TREND_DAYS = 30;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private Environment environment;

	// =============================================
	// 營收 / 訂單
	// =============================================
	public Map<String, Object> revenue() {
		Map<String, Object> result = new LinkedHashMap<>(queryRevenue());
		long thisMonth = ((Number) result.get("THIS_MONTH")).longValue();
		long lastMonth = ((Number) result.get("LAST_MONTH")).longValue();
		// 與上月相比的成長率（上月為 0 時不計算）
		result.put("GROWTH", lastMonth > 0 ? Math.round((thisMonth - lastMonth) * 1000.0 / lastMonth) / 10.0 : null);
		return result;
	}

	private Map<String, Object> queryRevenue() {
		return jdbcTemplate.queryForMap(
				"SELECT " +
				"  IFNULL(SUM(CASE WHEN ORD_CREATE >= DATE_FORMAT(CURDATE(), '%Y-%m-01') THEN ORD_TOTAL END), 0) THIS_MONTH, " +
				"  IFNULL(SUM(CASE WHEN ORD_CREATE >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
				"                   AND ORD_CREATE <  DATE_FORMAT(CURDATE(), '%Y-%m-01') THEN ORD_TOTAL END), 0) LAST_MONTH, " +
				"  IFNULL(SUM(CASE WHEN ORD_CREATE >= CURDATE() THEN ORD_TOTAL END), 0) TODAY, " +
				"  COUNT(CASE WHEN ORD_CREATE >= DATE_FORMAT(CURDATE(), '%Y-%m-01') THEN 1 END) THIS_MONTH_ORDERS " +
				"FROM holiday_dessert.main_order WHERE ORD_STATUS <> 3");
	}

	/** 訂單狀態分布（0:未完成 1:已完成 2:配送中 3:取消） */
	public List<Map<String, Object>> orderStatus() {
		String[] names = { "未完成", "已完成", "配送中", "已取消" };
		Map<Integer, Long> counts = new LinkedHashMap<>();
		jdbcTemplate.queryForList("SELECT ORD_STATUS, COUNT(*) CNT FROM holiday_dessert.main_order GROUP BY ORD_STATUS")
				.forEach(row -> counts.put(((Number) row.get("ORD_STATUS")).intValue(), ((Number) row.get("CNT")).longValue()));
		List<Map<String, Object>> result = new ArrayList<>();
		for (int status = 0; status < names.length; status++) {
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("status", status);
			item.put("name", names[status]);
			item.put("count", counts.getOrDefault(status, 0L));
			result.add(item);
		}
		return result;
	}

	/** 近 30 天每日營收（沒有訂單的日期補 0） */
	public List<Map<String, Object>> revenueTrend() {
		Map<String, Map<String, Object>> byDate = new LinkedHashMap<>();
		LocalDate start = LocalDate.now().minusDays(TREND_DAYS - 1L);
		for (int i = 0; i < TREND_DAYS; i++) {
			Map<String, Object> day = new LinkedHashMap<>();
			day.put("date", start.plusDays(i).toString());
			day.put("revenue", 0L);
			day.put("orders", 0L);
			byDate.put(start.plusDays(i).toString(), day);
		}
		jdbcTemplate.queryForList(
				"SELECT DATE_FORMAT(ORD_CREATE, '%Y-%m-%d') D, SUM(ORD_TOTAL) REVENUE, COUNT(*) ORDERS " +
				"FROM holiday_dessert.main_order " +
				"WHERE ORD_STATUS <> 3 AND ORD_CREATE >= CURDATE() - INTERVAL " + (TREND_DAYS - 1) + " DAY " +
				"GROUP BY D")
				.forEach(row -> {
					Map<String, Object> day = byDate.get(String.valueOf(row.get("D")));
					if (day != null) {
						day.put("revenue", ((Number) row.get("REVENUE")).longValue());
						day.put("orders", ((Number) row.get("ORDERS")).longValue());
					}
				});
		return new ArrayList<>(byDate.values());
	}

	public List<Map<String, Object>> recentOrders() {
		return jdbcTemplate.queryForList(
				"SELECT o.ORD_ID, m.MEM_NAME, o.ORD_TOTAL, o.ORD_STATUS, " +
				"       CASE o.ORD_STATUS WHEN 0 THEN '未完成' WHEN 1 THEN '已完成' WHEN 2 THEN '配送中' WHEN 3 THEN '已取消' END STATUS, " +
				"       DATE_FORMAT(o.ORD_CREATE, '%m/%d %H:%i') ORD_CREATE " +
				"FROM holiday_dessert.main_order o " +
				"JOIN holiday_dessert.member m ON m.MEM_ID = o.MEM_ID " +
				"ORDER BY o.ORD_CREATE DESC, o.ORD_ID DESC LIMIT 6");
	}

	public List<Map<String, Object>> topProducts() {
		return jdbcTemplate.queryForList(
				"SELECT p.PD_ID, p.PD_NAME, SUM(d.ORDD_NUMBER) QTY, " +
				"       SUM(d.ORDD_NUMBER * CASE WHEN d.ORDD_DISCOUNT_PRICE > 0 THEN d.ORDD_DISCOUNT_PRICE ELSE d.ORDD_PRICE END) REVENUE " +
				"FROM holiday_dessert.order_detail d " +
				"JOIN holiday_dessert.main_order o ON o.ORD_ID = d.ORD_ID AND o.ORD_STATUS <> 3 " +
				"JOIN holiday_dessert.product p ON p.PD_ID = d.PD_ID " +
				"GROUP BY p.PD_ID, p.PD_NAME ORDER BY QTY DESC, REVENUE DESC LIMIT 5");
	}

	// =============================================
	// 會員 / 內容
	// =============================================
	public Map<String, Object> members() {
		Map<String, Object> result = new LinkedHashMap<>(jdbcTemplate.queryForMap(
				"SELECT COUNT(*) TOTAL, IFNULL(SUM(MEM_STATUS = 1), 0) ACTIVE " +
				"FROM holiday_dessert.member WHERE " + TEST_MEMBER_FILTER));
		result.put("TEST", jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM holiday_dessert.member WHERE NOT (" + TEST_MEMBER_FILTER + ")", Long.class));
		return result;
	}

	public Map<String, Object> content() {
		return jdbcTemplate.queryForMap(
				"SELECT " +
				"  (SELECT COUNT(*) FROM holiday_dessert.product WHERE PD_IS_DEL = 0 AND PD_STATUS = 1) PRODUCTS_ON, " +
				"  (SELECT COUNT(*) FROM holiday_dessert.product WHERE PD_IS_DEL = 0) PRODUCTS, " +
				"  (SELECT COUNT(*) FROM holiday_dessert.promotion WHERE PM_STATUS = 1 " +
				"      AND (PM_REGULARLY = 1 OR NOW() BETWEEN IFNULL(PM_START, NOW()) AND IFNULL(PM_END, NOW()))) PROMOTIONS_ON, " +
				"  (SELECT COUNT(*) FROM holiday_dessert.news WHERE NEWS_STATUS = 1 " +
				"      AND NOW() BETWEEN IFNULL(NEWS_START, NOW()) AND IFNULL(NEWS_END, NOW())) NEWS_ON, " +
				"  (SELECT COUNT(*) FROM holiday_dessert.coupon WHERE CP_STATUS = 1) COUPONS_ON, " +
				"  (SELECT COUNT(*) FROM holiday_dessert.employee WHERE EMP_STATUS = 1) EMPLOYEES");
	}

	/** 今天與明天的排班 */
	public List<Map<String, Object>> schedules() {
		return jdbcTemplate.queryForList(
				"SELECT f.title TITLE, f.allDay ALL_DAY, IFNULL(e.EMP_NAME, '未指定') EMP_NAME, " +
				"       DATE_FORMAT(f.start, '%m/%d %H:%i') START_TIME, DATE_FORMAT(f.end, '%m/%d %H:%i') END_TIME, " +
				"       CASE WHEN f.start < CURDATE() + INTERVAL 1 DAY THEN '今天' ELSE '明天' END DAY_LABEL " +
				"FROM holiday_dessert.fullcalendar f " +
				"LEFT JOIN holiday_dessert.employee e ON e.EMP_ID = f.EMP_ID " +
				"WHERE f.start < CURDATE() + INTERVAL 2 DAY AND IFNULL(f.end, f.start) >= CURDATE() " +
				"ORDER BY f.start LIMIT 8");
	}

	// =============================================
	// 票務
	// =============================================
	public Map<String, Object> ticketSummary() {
		return jdbcTemplate.queryForMap(
				"SELECT IFNULL(SUM(TICKET_ORD_QUANTITY), 0) SOLD, IFNULL(SUM(TICKET_ORD_AMOUNT), 0) AMOUNT, " +
				"       COUNT(CASE WHEN TICKET_ORD_STATUS = 0 THEN 1 END) UNPAID " +
				"FROM holiday_dessert.ticket_order WHERE TICKET_ORD_STATUS IN (0, 1)");
	}

	/** 票務活動：依活動時間由近到遠，含售出率與預熱狀態 */
	public List<Map<String, Object>> ticketEvents() {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				"SELECT t.TICKET_ID, t.TICKET_NAME, t.TICKET_VENUE, t.TICKET_STATUS, " +
				"       CASE t.TICKET_STATUS WHEN 0 THEN '尚未開始' WHEN 1 THEN '售票中' WHEN 2 THEN '活動結束' WHEN 3 THEN '活動取消' END STATUS, " +
				"       DATE_FORMAT(t.TICKET_START, '%Y-%m-%d %H:%i') TICKET_START, " +
				"       DATE_FORMAT(t.TICKET_SALE_END, '%m/%d %H:%i') TICKET_SALE_END, " +
				"       COUNT(tt.TYPE_ID) TYPE_COUNT, IFNULL(SUM(tt.TYPE_QUANTITY), 0) QUANTITY, IFNULL(SUM(tt.TYPE_REMAINING), 0) REMAINING " +
				"FROM holiday_dessert.ticket t " +
				"LEFT JOIN holiday_dessert.ticket_type tt ON tt.TICKET_ID = t.TICKET_ID " +
				"GROUP BY t.TICKET_ID " +
				"ORDER BY t.TICKET_STATUS = 1 DESC, t.TICKET_START DESC LIMIT 8");
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			Map<String, Object> item = new LinkedHashMap<>(row);
			Integer ticketId = ((Number) row.get("TICKET_ID")).intValue();
			long quantity = ((Number) row.get("QUANTITY")).longValue();
			long remaining = ((Number) row.get("REMAINING")).longValue();
			// 預熱中以 Redis 即時剩餘數為準（DB 由非同步寫入，可能稍有延遲）
			int warmedRemaining = ticketService.warmedRemaining(ticketId);
			int warmedTypes = ticketService.warmedTypeCount(ticketId);
			if (warmedTypes > 0 && warmedTypes == ((Number) row.get("TYPE_COUNT")).intValue()) {
				remaining = warmedRemaining;
			}
			item.put("VENUE_NAME", TicketVenue.of(String.valueOf(row.get("TICKET_VENUE"))).getVenueName());
			item.put("REMAINING", remaining);
			item.put("SOLD", quantity - remaining);
			item.put("SOLD_RATE", quantity > 0 ? Math.round((quantity - remaining) * 1000.0 / quantity) / 10.0 : 0);
			item.put("WARMED_TYPES", warmedTypes);
			result.add(item);
		}
		return result;
	}

	// =============================================
	// 系統狀態
	// =============================================
	public Map<String, Object> system() {
		Map<String, Object> result = new LinkedHashMap<>();
		try {
			jdbcTemplate.queryForObject("SELECT 1", Integer.class);
			result.put("db", true);
		} catch (Exception e) {
			log.warn("[Dashboard] DB 檢查失敗", e);
			result.put("db", false);
		}
		try {
			Properties memory = stringRedisTemplate.execute((RedisConnection connection) -> connection.serverCommands().info("memory"));
			result.put("redis", true);
			result.put("redisMemory", memory != null ? memory.getProperty("used_memory_human", "-") : "-");
			long warmed = 0;
			try (Cursor<String> cursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match("ticket:remaining:*").count(1000).build())) {
				while (cursor.hasNext()) {
					cursor.next();
					warmed++;
				}
			}
			result.put("warmedTypes", warmed);
		} catch (Exception e) {
			log.warn("[Dashboard] Redis 檢查失敗", e);
			result.put("redis", false);
			result.put("redisMemory", "-");
			result.put("warmedTypes", 0);
		}
		result.put("kafka", environment.acceptsProfiles(Profiles.of("kafka")));
		result.put("profiles", String.join(", ", environment.getActiveProfiles()));
		return result;
	}

	/** 最近的後台操作（只取員工的 POST 請求；不顯示請求內容，避免帶出密碼等參數） */
	public List<Map<String, Object>> recentActions() {
		return jdbcTemplate.queryForList(
				"SELECT LOG_CREATE_BY, LOG_METHOD, LOG_URL, LOG_HTTP_STATUS_CODE, DATE_FORMAT(LOG_CREATE_TIME, '%m/%d %H:%i') LOG_TIME " +
				"FROM holiday_dessert.edit_log " +
				"WHERE LOG_TYPE = 'employee' AND LOG_METHOD = 'POST' " +
				"ORDER BY LOG_ID DESC LIMIT 6");
	}

}
