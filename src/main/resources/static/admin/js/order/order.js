$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "orderTables",
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
				data: "ORD_RECIPIENT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_RECIPIENT;
				}
			},
			{
				targets: [1],
				data: "MEM_GENDER",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_GENDER;
				}
			},
			{
				targets: [2],
				data: "MEM_EMAIL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_EMAIL;
				}
			},
			{
				targets: [3],
				data: "ORD_RECIPIENT_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_RECIPIENT_PHONE;
				}
			},
			{
				targets: [4],
				data: "ORD_ADDRESS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_ADDRESS;
				}
			},
			{
				targets: [5],
				data: "ORD_DELIVERY",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 0){
						return '超商取貨'
					} else if (data == 1){
						return '宅配'
					} else {
						return '取消'
					}
				}
			},
			{
				targets: [6],
				data: "ORD_SUBTOTAL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_SUBTOTAL;
				}
			},
			{
				targets: [7],
				data: "ORD_TOTAL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_TOTAL;
				}
			},
			{
				targets: [8],
				data: "ORD_DELIVERY_FEE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.ORD_DELIVERY_FEE;
				}
			},
			{
				targets: [9],
				data: "ORD_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 0){
						return '未完成'
					} else if (data == 1){
						return '已完成'
					} else if (data == 2){
						return '配送中'
					} else {
						return '取消'
					}
				}
			},
			{
				targets: [10],
				data: "ORD_PAYMENT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 0){
						return '貨到付款'
					} else if (data == 1){
						return '信用卡'
					} else {
						return '取消'
					}
				}
			},
			{
				targets: [11],
				data: "ORD_NOTE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == '' || data == null){
						return '無'
					} else {
						return row.ORD_NOTE
					}
				}
			},
			{
				targets: [12],
				data: "COUPON_USED",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COUPON_USED;
				}
			}
		],
		columns: [
			{
				data: "ORD_RECIPIENT",
				defaultContent: ""
			},
			{
				data: "MEM_GENDER",
				defaultContent: ""
			},
			{
				data: "MEM_EMAIL",
				defaultContent: ""
			},
			{
				data: "ORD_RECIPIENT_PHONE",
				defaultContent: ""
			},
			{
				data: "ORD_ADDRESS",
				defaultContent: ""
			},
			{
				data: "ORD_DELIVERY",
				defaultContent: ""
			},
			{
				data: "ORD_SUBTOTAL",
				defaultContent: ""
			},
			{
				data: "ORD_TOTAL",
				defaultContent: ""
			},
			{
				data: "ORD_DELIVERY_FEE",
				defaultContent: ""
			},
			{
				data: "ORD_STATUS",
				defaultContent: ""
			},
			{
				data: "ORD_PAYMENT",
				defaultContent: ""
			},
			{
				data: "ORD_NOTE",
				defaultContent: ""
			},
			{
				data: "COUPON_USED",
				defaultContent: ""
			}
		]
    });
});