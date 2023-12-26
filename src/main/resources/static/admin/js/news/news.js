$(function() {
	$('#news-table').DataTable({
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
				data: "PM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_NAME;
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
			},
			{
				targets: [5],
				data: "NEWS_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_START;
				}
			},
			{
				targets: [6],
				data: "NEWS_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.NEWS_END;
				}
			},
			{
				targets: [7],
				data: "NEWS_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.NEWS_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "NEWS_ID" },
			{ data: "NEWS_NAME" },
			{ data: "NEWS_CONTENT" },
			{ data: "PM_NAME" },
			{ data: "STATUS" },
			{ data: "NEWS_START" },
			{ data: "NEWS_END" },
        	{
            	data: "NEWS_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.NEWS_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#news-table").on("click", ".btn-update", function() {
		let newsId = $(this).data('id');
		let action = "/holidayDessert/admin/news/updateNews";
		let url = window.location.origin + action + "?newsId=" + newsId;
		window.location.href = url;
	});
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
            success("修改完成");
        }
    });
    
	function success(message) {
   		swal({
        	title: message,
        	type: "success",
        	showCancelButton: false,
        	confirmButtonColor: "#3085d6",
        	confirmButtonText: "確定"
    	}, function(result) {
			if (result == true && message == "新增完成") {
            	$("#mainForm").attr("action", "newsAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "newsUpdateSubmit");
            	$("#mainForm").submit();
			}
    	})
	}
	
	function warning(message) {
    	swal({
        	title: message,
        	type: "warning",
        	confirmButtonColor: "#DD6B55",
        	confirmButtonText: "確定",
        	closeOnConfirm: false
    	});
	}

	function checkValue() {
		let checkValue = true;
		if (!$("#newsName").val()) {
			warning("請輸入最新消息名稱");
			checkValue = false;
		} else if (!$("#newsContent").val()) {
			warning("請輸入最新消息內容");
			checkValue = false;
		} else if (!$("#newsStart").val()) {
			warning("請選擇開始時間");
			checkValue = false;
		} else if (!$("#newsEnd").val()) {
			warning("請選擇結束時間");
			checkValue = false;
		} else if($('#newsStart').val() > $('#newsEnd').val()){
			warning("開始時間不得大於結束時間");
			checkValue = false;
		}
		return checkValue;
	}

	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#newsStatus").val("1");
		} else {
			$("#newsStatus").val("0");
		}
	}
	
    autosize($('textarea.auto-growth'));
    $('.datepicker').bootstrapMaterialDatePicker({
        format: 'YYYY-MM-DD',
        clearButton: true,
        weekStart: 1,
        time: false
    });
    
	setMinDate();
});

$('#newsStart').on('change', function() {
	setMinDate();
});

function setMinDate() {
	$('#newsEnd').bootstrapMaterialDatePicker('setMinDate', $('#newsStart').val());
}

