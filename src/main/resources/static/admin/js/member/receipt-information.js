$(function() {
	$('#receipt-information-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "receiptInformationTables",
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
				data: "RCP_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.RCP_ID;
				}
			},
			{
				targets: [1],
				data: "MEM_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_ID;
				}
			},
			{
				targets: [2],
				data: "RCP_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.RCP_NAME;
				}
			},
			{
				targets: [3],
				data: "RCP_CVS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.RCP_CVS;
				}
			},
			{
				targets: [4],
				data: "RCP_ADDRESS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.RCP_ADDRESS;
				}
			},
			{
				targets: [5],
				data: "RCP_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.RCP_PHONE;
				}
			}
		],
		columns: [
			{ data: "RCP_ID" },
			{ data: "MEM_ID",defaultContent: "" },
			{ data: "RCP_NAME",defaultContent: "" },
			{ data: "RCP_CVS",defaultContent: "" },
			{ data: "RCP_ADDRESS",defaultContent: "" },
			{ data: "RCP_PHONE",defaultContent: "" }
		]
    });
});