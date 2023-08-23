$(function() {
	$('#product-collection-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "productCollectionTables",
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
				data: "PDC_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PDC_ID;
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
				data: "PDC_KEYWORD",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.PDC_KEYWORD;
				}
			},
			{
				targets: [3],
				data: "STATUS",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return row.STATUS;
				}
			},
			{
				targets: [4],
				data: "PDC_ID",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.PDC_ID + "'><i class='material-icons'>edit</i></button> ";
				}
			}
		],
		columns: [
			{ data: "PDC_ID" },
			{ data: "PDC_NAME" },
			{ data: "PDC_KEYWORD" },
			{ data: "STATUS" },
        	{
            	data: "PDC_ID",
            	render: function(data, type, row, meta) {
                	return " <button class='btn btn-default btn-circle waves-effect waves-circle waves-float btn-update' data-id='" + row.PDC_ID + "'><i class='material-icons'>edit</i></button> ";
            	}
        	}
		]
    });
    
    
    $("#product-collection-table").on("click", ".btn-update", function() {
		let pdcId = $(this).data('id');
		let action = "/holidayDessert/admin/product/updateProductCollection";
		let url = window.location.origin + action + "?pdcId=" + pdcId;
		window.location.href = url;
	});
    
	$('#add-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
            success("新增完成");
        }
    });
    
    $('#update-submit').on('click', function () {
		let result = checkValue();
        if (result == true) {
			statusCheck();
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
            	$("#mainForm").attr("action", "productCollectionAddSubmit");
            	$("#mainForm").submit();
        	} else if (result == true && message == "修改完成") {
				$("#mainForm").attr("action", "productCollectionUpdateSubmit");
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
		if (!$("#pdcName").val()) {
			warning("請輸入商品分類名稱");
			checkValue = false;
		} else if (!$("#pdcKeyword").val()) {
			warning("請輸入商品分類關鍵字");
			checkValue = false;
		}
		return checkValue;
	}
	
	function statusCheck(){
		if ($("#checkStatus").prop("checked")) {
			$("#pdcStatus").val("1");
		} else {
			$("#pdcStatus").val("0");
		}
	}
	
});