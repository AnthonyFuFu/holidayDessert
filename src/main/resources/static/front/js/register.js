$(function () {
    $('.hamburger').click(function () {
        $(this).toggleClass('open');
    });
	new Vue({
		el: '#register',
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
      		agreePolicy: false
		},
		methods: {
			checkEmailExist() {
				return new Promise((resolve, reject) => {
					axios.post('/holidayDessert/member/checkMemberAccountEmail', {
						memEmail: this.memEmail
					})
					.then(response => {
						let status = response.data.STATUS;
						let msg = response.data.MSG;
						if (this.isEmail(this.memEmail)) {
							if (status == "F") {
								this.cleanEmailTip();
								$('.email-block div').append("<span id='emailTip' style='color:red; font-size: smaller;text-align: center;margin:20px'>" + msg + "</span>");
								resolve({ status: "F"});
							} else if (status == "T") {
								this.cleanEmailTip();
								$('.email-block div').append("<span id='emailTip' style='color:green; font-size: smaller;text-align: center;margin:20px'>" + msg + "</span>");
								resolve({ status: "T"});
							}
						} else {
							this.cleanEmailTip();
							$('.email-block div').append("<span id='emailTip' style='color:red; font-size: smaller;text-align: center;margin:20px'>email不符合格式</span>");
							resolve({ status: "F"});
						}
					})
					.catch(error => {
						console.log(error);
						this.warning("執行失敗");
						reject(error);
					});
				});
			},
			cleanEmailTip() {
				if ($("#emailTip") != null) {
					$("#emailTip").remove();
				}
			},
			isCellphone(cellphone) {
				//手機格式 正規則
				return /^[09]{2}[0-9]{8}$/.test(cellphone);
			},
			isEmail(email) {
				//RFC822 正規則
				return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(email);	
			},
			checkForm() {
				//驗證資料
				if (this.memName == null || this.memName == '') {
					this.warning("請輸入中文姓名");
					this.$refs.memName.focus();
					return false;
				} else if (this.memPhone == null || this.memPhone == '') {
					this.warning("請輸入行動電話");
					this.$refs.memPhone.focus();
					return false;
				} else if (!this.isCellphone(this.memPhone)) {
					this.warning("行動電話格式不正確，請確認行動電話號碼");
					this.$refs.memPhone.focus();
					return false;
				} else if (this.memEmail == null || this.memEmail == '') {
					this.warning("請輸入電子信箱");
					this.$refs.memEmail.focus();
					return false;
				} else if (!this.isEmail(this.memEmail)) {
					this.warning("電子信箱格式錯誤,請確認妳的電子信箱是不是合法的");
					this.$refs.memEmail.focus();
					return false;
				} else if (this.memPassword == null || this.memPassword == '') {
					this.warning("請輸入密碼");
					this.$refs.memPassword.focus();
					return false;
				} else if (this.memPassword.length < 6) {
					this.warning("密碼必須大於6碼");
					this.$refs.memPassword.focus();
					return false;
				} else if (!this.agreePolicy) {
					this.warning("請勾選已閱讀會員權利說明");
					this.$refs.agreePolicy.focus();
					return false;
				}
				return true;
			},
			register() {
				this.checkEmailExist().then(result => {
					if (result.status === "T") {
						if (this.checkForm()) {
							axios.post('/holidayDessert/member/register', {
								memName: this.memName,
								memPhone: this.memPhone,
								memEmail: this.memEmail,
								memPassword: this.memPassword
							})
							.then(response => {
								if (response.data.STATUS == "F") {
									this.warning(response.data.MSG);
								} else {
									$(location).attr("href", "/holidayDessert/member/verification.html");
								}
							})
							.catch(error => {
								console.log(error);
								this.warning("執行失敗");
							});
						}
					} else {
						if (this.checkForm()) {
							this.warning("此email已經註冊,請選擇其他email");
						}
					}
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
								var memberSession = response.data.memberSession;
								localStorage.setItem('memberSession', JSON.stringify(memberSession));
								this.updateSession(memberSession);
								resolve({ status: "GLY", memberSession: memberSession });
								$(location).attr("href", "/holidayDessert/index.html");
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
			toggleAgree(){
				this.agreePolicy = !this.agreePolicy;
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
