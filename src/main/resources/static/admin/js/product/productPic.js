$(function() {
    
    $("#product-table").on("click", ".btn-update", function() {
		let pdId = $(this).data('id');
		let action = "/holidayDessert/admin/product/updateProduct";
		let url = window.location.origin + action + "?pdId=" + pdId;
		window.location.href = url;
	});
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			isDeleteCheck();
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			isDeleteCheck();
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
            	$("#mainForm").attr("action", "productAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "productUpdateSubmit");
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
		if (checkImage() == false) {
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
	
	function isDeleteCheck(){
		if ($("#checkIsDel").prop("checked")) {
			$("#pdIsDel").val("0");
		} else {
			$("#pdIsDel").val("1");
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