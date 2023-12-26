$(function() {
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
			regularlyCheck();
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
			regularlyCheck();
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
            	$("#mainForm").attr("action", "promotionAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "promotionUpdateSubmit");
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
		if (!$("#pmName").val()) {
			warning("請輸入優惠活動名稱");
			checkValue = false;
		} else if (!$("#pmDescription").val()) {
			warning("請輸入活動描述");
			checkValue = false;
		} else if (!$("#pmDiscount").val()) {
			warning("請輸入折扣幅度");
			checkValue = false;
		} else if (!$("#pmStart").val()) {
			warning("請選擇開始時間");
			checkValue = false;
		} else if (!$("#pmEnd").val()) {
			warning("請選擇結束時間");
			checkValue = false;
		} else if($('#pmStart').val() > $('#pmEnd').val()){
			warning("開始時間不得大於結束時間");
			checkValue = false;
		}
		return checkValue;
	}

	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#pmStatus").val("1");
		} else {
			$("#pmStatus").val("0");
		}
	}
	
	function regularlyCheck(){
		if ($("#checkRegularly").prop("checked")) {
			$("#pmRegularly").val("1");
		} else {
			$("#pmRegularly").val("0");
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
	
    //noUISlider
    var pmDis = $("#pmDiscount").val() != '' ? $("#pmDiscount").val()*100 : 100;
    var sliderBasic = document.getElementById('nouislider');
    noUiSlider.create(sliderBasic, {
        start: [pmDis],
        connect: 'lower',
        step: 1,
        range: {
            'min': [0],
            'max': [100]
        }
    });
    getNoUISliderValue(sliderBasic, true);
    
	//Get noUISlider Value and write on
	function getNoUISliderValue(slider, percentage) {
		slider.noUiSlider.on('update', function() {
			var val = slider.noUiSlider.get();
			var pmDiscount;
			if (percentage) {
				val = parseInt(val);
				pmDiscount = val/100;
				val += '%';
			}
			$(slider).parent().find('span.js-nouislider-value').text(val);
			$(slider).parent().find('#pmDiscount').val(pmDiscount);
			
		});
	}
    
});

$('#pmStart').on('change', function() {
	setMinDate();
});

function setMinDate() {
	$('#pmEnd').bootstrapMaterialDatePicker('setMinDate', $('#pmStart').val());
}

