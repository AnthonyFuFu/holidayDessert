// 票務訂單 DataTable（活動清單頁顯示全部；座位配置頁依 data-ticket-id 篩選）
let orderTable;

$(function() {
	if (!$('#order-table').length) {
		return;
	}
	let ticketId = $('#order-table').data('ticket-id') || 0;

	orderTable = $('#order-table').DataTable({
		bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
		ajax: {
			url: "/holidayDessert/admin/ticket/orderTables",
			data: function(params) {
				params.ticketId = ticketId;
			}
		},
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
			{ data: "TICKET_ORD_ID" },
			{ data: "TICKET_NAME", render: function(data) { return orderEscape(data); } },
			{
				data: "TYPE_NAME",
				render: function(data, type, row) {
					return (row.TYPE_AREA ? orderEscape(row.TYPE_AREA) + " / " : "") + orderEscape(data);
				}
			},
			{ data: "MEM_NAME", render: function(data) { return orderEscape(data); } },
			{ data: "TICKET_ORD_QUANTITY" },
			{ data: "TICKET_ORD_AMOUNT", render: function(data) { return "$" + Number(data).toLocaleString(); } },
			{ data: "TICKET_ORD_SEATS", orderable: false, render: function(data) { return data ? formatSeats(data) : "<span class='col-grey'>自由座</span>"; } },
			{ data: "STATUS" },
			{ data: "TICKET_ORD_TIME" }
		]
	});
});

function reloadOrderTable() {
	if (orderTable) {
		orderTable.ajax.reload(null, false);
	}
}

// 「3-5,3-6」→「3排5號、3排6號」
function formatSeats(seats) {
	return seats.split(",").map(function(seat) {
		let parts = seat.split("-");
		return parts.length == 2 ? parts[0] + "排" + parts[1] + "號" : orderEscape(seat);
	}).join("、");
}

function orderEscape(value) {
	return $('<div>').text(value == null ? '' : value).html();
}
