$(function() {
    
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
            	$("#mainForm").attr("action", "productPicAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "productPicUpdateSubmit");
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
	
	function deletePic(message,pdPicId) {
		swal({
			title: message,
			text: "被刪去的圖片無法復原 ! 確認刪除?",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "刪除",
			cancelButtonText: "取消",
			closeOnConfirm: false,
			closeOnCancel: false
		}, function(result) {
			if (result == true) {
				swal("已刪除", "", "success");
				productPicDelete(pdPicId);
			} else {
				swal("已取消", "", "error");
			}
		});
	}
	
	function checkValue() {
		let checkValue = true;
		if (!$("#pdPicSort").val()) {
			warning("請輸入圖片排序");
			checkValue = false;
		} else if ($("#pdName").val() == '0') {
			warning("請選擇商品");
			checkValue = false;
		} else if (checkImage() == false) {
			warning("請選擇圖片格式文件");
			checkValue = false;
		}
		
		if ($("#pdPicId").val() == null || $("#pdPicId").val() == "") {
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
	
	$("#imageFile").change(addImage);

	function addImage() {
		if (checkImage() == true){
			let pdImg = $("#pdImg");
			pdImg.attr('src', URL.createObjectURL(this.files[0]));
		}
	};
	
	$(".btn-close").click(function(){
		let pdPicId = $(this).data('id');
		deletePic("確認刪除",pdPicId);
	});
	
	function productPicDelete(pdPicId){
		
		$.ajax({
			url : "productPicDelete",
			cache: false,
			async: false,
			type : "POST",
			dataType : 'text',
			data : {
				pdPicId:pdPicId
			},
			success : function(result) {
				let url = window.location.origin + "/holidayDessert/admin/product/" + result;
				window.location.href = url;
			},
			error : function(err, tst, xhr) {
				alert(JSON.stringify(err.responseText));
				alert('送單異常');
			}
		});
		
	}
	
});