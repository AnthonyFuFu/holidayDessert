// 座位配置 / 搶票測試
// 場地版面：同一列的區域並排顯示（參考實際場地：小巨蛋紫區、特區、紅區左右並排）
const VENUE_LAYOUTS = {
	TAIPEI_ARENA: [["1F看台紫區", "B1特區", "1F看台紅區"], ["2F看台黃2區"], ["3F看台黃3區"]],
	TAIPEI_DOME: [["B2特區"], ["B1看台"], ["1F看台"], ["2F看台"], ["3F看台"]]
};

let zoneList = [];
let currentView = "card";   // card：分區卡片 / plan：場地平面圖
let colorMode = "price";    // price：依票價 / sold：依售出率
const VIEW_STORAGE_KEY = "ticket.seatMap.view";

$(function() {
	const ticketId = $('#ticketId').val();
	const venue = $('#ticketVenue').val();

	initViewSwitch();
	loadZones();

	$('#btn-refresh').on('click', function() {
		loadZones();
		reloadOrderTable();
	});

	// ── 預熱 ─────────────────────────────────────────
	$('#btn-init').on('click', function() {
		$.post("init", { ticketId: ticketId }, function(res) {
			notify(res.message, res.code == 200 ? "success" : "warning");
			loadZones();
		});
	});

	// ── 關閉預熱（停止搶票）──────────────────────────
	$('#btn-close').on('click', function() {
		swal({
			title: "確定關閉預熱？",
			text: "關閉後此活動將停止搶票，已成立的訂單不受影響；之後可再按「預熱至 Redis」重新開放",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "關閉預熱",
			cancelButtonText: "取消"
		}, function() {
			$.post("close", { ticketId: ticketId }, function(res) {
				notify(res.message, res.code == 200 ? "success" : "warning");
				loadZones();
			});
		});
	});

	// ── 重置測試資料 ─────────────────────────────────
	$('#btn-reset').on('click', function() {
		swal({
			title: "確定重置？",
			text: "將刪除此活動所有訂單、還原剩餘票數並清除 Redis 後重新預熱",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "重置",
			cancelButtonText: "取消",
			closeOnConfirm: false,
			showLoaderOnConfirm: true
		}, function() {
			$.post("reset", { ticketId: ticketId }, function(res) {
				swal({ title: res.message, type: res.code == 200 ? "success" : "error", confirmButtonText: "確定" });
				$('#simulate-result').hide();
				loadZones();
				reloadOrderTable();
			});
		});
	});

	// ── 搶票測試 ─────────────────────────────────────
	$('#btn-simulate').on('click', function() {
		let options = '<option value="0">全部分區（每人隨機）</option>';
		zoneList.forEach(function(zone) {
			options += '<option value="' + zone.typeId + '">' + escapeText(zone.typeArea + ' / ' + zone.typeName)
				+ '（$' + zone.typePrice + '，剩 ' + zone.typeRemaining + '）</option>';
		});
		$('#simZone').html(options);
		$('#simulateModal').modal('show');
	});

	$('#simulate-start').on('click', function() {
		let users = parseInt($('#simUsers').val(), 10);
		let max = parseInt($('#simUsers').attr('max'), 10);
		if (!users || users < 1 || users > max) {
			swal({ title: "人數需介於 1 ~ " + max, type: "warning", confirmButtonText: "確定" });
			return;
		}
		$('#simulateModal').modal('hide');
		swal({ title: "搶票中...", text: users + " 位會員同時開搶", showConfirmButton: false });

		$.post("simulate", {
			ticketId: ticketId,
			typeId: $('#simZone').val(),
			users: users,
			quantity: $('#simQuantity').val()
		}, function(res) {
			swal.close();
			if (res.code != 200) {
				swal({ title: res.message, type: "error", confirmButtonText: "確定" });
				return;
			}
			showSimulateResult(res.result);
			loadZones();
			// 訂單為非同步寫入，稍候再刷新
			setTimeout(reloadOrderTable, 1500);
		}).fail(function() {
			swal({ title: "搶票測試失敗", type: "error", confirmButtonText: "確定" });
		});
	});

	// ── 分區新增 / 修改 / 刪除 ────────────────────────
	$('#btn-add-zone').on('click', function() {
		openZoneModal(null);
	});

	$('#zone-table').on('click', '.btn-zone-edit', function() {
		openZoneModal(findZone($(this).data('id')));
	});

	$('#zone-table').on('click', '.btn-zone-delete', function() {
		let typeId = $(this).data('id');
		swal({
			title: "確定刪除此分區？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "刪除",
			cancelButtonText: "取消"
		}, function() {
			$.post("zoneDelete", { ticketId: ticketId, typeId: typeId }, function(res) {
				notify(res.message, res.code == 200 ? "success" : "warning");
				loadZones();
			});
		});
	});

	$('#zoneRows, #zoneSeats').on('input', syncZoneQuantity);

	$('#zone-save').on('click', function() {
		let data = $('#zoneForm').serializeArray();
		data.push({ name: "ticketId", value: ticketId });
		$.post("zoneSave", $.param(data), function(res) {
			if (res.code == 200) {
				$('#zoneModal').modal('hide');
				notify(res.message, "success");
				loadZones();
			} else {
				swal({ title: res.message, type: "warning", confirmButtonText: "確定" });
			}
		});
	});

	// ── 點擊分區查看座位 ──────────────────────────────
	$('#seat-map').on('click', '.ticket-zone', function() {
		showSeats($(this).data('id'));
	});

	// =============================================
	// functions
	// =============================================
	function loadZones() {
		$.get("zones", { ticketId: ticketId }, function(res) {
			zoneList = res.result || [];
			renderStats();
			renderLegend();
			renderSeatMap();
			renderZoneTable();
			renderPlan();
		});
	}

	// =============================================
	// 視圖切換：分區卡片 ⇄ 場地平面圖
	// =============================================
	function initViewSwitch() {
		if (!VenuePlan.supports(venue)) {
			$('#view-switch [data-view="plan"]').prop('disabled', true).attr('title', '自訂場地沒有平面圖');
		} else {
			try {
				currentView = localStorage.getItem(VIEW_STORAGE_KEY) === "plan" ? "plan" : "card";
			} catch (e) {
				currentView = "card";
			}
		}
		applyView();

		$('#view-switch').on('click', 'button', function() {
			currentView = $(this).data('view');
			try {
				localStorage.setItem(VIEW_STORAGE_KEY, currentView);
			} catch (e) {
				// 無法使用 localStorage 時不記住視圖
			}
			applyView();
		});

		$('#color-switch').on('click', 'button', function() {
			colorMode = $(this).data('mode');
			$('#color-switch button').removeClass('active');
			$(this).addClass('active');
			renderPlan();
		});
	}

	function applyView() {
		$('#view-switch button').removeClass('active');
		$('#view-switch [data-view="' + currentView + '"]').addClass('active');
		$('#card-view').toggle(currentView === "card");
		$('#plan-view').toggle(currentView === "plan");
		$('#color-switch').toggle(currentView === "plan");
		hidePlanTip();
	}

	// =============================================
	// 場地平面圖
	// =============================================
	function renderPlan() {
		if (!VenuePlan.supports(venue)) return;
		const svg = document.getElementById('venue-plan-svg');
		const unplaced = VenuePlan.render(svg, venue, zoneList, colorMode);

		// 圖例
		let legend;
		if (colorMode === "sold") {
			legend = VenuePlan.SOLD_BINS.map(function(bin) {
				return '<span class="ticket-legend-item"><i style="background:' + bin.color + '"></i>' + bin.label + '</span>';
			}).join('');
		} else {
			legend = priceTiers().map(function(tier) {
				return '<span class="ticket-legend-item"><i style="background:' + tier.color + '"></i>$' + tier.price.toLocaleString() + '</span>';
			}).join('');
		}
		$('#plan-legend').html(legend);

		// 新增或改名的分區沒有平面圖位置，列在下方仍可點擊查看
		if (unplaced.length) {
			$('#plan-unplaced').html('<span class="col-grey">未在平面圖上的分區：</span>' + unplaced.map(function(zone) {
				return '<a href="javascript:void(0)" class="plan-unplaced-zone" data-id="' + zone.typeId + '">'
					+ escapeText(zone.typeName) + '（剩 ' + zone.typeRemaining + '）</a>';
			}).join('')).show();
		} else {
			$('#plan-unplaced').hide().empty();
		}

		renderTierTable();
	}

	function priceTiers() {
		let tiers = {};
		zoneList.forEach(function(zone) {
			let tier = tiers[zone.typePrice] || (tiers[zone.typePrice] = { price: zone.typePrice, color: zone.typeColor, count: 0, quantity: 0, sold: 0 });
			tier.count++;
			tier.quantity += zone.typeQuantity;
			tier.sold += zone.typeSold;
		});
		return Object.keys(tiers).map(function(k) { return tiers[k]; }).sort(function(a, b) { return b.price - a.price; });
	}

	function renderTierTable() {
		let html = priceTiers().map(function(tier) {
			let rate = tier.quantity > 0 ? tier.sold * 100 / tier.quantity : 0;
			return '<tr class="tier-row" data-price="' + tier.price + '" tabindex="0">'
				+ '<td><i class="ticket-color-dot" style="background:' + tier.color + '"></i>$' + tier.price.toLocaleString() + '</td>'
				+ '<td class="text-right">' + tier.count + '</td>'
				+ '<td class="text-right">' + tier.quantity.toLocaleString() + '</td>'
				+ '<td class="text-right">' + tier.sold.toLocaleString() + '</td>'
				+ '<td><div class="ticket-tier-bar" title="' + rate.toFixed(1) + '%"><b style="width:' + rate + '%"></b></div></td>'
				+ '</tr>';
		}).join('');
		$('#tier-table tbody').html(html);
	}

	function highlightPrice(price) {
		$('.ticket-plan').toggleClass('dim', price != null);
		// jQuery 1.x 的 toggleClass 不支援 SVG 元素，改用 classList
		document.querySelectorAll('#venue-plan-svg .plan-zone').forEach(function(zone) {
			zone.classList.toggle('on', price != null && zone.getAttribute('data-price') == price);
		});
		$('#tier-table tr.tier-row').each(function() {
			$(this).toggleClass('on', $(this).data('price') == price);
		});
	}

	$('#tier-table').on('mouseenter focus', 'tr.tier-row', function() {
		highlightPrice($(this).data('price'));
	}).on('mouseleave blur', 'tr.tier-row', function() {
		highlightPrice(null);
	});

	$('#venue-plan-svg').on('click', '.plan-zone', function() {
		hidePlanTip();
		showSeats($(this).attr('data-id'));
	}).on('keydown', '.plan-zone', function(e) {
		if (e.key === "Enter" || e.key === " ") {
			e.preventDefault();
			showSeats($(this).attr('data-id'));
		}
	}).on('mousemove', '.plan-zone', function(e) {
		showPlanTip(findZone($(this).attr('data-id')), e.clientX, e.clientY);
	}).on('focus', '.plan-zone', function() {
		let box = this.getBoundingClientRect();
		showPlanTip(findZone($(this).attr('data-id')), box.right, box.top + box.height / 2);
	}).on('mouseleave blur', '.plan-zone', hidePlanTip);

	$('#plan-unplaced').on('click', '.plan-unplaced-zone', function() {
		showSeats($(this).data('id'));
	});

	function showPlanTip(zone, x, y) {
		if (!zone) return;
		let rate = zone.typeQuantity > 0 ? (zone.typeSold * 100 / zone.typeQuantity).toFixed(1) : '0.0';
		let tip = $('#plan-tip').html(
			'<div class="tip-title">' + escapeText(zone.typeArea) + '｜' + escapeText(zone.typeName) + '</div>'
			+ '<div class="tip-row"><span>票價</span><span>$' + zone.typePrice.toLocaleString() + '</span></div>'
			+ '<div class="tip-row"><span>已售 / 座位</span><span>' + zone.typeSold.toLocaleString() + ' / ' + zone.typeQuantity.toLocaleString() + '</span></div>'
			+ '<div class="tip-row"><span>剩餘</span><span>' + zone.typeRemaining.toLocaleString() + '</span></div>'
			+ '<div class="tip-row"><span>售出率</span><span>' + rate + '%</span></div>'
		).show();
		let width = tip.outerWidth(), height = tip.outerHeight();
		tip.css({
			left: Math.min(window.innerWidth - width - 8, x + 14),
			top: Math.max(8, y - height - 12)
		});
	}

	function hidePlanTip() {
		$('#plan-tip').hide();
	}

	function renderStats() {
		let total = 0, remaining = 0, initialized = zoneList.length > 0;
		zoneList.forEach(function(zone) {
			total += zone.typeQuantity;
			remaining += zone.typeRemaining;
			initialized = initialized && zone.initialized;
		});
		let sold = total - remaining;
		$('#stat-total').text(total.toLocaleString());
		$('#stat-sold').text(sold.toLocaleString());
		$('#stat-remaining').text(remaining.toLocaleString());
		$('#stat-rate').text((total > 0 ? Math.round(sold * 1000 / total) / 10 : 0) + "%");
		$('#stat-init').text(initialized ? "已預熱" : "未預熱").toggleClass('ready', initialized);
	}

	function renderLegend() {
		let prices = {};
		zoneList.forEach(function(zone) {
			if (!prices[zone.typePrice]) prices[zone.typePrice] = zone.typeColor;
		});
		let html = Object.keys(prices).map(Number).sort(function(a, b) { return b - a; }).map(function(price) {
			return '<span class="ticket-legend-item"><i style="background:' + prices[price] + '"></i>$' + price.toLocaleString() + '</span>';
		}).join('');
		$('#price-legend').html(html);
	}

	function renderSeatMap() {
		if (zoneList.length == 0) {
			$('#seat-map').html('<p class="text-center col-grey">尚無分區，請點選下方「新增分區」</p>');
			return;
		}
		// 依區域分組（保留排序）
		let areas = {};
		zoneList.forEach(function(zone) {
			(areas[zone.typeArea] = areas[zone.typeArea] || []).push(zone);
		});
		// 套用場地版面，未列入版面的區域各自一列
		let layout = (VENUE_LAYOUTS[venue] || []).map(function(row) {
			return row.filter(function(area) { return areas[area]; });
		}).filter(function(row) { return row.length > 0; });
		let placed = [].concat.apply([], layout);
		Object.keys(areas).forEach(function(area) {
			if (placed.indexOf(area) < 0) layout.push([area]);
		});

		let html = '<div class="ticket-stage">STAGE 舞台</div>';
		layout.forEach(function(row) {
			html += '<div class="ticket-area-row">';
			row.forEach(function(area) {
				let center = row.length > 1 && row.indexOf(area) == Math.floor(row.length / 2);
				html += '<div class="ticket-area' + (center ? ' center' : '') + '">'
					+ '<div class="ticket-area-title">' + escapeText(area) + '</div>'
					+ '<div class="ticket-zones">' + areas[area].map(zoneBlock).join('') + '</div>'
					+ '</div>';
			});
			html += '</div>';
		});
		$('#seat-map').html(html);
	}

	function zoneBlock(zone) {
		let soldPct = zone.typeQuantity > 0 ? Math.round(zone.typeSold * 100 / zone.typeQuantity) : 0;
		let soldOut = zone.typeRemaining <= 0;
		return '<div class="ticket-zone' + (soldOut ? ' sold-out' : '') + '" data-id="' + zone.typeId + '"'
			+ ' style="background:' + zone.typeColor + ';color:' + textColor(zone.typeColor) + '"'
			+ ' title="' + escapeText(zone.typeName) + '｜已售 ' + zone.typeSold + ' / ' + zone.typeQuantity + '">'
			+ '<div class="ticket-zone-name">' + escapeText(zone.typeName) + '</div>'
			+ '<div class="ticket-zone-price">$' + zone.typePrice.toLocaleString() + '</div>'
			+ '<div class="ticket-zone-remain">' + (soldOut ? '已售完' : '剩 ' + zone.typeRemaining) + '</div>'
			+ '<div class="ticket-zone-bar"><span style="width:' + soldPct + '%"></span></div>'
			+ '</div>';
	}

	function renderZoneTable() {
		let html = zoneList.map(function(zone) {
			let layout = zone.typeRows ? zone.typeRows + ' × ' + zone.typeSeatsPerRow : '<span class="col-grey">自由座</span>';
			return '<tr>'
				+ '<td>' + escapeText(zone.typeArea) + '</td>'
				+ '<td><i class="ticket-color-dot" style="background:' + zone.typeColor + '"></i>' + escapeText(zone.typeName) + '</td>'
				+ '<td>$' + zone.typePrice.toLocaleString() + '</td>'
				+ '<td>' + layout + '</td>'
				+ '<td>' + zone.typeQuantity.toLocaleString() + '</td>'
				+ '<td>' + zone.typeSold.toLocaleString() + '</td>'
				+ '<td>' + zone.typeRemaining.toLocaleString() + '</td>'
				+ '<td>' + zone.typeMaxPerPerson + '</td>'
				+ '<td>'
				+ '<button class="btn btn-default btn-circle waves-effect waves-circle waves-float btn-zone-edit" data-id="' + zone.typeId + '"><i class="material-icons">edit</i></button> '
				+ '<button class="btn btn-default btn-circle waves-effect waves-circle waves-float btn-zone-delete" data-id="' + zone.typeId + '"><i class="material-icons">delete</i></button>'
				+ '</td></tr>';
		}).join('');
		$('#zone-table tbody').html(html);
	}

	function openZoneModal(zone) {
		$('#zoneModalTitle').text(zone ? '修改分區 - ' + zone.typeName : '新增分區');
		$('#zoneTypeId').val(zone ? zone.typeId : '');
		$('#zoneArea').val(zone ? zone.typeArea : '');
		$('#zoneName').val(zone ? zone.typeName : '');
		$('#zonePrice').val(zone ? zone.typePrice : '');
		$('#zoneMax').val(zone ? zone.typeMaxPerPerson : 4);
		$('#zoneRows').val(zone ? zone.typeRows : 10);
		$('#zoneSeats').val(zone ? zone.typeSeatsPerRow : 20);
		$('#zoneQuantity').val(zone ? zone.typeQuantity : '');
		$('#zoneColor').val(zone ? zone.typeColor : '#9e9e9e');
		syncZoneQuantity();
		$('#zoneModal').modal('show');
	}

	// 有排數時座位數 = 排數 × 每排座位數；無排數為自由座，可直接輸入張數
	function syncZoneQuantity() {
		let rows = parseInt($('#zoneRows').val(), 10);
		let seats = parseInt($('#zoneSeats').val(), 10);
		let seatMap = rows > 0;
		$('#zoneSeats').prop('disabled', !seatMap);
		$('#zoneQuantity').prop('readonly', seatMap);
		if (seatMap && seats > 0) {
			$('#zoneQuantity').val(rows * seats);
		}
	}

	function showSeats(typeId) {
		$.get("zoneSeats", { typeId: typeId }, function(res) {
			let data = res.result;
			$('#seatModalTitle').text(data.typeName + ' 座位明細');
			if (!data.seatMap) {
				$('#seatModalHint').text('自由座分區，不配位');
				$('#seat-grid').html('');
				$('#seatModal').modal('show');
				return;
			}
			let sold = {};
			(data.soldSeats || []).forEach(function(seat) { sold[seat] = true; });
			$('#seatModalHint').text('已售 ' + Object.keys(sold).length + ' / ' + (data.typeRows * data.typeSeatsPerRow)
				+ (data.initialized ? '（Redis 即時）' : '（尚未預熱，依 DB 訂單）'));

			let html = '';
			for (let row = 1; row <= data.typeRows; row++) {
				html += '<div class="ticket-seat-row"><span class="ticket-row-label">' + row + '排</span>';
				for (let seat = 1; seat <= data.typeSeatsPerRow; seat++) {
					let key = row + '-' + seat;
					html += '<i class="ticket-seat' + (sold[key] ? ' sold' : '') + '" title="' + row + '排' + seat + '號"></i>';
				}
				html += '</div>';
			}
			$('#seat-grid').html(html);
			$('#seatModal').modal('show');
		});
	}

	function showSimulateResult(r) {
		let samples = (r.samples || []).map(function(s) { return '<li>' + escapeText(s) + '</li>'; }).join('');
		let html = '<div class="ticket-simulate-head"><b>搶票測試結果</b>'
			+ '<span>' + r.users + ' 人 / ' + r.zones + ' 個分區 / 耗時 ' + r.elapsedMs + ' ms / 約 ' + r.tps + ' req/s'
			+ (r.finished ? '' : '（逾時，部分請求未完成）') + '</span>'
			+ '<a href="javascript:void(0)" class="ticket-simulate-close">&times;</a></div>'
			+ '<div class="ticket-simulate-stats">'
			+ statBox('搶票成功', r.success, 'bg-green')
			+ statBox('售出張數', r.ticketsSold, 'bg-teal')
			+ statBox('已售罄', r.soldOut, 'bg-grey')
			+ statBox('超過個人上限', r.limitExceeded, 'bg-orange')
			+ statBox('系統繁忙(限流)', r.busy, 'bg-amber')
			+ statBox('其他失敗', r.failed, 'bg-red')
			+ '</div>'
			+ (samples ? '<div class="ticket-simulate-samples"><b>成功範例（電腦配位結果）</b><ul>' + samples + '</ul></div>' : '');
		$('#simulate-result').html(html).show();
	}

	$('#simulate-result').on('click', '.ticket-simulate-close', function() {
		$('#simulate-result').hide();
	});

	function statBox(label, value, color) {
		return '<div class="ticket-stat ' + color + '"><div class="value">' + Number(value).toLocaleString() + '</div><div class="label">' + label + '</div></div>';
	}

	function findZone(typeId) {
		return zoneList.find(function(zone) { return zone.typeId == typeId; });
	}

	function notify(message, type) {
		$.notify({ message: message }, {
			type: type == "success" ? "bg-green" : "bg-orange",
			placement: { from: "top", align: "right" },
			delay: 2500
		});
	}

	// 依背景亮度決定文字黑白
	function textColor(hex) {
		let c = (hex || '#9e9e9e').replace('#', '');
		if (c.length == 3) c = c.split('').map(function(x) { return x + x; }).join('');
		let r = parseInt(c.substr(0, 2), 16), g = parseInt(c.substr(2, 2), 16), b = parseInt(c.substr(4, 2), 16);
		return (r * 299 + g * 587 + b * 114) / 1000 > 150 ? '#212121' : '#ffffff';
	}

	function escapeText(value) {
		return $('<div>').text(value == null ? '' : value).html();
	}
});
