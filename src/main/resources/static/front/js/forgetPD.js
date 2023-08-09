
function sendPD() {
	$.ajax({
        url: "/forecast/member/setPD",
        cache: false,
        async: false,
        dataType: "json",
        type: "POST",
		data: {
			email : $("#email").val()
		},
        error: function(xhr) {
            alert("執行失敗");
        },
        success: function(data) {
        	if(data != null && data.length > 0){
        		for(i = 0 ; i < data.length; i++){
        			var out = data[i];
        			if(out.STATUS == 'Y') {
        				location.href = "/forecast/member/forgetPDSuccess";
        			}
        		}
        	}
        }
    });
}
