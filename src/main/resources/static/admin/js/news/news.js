$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "newsTables",
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
				data: "NEWS_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_ID;
				}
			},
			{
				targets: [1],
				data: "NEWS_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_NAME;
				}
			},
			{
				targets: [2],
				data: "NEWS_CONTENT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_CONTENT;
				}
			},
			{
				targets: [3],
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			},
			{
				targets: [4],
				data: "NEWS_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_START;
				}
			},
			{
				targets: [5],
				data: "NEWS_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_END;
				}
			}
		],
		columns: [
			{
				data: "NEWS_ID",
				defaultContent: ""
			},
			{
				data: "NEWS_NAME",
				defaultContent: ""
			},
			{
				data: "NEWS_CONTENT",
				defaultContent: ""
			},
			{
				data: "STATUS",
				defaultContent: ""
			},
			{
				data: "NEWS_START",
				defaultContent: ""
			},
			{
				data: "NEWS_END",
				defaultContent: ""
			}
		]
    });
});