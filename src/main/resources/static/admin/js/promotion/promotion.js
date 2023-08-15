$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "promotionTables",
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
				data: "PM_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_ID;
				}
			},
			{
				targets: [1],
				data: "PM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_NAME;
				}
			},
			{
				targets: [2],
				data: "PM_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DESCRIPTION;
				}
			},
			{
				targets: [3],
				data: "PM_DISCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DISCOUNT;
				}
			},
			{
				targets: [4],
				data: "PM_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_START;
				}
			},
			{
				targets: [5],
				data: "PM_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_END;
				}
			},
			{
				targets: [6],
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			},
			{
				targets: [7],
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
				data: "PM_ID",
				defaultContent: ""
			},
			{
				data: "PM_NAME",
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
				data: "PM_START",
				defaultContent: ""
			},
			{
				data: "PM_END",
				defaultContent: ""
			},
			{
				data: "STATUS",
				defaultContent: ""
			},
			{
				data: "PM_REGULARLY",
				defaultContent: ""
			}
		]
    });
});