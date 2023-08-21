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
			},
			{
				targets: [11],
				data: "EMP_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.EMP_ID + "'><i class='material-icons'>edit</i></button> "
				}
			}
		],
		columns: [
			{ data: "EMP_ID" },
        	{ data: "EMP_NAME" },
        	{ data: "EMP_ACCOUNT" },
        	{ data: "EMP_PHONE" },
        	{ data: "EMP_EMAIL" },
        	{ data: "DEPT_NAME" },
        	{ data: "DEPT_LOC" },
        	{ data: "EMP_LEVEL" },
        	{ data: "EMP_STATUS" },
        	{ data: "EMP_HIREDATE" },
        	{
            	data: "EMP_PICTURE",
            	render: function(data, type, row, meta) {
                	if (row.EMP_PICTURE != '' && row.EMP_PICTURE != null) {
                    	return "<div class=\"preview\"><img class=\"empImg\" src='" + row.EMP_PICTURE + "' /></div>";
                	} else {
                    	return "<div class=\"preview\"><img class=\"empImg\"><span class=\"text\">預覽圖</span></div>";
                	}
            	}
        	},
        	{
            	data: "EMP_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.EMP_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#btn-add-employee").on("click", function() {
		let action = "/holidayDessert/admin/employee/addEmployee";
		let url = window.location.origin + action;
		window.location.href = url;
	});
	
    $("#employee-table").on("click", ".btn-update", function() {
		let empId = $(this).data('id');
		let action = "/holidayDessert/admin/employee/updateEmployee";
		let url = window.location.origin + action + "?empId=" + empId;
		window.location.href = url;
	});
	
	$("#employee-table").on("click", ".btn-delete", function() {
		if (confirm("確定離職嗎？")) {
			$("#id").val($(this).data('id'));
			$("#mainForm").attr("action", "resignEmployee");
			$("#mainForm").submit();
		}
	})
	
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
            	$("#mainForm").attr("action", "employeeAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "employeeUpdateSubmit");
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
		if (!$("#empAccount").val()) {
			warning("請輸入員工帳號");
			checkValue = false;
		} else if (!$("#empPassword").val()) {
			warning("請輸入員工密碼");
			checkValue = false;
		} else if ($("#department").val() == '0') {
			warning("請選擇部門");
			checkValue = false;
		} else if (!$("#empName").val()) {
			warning("員工姓名");
			checkValue = false;
		} else if (!$("#empPhone").val()) {
			warning("請輸入電話號碼");
			checkValue = false;
		} else if (!isTel($("#empPhone").val())) {
			warning("請輸入正確電話號碼");
			checkValue = false;
		} else if (!$("#empEmail").val()) {
			warning("請輸入電子信箱");
			checkValue = false;
		} else if (!isEmail($("#empEmail").val())) {
			warning("請輸入正確電子信箱");
			checkValue = false;
		} else if (!$("#empJob").val()) {
			warning("請輸入職稱");
			checkValue = false;
		} else if (!$("#empSalary").val()) {
			warning("請輸入薪水");
			checkValue = false;
		} else if (checkImage() == false) {
			warning("請選擇圖片格式文件");
			checkValue = false;
		}
		
		if ($("#empId").val() == null || $("#empId").val() == "") {
			if ($("#imageFile").val() == "") {
				warning("請選擇圖片");
				return false;
			}
		}
		
		return checkValue;
	}
	
	function checkImage() {
        let imageFile = $('#imageFile')[0];
        if (imageFile.files.length > 0) {
            let file = imageFile.files[0];
            let allowedTypes = ['image/jpg','image/jpeg','image/png','image/gif'];

            if (allowedTypes.indexOf(file.type) === -1) {
				warning("請選擇圖片格式文件 (JPG, JPEG, PNG 或 GIF)");
                return false;
            }
            return true;
        }
    };
	
	$("#imageFile").change(addimage);

	function addimage() {
		if (checkImage() == true){
			let empImg = $("#empImg");
			empImg.attr('src', URL.createObjectURL(this.files[0]));
		}
	};

});

//驗證連結網址是否有效
function checkURL(obj) {
	if ($.trim(obj.val()) != "" && obj.val().match(/http(s)?:////([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/
		) == null) {
	obj.focus();
		return "\n請輸入正確連結網址，EX：http://www.google.com";
	} else {
		return "";
	}
}
//RFC822 正規則
function isEmail(email){
	return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(email);	
}
//手機格式 正規則
function isCellphone(cellphone){
	return /^[09]{2}[0-9]{8}$/.test(cellphone);	
}
//電話格式 正規則
function isTel(phone) {
	const regex = /(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})/;
	return regex.test(phone);
}
//身份證驗證正規則
function checkTwID(id){
	//建立字母分數陣列(A~Z)
	let city = new Array(
		1,10,19,28,37,46,55,64,39,73,82, 2,11,
		20,48,29,38,47,56,65,74,83,21, 3,12,30
	)
	id = id.toUpperCase();
	// 使用「正規表達式」檢驗格式
	if (id.search(/^[A-Z](1|2)\d{8}$/i) == -1) {
		alert('基本格式錯誤');
		return false;
	} else {
	//將字串分割為陣列(IE必需這麼做才不會出錯)
	id = id.split('');
	//計算總分
	let total = city[id[0].charCodeAt(0)-65];
	for(let i=1; i<=8; i++){
		total += eval(id[i]) * (9 - i);
	}
	//補上檢查碼(最後一碼)
	total += eval(id[9]);
	//檢查比對碼(餘數應為0);
	return ((total%10 == 0 ));
	}
}
