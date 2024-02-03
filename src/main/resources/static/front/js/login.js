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
$(function () {

    $('.hamburger').click(function () {
        $(this).toggleClass('open');
    });

	new Vue({
		el: '#login',
		data: {
			// model 屬性
			memId:'',
			memName:'',
			memAccount:'',
			memEmail: '',
			memPassword: '',
			memGender:'',
			memPhone:'',
			memAddress:'',
			memBirthday:'',
			memStatus:'',
			memVerificationStatus:'',
			memVerificationCode:'',
			memGoogleUid:'',
			// 其他屬性
			memberSession: null,
      		passwordType: 'password',
      		rememberMe: false
		},
		methods: {
			login() {
				var vm = this;
				axios.post('/holidayDessert/front/login', {
					memEmail: this.memEmail,
					memPassword: this.memPassword
				})
				.then(response => {
					
					if (response.data.STATUS == "N") {
						alert(response.data.MSG);
					} else if (location.href.includes("/member/verification")) {
						$(location).attr("href", "/holidayDessert/index");
					} else {
						var memberSession = response.data.memberSession;
						vm.memEmail = memberSession.memEmail;
						vm.memPassword = memberSession.memPassword;
						vm.memAddress = memberSession.memAddress;
						vm.memberSession = memberSession;
					}
					
				})
				.catch(error => {
					console.log(error);
					alert("執行失敗");
				});
				
			},
			passwordVision(isMouseDown){
				// 切換密碼可視性
				if (isMouseDown) {
					this.passwordType = 'text';
				} else {
					this.passwordType = 'password';
				}
				
			},
			toggleRememberMe(){
				this.rememberMe = !this.rememberMe;
			}
			
		}
	});
});

function login() {
	$.ajax({
		url: "/holidayDessert/front/login",
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



