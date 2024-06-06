$(function () {
	new Vue({
		el: '#verification',
		data: {
			// model 屬性
			memEmail: ''
		},
		methods: {
			isEmail(email) {
				//RFC822 正規則
				return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(email);	
			},
			checkForm() {
				//驗證資料
				if (this.memEmail == null || this.memEmail == '') {
					this.warning("請輸入電子信箱");
					this.$refs.memEmail.focus();
					return false;
				} else if (!this.isEmail(this.memEmail)) {
					this.warning("電子信箱格式錯誤,請確認妳的電子信箱是不是合法的");
					this.$refs.memEmail.focus();
					return false;
				}
				return true;
			}
			,reSendEmail() {
				if (this.checkForm()) {
					axios.post('/holidayDessert/member/reSendEmail', null, {
						params:{
							memEmail: this.memEmail
						}
					})
					.then(response => {
						if (response.data.STATUS == "F") {
							this.warning(response.data.MSG);
						} else if (response.data.STATUS == "RS") {
							this.success(response.data.MSG);
						} else {
							this.success(response.data.MSG);
						}
					})
					.catch(error => {
						console.log(error);
						this.warning("執行失敗");
					});
				}
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
		}
	});
});
