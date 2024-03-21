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
			memberSession: '',
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
						localStorage.setItem('memberSession', JSON.stringify(memberSession));
//						sessionStorage.setItem('memberSession', JSON.stringify(memberSession));
						vm.memberSession = memberSession;
						vm.memId = memberSession.memId;
						vm.memName = memberSession.memName;
						vm.memAccount = memberSession.memAccount;
						vm.memPassword = memberSession.memPassword;
						vm.memGender = memberSession.memGender;
						vm.memPhone = memberSession.memPhone;
						vm.memEmail = memberSession.memEmail;
						vm.memAddress = memberSession.memAddress;
						vm.memBirthday = memberSession.memBirthday;
						vm.memStatus = memberSession.memStatus;
						vm.memVerificationCode = memberSession.memVerificationCode;
						vm.memVerificationStatus = memberSession.memVerificationStatus;
						vm.memGoogleUid = memberSession.memGoogleUid;
					}
				})
				.catch(error => {
					console.log(error);
					alert("執行失敗");
				});
				
			},
			logout() {
				localStorage.removeItem('memberSession');
				this.memberSession = '';
			},
			loadMemberSession() {
				var memberSession = localStorage.getItem('memberSession');
//				var memberSession = sessionStorage.getItem('memberSession');
				if (memberSession) {
					this.memberSession = JSON.parse(memberSession);
					this.memId = this.memberSession.memId;
					this.memName = this.memberSession.memName;
					this.memAccount = this.memberSession.memAccount;
					this.memPassword = this.memberSession.memPassword;
					this.memGender = this.memberSession.memGender;
					this.memPhone = this.memberSession.memPhone;
					this.memEmail = this.memberSession.memEmail;
					this.memAddress = this.memberSession.memAddress;
					this.memBirthday = this.memberSession.memBirthday;
					this.memStatus = this.memberSession.memStatus;
					this.memVerificationCode = this.memberSession.memVerificationCode;
					this.memVerificationStatus = this.memberSession.memVerificationStatus;
					this.memGoogleUid = this.memberSession.memGoogleUid;
				}
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
		},
		mounted() {
			this.loadMemberSession();
		}
	});
});

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

