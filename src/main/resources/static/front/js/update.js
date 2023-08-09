$(() => {
	// 切換頁籤
	$('#member_tabs').on('click', 'button',(event) => {
		if (event.target.nodeName !== 'BUTTON') return
		$(`#member_tabs .active`).removeClass('active')
		$(event.target).addClass('active')
		const currentTab = $(event.target).data(`member-tab`)
		$(`.member-panel.active`).removeClass('active')
		$(`.member-panel[data-member-panel="${currentTab}"]`).addClass('active')
	})
	
	// 新增准考證燈箱 => 原始成績轉換
	const rawField = ['chinese', 'english', 'math_a', 'math_b', 'society', 'nature']
	rawField.forEach((subject) => {
	  $(`#add_${subject}_raw`).keyup((event) => {
	    const score = $(event.target).val()
	    const grade = gradeConverter(subject, score)
	    const gradeText = typeof (grade) === 'number' ? `${grade}級分` : ''
	    $(event.target).siblings('span').text(gradeText)
	  })
	})
	
	function gradeConverter(subject, score) {
	  score = parseFloat($.trim(score))
	  if (isNaN(score)) return ''
	  const CI = {
	    chinese: 5.38,
	    english: 6.23,
	    math_a: 6.03,
	    math_b: 6.03,
	    society: 8.33,
	    nature: 8.27,
	  }
	  if (score <= 0) return 0
	  else if (score > CI[subject] * 14) return 15
	  else return Math.ceil(score / CI[subject])
	}
	
	$('#exam').on('click', (event) => {
	    const container = $(event.target)
	    if (container.hasClass('panel-admission-ticket')) return
	    if (container.hasClass('active') || container.closest('dl').hasClass('active')) return
	    let selElement = undefined
	    if (container.nodeName === 'DL') selElement = event.target
	    else selElement = container.closest('dl')
	    updateMemberExamNum(selElement)
	})
})

var checkIdNoVal = "T";
var checkEmailVal = "F";

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
					$("#Memberemail").after("<label id='emailTip' style='color:green;'>此email可以使用</label>");
				} else {
				
					checkEmailVal = "F";
					if($("#emailTip")!=null) {
						$("#emailTip").remove();
					}
					$("#Memberemail").after("<label id='emailTip' style='color:red;'>此email已經註冊,請選擇其他email</label>");
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
function checkIdNo() {
	if(checkTwID($("#idNo").val())) {
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
	} else {
//		if($("#idTip")!=null) {
//			$("#idTip").remove();
//		}
//		$("#idNo").after("<label id='idTip' style='color:red;'>身份證不符合格式</label>");
	}
}

function update() {

	//驗證資料
	if($("#chineseName").val()==null || $("#chineseName").val()==""){
		alert("請輸入中文姓名");
		$("#chineseName").focus();
		return false;
	} else if($("#gender").val()==null || $("#gender").val()=="" || $("#gender").val()==0){
		alert("請選擇性別");
		$("#gender").focus();
		return false;
	} else if($("#idNo").val()!=null && $("#idNo").val().length==10 && !checkTwID($("#idNo").val()) || checkIdNoVal=="F"){
		alert("身份證字號格式錯誤,提醒您,請勿冒用他人身份證字號");
		$("#idNo").focus();
		return false;
	} else if($("#idNo").val()!=null && $("#idNo").val().length==10 && $("#gender").val()=="M" && $("#idNo").val().charAt(1)!="1" || $("#idNo").val()!=null &&  $("#idNo").val().length==10 && $("#gender").val()=="F" && $("#idNo").val().charAt(1)!="2"){
		alert("身份證字號對應性別錯誤,提醒您,請勿冒用他人身份證字號");
		$("#gender").focus();
		return false;
	}   else if($("#cellphone").val()==null || $("#cellphone").val()==""){
		alert("請輸入行動電話");
		$("#cellphone").focus();
		return false;
	} else if(!isCellphone($("#cellphone").val())){
		alert("行動電話格式不正確，請確認行動電話號碼");
		$("#cellphone").focus();
		return false;
	}  
	
	var link = "/forecast/member/updateSubmit";

	$("#mainForm").attr("action", link);
	$("#mainForm").submit();
	
}

function checkSerialNumber() {
	
	if($("#serial_number").val() == null || $("#serial_number").val() == "" || $("#auth_password").val() == null || $("#auth_password").val() == ""){
		alert("請填寫序號和序號密碼");
		return false;
	}
	
// 		var wishArray = new Array();
// 		$('input:checkbox:checked[name="wish_id"]').each(function(i) { 
// 			wishArray[i] = this.value; 
//  		});
	
	$.ajax({
		url : "/forecast/authorization/checkSerialNumber",
		cache: false,
		async: false,
		type : "POST",
		dataType : 'text',
		data : {
			serial_number : $("#serial_number").val(),
			password : $("#auth_password").val()
		}, success : function(data) {
			if(data == "T" ){
				
				var link = "/forecast/wish/12/select";

				$("#wishForm").attr("action", link);
				$("#wishForm").submit();
				
			}else if(data == "F"){
				alert("序號填寫失敗,請確認序號&密碼是否有誤、准考證是否登錄、身份證是否有填寫");
				return false;
			}else{
				alert("發生異常請重整後重新輸入");
				return false;
			}
		}, error : function(xhr, ajaxOptions, thrownError) {
			alert("序號填寫發生錯誤");
		}
	});
	
}

function addExamNum() {
  $('.grade-panel.gsat').hasClass('active') ? addExamNumGrade() : addExamNumRaw();		
}

function addExamNumGrade() {
  var num_check = /^[0-9]*$/;
  var id_check = /^[A-Z0-9]{10}$/;
  let regex = /^([0-9]){8}/;
  
  var exam_num = $("#add_exam").val() == null ? "" : $.trim($("#add_exam").val());
  var ch = $("#add_chinese").val() == "" ? "0" : $("#add_chinese").val();
  var en = $("#add_english").val() == "" ? "0" : $("#add_english").val();
  var ma = $("#add_math_a").val() == "" ? "0" : $("#add_math_a").val();
  var mb = $("#add_math_b").val() == "" ? "0" : $("#add_math_b").val();
  var soci = $("#add_society").val() == "" ? "0" : $("#add_society").val();
  var natu = $("#add_natural").val() == "" ? "0" : $("#add_natural").val();
  var hear = $("#add_hearing").val() == "" ? "F" : $("#add_hearing").val();
  var apcs_con = $("#add_apcs_concept").val() == null ? "" : $.trim($("#add_apcs_concept").val());
  var apcs_pra = $("#add_apcs_practice").val() == null ? "" : $.trim($("#add_apcs_practice").val());
  
  var getUrlString = location.href;
  var url = new URL(getUrlString);
  var sdno = url.searchParams.get('sd_no');
  var sex = url.searchParams.get('sex');
  var schoolDept = document.querySelector('#schoolDept');
  var sdSex = document.querySelector('#sdSex');
  schoolDept.innerHTML = sdno;
  sdSex.innerHTML = sex;
  
  if (exam_num == "") {
    $("#exam_num").focus();
    alert("請輸入准考證號碼,謝謝!");
    return false;
  }
  
  if (exam_num.length != 8) {
    $("#exam_num").focus();
    alert("准考證號碼長度錯誤，請重新輸入");
    return false;
  }
  
  if (!regex.test(exam_num)) {
	alert("請勿輸入特殊字元");
	return false;
  }
  
  if (ch == "") {
    alert("請選擇國文分數");
    return false;
  }

  if (en == "") {
    alert("請選擇英文分數:"+en);
    return false;
  }

  if (ma == "" || mb == "") {
    alert("請選擇數A或數B分數");
    return false;
  }

  if (soci == "") {
    alert("請選擇社會分數");
    return false;
  }

  if (natu == "") {
    alert("請選擇自然分數");
    return false;
  }

  if (!num_check.test(ch)) {
    alert("國文分數格式錯誤,請重新輸入,謝謝!");
    return false;
  }

  if (!num_check.test(en)) {
    alert("英文分數格式錯誤,請重新輸入,謝謝!");
    return false;
  }

  if (!num_check.test(ma) || !num_check.test(mb)) {
    alert("數A或數B分數格式錯誤,請重新輸入,謝謝!");
    return false;
  }

  if (!num_check.test(soci)) {
    alert("社會分數格式錯誤,請重新輸入,謝謝!");
    return false;
  }

  if (!num_check.test(natu)) {
    alert("自然分數格式錯誤,請重新輸入,謝謝!");
    return false;
  }

  if (apcs_con != "") {
    if (!num_check.test(apcs_con)) {
      alert("APCS觀念題分數格式錯誤,請重新輸入,謝謝!");
      return false;
    }
  } else {
    apcs_con = 0;
  }

  if (apcs_pra != "") {
    if (!num_check.test(apcs_pra)) {
      alert("APCS實作題分數格式錯誤,請重新輸入,謝謝!");
      return false;
    }
  } else {
    apcs_pra = 0;
  }

  let result = checkExamNum(exam_num, ch, en, ma, mb, soci, natu, hear, apcs_con, apcs_pra);
  
  
  if (result == "C") {
    alert("請勿重複建立資料");
    return false;
  } else if (result == "B") {
    if ($("#add_id_no").val() == "") {
      var id_no = prompt("分數輸入錯誤,請輸入考生身份證字號驗證(請務必確實輸入)", "");
      $("#id_no").val(id_no);
    }
    $("#status").val("A");

  } else if (result == "N") {
    $("#status").val("");
  } else {
    $("#status").val(result);
  }

  if (result == "F") {
    alert("您好，因您填寫的准考證驗證失敗,若有疑問請與甄戰同仁做聯繫,聯絡電話：(02)5580-2006");
  } else if ($("#status").val() == "A" || $("#status").val() == "B") {
    alert("您好，因您填寫的准考證資料有誤，目前暫無法使用，請與甄戰同仁做聯繫，並提供您的准考證、身份證或分數照片，以便進行資料驗証，謝謝!!");
  } else if (result == "false") {
    alert("您好，因網站瀏覽人數過多，導致新增准考證失敗，請重新新增准考證");
    return false;
  }

  $("#exam_number").val(exam_num);
  $("#chinese").val(ch);
  $("#english").val(en);
  $("#mathematics_a").val(ma);
  $("#mathematics_b").val(mb);
  $("#society").val(soci);
  $("#naturally").val(natu);
  $("#english_hearing").val(hear);
  $("#apcs_concept").val(apcs_con);
  $("#apcs_practice").val(apcs_pra);
  if ($("#id_no").val() == null || $("#id_no").val() == "") {
    $("#id_no").val($("#add_id_no").val());
  }
  if ($("#schoolDept").val() != null || $("#schoolDept").val() != "") {
    $("#schoolDept").val(sdno);
  }
  if ($("#sdSex").val() != null || $("#sdSex").val() != "") {
    $("#sdSex").val(sex);
  }
  const link = "/forecast/member/addExamNum";

  if(confirm("您好，以下是您輸入的資訊，確認送出後即無法修改。\n准考證:"+exam_num+"\n國:"+ch+" 英:"+en+" 數A:"+ma+" 數B:"+mb+" 社:"+soci+" 自:"+natu+" 英聽:"+hear+" APCS觀念:"+apcs_con+" APCS實作:"+apcs_pra)) {
	  $("#examNumForm").attr("action", link);
	  $("#examNumForm").submit();
  }
}

function addExamNumRaw() {
  const num_check = /^[0-9]*$/

  let exam_num = $('#add_exam').val() == null ? '' : $.trim($('#add_exam').val())
  let chraw = $('#add_chinese_raw').val() == '' ? 0 : $('#add_chinese_raw').val()
  let enraw = $('#add_english_raw').val() == '' ? 0 : $('#add_english_raw').val()
  let maraw = $('#add_math_a_raw').val() == '' ? 0 : $('#add_math_a_raw').val()
  let mbraw = $('#add_math_b_raw').val() == '' ? 0 : $('#add_math_b_raw').val()
  let sociraw = $('#add_society_raw').val() == '' ? 0 : $('#add_society_raw').val()
  let naturaw = $('#add_nature_raw').val() == '' ? 0 : $('#add_nature_raw').val()
  let hear = $('#add_hearing_raw').val() == '' ? 'F' : $('#add_hearing_raw').val()
  let apcs_con = $('#add_apcs_concept_raw').val() == null ? '' : $.trim($('#add_apcs_concept_raw').val())
  let apcs_pra = $('#add_apcs_practice_raw').val() == null ? '' : $.trim($('#add_apcs_practice_raw').val())

  if (exam_num == '') {
    $('#exam_num').focus()
    alert('請輸入准考證號碼,謝謝!')
    return false
  }

  if (exam_num.length != 8) {
    $('#exam_num').focus()
    alert('准考證號碼長度錯誤，請重新輸入')
    return false
  }

  if (chraw == '') {
    alert('請輸入國文分數')
    return false
  }

  if (enraw == '') {
    alert('請輸入英文分數')
    return false
  }

  if (maraw == "" || mbraw == "") {
    alert("請輸入數A或數B分數");
    return false;
  }

  if (sociraw == '') {
    alert('請輸入社會分數')
    return false
  }

  if (naturaw == '') {
    alert('請輸入自然分數')
    return false
  }

  if (!num_check.test(chraw)) {
    alert('國文分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (!num_check.test(enraw)) {
    alert('英文分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (!num_check.test(maraw)) {
    alert('數學分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (!num_check.test(mbraw)) {
    alert('數學分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (!num_check.test(sociraw)) {
    alert('社會分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (!num_check.test(naturaw)) {
    alert('自然分數格式錯誤,請重新輸入,謝謝!')
    return false
  }

  if (apcs_con != '') {
    if (!num_check.test(apcs_con)) {
      alert('APCS觀念題分數格式錯誤,請重新輸入,謝謝!')
      return false
    }
  } else {
    apcs_con = 0
  }

  if (apcs_pra != '') {
    if (!num_check.test(apcs_pra)) {
      alert('APCS實作題分數格式錯誤,請重新輸入,謝謝!')
      return false
    }
  } else {
    apcs_pra = 0
  }

  let result = checkExamNum(exam_num, chraw, enraw, maraw, mbraw, sociraw, naturaw, hear, apcs_con, apcs_pra)

  if (result == 'C') {
    alert('請勿重複建立資料')
    return false
  } else if (result == 'B') {
    if ($('#add_id_no').val() == '') {
      var id_no = prompt('分數輸入錯誤,請輸入考生身份證字號驗證(請務必確實輸入)', '')
      $('#id_no').val(id_no)
    }
    $('#status').val('A')

  } else if (result == 'N') {
    $('#status').val('')
  } else {
    $('#status').val(result)
  }

  if (result == 'F') {
    alert('您好，因您填寫的准考證驗證失敗,若有疑問請與甄戰同仁做聯繫,聯絡電話：(02)5580-2006')
  } else if ($('#status').val() == 'A' || $('#status').val() == 'B') {
    alert('您好，因您填寫的准考證資料有誤，目前暫無法使用，請與甄戰同仁做聯繫，並提供您的准考證、身份證或分數照片，以便進行資料驗証，謝謝!!')
  } else if (result == 'false') {
    alert('您好，因網站瀏覽人數過多，導致新增准考證失敗，請重新新增准考證')
    return false
  }

  const subjectGrade = {}
  const subjectField = ['chinese', 'english', 'math_a', 'math_b', 'society', 'nature']
  subjectField.forEach((subject) => {
    const data = $(`#add_${subject}_grade`).text().replace('級分', '')
    subjectGrade[subject] = data
  })

  $('#exam_number').val(exam_num)
  $('#chinese').val(subjectGrade.chinese)
  $('#chinese_raw').val(chraw)
  $('#english').val(subjectGrade.english)
  $('#english_raw').val(enraw)
  $('#mathematicsA').val(subjectGrade.math_a)
  $('#mathematicsA_raw').val(maraw)
  $('#mathematicsB').val(subjectGrade.math_b)
  $('#mathematicsB_raw').val(mbraw)
  $('#society').val(subjectGrade.society)
  $('#society_raw').val(sociraw)
  $('#naturally').val(subjectGrade.nature)
  $('#naturally_raw').val(naturaw)
  $('#english_hearing').val(hear)
  $('#apcs_concept').val(apcs_con)
  $('#apcs_practice').val(apcs_pra)
  if ($('#id_no').val() == null || $('#id_no').val() == '') {
    $('#id_no').val($('#add_id_no').val())
  }
  const link = '/forecast/member/addExamNum'


  $('#examNumForm').attr('action', link)
  $('#examNumForm').submit()
}

function checkExamNum(exam_num, ch, en, ma, mb, soci, natu, hear, apcs_con, apcs_pra){
// 		alert(apcs_pra);
	var status = false;
	$.ajax({
		url: "/forecast/member/checkExamNum",
		cache: false,
		async: false,
		dataType: "text",
		type: "POST",
		data: {
			member_id : $("#member_id").val(),
			exam_num : exam_num,
			chinese : ch,
			english : en,
			ma : ma,
			mb : mb,
			society : soci,
			natural : natu,
			hearing : hear,
			apcs_concept : apcs_con,
			apcs_practice : apcs_pra
		},
		error: function(xhr) {
			alert("執行失敗");
		},
		success: function(data) {
			
			if(data != null && data != ""){
				status = data;
			}
// 				window.location.reload();

		}
	});
	
	return status;
}

function updateMemberExamNum(selElement){
  if ($(selElement).attr('value') != null) {
    let status = $(selElement).attr('status')

    if (status == "B") {
      if ($("#add_id_no").val() == "") {
        var id_no = prompt("請輸入考生身份證字號驗證(請務必確實輸入)", "");
        $("#id_no").val(id_no);
      }

      if (id_no == null || id_no == "") {
        alert("請輸入身份證");
        location.reload();
        return false;
      } else if (checkTwID(id_no)) {
        status = "A";
      } else {
        alert("身份證格式錯誤");
        location.reload();
        return false;
      }
    }

    if (confirm("確定要修改帳號預設准考證?")) {
      
      $.ajax({
        url: "/forecast/member/updateMemberExamNum",
        cache: false,
        async: false,
        type: "POST",
        dataType: 'text',
        data: {
          exam_num_id: $(selElement).attr('value'),
          exam_num: $(selElement).find('.number').text(),
          status: status,
          id_no: id_no
        }, success: function (data) {
          if (data == "T") {
            alert("修改預設准考證成功");
            location.reload();
          } else {
            alert("修改預設准考證失敗");
          }
        }, error: function (xhr, ajaxOptions, thrownError) {
          alert("修改預設准考證發生錯誤");
        }
      });
    } else {
      location.reload();
    }
  }	
}

function getScore(id) {

	$.ajax({
		url : "/forecast/member/getScore",
		cache: false,
		async: false,
		type : "POST",
		dataType : 'json',
		data : {
			exam_id : id
		}, success : function(data) {
			if(data != null && data.length > 0){

					$("#exam_id").val(id);
					$("#update_chinese").val(data[0].chinese);
					$("#update_english").val(data[0].english);
					$("#update_math").val(data[0].mathematics);
					$("#update_society").val(data[0].society);
					$("#update_natural").val(data[0].naturally);
					$("#update_hearing").val(data[0].english_hearing);
					$("#update_apcs_concept").val(data[0].apcs_concept);
					$("#update_apcs_practice").val(data[0].apcs_practice);

			}
		}, error : function(xhr, ajaxOptions, thrownError) {
			alert("取得分數發生錯誤");
		}
	});
}

function updateExamScore() {
	//exam_id
	//update_
	var num_check = /^[0-9]*$/;
	var id_check = /^[A-Z0-9]{10}$/;
	
	var ch = $("#update_chinese :selected").val() == "" ? 0 : $("#update_chinese :selected").val();
	var en = $("#update_english :selected").val() == "" ? 0 : $("#update_english :selected").val();
	var math = $("#update_math :selected").val() == "" ? 0 : $("#update_math :selected").val();
	var soci = $("#update_society :selected").val() == "" ? 0 : $("#update_society :selected").val();
	var natu = $("#update_natural :selected").val() == "" ? 0 : $("#update_natural :selected").val();
	var hear = $("#update_hearing :selected").val() == "" ? "F" :$("#update_hearing :selected").val();
	var apcs_con = $("#update_apcs_concept").val() == null ? "" : $.trim($("#update_apcs_concept").val());
	var apcs_pra = $("#update_apcs_practice").val() == null ? "" : $.trim($("#update_apcs_practice").val());

	if(!num_check.test(ch)){
		alert("國文分數格式錯誤,請重新輸入,謝謝!");
		return false;
	}
	
	if(!num_check.test(en)){
		alert("英文分數格式錯誤,請重新輸入,謝謝!");
		return false;
	}
	
	if(!num_check.test(math)){
		alert("數學分數格式錯誤,請重新輸入,謝謝!");
		return false;
	}
	
	if(!num_check.test(soci)){
		alert("社會分數格式錯誤,請重新輸入,謝謝!");
		return false;
	}
	
	if(!num_check.test(natu)){
		alert("自然分數格式錯誤,請重新輸入,謝謝!");
		return false;
	}
	
	if(apcs_con != ""){
		if(!num_check.test(apcs_con)){
			alert("APCS觀念題分數格式錯誤,請重新輸入,謝謝!");
			return false;
		}
	}else{
		$("#apcs_concept").val("0");
	}
	
	if(apcs_pra != ""){
		if(!num_check.test(apcs_pra)){
			alert("APCS實作題分數格式錯誤,請重新輸入,謝謝!");
			return false;
		}
	}else{
		$("#apcs_practice").val("0");
	}
	
	$("#chinese").val(ch);
	$("#english").val(en);
	$("#mathematics").val(math);
	$("#society").val(soci);
	$("#naturally").val(natu);
	$("#english_hearing").val(hear);
	$("#apcs_concept").val(apcs_con);
	$("#apcs_practice").val(apcs_pra);
	
	var link = "/forecast/member/updateScoreSubmit";

	$("#examNumForm").attr("action", link);
	$("#examNumForm").submit();
}

function updatePassword() {
	if ($("#oldPW").val() == "") {
		alert("請輸入舊密碼");
		return false;
	}
	
	if ($("#oldPW").val().length < 6) {
		alert("密碼必須大於6碼");
		return false;
	}

	if ($("#memberPassword").val() != "" || $("#checkPassword").val() != "") {
		if ($("#memberPassword").val().length < 6) {
			alert("密碼必須大於6碼");
			$("#memberPassword").focus();
			return false;
		} else if ($("#checkPassword").val() != $("#memberPassword").val()) {
			alert("確認密碼需與密碼相同");
			$("#checkPassword").focus();
			return false;
		}
	} else {
		alert("請輸入新密碼");
		$("#memberPassword").focus();
		return false;
	}
	$.ajax({
		url: "/forecast/member/checkPassword",
		cache: false,
		async: false,
		dataType: "text",
		type: "POST",
		data: {
			oldPW: $("#oldPW").val()
		},
		error: function (xhr) {
			alert("發生錯誤,請重整頁面");
		},
		success: function (data) {
			if (data == "T") {
				$("#password").val($("#memberPassword").val());
				
				var link = "/forecast/member/updatePasswordSubmit";
				
				$("#updatePassForm").attr("action", link);
				$("#updatePassForm").submit();
			} else {
				alert("舊密碼輸入錯誤");
				return false;
			}
		}
	})
}
