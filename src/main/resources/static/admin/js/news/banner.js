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
            	$("#mainForm").attr("action", "bannerAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "bannerUpdateSubmit");
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
	
	function deleteBanner(message,banId) {
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
				bannerDelete(banId);
			} else {
				swal("已取消", "", "error");
			}
		});
	}
	
	function checkValue() {
		let checkValue = true;
		if ($("#newsName").val() == '0') {
			warning("請選擇最新消息");
			checkValue = false;
		} else if (checkImage() == false) {
			warning("請選擇圖片格式文件");
			checkValue = false;
		}
		
		if ($("#banId").val() == null || $("#banId").val() == "") {
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
			let newsImg = $("#newsImg");
			newsImg.attr('src', URL.createObjectURL(this.files[0]));
		}
	};
	
	$(".btn-close").click(function(){
		let banId = $(this).data('id');
		deleteBanner("確認刪除",banId);
	});
	
	function bannerDelete(banId){
		
		$.ajax({
			url : "bannerDelete",
			cache: false,
			async: false,
			type : "POST",
			dataType : 'text',
			data : {
				banId:banId
			},
			success : function(result) {
				let url = window.location.origin + "/holidayDessert/admin/news/" + result;
				window.location.href = url;
			},
			error : function(err, tst, xhr) {
				alert(JSON.stringify(err.responseText));
				alert('送單異常');
			}
		});
		
	}
	
});