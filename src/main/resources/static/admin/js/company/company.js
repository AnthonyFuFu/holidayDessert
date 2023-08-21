$(function() {
	$('#company-table').DataTable({
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
			},
			{
				targets: [5],
				data: "COM_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.COM_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "COM_ID" },
			{ data: "COM_NAME" },
			{ data: "COM_ADDRESS" },
			{ data: "COM_PHONE" },
			{ data: "COM_MEMO" },
        	{
            	data: "COM_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.COM_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#btn-add-company").on("click", function() {
		let action = "/holidayDessert/admin/company/addCompany";
		let url = window.location.origin + action;
		window.location.href = url;
	});
	
    $("#company-table").on("click", ".btn-update", function() {
		let comId = $(this).data('id');
		let action = "/holidayDessert/admin/company/updateCompany";
		let url = window.location.origin + action + "?comId=" + comId;
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
            	$("#mainForm").attr("action", "companyAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "companyUpdateSubmit");
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
		if (!$("#comName").val()) {
			warning("請輸入店鋪名稱");
			checkValue = false;
		} else if (!$("#comAddress").val()) {
			warning("請輸入店鋪地址");
			checkValue = false;
		} else if (!isTel($("#comPhone").val())) {
			warning("請輸入店鋪電話號碼");
			checkValue = false;
		} else if (!$("#comMemo").val()) {
			warning("請輸入店鋪詳細資訊");
			checkValue = false;
		}
		
		return checkValue;
	}
	
});

//電話格式 正規則
function isTel(phone) {
	const regex = /(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})/;
	return regex.test(phone);
}
