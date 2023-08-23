$(function() {
	$('#product-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "productTables",
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
					return row.PD_ID;
				}
			},
			{
				targets: [1],
				data: "PD_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_NAME;
				}
			},
			{
				targets: [2],
				data: "PDC_NAME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PDC_NAME;
				}
			},
			{
				targets: [3],
				data: "PD_PRICE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_PRICE;
				}
			},
			{
				targets: [4],
				data: "PD_DESCRIPTION",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_DESCRIPTION;
				}
			},
			{
				targets: [5],
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			},
			{
				targets: [6],
				data: "PD_UPDATE_TIME",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PD_UPDATE_TIME;
				}
			},
			{
				targets: [7],
				data: "PD_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.PD_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "PD_ID" },
			{ data: "PD_NAME" },
			{ data: "PDC_NAME" },
			{ data: "PD_PRICE" },
			{ data: "PD_DESCRIPTION" },
			{ data: "STATUS" },
			{ data: "PD_UPDATE_TIME" },
        	{
            	data: "PD_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.PD_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    $("#product-table").on("click", ".btn-update", function() {
		let pdId = $(this).data('id');
		let action = "/holidayDessert/admin/product/updateProduct";
		let url = window.location.origin + action + "?pdId=" + pdId;
		window.location.href = url;
	});
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
			isDeleteCheck();
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
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
		if (!$("#pdName").val()) {
			warning("請輸入商品名稱");
			checkValue = false;
		} else if ($("#productCollection").val() == '0') {
			warning("請選擇商品分類");
			checkValue = false;
		} else if (!$("#pdPrice").val()) {
			warning("請輸入商品原價");
			checkValue = false;
		} else if (!$("#pdDescription").val()) {
			warning("請輸入商品資訊");
			checkValue = false;
		} else if (!$("#pdDisplayQuantity").val()) {
			warning("請輸入商品圖片陳列數量");
			checkValue = false;
		}
		return checkValue;
	}

	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#pdStatus").val("1");
		} else {
			$("#pdStatus").val("0");
		}
	}
	
	function isDeleteCheck(){
		if ($("#checkIsDel").prop("checked")) {
			$("#pdIsDel").val("0");
		} else {
			$("#pdIsDel").val("1");
		}
	}
	
});