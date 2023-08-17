$(function() {
	$('#promotion-detail-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "promotionDetailTables",
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
				data: "PM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_NAME;
				}
			},
			{
				targets: [1],
				data: "PM_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '上架'
					} else {
						return '下架'
					}
				}
			},
			{
				targets: [2],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [3],
				data: "PD_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '上架'
					} else {
						return '下架'
					}
				}
			},
			{
				targets: [4],
				data: "PM_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DESCRIPTION;
				}
			},
			{
				targets: [5],
				data: "PM_DISCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DISCOUNT;
				}
			},
			{
				targets: [6],
				data: "PD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_PRICE;
				}
			},
			{
				targets: [7],
				data: "PMD_PD_DISCOUNT_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_PD_DISCOUNT_PRICE;
				}
			},
			{
				targets: [8],
				data: "PMD_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_START;
				}
			},
			{
				targets: [9],
				data: "PMD_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_END;
				}
			},
			{
				targets: [10],
				data: "PM_REGULARLY",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '例行'
					} else {
						return '非例行'
					}
				}
			}
		],
		columns: [
			{
				data: "PM_NAME",
				defaultContent: ""
			},
			{
				data: "PM_STATUS",
				defaultContent: ""
			},
			{
				data: "PD_NAME",
				defaultContent: ""
			},
			{
				data: "PD_STATUS",
				defaultContent: ""
			},
			{
				data: "PM_DESCRIPTION",
				defaultContent: ""
			},
			{
				data: "PM_DISCOUNT",
				defaultContent: ""
			},
			{
				data: "PD_PRICE",
				defaultContent: ""
			},
			{
				data: "PMD_PD_DISCOUNT_PRICE",
				defaultContent: ""
			},
			{
				data: "PMD_START",
				defaultContent: ""
			},
			{
				data: "PMD_END",
				defaultContent: ""
			},
			{
				data: "PM_REGULARLY",
				defaultContent: ""
			}
		]
    });
});