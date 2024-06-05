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
						this.warning(response.data.MSG);
					} else if (location.href.includes("/member/verification")) {
						$(location).attr("href", "/holidayDessert/index.html");
					} else {
						this.success(response.data.MSG);
						var memberSession = response.data.memberSession;
						localStorage.setItem('memberSession', JSON.stringify(memberSession));
						this.updateSession(memberSession);
					}
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			},
			logout() {
				axios.post('/holidayDessert/front/logout')
				.then(response => {
					if (response.data.STATUS == "N") {
						this.warning(response.data.MSG);
					} else {
						this.success(response.data.MSG);
						localStorage.removeItem('memberSession');
						this.memberSession = '';
					}
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			},
			loadMemberSession() {
				var memberSession = localStorage.getItem('memberSession');
				if (memberSession) {
					this.updateSession(JSON.parse(memberSession));
				}
			},
			googleLogin() {
				return new Promise((resolve, reject) => {
					axios.post('/holidayDessert/front/google/login')
						.then(response => {
							if (response.data.STATUS == "N") {
								resolve({ status: "N", memberSession: '' });
							} else if (response.data.STATUS == "GLN") {
								resolve({ status: "GLN", memberSession: '' });
							} else {
								this.success(response.data.MSG);
								var memberSession = response.data.memberSession;
								localStorage.setItem('memberSession', JSON.stringify(memberSession));
								this.updateSession(memberSession);
								resolve({ status: "GLY", memberSession: memberSession });
							}
						})
						.catch(error => {
							console.log(error);
							this.warning("執行失敗");
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
			},
			success(message) {
				swal({
					title: message,
					type: "success",
					showCancelButton: false,
					confirmButtonColor: "#3085d6",
					confirmButtonText: "確定"
				});
			},
			warning(message) {
				swal({
					title: message,
					type: "warning",
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "確定",
					closeOnConfirm: false
				});
			}
		},
		mounted() {
			this.loadMemberSession();
		}
	});
	
});

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

