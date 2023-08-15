$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "productTables",
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
				data: "PD_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_ID;
				}
			},
			{
				targets: [1],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [2],
				data: "PD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_PRICE;
				}
			},
			{
				targets: [3],
				data: "PD_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_DESCRIPTION;
				}
			},
			{
				targets: [4],
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			}
		],
		columns: [
			{
				data: "PD_ID",
				defaultContent: ""
			},
			{
				data: "PD_NAME",
				defaultContent: ""
			},
			{
				data: "PD_PRICE",
				defaultContent: ""
			},
			{
				data: "PD_DESCRIPTION",
				defaultContent: ""
			},
			{
				data: "STATUS",
				defaultContent: ""
			}
		]
    });
});