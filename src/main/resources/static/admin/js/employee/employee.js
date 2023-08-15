$(function() {
	$('.basic-dataTable').DataTable({
//        responsive: true,
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
				data: "EMP_LEVEL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_LEVEL;
				}
			},
			{
				targets: [6],
				data: "EMP_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_STATUS;
				}
			},
			{
				targets: [7],
				data: "EMP_HIREDATE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.EMP_HIREDATE;
				}
			},
			{
				targets: [8],
				data: "EMP_PICTURE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if (row.EMP_PICTURE != '' && row.EMP_PICTURE != null) {
						return "<img src='" + row.EMP_PICTURE + "' width='130px' height='160px' />";
					} else {
						return "<div class=\"preview\"><img class=\"mapImg\"><span class=\"text\">預覽圖</span></div>"
					}
				}
			}
		]
    });

//	$("#dynamic-table").on("click", ".btn-update", function() {
//		$("#id").val($(this).data('id'));
//		$("#mainForm").attr("action", "/tkbrule/areaLocation/updateLocation?area_id=" + area_id);
//		$("#mainForm").submit();
//	})
//
//	$(".btn-add").on("click", function() {
//		$("#mainForm").attr("action", "/tkbrule/areaLocation/addLocation");
//		$("#mainForm").submit();
//	})
//
//	$("#dynamic-table").on("click", ".btn-delete", function() {
//		if (confirm("確定刪除嗎？")) {
//			$("#id").val($(this).data('id'));
//			$("#mainForm").attr("action", "/tkbrule/areaLocation/deleteLocation");
//			$("#mainForm").submit();
//		}
//	})
//
//	$("#add-submit").on("click", function() {
//		let result = checkValue();
//		if (result) {
//			if ($("#status_check").prop("checked")) {
//				$("#status").val("1");
//			} else {
//				$("#status").val("0");
//			}
//			$("#mainForm").attr("action", "/tkbrule/areaLocation/locationAddSubmit");
//			$("#mainForm").submit();
//		}
//	})
//
//	$("#update-submit").on("click", function() {
//		let result = checkValue();
//		if (result) {
//			if ($("#status_check").prop("checked")) {
//				$("#status").val("1");
//			} else {
//				$("#status").val("0");
//			}
//			let url = new URL(location.href);
//			let area = url.searchParams.get('area_id');
//			$("#mainForm").attr("action", "/tkbrule/areaLocation/locationUpdateSubmit?area=" + area);
//			$("#mainForm").submit();
//		}
//	})
//
//	function checkValue() {
//		let checkValue = true;
//		if (!$("#name").val()) {
//			alert("請輸入學堂名稱")
//			checkValue = false;
//		} else if ($("#name").val().length > 20) {
//			alert("區域請勿超過20字")
//			checkValue = false;
//		} else if ($("#area_id").val() == 'N') {
//			alert("請選擇區域")
//			checkValue = false;
//		} else if ($("#website_code").val() == '0') {
//			alert("請選擇品牌")
//			checkValue = false;
//		} else if (!$("#tel").val()) {
//			alert("請輸入電話號碼")
//			checkValue = false;
//		} else if (!isTel($("#tel").val())) {
//			alert("請輸入正確電話號碼")
//			checkValue = false;
//		} else if (!$("#address").val()) {
//			alert("請輸入地址")
//			checkValue = false;
//		} else if (!$("#url").val()) {
//			alert("請輸入GOOGLE MAP URL")
//			checkValue = false;
//		} else if (!$("#sort").val()) {
//			alert("請輸入排序")
//			checkValue = false;
//		} else if (!$("#do_business_time").val()) {
//			alert("請輸入諮詢時間")
//			checkValue = false;
//		} else if (!$("#share_url").val()) {
//			alert("請輸入分享地圖URL")
//			checkValue = false;
//		}
//		if ($("#website_code").val() == 'T') {
//			if ($("#type").val() == 'N') {
//				alert("請輸入判別數位學堂或諮詢據點")
//				checkValue = false;
//			}
//			if (!$("#branch_no").val()) {
//				alert("請輸入館別編號")
//				checkValue = false;
//			}
//		}
//		
//		if ($("#id").val() == null || $("#id").val() == "") {
//			if ($("#imageFile").val() == "") {
//				alert("請選擇圖片");
//				return false;
//			}
//			$("#mainForm").attr("action", "addSubmit");
//		} else {
//			$("#mainForm").attr("action", "updateSubmit");
//		}
//
//		return checkValue;
//	}
//
//	$('#imageFile').ace_file_input({
//		no_file: 'No File ...',
//		btn_choose: 'Choose',
//		btn_change: 'Change',
//		droppable: false,
//		onchange: null,
//		thumbnail: false, //| true | large
//		//whitelist:'gif|png|jpg|jpeg'
//		allowExt: ["jpeg", "jpg", "png", "gif"]
//		//blacklist:'exe|php'
//		//onchange:''
//		//
//	}).on('file.error.ace', function(event, info) {
//		alert("請選擇符合的圖片格式【jpeg、jpg、png、gif】");
//	});
//
//	$("#imageFile").change(addimage);
//
//	$("a.remove").on("click", function() {
//		$("#mapImg").removeAttr("src");
//	})
//
//	function addimage() {
//		let mapImg = $("#mapImg");
//		mapImg.attr('src', URL.createObjectURL(this.files[0]));
//	};
//
//	function isTel(phone) {
//		const regex = /(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})/;
//		return regex.test(phone);
//	}
//
//	$(function() {
//		// 初始高度
//		$('#file_info').each(function() {
//			this.style.height = (this.scrollHeight) + 'px';
//		});
//		$('#remark').each(function() {
//			this.style.height = (this.scrollHeight) + 'px';
//		});
//		$('#do_business_time').each(function() {
//			this.style.height = (this.scrollHeight) + 'px';
//		});
//		// 輸入調整高度
//		$('#file_info').on('input', function() {
//			this.style.height = 'auto';
//			this.style.height = this.scrollHeight + 'px';
//		});
//		$('#remark').on('input', function() {
//			this.style.height = 'auto';
//			this.style.height = this.scrollHeight + 'px';
//		});
//		$('#do_business_time').on('input', function() {
//			this.style.height = 'auto';
//			this.style.height = this.scrollHeight + 'px';
//		});
//	});
});