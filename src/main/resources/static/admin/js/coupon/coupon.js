$(function() {
	$('#coupon-table').DataTable({
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
				data: "CP_PICTURE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if (row.CP_PICTURE != '' && row.CP_PICTURE != null) {
						return "<div class=\"preview\"><img class=\"couponImg\" src='" + row.CP_PICTURE + "' /></div>";
					} else {
						return "<div class=\"preview\"><img class=\"couponImg\"><span class=\"text\">預覽圖</span></div>"
					}
				}
			},
			{
				targets: [5],
				data: "CP_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.CP_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "CP_ID" },
			{ data: "CP_NAME" },
			{ data: "CP_DISCOUNT" },
			{ data: "STATUS" },
        	{
            	data: "CP_PICTURE",
            	render: function(data, type, row, meta) {
                	if (row.CP_PICTURE != '' && row.CP_PICTURE != null) {
                    	return "<div class=\"preview\"><img class=\"couponImg\" src='" + row.CP_PICTURE + "' /></div>";
                	} else {
                    	return "<div class=\"preview\"><img class=\"couponImg\"><span class=\"text\">預覽圖</span></div>";
                	}
            	}
        	},
        	{
            	data: "CP_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.CP_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#coupon-table").on("click", ".btn-update", function() {
		let cpId = $(this).data('id');
		let action = "/holidayDessert/admin/coupon/updateCoupon";
		let url = window.location.origin + action + "?cpId=" + cpId;
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
            	$("#mainForm").attr("action", "couponAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "couponUpdateSubmit");
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
		if (!$("#cpName").val()) {
			warning("請輸入優惠券名稱");
			checkValue = false;
		} else if (!$("#cpDiscount").val()) {
			warning("請輸入折抵金額");
			checkValue = false;
		} else if (checkImage() == false) {
			warning("請選擇圖片格式文件");
			checkValue = false;
		}
		
		if ($("#cpId").val() == null || $("#cpId").val() == "") {
			if ($("#imageFile").val() == "") {
				warning("請選擇圖片");
				return false;
			}
		}
		
		return checkValue;
	}
	
	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#cpStatus").val("1");
		} else {
			$("#cpStatus").val("0");
		}
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
			let couponImg = $("#couponImg");
			couponImg.attr('src', URL.createObjectURL(this.files[0]));
		}
	};
	
});