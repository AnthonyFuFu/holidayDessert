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
			},
			{
				targets: [5],
				data: "FUNC_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.FUNC_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "FUNC_ID" },
			{ data: "FUNC_NAME" },
			{ data: "FUNC_LINK" },
			{ data: "FUNC_STATUS" },
			{ data: "FUNC_ICON" },
        	{
            	data: "FUNC_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.FUNC_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#emp-function-table").on("click", ".btn-update", function() {
		let funcId = $(this).data('id');
		let action = "/holidayDessert/admin/employee/updateEmpFunction";
		let url = window.location.origin + action + "?funcId=" + funcId;
		window.location.href = url;
	});
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
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
            	$("#mainForm").attr("action", "empFunctionAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "empFunctionUpdateSubmit");
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
		if (!$("#funcName").val()) {
			warning("請輸入功能名稱");
			checkValue = false;
		} else if (!$("#funcLink").val()) {
			warning("請輸入連結");
			checkValue = false;
		} else if (!$("#funcStatus").val()) {
			warning("請輸入狀態");
			checkValue = false;
		} else if (!$("#funcIcon").val()) {
			warning("請輸入ICON");
			checkValue = false;
		}
		return checkValue;
	}
	
});

