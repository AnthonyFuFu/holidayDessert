$(function() {
	$('#department-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "departmentTables",
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
				data: "DEPT_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.DEPT_ID;
				}
			},
			{
				targets: [1],
				data: "DEPT_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.DEPT_NAME;
				}
			},
			{
				targets: [2],
				data: "DEPT_LOC",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.DEPT_LOC;
				}
			},
			{
				targets: [3],
				data: "DEPT_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.DEPT_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "DEPT_ID" },
			{ data: "DEPT_NAME" },
			{ data: "DEPT_LOC" },
        	{
            	data: "DEPT_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.DEPT_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#btn-add-department").on("click", function() {
		let action = "/holidayDessert/admin/company/addDepartment";
		let url = window.location.origin + action;
		window.location.href = url;
	});
	
    $("#department-table").on("click", ".btn-update", function() {
		let deptId = $(this).data('id');
		let action = "/holidayDessert/admin/company/updateDepartment";
		let url = window.location.origin + action + "?deptId=" + deptId;
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
            	$("#mainForm").attr("action", "departmentAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "departmentUpdateSubmit");
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
		if (!$("#deptName").val()) {
			warning("請輸入部門名稱");
			checkValue = false;
		} else if (!$("#deptLoc").val()) {
			warning("請輸入部門地址");
			checkValue = false;
		}
		return checkValue;
	}
	
});
