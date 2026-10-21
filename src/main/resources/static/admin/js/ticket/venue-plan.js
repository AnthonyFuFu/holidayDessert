// 場地平面圖（SVG）：依場地實際位置畫出各分區，可依票價或售出率著色
// 分區以名稱對應幾何位置；自訂場地或改過名稱的分區無對應位置，由呼叫端另外列出
const VenuePlan = (function() {

	const SVG_NS = "http://www.w3.org/2000/svg";

	// 售出率分級（單一藍色色階，由淺到深）
	const SOLD_BINS = [
		{ min: 0, max: 0, color: "#e9eef6", label: "0%" },
		{ min: 0.0001, max: 0.2, color: "#b7d3f6", label: "1–20%" },
		{ min: 0.2, max: 0.4, color: "#86b6ef", label: "20–40%" },
		{ min: 0.4, max: 0.6, color: "#5598e7", label: "40–60%" },
		{ min: 0.6, max: 0.8, color: "#2a78d6", label: "60–80%" },
		{ min: 0.8, max: 0.9999, color: "#1c5cab", label: "80–99%" },
		{ min: 1, max: 1, color: "#0d366b", label: "售完" }
	];

	// ── 幾何工具 ──────────────────────────────────────
	// deg：從正上方順時針
	function polar(cx, cy, r, deg) {
		const a = deg * Math.PI / 180;
		return [cx + r * Math.sin(a), cy - r * Math.cos(a)];
	}

	function sector(cx, cy, r1, r2, a1, a2) {
		a1 += 0.5; a2 -= 0.5; // 分區間留縫
		const p1 = polar(cx, cy, r2, a1), p2 = polar(cx, cy, r2, a2);
		const p3 = polar(cx, cy, r1, a2), p4 = polar(cx, cy, r1, a1);
		const large = a2 - a1 > 180 ? 1 : 0;
		return {
			d: "M" + p1 + " A" + r2 + "," + r2 + " 0 " + large + " 1 " + p2 + " L" + p3 + " A" + r1 + "," + r1 + " 0 " + large + " 0 " + p4 + " Z",
			c: polar(cx, cy, (r1 + r2) / 2, (a1 + a2) / 2)
		};
	}

	function rect(x, y, w, h) {
		return { d: "M" + x + "," + y + " h" + w + " v" + h + " h" + (-w) + " Z", c: [x + w / 2, y + h / 2] };
	}

	// ── 台北小巨蛋（2.webp）：舞台在上，中央特區，左紫右紅，下方黃2、黃3 弧形看台 ──
	function taipeiArena() {
		const s = {};
		s["特B區"] = rect(302, 100, 93, 118); s["特A區"] = rect(405, 100, 93, 118);
		s["特D區"] = rect(302, 228, 93, 118); s["特C區"] = rect(405, 228, 93, 118);
		["B", "C", "D", "E"].forEach(function(k, i) {
			const y = 100 + i * 63;
			s["紫2" + k + "區"] = rect(150, y, 64, 58); s["紫1" + k + "區"] = rect(222, y, 70, 58);
			s["紅1" + k + "區"] = rect(508, y, 70, 58); s["紅2" + k + "區"] = rect(586, y, 64, 58);
		});
		const cx = 400, cy = 220, step = 110 / 6;
		["A", "B", "C", "D", "E"].forEach(function(k, i) {
			s["黃2" + k + "區"] = sector(cx, cy, 230, 282, 140 + i * 16, 156 + i * 16);
		});
		["C", "D", "E", "F", "G", "H"].forEach(function(k, i) {
			const a1 = 125 + i * step, a2 = a1 + step;
			s["黃3" + k + "前段"] = sector(cx, cy, 296, 326, a1, a2);
			s["黃3" + k + "中段"] = sector(cx, cy, 330, 360, a1, a2);
			s["黃3" + k + "後段"] = sector(cx, cy, 364, 390, a1, a2);
		});
		s["黃3A區"] = sector(cx, cy, 296, 360, 100, 112.5); s["黃3B區"] = sector(cx, cy, 296, 360, 112.5, 125);
		s["黃3I區"] = sector(cx, cy, 296, 360, 235, 247.5); s["黃3J區"] = sector(cx, cy, 296, 360, 247.5, 260);
		return {
			viewBox: "0 0 800 630",
			stage: [250, 22, 300, 50],
			labels: [["紫區", 220, 92], ["B1 特區", 400, 92], ["紅區", 580, 92], ["黃2區", 400, 468], ["黃3區", 400, 624]],
			shapes: s
		};
	}

	// ── 台北大巨蛋（1.webp）：舞台在上，B2 特區 A/B 兩排，外圍馬蹄形看台 1xx / 2xx / 4xx / 5xx ──
	function taipeiDome() {
		const s = {};
		for (let i = 1; i <= 6; i++) {
			s["A" + i + "區"] = rect(311 + (i - 1) * 30, 160, 28, 54);
			s["B" + i + "區"] = rect(311 + (i - 1) * 30, 220, 28, 54);
		}
		const cx = 400, cy = 330, span = 270 / 24;
		for (let i = 0; i < 24; i++) {
			const a1 = 45 + i * span, a2 = a1 + span;
			s[(101 + i) + "區"] = sector(cx, cy, 160, 205, a1, a2);
			s[(201 + i) + "區"] = sector(cx, cy, 212, 252, a1, a2);
			s[(401 + i) + "區"] = sector(cx, cy, 259, 294, a1, a2);
		}
		for (let i = 0; i < 12; i++) {
			s[(501 + i) + "區"] = sector(cx, cy, 301, 334, 90 + i * 15, 105 + i * 15);
		}
		return {
			viewBox: "0 86 800 596",
			stage: [320, 100, 160, 38],
			labels: [["B2 特區", 400, 152]],
			shapes: s
		};
	}

	const LAYOUTS = {
		TAIPEI_ARENA: taipeiArena(),
		TAIPEI_DOME: taipeiDome()
	};

	// ── 顏色 ──────────────────────────────────────────
	function soldBin(zone) {
		const rate = zone.typeQuantity > 0 ? zone.typeSold / zone.typeQuantity : 0;
		return SOLD_BINS.find(function(b) { return rate >= b.min && rate <= b.max; }) || SOLD_BINS[SOLD_BINS.length - 1];
	}

	function fillOf(zone, mode) {
		return mode === "sold" ? soldBin(zone).color : zone.typeColor;
	}

	// 依背景亮度決定文字黑白
	function inkFor(hex) {
		let c = (hex || "#9e9e9e").replace("#", "");
		if (c.length == 3) c = c.split("").map(function(x) { return x + x; }).join("");
		const r = parseInt(c.substr(0, 2), 16), g = parseInt(c.substr(2, 2), 16), b = parseInt(c.substr(4, 2), 16);
		return (r * 299 + g * 587 + b * 114) / 1000 > 150 ? "#212121" : "#ffffff";
	}

	function shortLabel(name) {
		return name.replace("區", "").replace("前段", "前").replace("中段", "中").replace("後段", "後");
	}

	function el(tag, attrs, text) {
		const node = document.createElementNS(SVG_NS, tag);
		Object.keys(attrs).forEach(function(k) { node.setAttribute(k, attrs[k]); });
		if (text != null) node.textContent = text;
		return node;
	}

	return {
		SOLD_BINS: SOLD_BINS,

		/** 此場地是否有平面圖 */
		supports: function(venue) {
			return !!LAYOUTS[venue];
		},

		/**
		 * 畫平面圖
		 * @return 沒有對應位置的分區（自訂或改名的分區）
		 */
		render: function(svg, venue, zones, mode) {
			const layout = LAYOUTS[venue];
			while (svg.firstChild) svg.removeChild(svg.firstChild);
			if (!layout) return zones;

			svg.setAttribute("viewBox", layout.viewBox);
			const st = layout.stage;
			svg.appendChild(el("rect", { x: st[0], y: st[1], width: st[2], height: st[3], rx: 6, "class": "plan-stage" }));
			svg.appendChild(el("text", { x: st[0] + st[2] / 2, y: st[1] + st[3] / 2, "class": "plan-stage-text" }, "STAGE"));
			layout.labels.forEach(function(l) {
				svg.appendChild(el("text", { x: l[1], y: l[2], "class": "plan-area-text" }, l[0]));
			});

			const unplaced = [];
			zones.forEach(function(zone) {
				const shape = layout.shapes[zone.typeName];
				if (!shape) {
					unplaced.push(zone);
					return;
				}
				const fill = fillOf(zone, mode);
				svg.appendChild(el("path", {
					d: shape.d,
					fill: fill,
					"class": "plan-zone" + (zone.typeRemaining <= 0 ? " sold-out" : ""),
					tabindex: 0,
					"data-id": zone.typeId,
					"data-price": zone.typePrice,
					"aria-label": zone.typeName + "，票價 " + zone.typePrice + "，已售 " + zone.typeSold + " / " + zone.typeQuantity
				}));
				svg.appendChild(el("text", { x: shape.c[0], y: shape.c[1], "class": "plan-zone-text", fill: inkFor(fill) }, shortLabel(zone.typeName)));
			});
			return unplaced;
		}
	};
})();
