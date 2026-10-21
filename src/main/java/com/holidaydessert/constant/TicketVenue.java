package com.holidaydessert.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 場地模板：新增活動時依場地自動產生分區（票種）、票價與座位數
 *
 * 座位數 = 排數 × 每排座位數，票數分配即為各分區座位數加總
 */
public enum TicketVenue {

	CUSTOM("自訂場地", List.of()),
	TAIPEI_DOME("台北大巨蛋", Templates.taipeiDome()),
	TAIPEI_ARENA("台北小巨蛋", Templates.taipeiArena());

	/**
	 * 分區模板
	 *
	 * @param area        區域群組（座位圖分層顯示用）
	 * @param name        分區名稱（即票種名稱）
	 * @param price       票價
	 * @param rows        排數
	 * @param seatsPerRow 每排座位數
	 * @param color       座位圖顏色
	 */
	public record Zone(String area, String name, int price, int rows, int seatsPerRow, String color) {
		public int quantity() {
			return rows * seatsPerRow;
		}
	}

	private final String venueName;
	private final List<Zone> zones;

	TicketVenue(String venueName, List<Zone> zones) {
		this.venueName = venueName;
		this.zones = zones;
	}

	public String getVenueName() {
		return venueName;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public int getCapacity() {
		return zones.stream().mapToInt(Zone::quantity).sum();
	}

	public static TicketVenue of(String code) {
		return Arrays.stream(values()).filter(v -> v.name().equals(code)).findFirst().orElse(CUSTOM);
	}

	// 放在巢狀類別，避免 enum 常數初始化時存取到尚未初始化的靜態欄位
	private static final class Templates {

		/**
		 * 台北大巨蛋（參考 1.webp：嘉年華 世界巡迴演唱會）
		 * B2特區 → B1看台(1xx) → 1F看台(2xx) → 2F看台(4xx) → 3F看台(5xx)，中間區塊票價較高、兩端較低
		 */
		static List<Zone> taipeiDome() {
			List<Zone> zones = new ArrayList<>();
			// B2 特區：A/B 兩排各 6 區，中間 6880、最外側 5880
			for (String row : List.of("A", "B")) {
				for (int i = 1; i <= 6; i++) {
					boolean side = i == 1 || i == 6;
					zones.add(new Zone("B2特區", row + i + "區", side ? 5880 : 6880, 20, 25, side ? "#1e88e5" : "#1a237e"));
				}
			}
			// B1 看台 101~124：105~120 正面 5880、兩端 4880
			for (int i = 101; i <= 124; i++) {
				boolean front = i >= 105 && i <= 120;
				zones.add(new Zone("B1看台", i + "區", front ? 5880 : 4880, 15, 30, front ? "#1e88e5" : "#c62828"));
			}
			// 1F 看台 201~224：205~220 正面 4880、兩端 3880
			for (int i = 201; i <= 224; i++) {
				boolean front = i >= 205 && i <= 220;
				zones.add(new Zone("1F看台", i + "區", front ? 4880 : 3880, 12, 30, front ? "#c62828" : "#f48fb1"));
			}
			// 2F 看台 401~424：405~420 正面 3280、兩端 2280
			for (int i = 401; i <= 424; i++) {
				boolean front = i >= 405 && i <= 420;
				zones.add(new Zone("2F看台", i + "區", front ? 3280 : 2280, 10, 30, front ? "#2e7d32" : "#66bb6a"));
			}
			// 3F 看台 501~512：1880
			for (int i = 501; i <= 512; i++) {
				zones.add(new Zone("3F看台", i + "區", 1880, 10, 30, "#fbc02d"));
			}
			return List.copyOf(zones);
		}

		/**
		 * 台北小巨蛋（參考 2.webp：蕭敬騰 世界巡迴演唱會）
		 * B1 特區 → 1F 紫區 / 紅區（左右對稱）→ 2F 黃2區 → 3F 黃3區（前中後段分價）
		 */
		static List<Zone> taipeiArena() {
			List<Zone> zones = new ArrayList<>();
			// B1 特區（搖滾區）
			zones.add(new Zone("B1特區", "特A區", 4200, 20, 30, "#4fc3f7"));
			zones.add(new Zone("B1特區", "特B區", 4200, 20, 30, "#4fc3f7"));
			zones.add(new Zone("B1特區", "特C區", 3800, 20, 30, "#81d4fa"));
			zones.add(new Zone("B1特區", "特D區", 3800, 20, 30, "#81d4fa"));
			// 1F 看台 紫區 / 紅區：1 字頭靠舞台 3300/3000，2 字頭外側 2800/2500
			for (String[] side : List.of(new String[] { "紫", "#6a1b9a" }, new String[] { "紅", "#d32f2f" })) {
				String area = "1F看台" + side[0] + "區";
				zones.add(new Zone(area, side[0] + "1B區", 3300, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "1C區", 3300, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "1D區", 3000, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "1E區", 3000, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "2B區", 2800, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "2C區", 2800, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "2D區", 2500, 12, 20, side[1]));
				zones.add(new Zone(area, side[0] + "2E區", 2500, 12, 20, side[1]));
			}
			// 2F 看台 黃2區：正中 2C 最貴，往兩側遞減
			zones.add(new Zone("2F看台黃2區", "黃2A區", 2800, 10, 25, "#f9a825"));
			zones.add(new Zone("2F看台黃2區", "黃2B區", 3300, 10, 25, "#f9a825"));
			zones.add(new Zone("2F看台黃2區", "黃2C區", 3800, 10, 25, "#f9a825"));
			zones.add(new Zone("2F看台黃2區", "黃2D區", 3300, 10, 25, "#f9a825"));
			zones.add(new Zone("2F看台黃2區", "黃2E區", 2800, 10, 25, "#f9a825"));
			// 3F 看台 黃3區：3A/3B/3I/3J 側邊 1500；3C~3H 依前、中、後段分 2000/1500/800
			zones.add(new Zone("3F看台黃3區", "黃3A區", 1500, 8, 20, "#fff176"));
			zones.add(new Zone("3F看台黃3區", "黃3B區", 1500, 8, 20, "#fff176"));
			for (String code : List.of("C", "D", "E", "F", "G", "H")) {
				zones.add(new Zone("3F看台黃3區", "黃3" + code + "前段", 2000, 6, 25, "#fdd835"));
				zones.add(new Zone("3F看台黃3區", "黃3" + code + "中段", 1500, 6, 25, "#fff176"));
				zones.add(new Zone("3F看台黃3區", "黃3" + code + "後段", 800, 4, 25, "#ffcc80"));
			}
			zones.add(new Zone("3F看台黃3區", "黃3I區", 1500, 8, 20, "#fff176"));
			zones.add(new Zone("3F看台黃3區", "黃3J區", 1500, 8, 20, "#fff176"));
			return List.copyOf(zones);
		}
	}

}
