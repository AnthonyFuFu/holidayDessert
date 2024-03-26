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
			isCellphone(cellphone) {
				//手機格式 正規則
				return /^[09]{2}[0-9]{8}$/.test(cellphone);
			},
			isEmail(email) {
				//RFC822 正規則
				return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(email);	
			},
			register() {
				var checkEmailVal = "F";
				
				//驗證資料
				if (this.memName == null || this.memName == '') {
					alert("請輸入中文姓名");
					this.$refs.memName.focus();
					return false;
				} else if (this.memPhone == null || this.memPhone == '') {
					alert("請輸入行動電話");
					this.$refs.memPhone.focus();
					return false;
				} else if (!this.isCellphone(this.memPhone)) {
					alert("行動電話格式不正確，請確認行動電話號碼");
					this.$refs.memPhone.focus();
					return false;
				} else if (this.memEmail == null || this.memEmail == '') {
					alert("請輸入電子信箱");
					this.$refs.memEmail.focus();
					return false;
				} else if (!this.isEmail(this.memEmail) || checkEmailVal == "F") {
					alert("電子信箱格式錯誤,請確認妳的電子信箱是不是合法的");
					this.$refs.memEmail.focus();
					return false;
				} else if (this.memPassword == null || this.memPassword == '') {
					alert("請輸入密碼");
					this.$refs.memPassword.focus();
					return false;
				} else if (this.memPassword.length < 6) {
					alert("密碼必須大於6碼");
					this.$refs.memPassword.focus();
					return false;
				}

				if ($("input[name='approve']:checked").length <= 0) {
					alert("請勾選已閱讀會員權利說明");
					$("input[name='approve']").focus();
					return false;
				}
//				axios.post('/holidayDessert/front/member/register', {
//					memEmail: this.memEmail,
//					memPassword: this.memPassword
//				})
//				.then(response => {
//					if (response.data.STATUS == "N") {
//						alert(response.data.MSG);
//					} else if (location.href.includes("/member/verification")) {
//						$(location).attr("href", "/holidayDessert/index");
//					} else {
//						var memberSession = response.data.memberSession;
//						localStorage.setItem('memberSession', JSON.stringify(memberSession));
////						sessionStorage.setItem('memberSession', JSON.stringify(memberSession));
//						this.updateSession(memberSession);
//					}
//				})
//				.catch(error => {
//					console.log(error);
//					alert("執行失敗");
//				});
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
			toggleAgree(){
				this.agreePolicy = !this.agreePolicy;
			}
		},
		mounted() {
			this.loadMemberSession();
		}
	});
	
});



var check = "T";
var checkIdNoVal = "T";
var checkEmailVal = "F";

checkEmailExist();

//改變性別時觸發
function changeGender() {
	$("#memberGender").val($("#gender").val());
}

//檢查有無重複的email
function checkEmailExist() {
	
	if(isEmail($("#Memberemail").val())){
		$.ajax({
			url : "/forecast/api/checkMemberAccountEmail",
			cache: false,
			async: false,
			type : "POST",
			dataType : 'text',
			data : {
				email:$("#Memberemail").val()
			},
			success : function(msg) {
				
				if(msg == "true") {
					checkEmailVal = "T";
					if($("#emailTip")!=null) {
						$("#emailTip").remove();
					}
					console.log($("#emailTip"));
					$("#Memberemail").after("<p id='emailTip' style='color:green;'>此email可以使用</p>");
				} else {
				
					checkEmailVal = "F";
					if($("#emailTip")!=null) {
						$("#emailTip").remove();
					}
					$("#Memberemail").after("<p id='emailTip' style='color:red;'>此email已經註冊,請選擇其他email</p>");
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				
			}
		});
	} else {
		if($("#emailTip")!=null) {
			$("#emailTip").remove();
		}
		$("#Memberemail").after("<label id='emailTip' style='color:red;'>email不符合格式</label>");
	}
}

function add() {
	if(checkEmailVal == "F") {
		if(isEmail($("#Memberemail").val())){
			$.ajax({
				url : "/forecast/api/checkMemberAccountEmail",
				cache: false,
				async: false,
				type : "POST",
				dataType : 'text',
				data : {
					email:$("#Memberemail").val()
				},
				success : function(msg) {
					
					if(msg == "true") {
						checkEmailVal = "T";
						if($("#emailTip")!=null) {
							$("#emailTip").remove();
						}
						console.log($("#emailTip"));
						$("#Memberemail").after("<p id='emailTip' style='color:green;'>此email可以使用</p>");
					} else {
					
						checkEmailVal = "F";
						if($("#emailTip")!=null) {
							$("#emailTip").remove();
						}
						$("#Memberemail").after("<p id='emailTip' style='color:red;'>此email已經註冊,請選擇其他email</p>");
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					
				}
			});
		} else {
			if($("#emailTip")!=null) {
				$("#emailTip").remove();
			}
			$("#Memberemail").after("<label id='emailTip' style='color:red;'>email不符合格式</label>");
		}
	}
	
	//驗證資料
	if($("#chineseName").val()==null || $("#chineseName").val()==""){
		alert("請輸入中文姓名");
		$("#chineseName").focus();
		return false;
//	} else if($("#gender").val()==null || $("#gender").val()=="" || $("#gender").val()==0){
//		alert("請選擇性別");
//		$("#gender").focus();
//		return false;
	} else if($("#idNo").val()!=null && $("#idNo").val().length==10 && !checkTwID($("#idNo").val()) || checkIdNoVal=="F"){
		alert("身份證字號格式錯誤,提醒您,請勿冒用他人身份證字號");
		$("#idNo").focus();
		return false;
	} else if($("#idNo").val()!=null && $("#idNo").val().length==10 && $("#gender").val()=="M" && $("#idNo").val().charAt(1)!="1" || $("#idNo").val()!=null &&  $("#idNo").val().length==10 && $("#gender").val()=="F" && $("#idNo").val().charAt(1)!="2"){
		alert("身份證字號對應性別錯誤,提醒您,請勿冒用他人身份證字號");
		$("#gender").focus();
		return false;
	} else if($("#Memberemail").val()==null || $("#Memberemail").val()==""){
		alert("請輸入電子信箱");
		$("#Memberemail").focus();
		return false;
	} else if(!isEmail($("#Memberemail").val()) || checkEmailVal=="F"){alert(!isEmail($("#Memberemail").val())+","+checkEmailVal);
		alert("電子信箱格式錯誤,請確認妳的電子信箱是不是合法的");
		$("#Memberemail").focus();
		return false;
	} else if($("#cellphone").val()==null || $("#cellphone").val()==""){
		alert("請輸入行動電話");
		$("#cellphone").focus();
		return false;
	} else if(!isCellphone($("#cellphone").val())){
		alert("行動電話格式不正確，請確認行動電話號碼");
		$("#cellphone").focus();
		return false;
	} else if($("#MemberPassword").val()==null || $("#MemberPassword").val()==""){
		alert("請輸入密碼");
		$("#MemberPassword").focus();
		return false;
	} else if($("#MemberPassword").val().length<6){
		alert("密碼必須大於6碼");
		$("#MemberPassword").focus();
		return false;
	} else if($("#checkPassword").val()!=$("#MemberPassword").val()){
		alert("確認密碼需與密碼相同");
		$("#checkPassword").focus();
		return false;
	} 
	
	if($("input[name='approve']:checked").length<=0) {
		alert("請勾選已閱讀會員權利說明");
		$("input[name='approve']").focus();
		return false;
	}
	
// 		$("#educationValue").val($("#education :selected").val());
// 		$("#departmentValue").val($("#department :selected").val());
// 		$("#memberSchool").val($("#education option:selected").text());
// 		$("#memberDepartment").val($("#department option:selected").text());
//	alert("註冊完成");
	if(check == "T") {
		check = "F";
		$("#mainForm").attr("action", "addSubmit");
		$("#mainForm").submit();
//		alert("註冊成功");

	}
	
}
