function chat() {
	$.ajax({
		url: "api/openai/chat",
		cache: false,
		async: false,
		dataType: "text",
		//		dataType: "json",
		type: "POST",
		data: {
			prompt: $("[name='prompt']").val()
		},
		error: function(xhr) {
			alert("執行失敗");
		},
		success: function(data) {
        	var response = JSON.parse(data);
        	var result = response.result;
        	alert(result);
		}
	});
}

$(function(){
	$('#uploadForm').submit(function(event) {
		event.preventDefault(); // 防止表单默认提交

		var formData = new FormData();
		var file = $('#file')[0].files[0];
		formData.append('file', file);

		$.ajax({
			url: "api/ocr/recognizeText", // 替换为你的文件上传接口
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(data) {
				$('#result').empty();
				$('#result').html(data.result);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#result').empty();
				$('#result').html('<p>上傳失败</p>');
			}
		});
	});
	
	$('#uploadForm2').submit(function(event) {
		event.preventDefault(); // 防止表单默认提交

		var formData = new FormData();
		var file = $('#file2')[0].files[0];
		formData.append('file', file);

		$.ajax({
			url: "api/extractPDF", // 替换为你的文件上传接口
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(data) {
				$('#result').empty();
				$('#result').html(data.result);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#result').empty();
				$('#result').html('<p>上傳失败</p>');
			}
		});
	});
});