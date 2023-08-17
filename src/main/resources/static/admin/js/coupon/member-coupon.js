$(function() {
	$('#member-coupon-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "memberCouponTables",
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
				data: "CP_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.CP_NAME;
				}
			},
			{
				targets: [2],
				data: "CP_DISCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.CP_DISCOUNT;
				}
			},
			{
				targets: [3],
				data: "MEM_CP_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_CP_START;
				}
			},
			{
				targets: [4],
				data: "MEM_CP_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_CP_END;
				}
			},
			{
				targets: [5],
				data: "MEM_CP_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '可用'
					} else {
						return '不可用'
					}
				}
			},
			{
				targets: [6],
				data: "MEM_CP_RECORD",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == null){
						return '未使用'
					} else {
						return data;
					}
				}
			}
		],
		columns: [
			{
				data: "MEM_NAME",
				defaultContent: ""
			},
			{
				data: "CP_NAME",
				defaultContent: ""
			},
			{
				data: "CP_DISCOUNT",
				defaultContent: ""
			},
			{
				data: "MEM_CP_START",
				defaultContent: ""
			},
			{
				data: "MEM_CP_END",
				defaultContent: ""
			},
			{
				data: "MEM_CP_STATUS",
				defaultContent: ""
			},
			{
				data: "MEM_CP_RECORD",
				defaultContent: ""
			}
		]
    });
});