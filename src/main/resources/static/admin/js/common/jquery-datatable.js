$(function () {
    $('.basic-dataTable').DataTable({
        responsive: true,
        bAutoWidth: false,
		serverSide: true,
		"processing": true,
		"ordering": true,
        "ajax": "memberTables",
        "aaSorting": [],
        "oLanguage": {
			"sProcessing": "處理中...",
			"sLengthMenu": "顯示 _MENU_ 筆",
			"sZeroRecords": "目前無資料",
			"sEmptyTable": "目前無資料",
			"sInfo": "目前顯示： 第 _START_ 筆 到 第 _END_ 筆， 共有 _TOTAL_ 筆",
			"sInfoEmpty": "找尋不到相關資料",
			"sInfoFiltered": "(已過濾 _MAX_ 筆)",
			"sSearch": "搜尋：",
			"oPaginate": {
				"sFirst": "First",
				"sPrevious": "上一頁",
				"sNext": "下一頁",
				"sLast": "Last"
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
		"columnDefs": [
			{
				"targets": [0],
				"data": "MEM_ID",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_NAME;
				}
			},
			{
				"targets": [1],
				"data": "MEM_NAME",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_NAME;
				}
			},
			{
				"targets": [2],
				"data": "MEM_ACCOUNT",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_ACCOUNT;
				}
			},
			{
				"targets": [3],
				"data": "MEM_PHONE",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_PHONE;
				}
			},
			{
				"targets": [4],
				"data": "MEM_EMAIL",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_EMAIL;
				}
			},
			{
				"targets": [5],
				"data": "MEM_ADDRESS",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_ADDRESS;
				}
			},
			{
				"targets": [6],
				"data": "MEM_BIRTHDAY",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.MEM_BIRTHDAY;
				}
			},
			{
				"targets": [7],
				"data": "GENDER",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return row.GENDER;
				}
			}
		],
		"columns": [
			{
				"data": null,
				"render": function(data, type, full, meta) {
					return meta.row + 1 + meta.settings._iDisplayStart;
				}
			},
			{
				"data": "MEM_ID",
				"defaultContent": "",
				"orderable": false,

			},
			{
				"data": "MEM_NAME",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "MEM_ACCOUNT",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "MEM_PHONE",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "MEM_EMAIL",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "MEM_ADDRESS",
				"defaultContent": ""
			},
			{
				"data": "MEM_BIRTHDAY",
				"defaultContent": ""
			},
			{
				"data": "GENDER",
				"defaultContent": ""
			},
		]
    });
});
