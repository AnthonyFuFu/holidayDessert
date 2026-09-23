$(document).ready(function() {
	var empId = $('#loginEmpId').val();
	var name = $('.name').text();
	
	var calendarEl = $('#calendar')[0];		// 使用 jQuery 獲取元素並轉換為原生 DOM 元素
	var calendar = new FullCalendar.Calendar(calendarEl, {
		timeZone: 'Asia/Taipei',
		headerToolbar: {
			left: 'prev,next today',
			center: 'title',
			right: 'dayGridMonth,timeGridWeek,timeGridDay'
		},
		eventSources: [
			{
				color: '#378006',			// 設定事件背景顏色
				textColor: '#ffffff',		// 設定事件文字顏色
				url: 'listFullcalendar',	// 事件的 API 路徑
				method: 'GET',         		// 請求方法 (GET/POST)
				extraParams: {				// 傳遞的額外參數
					EMP_ID: empId
				},
				failure: function() {		// 請求失敗時的回調函數
					alert('Error while fetching events!');
				}
			},
			{
				color: '#0066CC',			// 設定事件背景顏色
				textColor: '#ffffff',		// 設定事件文字顏色
				url: 'getManagedEmployees',	// 事件的 API 路徑
				method: 'GET',         		// 請求方法 (GET/POST)
				extraParams: {				// 傳遞的額外參數
					EMP_ID: empId
				},
				failure: function() {		// 請求失敗時的回調函數
					alert('Error while fetching events!');
				}
			}
		],
		dateClick: function(info) {
			setStartDate(info.dateStr);
			$('#calendarStart').focus(); // 手動聚焦到 datepicker 輸入框
		},
		eventClick: function(info) {
			// 阻止 FullCalendar 依事件的 url 欄位跳轉頁面（url 為 null 時會導向 admin/calendar/null）
			info.jsEvent.preventDefault();

			let eventId = info.event.id;
			let eventEmpId = info.event.extendedProps.EMP_ID;
			// 自己的假單不能自己審核，需由直屬主管審核
			if (String(eventEmpId) === String(empId)) {
				swal({
					title: "無法審核自己的假單",
					text: "請由直屬主管進行審核",
					type: "info",
					confirmButtonColor: "#3085d6",
					confirmButtonText: "確定"
				});
				return;
			}
			approveConfirm(eventId, info.event.title);
		},
		dayCellDidMount: function(info) {
		    info.el.classList.add('datepicker');
		},
		weekends: true,
		locale: 'zh-tw',
		height: 'auto',
		contentHeight: 600,
		footerToolbar: {
		    left: '',
		    center: '',
		    right: 'prev,next'
		}
	});
	calendar.render();
	
	$('#calendarTitle').val(name);
	$('#calendarEmpId').val(empId);
	
	// 為具有 datepicker 類別的元素啟用 datetime-picker
	function setStartDate(defaultDate) {
		autosize($('textarea.auto-growth'));
		$('.datepicker').bootstrapMaterialDatePicker({
		    format: 'YYYY-MM-DD HH:mm:00',
		    clearButton: true,
		    weekStart: 1,
		    time: true
		});
		// 設置選擇的日期到 #calendarStart 輸入框
		$('#calendarStart').val(defaultDate);
		$('#calendarStart').bootstrapMaterialDatePicker('setDate', defaultDate);
		setMinDate();
		// 設置一個監聽器，當用戶選擇了開始時間後，執行 setEndDate
		$('#calendarStart').on('change', function() {
		    setEndDate();
		});
	}
	function setEndDate() {
	    $('#calendarEnd').focus();
	    $('.datepicker').bootstrapMaterialDatePicker({
	        format: 'YYYY-MM-DD HH:mm:00',
	        clearButton: true,
	        weekStart: 1,
	        time: true
	    });
	}
	function setMinDate() {
		$('#calendarEnd').bootstrapMaterialDatePicker('setMinDate', $('#calendarStart').val());
	}

	$('#addCalendar').on('click', function () {
		let result = checkValue();
	    if (result == true) {
			statusCheck();
	        success("新增完成");
	    }
	});
	function checkValue() {
		let checkValue = true;
		if (!$("#calendarStart").val()) {
			warning("請選擇開始時間");
			checkValue = false;
		} else if (!$("#calendarEnd").val()) {
			warning("請選擇結束時間");
			checkValue = false;
		} else if($('#calendarStart').val() > $('#calendarEnd').val()){
			warning("開始時間不得大於結束時間");
			checkValue = false;
		}
		return checkValue;
	}
	function success(message) {
		swal({
	    	title: message,
	    	type: "success",
	    	showCancelButton: false,
	    	confirmButtonColor: "#3085d6",
	    	confirmButtonText: "確定"
		}, function(result) {
			if (result == true && message == "新增完成") {
				console.log($("#calendarForm").serialize()); // 查看提交的表單數據
	        	$("#calendarForm").attr("action", "calendarAddSubmit");
				$("#calendarForm")[0].submit();  // 使用原生 DOM 提交方法
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

	// 准假審核彈窗：先選擇 准假 / 不准假，再二次確認（可取消）
	function approveConfirm(eventId, title) {
		swal({
			title: "是否批准此假？",
			text: title,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#32CD32",
			confirmButtonText: "准假",
			cancelButtonText: "不准假",
			closeOnConfirm: false,
			closeOnCancel: false
		}, function(result) {
			if (result == true) {
				approveCheck(eventId, "approve", "確定准假？", "#32CD32");
			} else {
				approveCheck(eventId, "notApprove", "確定不准假？", "#DD6B55");
			}
		});
	}

	// 二次確認：確定 送出審核，取消 關閉彈窗不做任何事
	function approveCheck(eventId, url, message, color) {
		swal({
			title: message,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: color,
			confirmButtonText: "確定",
			cancelButtonText: "取消",
			closeOnConfirm: false
		}, function(result) {
			if (result == true) {
				approveSubmit(eventId, url);
			}
		});
	}

	function approveSubmit(eventId, url) {
		$.ajax({
			url: url,
			method: 'POST',
			data: {
				id: eventId
			},
			success: function(response) {
				if (response.status === 'success') {
					swal({
						title: response.message,
						type: "success",
						confirmButtonColor: "#3085d6",
						confirmButtonText: "確定"
					}, function() {
						location.reload();
					});
				} else {
					swal("審核失敗", response.message, "error");
				}
			},
			error: function(xhr, status, error) {
				console.error('API調用失敗: ' + error);
				swal("審核失敗", "請稍後再試", "error");
			}
		});
	}

	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#funcStatus").val("1");
		} else {
			$("#funcStatus").val("0");
		}
	}
});