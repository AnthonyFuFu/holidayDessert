$(function() {
	$('#emp-function-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "empFunctionTables",
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
				data: "FUNC_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.FUNC_ID;
				}
			},
			{
				targets: [1],
				data: "FUNC_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.FUNC_NAME;
				}
			},
			{
				targets: [2],
				data: "FUNC_LINK",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.FUNC_LINK;
				}
			},
			{
				targets: [3],
				data: "FUNC_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '啟用'
					} else {
						return '停用'
					}
				}
			},
			{
				targets: [4],
				data: "FUNC_ICON",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return '<i class="material-icons">'+ data +'</i>'+ data;
				}
			}
		],
		columns: [
			{
				data: "FUNC_ID",
				defaultContent: ""
			},
			{
				data: "FUNC_NAME",
				defaultContent: ""
			},
			{
				data: "FUNC_LINK",
				defaultContent: ""
			},
			{
				data: "FUNC_STATUS",
				defaultContent: ""
			},
			{
				data: "FUNC_ICON",
				defaultContent: ""
			}
		]
    });
});

