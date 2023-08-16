$(function() {
	$('#employee-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "employeeTables",
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
				data: "EMP_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_ID;
				}
			},
			{
				targets: [1],
				data: "EMP_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_NAME;
				}
			},
			{
				targets: [2],
				data: "EMP_ACCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_ACCOUNT;
				}
			},
			{
				targets: [3],
				data: "EMP_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_PHONE;
				}
			},
			{
				targets: [4],
				data: "EMP_EMAIL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_EMAIL;
				}
			},
			{
				targets: [5],
				data: "DEPT_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.DEPT_NAME;
				}
			},
			{
				targets: [6],
				data: "DEPT_LOC",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.DEPT_LOC;
				}
			},
			{
				targets: [7],
				data: "EMP_LEVEL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 0){
						return '最高管理員'
					} else if (data == 1){
						return '一般管理員'
					} else {
						return '尚未填寫'
					}
				}
			},
			{
				targets: [8],
				data: "EMP_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '啟用'
					} else {
						return '停權'
					}
				}
			},
			{
				targets: [9],
				data: "EMP_HIREDATE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_HIREDATE;
				}
			},
			{
				targets: [10],
				data: "EMP_PICTURE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if (row.EMP_PICTURE != '' && row.EMP_PICTURE != null) {
						return "<img src='" + row.EMP_PICTURE + "' width='90px' height='110px' />";
					} else {
						return "<div class=\"preview\"><img class=\"mapImg\"><span class=\"text\">預覽圖</span></div>"
					}
				}
			}
		],
		columns: [
			{
				data: "EMP_ID",
				defaultContent: ""
			},
			{
				data: "EMP_NAME",
				defaultContent: ""
			},
			{
				data: "EMP_ACCOUNT",
				defaultContent: ""
			},
			{
				data: "EMP_PHONE",
				defaultContent: ""
			},
			{
				data: "EMP_EMAIL",
				defaultContent: ""
			},
			{
				data: "DEPT_NAME",
				defaultContent: ""
			},
			{
				data: "DEPT_LOC",
				defaultContent: ""
			},
			{
				data: "EMP_LEVEL",
				defaultContent: ""
			},
			{
				data: "EMP_STATUS",
				defaultContent: ""
			},
			{
				data: "EMP_HIREDATE",
				defaultContent: ""
			},
			{
				data: "EMP_PICTURE",
				defaultContent: ""
			}
		]
    });
});

