$(function() {
	// =============================================
	// 活動清單
	// =============================================
	if ($('#ticket-table').length) {
		$('#ticket-table').DataTable({
			bAutoWidth: false,
			serverSide: true,
			processing: true,
			ordering: true,
			ajax: "ticketTables",
			aaSorting: [],
			oLanguage: {
				sProcessing: "處理中...",
				sLengthMenu: "顯示 _MENU_ 筆",
				sZeroRecords: "目前無資料",
				sEmptyTable: "目前無資料",
				sInfo: "目前顯示： 第 _START_ 筆 到 第 _END_ 筆， 共有 _TOTAL_ 筆",
				sInfoEmpty: "找尋不到相關資料",
				sInfoFiltered: "(已過濾 _MAX_ 筆)",
				sSearch: "搜尋：",
				oPaginate: {
					sFirst: "First",
					sPrevious: "上一頁",
					sNext: "下一頁",
					sLast: "Last"
				}
			},
			columns: [
				{ data: "TICKET_ID" },
				{ data: "TICKET_NAME", render: function(data) { return escapeHtml(data); } },
				{ data: "TICKET_VENUE", render: function(data, type, row) { return row.VENUE_NAME; } },
				{ data: "TICKET_START" },
				{ data: "TICKET_SALE_START" },
				{ data: "TYPE_COUNT", orderable: false },
				{ data: "TICKET_QUANTITY", render: function(data) { return Number(data).toLocaleString(); } },
				{
					data: "TICKET_REMAINING",
					render: function(data, type, row) {
						let total = Number(row.TICKET_QUANTITY);
						let rate = total > 0 ? Math.round((total - Number(data)) * 100 / total) : 0;
						return Number(data).toLocaleString() + " <small class='col-grey'>(已售 " + rate + "%)</small>";
					}
				},
				{ data: "STATUS" },
				{
					data: "WARMED_TYPES",
					orderable: false,
					render: function(data, type, row) {
						if (!data) return "<span class='label bg-grey'>未預熱</span>";
						return "<span class='label bg-green'>預熱中</span> <small class='col-grey'>" + data + " / " + row.TYPE_COUNT + "</small>";
					}
				},
				{
					data: "TICKET_ID",
					orderable: false,
					render: function(data, type, row) {
						return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' title='修改活動' data-id='" + row.TICKET_ID + "'><i class='material-icons'>edit</i></button> "
							+ " <button class='btn bg-teal btn-circle waves-effect waves-circle waves-float btn-seat' title='座位配置 / 搶票測試' data-id='" + row.TICKET_ID + "'><i class='material-icons'>event_seat</i></button> ";
					}
				}
			]
		});

		$("#ticket-table").on("click", ".btn-update", function() {
			window.location.href = "/holidayDessert/admin/ticket/updateTicket?ticketId=" + $(this).data('id');
		});

		$("#ticket-table").on("click", ".btn-seat", function() {
			window.location.href = "/holidayDessert/admin/ticket/seatMap?ticketId=" + $(this).data('id');
		});
	}

	// =============================================
	// 活動表單
	// =============================================
	if ($('#mainForm').length) {
		$('.datepicker').bootstrapMaterialDatePicker({
			format: 'YYYY-MM-DD HH:mm:00',
			clearButton: true,
			weekStart: 1,
			time: true
		});
		$('#ticketSaleStart').on('change', function() {
			$('#ticketSaleEnd').bootstrapMaterialDatePicker('setMinDate', $('#ticketSaleStart').val());
		});

		$('#ticketVenue').on('change', showVenueHint);
		showVenueHint();

		$('#add-submit').on('click', function() {
			if (checkValue()) {
				confirmSubmit("確定新增？", "將依場地自動建立分區與座位", "ticketAddSubmit");
			}
		});

		$('#update-submit').on('click', function() {
			if (checkValue()) {
				confirmSubmit("確定修改？", "", "ticketUpdateSubmit");
			}
		});
	}

	function showVenueHint() {
		let option = $('#ticketVenue option:selected');
		let zoneCount = Number(option.data('zone-count'));
		let hint = zoneCount > 0
			? "將自動建立 " + zoneCount + " 個分區，共 " + Number(option.data('capacity')).toLocaleString() + " 個座位（建立後可於座位配置頁調整）"
			: "自訂場地不會建立分區，請於建立後至座位配置頁新增分區";
		$('#venueHint').text(hint);
	}

	function confirmSubmit(title, text, action) {
		swal({
			title: title,
			text: text,
			type: "info",
			showCancelButton: true,
			confirmButtonColor: "#3085d6",
			confirmButtonText: "確定",
			cancelButtonText: "取消"
		}, function(result) {
			if (result == true) {
				$("#mainForm").attr("action", action);
				$("#mainForm").submit();
			}
		});
	}

	function warning(message) {
		swal({
			title: message,
			type: "warning",
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "確定"
		});
	}

	function checkValue() {
		if (!$("#ticketName").val().trim()) {
			warning("請輸入活動名稱");
			return false;
		}
		if (!$("#ticketStart").val()) {
			warning("請選擇活動時間");
			return false;
		}
		if (!$("#ticketSaleStart").val() || !$("#ticketSaleEnd").val()) {
			warning("請選擇售票期間");
			return false;
		}
		if ($('#ticketSaleStart').val() > $('#ticketSaleEnd').val()) {
			warning("售票開始時間不得大於結束時間");
			return false;
		}
		return true;
	}
});

function escapeHtml(value) {
	return $('<div>').text(value == null ? '' : value).html();
}
