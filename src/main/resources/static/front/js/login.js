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
						this.updateSession(memberSession);
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
				this.getGoogleLogin().then(result => {
					if (result.status === "GLY") {
						this.updateSession(result.memberSession);
					} else {
						var memberSession = localStorage.getItem('memberSession');
						// var memberSession = sessionStorage.getItem('memberSession');
						if (memberSession) {
							this.updateSession(JSON.parse(memberSession));
						}
					}
				});
			},
			getGoogleLogin() {
				return new Promise((resolve, reject) => {
					axios.post('/holidayDessert/front/getGoogleLogin')
						.then(response => {
							if (response.data.STATUS == "N") {
								resolve({ status: "N", memberSession: '' });
							} else if (response.data.STATUS == "GLN") {
								resolve({ status: "GLN", memberSession: '' });
							} else {
								var memberSession = response.data.memberSession;
								localStorage.setItem('memberSession', JSON.stringify(memberSession));
								this.updateSession(memberSession);
								resolve({ status: "GLY", memberSession: memberSession });
							}
						})
						.catch(error => {
							console.log(error);
							alert("執行失敗");
							reject(error);
						});
				});
			},
			updateSession(memberSession) {
				this.memberSession = memberSession;
				this.memId = memberSession.memId;
				this.memName = memberSession.memName;
				this.memAccount = memberSession.memAccount;
				this.memPassword = memberSession.memPassword;
				this.memGender = memberSession.memGender;
				this.memPhone = memberSession.memPhone;
				this.memEmail = memberSession.memEmail;
				this.memAddress = memberSession.memAddress;
				this.memBirthday = memberSession.memBirthday;
				this.memStatus = memberSession.memStatus;
				this.memVerificationCode = memberSession.memVerificationCode;
				this.memVerificationStatus = memberSession.memVerificationStatus;
				this.memGoogleUid = memberSession.memGoogleUid;
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

