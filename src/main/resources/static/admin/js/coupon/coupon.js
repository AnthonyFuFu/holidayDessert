$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "couponTables",
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
				data: "CP_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.CP_ID;
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
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			},
			{
				targets: [4],
				data: "CP_PIC",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.CP_PIC;
				}
			}
		],
		columns: [
			{
				data: "CP_ID",
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
				data: "STATUS",
				defaultContent: ""
			},
			{
				data: "CP_PIC",
				defaultContent: ""
			}
		]
    });
});