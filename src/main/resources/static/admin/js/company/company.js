$(function() {
	$('.basic-dataTable').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "companyTables",
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
				data: "COM_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COM_ID;
				}
			},
			{
				targets: [1],
				data: "COM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COM_NAME;
				}
			},
			{
				targets: [2],
				data: "COM_ADDRESS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COM_ADDRESS;
				}
			},
			{
				targets: [3],
				data: "COM_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COM_PHONE;
				}
			},
			{
				targets: [4],
				data: "COM_MEMO",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.COM_MEMO;
				}
			}
		],
		columns: [
			{
				data: "COM_ID",
				defaultContent: ""
			},
			{
				data: "COM_NAME",
				defaultContent: ""
			},
			{
				data: "COM_ADDRESS",
				defaultContent: ""
			},
			{
				data: "COM_PHONE",
				defaultContent: ""
			},
			{
				data: "COM_MEMO",
				defaultContent: ""
			}
		]
    });
});