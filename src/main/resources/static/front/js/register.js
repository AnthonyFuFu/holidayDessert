
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

//檢查有無重複的身份證字號
//function checkIdNo() {
//	if(checkTwID($("#idNo").val())) {
//		$.ajax({
//			url : "checkIdNo",
//			cache: false,
//			async: false,
//			type : "POST",
//			dataType : 'text',
//			data : {
//				id_no:$("#idNo").val()
//			},
//			success : function(msg) {
//				if(msg == "true") {
//					checkIdNoVal = "T";
//					if($("#idTip")!=null){
//						$("#idTip").remove();
//					}
//					$("#idNo").after("<label id='idTip' style='color:green;'>此身份證可以使用</label>");
//				} else {
//					checkIdNoVal = "F";
//					if($("#idTip")!=null) {
//						$("#idTip").remove();
//					}
//					$("#idNo").after("<label id='idTip' style='color:red;'>此身份證已經註冊,請選擇其他身份證</label>");
//				}
//			},
//			error : function(xhr, ajaxOptions, thrownError) {
//				
//			}
//		});
//	} else {
//		if($("#idTip")!=null) {
//			$("#idTip").remove();
//		}
//		$("#idNo").after("<label id='idTip' style='color:red;'>身份證不符合格式</label>");
//	}
//}

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


//驗證是否為空值
function checkNull(obj, init) {
	if ($.trim(obj.val()) == "" || $.trim(obj.val()) == init) {
		obj.focus();
		return true;
	} else {
		return false;
	}
}

//驗證中英文字數長度
function checkByteLen(string, len) {
	var string_length = 0;
	for(var i=0; i<string.length; i++) {
		if(string[i].match(/[^\x00-\xff]/ig) != null) {
			string_length += 3;
		} else {
			string_length ++;
		}
	}
	if(string_length > parseInt(len)) {
		return true;
	} else {
		return false;
	}
}



//驗證連結網址是否有效
function checkURL(obj) {
	if ($.trim(obj.val()) != "" && obj.val().match(/http(s)?:////([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/
   ) == null) {
	obj.focus();
		return "\n請輸入正確連結網址，EX：http://www.google.com";
	} else {
		return "";
	}
}
//RFC822 正規則
function isEmail(email){
	return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(email);	
}
//手機格式 正規則
function isCellphone(cellphone){
	return /^[09]{2}[0-9]{8}$/.test(cellphone);	
}
//身份證驗證正規則
function checkTwID(id){
	
	//建立字母分數陣列(A~Z)
	var city = new Array(
	1,10,19,28,37,46,55,64,39,73,82, 2,11,
	20,48,29,38,47,56,65,74,83,21, 3,12,30
	)
	id = id.toUpperCase();
	// 使用「正規表達式」檢驗格式
	if (id.search(/^[A-Z](1|2)\d{8}$/i) == -1) {
		alert('基本格式錯誤');
		return false;
	} 
	else {
	//將字串分割為陣列(IE必需這麼做才不會出錯)
		id = id.split('');
	//計算總分
	var total = city[id[0].charCodeAt(0)-65];
	for(var i=1; i<=8; i++){
		total += eval(id[i]) * (9 - i);
	}
	//補上檢查碼(最後一碼)
	total += eval(id[9]);
	
	//檢查比對碼(餘數應為0);
	return ((total%10 == 0 ));
	}
}