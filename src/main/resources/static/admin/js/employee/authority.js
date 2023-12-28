$(function() {
	
	let port = window.location.port;
	let httpUrl = window.location.protocol + '//' + window.location.hostname + (port ? ':' + port : '') + '/';
	
	$('#authority-table').DataTable({
        bAutoWidth: false,
		serverSide: true,
		processing: true,
		ordering: true,
        ajax: "authorityTables",
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
				data: "EMP_PICTURE",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					if (row.EMP_PICTURE != '' && row.EMP_PICTURE != null) {
						return "<div class=\"preview\"><img class=\"empImg\" src='" + httpUrl + row.EMP_PICTURE + "' /></div>";
					} else {
						return "<div class=\"preview\"><img class=\"empImg\"><span class=\"text\">預覽圖</span></div>"
					}
				}
			},
			{
				targets: [3],
				data: "AUTH_LIST",
				searching: false,
				orderable: false,
				render: function(data, type, row, meta) {
					var authList = '';
					if (Array.isArray(row.AUTH_LIST) && row.AUTH_LIST.length > 0) {
						row.AUTH_LIST.forEach(function(auth) {
							authList +=
							`<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
								<div class="form-group"><div class="switch">
                                <label>${auth.FUNC_NAME}<input type="checkbox" id="checkStatus" class="form-control" ${auth.AUTH_STATUS == 1 ? 'checked' : ''}><span class="lever switch-col-cyan"></span></label>
                                </div></div>
                             </div>`
						});
					} else {
						authList = 'No data';
					}
					return authList;
				}
			}
		],
		columns: [
			{ data: "EMP_ID" },
        	{ data: "EMP_NAME" },
        	{
            	data: "EMP_PICTURE",
            	render: function(data, type, row, meta) {
                	if (row.EMP_PICTURE != '' && row.EMP_PICTURE != null) {
                    	return "<div class=\"preview\"><img class=\"empImg\" src='" + httpUrl + row.EMP_PICTURE + "' /></div>";
                	} else {
                    	return "<div class=\"preview\"><img class=\"empImg\"><span class=\"text\">預覽圖</span></div>";
                	}
            	}
        	},
        	{ data: "AUTH_LIST" }
		]
    });
    
});

