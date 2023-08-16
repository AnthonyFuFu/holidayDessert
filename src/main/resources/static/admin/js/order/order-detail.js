$(function() {
	$('#order-detail-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "orderDetailTables",
        aaSorting: [],
        oLanguage: {
			sProcessing: "處理中...",
			sLengthMenu: "顯示 _MENU_ 筆",
			sZeroRecords: "目前無資料",
			sEmptyTable: "目前無資料",
			sInfo: "目前顯示： 第 _START_ 筆 到 第 _END_ 筆， 共有 _TOTAL_ 筆",
			sInfoEmpty: "找尋不到相關資料",
			sInfoFiltered: "(已過濾 _MAX_ 筆)",
			sSearch: "搜尋 (訂單ID、收件人、訂單品項)：",
			oPaginate: {
				sFirst: "First",
				sPrevious: "上一頁",
				sNext: "下一頁",
				sLast: "Last"
			},
			select: {
				rows: {
					_: "，已選擇 %d 筆"
				}
			}
		},
		select: {
			style: 'multi'
		},
		columnDefs: [
			{
				targets: [0],
				data: "ORD_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_ID;
				}
			},
			{
				targets: [1],
				data: "ORD_RECIPIENT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_RECIPIENT;
				}
			},
			{
				targets: [2],
				data: "ORD_RECIPIENT_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_RECIPIENT_PHONE;
				}
			},
			{
				targets: [3],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [4],
				data: "ORDD_NUMBER",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORDD_NUMBER;
				}
			},
			{
				targets: [5],
				data: "ORDD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORDD_PRICE;
				}
			},
			{
				targets: [6],
				data: "ORDD_DISCOUNT_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORDD_DISCOUNT_PRICE;
				}
			},
			{
				targets: [7],
				data: "ORD_CREATE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_CREATE;
				}
			}
		],
		columns: [
			{
				data: "ORD_ID",
				defaultContent: ""
			},
			{
				data: "ORD_RECIPIENT",
				defaultContent: ""
			},
			{
				data: "ORD_RECIPIENT_PHONE",
				defaultContent: ""
			},
			{
				data: "PD_NAME",
				defaultContent: ""
			},
			{
				data: "ORDD_NUMBER",
				defaultContent: ""
			},
			{
				data: "ORDD_PRICE",
				defaultContent: ""
			},
			{
				data: "ORDD_DISCOUNT_PRICE",
				defaultContent: ""
			},
			{
				data: "ORD_CREATE",
				defaultContent: ""
			}
		]
    });
});