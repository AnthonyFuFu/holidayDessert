$(function() {

	let getUrl = location.href;
	var url = new URL(getUrl);
	var area_id = url.searchParams.get('area_id');

	myTable = $('#dynamic-table').DataTable({
		bAutoWidth: false,
		serverSide: true,
		"processing": true,
		"ordering": true,
		"ajax": "locationTables?area_id=" + area_id,
		"aaSorting": [],
		"oLanguage": {
			"sProcessing": "處理中...",
			"sLengthMenu": "顯示 _MENU_ 筆",
			"sZeroRecords": "目前無資料",
			"sEmptyTable": "目前無資料",
			"sInfo": "目前顯示： 第 _START_ 筆 到 第 _END_ 筆， 共有 _TOTAL_ 筆",
			"sInfoEmpty": "找尋不到相關資料",
			"sInfoFiltered": "(已過濾 _MAX_ 筆)",
			"sSearch": "搜尋：",
			"oPaginate": {
				"sFirst": "First",
				"sPrevious": "上一頁",
				"sNext": "下一頁",
				"sLast": "Last"
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
		"columnDefs": [
			{
				"targets": [1],
				"data": "WEBSITE",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return "<label class='pos-rel'><span class='lbl'>" + row.WEBSITE + "</span></label>";
				}
			},
			{
				"targets": [2],
				"data": "NAME",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return "<label class='pos-rel'><span class='lbl'>" + row.NAME + "</span></label>";
				}
			},
			{
				"targets": [3],
				"data": "AREA_NAME",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return "<label class='pos-rel'><span class='lbl'>" + row.AREA_NAME + "</span></label>";
				}
			},
			{
				"targets": [4],
				"data": "TEL",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return "<label class='pos-rel'><span class='lbl'>" + row.TEL + "</span></label>";
				}
			},
			{
				"targets": [5],
				"data": "ADDRESS",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return "<label class='pos-rel'><span class='lbl'>" + row.ADDRESS + "</span></label>";
				}
			},
			{
				"targets": [6],
				"data": "IMAGE_URL",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					if (row.IMAGE != '' && row.IMAGE != null) {
						return "<img src='" + row.IMAGE_URL + "' width='100px' height='100px' />";
					} else {
						return "<div id=\"preview\"><img id=\"mapImg\"><span class=\"text\">預覽圖</span></div>"
					}
				}
			},
			{
				"targets": [7],
				"data": "ID",
				"searching": false,
				"orderable": false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-xs btn-info btn-update' type='button' data-id='" + row.ID + "' ><i class='ace-icon fa fa-pencil bigger-120'></i></button> "
						+ " <button class='btn btn-xs btn-danger btn-sm btn-delete' type='button' data-id='" + row.ID + "' ><i class='ace-icon fa fa-trash-o bigger-120'></i></button> ";
				}
			}
		],
		"columns": [
			{
				"data": null,
				"render": function(data, type, full, meta) {
					return meta.row + 1 + meta.settings._iDisplayStart;
				}
			},
			{
				"data": "ID",
				"defaultContent": "",
				"orderable": false,

			},
			{
				"data": "WEBSITE",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "NAME",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "TEL",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "ADDRESS",
				"defaultContent": "",
				"orderable": false,
			},
			{
				"data": "IMAGE",
				"defaultContent": ""
			},
		]
	});

	$("#dynamic-table").on("click", ".btn-update", function() {
		$("#id").val($(this).data('id'));
		$("#mainForm").attr("action", "/tkbrule/areaLocation/updateLocation?area_id=" + area_id);
		$("#mainForm").submit();
	})

	$(".btn-add").on("click", function() {
		$("#mainForm").attr("action", "/tkbrule/areaLocation/addLocation");
		$("#mainForm").submit();
	})

	$("#dynamic-table").on("click", ".btn-delete", function() {
		if (confirm("確定刪除嗎？")) {
			$("#id").val($(this).data('id'));
			$("#mainForm").attr("action", "/tkbrule/areaLocation/deleteLocation");
			$("#mainForm").submit();
		}
	})

	$("#add-submit").on("click", function() {
		let result = checkValue();
		if (result) {
			if ($("#status_check").prop("checked")) {
				$("#status").val("1");
			} else {
				$("#status").val("0");
			}
			$("#mainForm").attr("action", "/tkbrule/areaLocation/locationAddSubmit");
			$("#mainForm").submit();
		}
	})

	$("#update-submit").on("click", function() {
		let result = checkValue();
		if (result) {
			if ($("#status_check").prop("checked")) {
				$("#status").val("1");
			} else {
				$("#status").val("0");
			}
			let url = new URL(location.href);
			let area = url.searchParams.get('area_id');
			$("#mainForm").attr("action", "/tkbrule/areaLocation/locationUpdateSubmit?area=" + area);
			$("#mainForm").submit();
		}
	})

	function checkValue() {
		let checkValue = true;
		if (!$("#name").val()) {
			alert("請輸入學堂名稱")
			checkValue = false;
		} else if ($("#name").val().length > 20) {
			alert("區域請勿超過20字")
			checkValue = false;
		} else if ($("#area_id").val() == 'N') {
			alert("請選擇區域")
			checkValue = false;
		} else if ($("#website_code").val() == '0') {
			alert("請選擇品牌")
			checkValue = false;
		} else if (!$("#tel").val()) {
			alert("請輸入電話號碼")
			checkValue = false;
		} else if (!isTel($("#tel").val())) {
			alert("請輸入正確電話號碼")
			checkValue = false;
		} else if (!$("#address").val()) {
			alert("請輸入地址")
			checkValue = false;
		} else if (!$("#url").val()) {
			alert("請輸入GOOGLE MAP URL")
			checkValue = false;
		} else if (!$("#sort").val()) {
			alert("請輸入排序")
			checkValue = false;
		} else if (!$("#do_business_time").val()) {
			alert("請輸入諮詢時間")
			checkValue = false;
		} else if (!$("#share_url").val()) {
			alert("請輸入分享地圖URL")
			checkValue = false;
		}
		if ($("#website_code").val() == 'T') {
			if ($("#type").val() == 'N') {
				alert("請輸入判別數位學堂或諮詢據點")
				checkValue = false;
			}
			if (!$("#branch_no").val()) {
				alert("請輸入館別編號")
				checkValue = false;
			}
		}
		
		if ($("#id").val() == null || $("#id").val() == "") {
			if ($("#imageFile").val() == "") {
				alert("請選擇圖片");
				return false;
			}
			$("#mainForm").attr("action", "addSubmit");
		} else {
			$("#mainForm").attr("action", "updateSubmit");
		}

		return checkValue;
	}

	$('#imageFile').ace_file_input({
		no_file: 'No File ...',
		btn_choose: 'Choose',
		btn_change: 'Change',
		droppable: false,
		onchange: null,
		thumbnail: false, //| true | large
		//whitelist:'gif|png|jpg|jpeg'
		allowExt: ["jpeg", "jpg", "png", "gif"]
		//blacklist:'exe|php'
		//onchange:''
		//
	}).on('file.error.ace', function(event, info) {
		alert("請選擇符合的圖片格式【jpeg、jpg、png、gif】");
	});

	$("#imageFile").change(addimage);

	$("a.remove").on("click", function() {
		$("#mapImg").removeAttr("src");
	})

	function addimage() {
		let mapImg = $("#mapImg");
		mapImg.attr('src', URL.createObjectURL(this.files[0]));
	};

	function isTel(phone) {
		const regex = /(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})/;
		return regex.test(phone);
	}

	$(function() {
		// 初始高度
		$('#file_info').each(function() {
			this.style.height = (this.scrollHeight) + 'px';
		});
		$('#remark').each(function() {
			this.style.height = (this.scrollHeight) + 'px';
		});
		$('#do_business_time').each(function() {
			this.style.height = (this.scrollHeight) + 'px';
		});
		// 輸入調整高度
		$('#file_info').on('input', function() {
			this.style.height = 'auto';
			this.style.height = this.scrollHeight + 'px';
		});
		$('#remark').on('input', function() {
			this.style.height = 'auto';
			this.style.height = this.scrollHeight + 'px';
		});
		$('#do_business_time').on('input', function() {
			this.style.height = 'auto';
			this.style.height = this.scrollHeight + 'px';
		});
	});
});