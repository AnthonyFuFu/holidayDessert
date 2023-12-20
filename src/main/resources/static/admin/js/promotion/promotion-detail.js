$(function() {
	$('#promotion-detail-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "promotionDetailTables",
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
				data: "PM_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_NAME;
				}
			},
			{
				targets: [1],
				data: "PM_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '上架'
					} else {
						return '下架'
					}
				}
			},
			{
				targets: [2],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [3],
				data: "PD_STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '上架'
					} else {
						return '下架'
					}
				}
			},
			{
				targets: [4],
				data: "PM_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DESCRIPTION;
				}
			},
			{
				targets: [5],
				data: "PM_DISCOUNT",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PM_DISCOUNT;
				}
			},
			{
				targets: [6],
				data: "PD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_PRICE;
				}
			},
			{
				targets: [7],
				data: "PMD_PD_DISCOUNT_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_PD_DISCOUNT_PRICE;
				}
			},
			{
				targets: [8],
				data: "PMD_START",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_START;
				}
			},
			{
				targets: [9],
				data: "PMD_END",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PMD_END;
				}
			},
			{
				targets: [10],
				data: "PM_REGULARLY",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if(data == 1){
						return '例行'
					} else {
						return '非例行'
					}
				}
			}
		],
		columns: [
			{
				data: "PM_NAME",
				defaultContent: ""
			},
			{
				data: "PM_STATUS",
				defaultContent: ""
			},
			{
				data: "PD_NAME",
				defaultContent: ""
			},
			{
				data: "PD_STATUS",
				defaultContent: ""
			},
			{
				data: "PM_DESCRIPTION",
				defaultContent: ""
			},
			{
				data: "PM_DISCOUNT",
				defaultContent: ""
			},
			{
				data: "PD_PRICE",
				defaultContent: ""
			},
			{
				data: "PMD_PD_DISCOUNT_PRICE",
				defaultContent: ""
			},
			{
				data: "PMD_START",
				defaultContent: ""
			},
			{
				data: "PMD_END",
				defaultContent: ""
			},
			{
				data: "PM_REGULARLY",
				defaultContent: ""
			}
		]
    });
    
    $('#issue-promotion-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "issuePromotionTables",
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
				data: "PD_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return "<input type=\"checkbox\" id=" + row.PD_ID + " name=\"selectList\"><label for=" + row.PD_ID + "></label>"
				}
			},
			{
				targets: [1],
				data: "PDC_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PDC_NAME;
				}
			},
			{
				targets: [2],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [3],
				data: "PD_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_DESCRIPTION;
				}
			},
			{
				targets: [4],
				data: "PD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_PRICE;
				}
			}
		],
		columns: [
			{
				data: "PD_ID",
				defaultContent: ""
			},
			{
				data: "PDC_NAME",
				defaultContent: ""
			},
			{
				data: "PD_NAME",
				defaultContent: ""
			},
			{
				data: "PD_DESCRIPTION",
				defaultContent: ""
			},
			{
				data: "PD_PRICE",
				defaultContent: ""
			}
		]
    });
});

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

function issueDailyPromotion(){
	var productArray = new Array();
	var productList;
	$("input[name='selectList']:checked").each(function(i) {
		productArray[i] = this.id;
	});
	productList = productArray.join(",");
	$("[name='products']").val(productList);
	
	if($("[name='promotionId']").val() == '0'){
		alert("請選擇優惠活動！");
		return false;
	} else if ($("[name='products']").val() == ''){
		alert("請選擇要安排優惠的商品！");
		return false;
	}
	var check = false;
	check = confirm("【提醒您，發放後無法修改，您確定要安排這些優惠活動嗎？】");

	if (check == true) {
		var link = "/holidayDessert/admin/promotion/issueDailyPromotionSubmit";
		$("#issuePromotion").attr("action", link);
		$("#issuePromotion").submit();
	}
	
}

function issueWeeklyPromotion(){
	var productArray = new Array();
	var productList;
	$("input[name='selectList']:checked").each(function(i) {
		productArray[i] = this.id;
	});
	productList = productArray.join(",");
	$("[name='products']").val(productList);
	
	if($("[name='promotionId']").val() == '0'){
		alert("請選擇優惠活動！");
		return false;
	} else if ($("[name='products']").val() == ''){
		alert("請選擇要安排優惠的商品！");
		return false;
	}
	
	var check = false;
	check = confirm("【提醒您，發放後無法修改，您確定要安排這些優惠活動嗎？】");

	if (check == true) {
		var link = "/holidayDessert/admin/promotion/issueWeeklyPromotionSubmit";
		$("#issuePromotion").attr("action", link);
		$("#issuePromotion").submit();
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


