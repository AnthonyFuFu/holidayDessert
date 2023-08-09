function toSend(){
	if($("#email").val() == ""){
		alert("Email不可為空");
		return false;
	}
	$.ajax({
		url : "reSendEmail",
		cache: false,
		async: false,
		type : "POST",
		dataType : 'text',
		data : {
			email:$("#email").val()
		},
		success : function(msg) {
			alert(msg);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			
		}
	});
}