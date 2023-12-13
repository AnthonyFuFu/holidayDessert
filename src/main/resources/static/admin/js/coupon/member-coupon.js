$(function() {
	$('#member-coupon-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "memberCouponTables",
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
				data: "MEM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_NAME;
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
				data: "MEM_CP_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_CP_START;
				}
			},
			{
				targets: [4],
				data: "MEM_CP_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_CP_END;
				}
			},
			{
				targets: [5],
				data: "MEM_CP_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '可用'
					} else {
						return '不可用'
					}
				}
			},
			{
				targets: [6],
				data: "MEM_CP_RECORD",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == null){
						return '未使用'
					} else {
						return data;
					}
				}
			}
		],
		columns: [
			{
				data: "MEM_NAME",
				defaultContent: ""
			},
			{
				data: "CP_NAME",
				defaultContent: ""
			},
			{
				data: "CP_DISCOUNT",
				defaultContent: ""
			},
			{
				data: "MEM_CP_START",
				defaultContent: ""
			},
			{
				data: "MEM_CP_END",
				defaultContent: ""
			},
			{
				data: "MEM_CP_STATUS",
				defaultContent: ""
			},
			{
				data: "MEM_CP_RECORD",
				defaultContent: ""
			}
		]
    });
    
    
    
    $('#issue-coupon-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "issueCouponTables",
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
				data: "MEM_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return "<input type=\"checkbox\" id=" + row.MEM_ID + " name=\"selectList\"><label for=" + row.MEM_ID + "></label>"
				}
			},
			{
				targets: [1],
				data: "MEM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_NAME;
				}
			},
			{
				targets: [2],
				data: "GENDER",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.GENDER;
				}
			},
			{
				targets: [3],
				data: "MEM_ACCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_ACCOUNT;
				}
			},
			{
				targets: [4],
				data: "MEM_PHONE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_PHONE;
				}
			},
			{
				targets: [5],
				data: "MEM_EMAIL",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_EMAIL;
				}
			},
			{
				targets: [6],
				data: "MEM_BIRTHDAY",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.MEM_BIRTHDAY;
				}
			},
			{
				targets: [7],
				data: "TOTAL_EXPENSE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.TOTAL_EXPENSE;
				}
			}
		],
		columns: [
			{
				data: "MEM_ID",
				defaultContent: ""
			},
			{
				data: "MEM_NAME",
				defaultContent: ""
			},
			{
				data: "GENDER",
				defaultContent: ""
			},
			{
				data: "MEM_ACCOUNT",
				defaultContent: ""
			},
			{
				data: "MEM_PHONE",
				defaultContent: ""
			},
			{
				data: "MEM_EMAIL",
				defaultContent: ""
			},
			{
				data: "MEM_BIRTHDAY",
				defaultContent: ""
			},
			{
				data: "TOTAL_EXPENSE",
				defaultContent: ""
			}
		]
    });
    
});

function issueDailyCouponSubmit() {
	//追蹤清單
	var memberArray = new Array();
	var memberList;
	var i = 0;
	$(".choice").each(function() {
		if ($(this).attr("chk") == "t") {
			memberArray[i] = $(this).children("div").attr("member-id");
			i++;
		}
	});
	memberList = memberArray.join(",");
	$("[name='member_list']").val(memberList);

	var check = false;
	
	check = confirm("【提醒您，發放後無法修改，您確定要發放這些優惠券嗎？】");

	if (check == true) {
		var link = "/forecast/coupon/issueDailyCouponSubmit";
		$("#issueCoupon").attr("action", link);
		$("#issueCoupon").submit();
	}
	
}

var check = false;
//全選
function checkAll() {
	if (!check) {
		$(":checkbox[name='selectList']").each(function() {
			this.checked = true;
		});
		check = true;
	} else {
		$(":checkbox[name='selectList']").each(function() {
			this.checked = false;
		});
		check = false;
	}
}

function issueDailyCoupon(){
	var memberArray = new Array();
	var memberList;
	$("input[name='selectList']:checked").each(function(i) {
		memberArray[i] = this.id;
	});
	memberList = memberArray.join(",");
	$("[name='members']").val(memberList);
	
	if($("[name='couponId']").val() == '0'){
		alert("請選擇優惠券！");
		return false;
	} else if ($("[name='members']").val() == ''){
		alert("請選擇要發放的會員！");
		return false;
	}
	var check = false;
	check = confirm("【提醒您，發放後無法修改，您確定要發放這些優惠券嗎？】");

	if (check == true) {
		var link = "/holidayDessert/admin/coupon/issueDailyCouponSubmit";
		$("#issueCoupon").attr("action", link);
		$("#issueCoupon").submit();
	}
	
}

function issueWeeklyCoupon(){
	var memberArray = new Array();
	var memberList;
	$("input[name='selectList']:checked").each(function(i) {
		memberArray[i] = this.id;
	});
	memberList = memberArray.join(",");
	$("[name='members']").val(memberList);
	
	if($("[name='couponId']").val() == '0'){
		alert("請選擇優惠券！");
		return false;
	} else if ($("[name='members']").val() == ''){
		alert("請選擇要發放的會員！");
		return false;
	}
	
	var check = false;
	check = confirm("【提醒您，發放後無法修改，您確定要發放這些優惠券嗎？】");

	if (check == true) {
		var link = "/holidayDessert/admin/coupon/issueWeeklyCouponSubmit";
		$("#issueCoupon").attr("action", link);
		$("#issueCoupon").submit();
	}
	
}

function deleteData (msg) {
	var checkArray = new Array();
	var checkList;
	$("input[name='selectList']:checked").each(function(i) {
		checkArray[i] = this.value;
	});
	checkList = checkArray.join(",");
	$("#deleteList").val(checkList);

	if ($("input[name='selectList']:checked").length <= 0) {
		alert("請選擇要刪除的資料！");
		return false;
	}
	if (typeof (msg) == "undefined") {
		if (window.confirm("您真的確定要刪除嗎？")) {
			var frm = $("#mainForm").attr("action", "delete");
			frm.submit();
		}
	} else {
		if (window.confirm(msg)) {
			var frm = $("#mainForm").attr("action", "delete");
			frm.submit();
		}
	}
}

