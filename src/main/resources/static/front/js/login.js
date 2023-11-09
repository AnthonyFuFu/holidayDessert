//$(function(){
//	$(".btn-login").click(function(){
//		$(".nav-content-mobile").fadeOut();
//		$(".nav-content-mobile nav").removeClass('active');
//		$(".login-area").stop().fadeIn();
//	});
//	$(".btn-close-login").click(function(){
//		$(".login-area").stop().fadeOut();
//	});
//});

function login() {
	$.ajax({
		url: "/holidayDessert/front/doLogin",
		cache: false,
		async: false,
		dataType: "json",
		type: "POST",
		data: {
			memEmail: $("[name='memEmail']").val(),
			memPassword: $("[name='memPassword']").val()
		},
		error: function(xhr) {
			console.log(xhr);
			alert("執行失敗");
		},
		success: function(data) {
			if (data.STATUS == "N") {
				alert(data.MSG);
			} else if (location.href.includes("/member/verification")) {
				$(location).attr("href", "/holidayDessert/index");
			} else {
				window.location.reload();
			}
		}
	});
}

function logout() {
	$.ajax({
		url: "/holidayDessert/front/logout",
		cache: false,
		async: false,
		type: "POST",
		error: function(xhr) {
			alert("執行失敗");
		},
		success: function(data) {
			window.location.reload();
		}
	});
}

function register() {
	setTimeout(window.open('/holidayDessert/member/register'), 500);
	//$(location).attr("href", "/forecast/member/register");
}

function googleLogin() {
	setTimeout(window.open('/holidayDessert/google/login'), 500);
}

function forgetPD() {
	$(location).attr("href", "/holidayDessert/member/forgetPD");
}

function vfEmail() {
	$(location).attr("href", "/holidayDessert/member/verification");
}



