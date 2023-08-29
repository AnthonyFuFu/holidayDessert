$(function() {
	$('#cart-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "cartTables",
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
				data: "MEM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_NAME;
				}
			},
			{
				targets: [1],
				data: "MEM_ACCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_ACCOUNT;
				}
			},
			{
				targets: [2],
				data: "GENDER",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.GENDER;
				}
			},
			{
				targets: [3],
				data: "MEM_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_PHONE;
				}
			},
			{
				targets: [4],
				data: "MEM_EMAIL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_EMAIL;
				}
			},
			{
				targets: [5],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [6],
				data: "CART_PD_QUANTITY",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.CART_PD_QUANTITY;
				}
			}
		],
		columns: [
			{ data: "MEM_NAME",defaultContent: "" },
			{ data: "MEM_ACCOUNT",defaultContent: "" },
			{ data: "GENDER",defaultContent: "" },
			{ data: "MEM_PHONE",defaultContent: "" },
			{ data: "MEM_EMAIL",defaultContent: "" },
			{ data: "PD_NAME",defaultContent: "" },
			{ data: "CART_PD_QUANTITY",defaultContent: "" }
		]
    });
});