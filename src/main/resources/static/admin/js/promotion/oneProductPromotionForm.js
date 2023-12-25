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
            	$("#mainForm").attr("action", "oneProductPromotionAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "oneProductPromotionUpdateSubmit");
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
		if (!$("#product").val()) {
			warning("請選擇商品");
			checkValue = false;
		} else if (!$("#promotion").val()) {
			warning("請選擇優惠活動");
			checkValue = false;
		} else if (!$("#pmdStart").val()) {
			warning("請選擇優惠活動開始時間");
			checkValue = false;
		} else if (!$("#pmdEnd").val()) {
			warning("請選擇優惠活動結束時間");
			checkValue = false;
		} else if (!$("#pmdPdDiscountPrice").val()) {
			warning("請輸入商品活動優惠價");
			checkValue = false;
		} else if ($("#pmdPdDiscountPrice").val() == 0) {
			warning("商品活動優惠價不得為0");
			checkValue = false;
		} else if ($("#pmdPdDiscountPrice").val() > $('#product option:selected').data('price')) {
			warning("特惠價不得大於原價");
			checkValue = false;
		}
		return checkValue;
	}

    autosize($('textarea.auto-growth'));
    $('.datepicker').bootstrapMaterialDatePicker({
        format: 'YYYY-MM-DD',
        clearButton: true,
        weekStart: 1,
        time: false
    });
    
});

$('#product').on('change', function() {
	let pdPrice = $('#product option:selected').data('price') ? $('#product option:selected').data('price') : 0;
	let pmdiscount = $('#promotion option:selected').data('pmdiscount') ? $('#promotion option:selected').data('pmdiscount') : 0;
	if($('#promotion').val() == '1'){
		$("#pmdPdDiscountPrice").val('0')
		$('#pmdPdDiscountPrice').prop('disabled', false);
	} else {
		$('#pmdPdDiscountPrice').val(pdPrice*pmdiscount);
		$('#pmdPdDiscountPrice').prop('disabled', true);
	}
});

$('#promotion').on('change', function() {
	let pdPrice = $('#product option:selected').data('price') ? $('#product option:selected').data('price') : 0;
	let pmdiscount = $('#promotion option:selected').data('pmdiscount') ? $('#promotion option:selected').data('pmdiscount') : 0;
	if($('#promotion').val() == '1'){
		$("#pmdPdDiscountPrice").val('0')
		$('#pmdPdDiscountPrice').prop('disabled', false);
	} else {
		$('#pmdPdDiscountPrice').val(pdPrice*pmdiscount);
		$('#pmdPdDiscountPrice').prop('disabled', true);
	}
});


