$(function() {
	new Vue({
		el: '#mainProduct',
		data: {
			// model 屬性
			mainProductList:[],
        	activeIndex: 0 // 用來追踪當前選中的按鈕
		},
		created(){
			this.getMainProductList()
		},
		methods: {
			getMainProductList() {
				axios.post('/holidayDessert/getMainProductList')
				.then(response => {
					this.mainProductList = response.data.result;
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			},
			setActive(index) {
				this.activeIndex = index;
			},
			split(description) {
				return description.split('\n');
			}
		}
	});
	
	new Vue({
		el: '#newArrival',
		data: {
			// model 屬性
			popularList:[],
			newArrivalList:[],
			commentList:[]
		},
		created(){
			this.getNewArrivalList()
		},
		methods: {
			getNewArrivalList() {
				axios.post('/holidayDessert/getNewArrivalList')
				.then(response => {
					this.newArrivalList = response.data.result;
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			}
		}
	});
	new Vue({
		el: '#popular',
		data: {
			// model 屬性
			popularList:[]
		},
		created(){
			this.getPopularList()
		},
		methods: {
			getPopularList() {
				axios.post('/holidayDessert/getPopularList')
				.then(response => {
					this.popularList = response.data.result;
					this.$nextTick(() => {
						this.initializeOwlCarousel();
					});
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			},
			initializeOwlCarousel() {
				var popularCarousel = $('.popular-carousel');
            	// 销毁之前的实例，防止重复初始化
            	popularCarousel.trigger('destroy.owl.carousel');
            	popularCarousel.find('.owl-stage-outer').children().unwrap();
            	popularCarousel.removeClass("owl-center owl-loaded owl-text-select-on");
				popularCarousel.children().each(function(index) {
					$(this).attr('data-position', index);
				});
				popularCarousel.owlCarousel({
					center: true,
					nav: false,
					dots: true,
					loop: true,
					responsiveClass: true,
					responsive: {
						0:		{items: 2},
						480:	{items: 2},
						768:	{items: 3},
						1024:	{items: 5},
						1600:	{items: 7}
					}
				});
				$(document).on('click', '.owl-item>.popular-item', function() {
					popularCarousel.trigger('to.owl.carousel', [$(this).data('position'), 300]);
				});
    		}
		}
	});
	new Vue({
		el: '#comment',
		data: {
			// model 屬性
			commentList:[]
		},
		created(){
			this.getCommentList()
    		window.addEventListener('resize', this.handleResize);
		},
		beforeDestroy() {
			window.removeEventListener('resize', this.handleResize);
			this.destroyOwlCarousel();
		},
		methods: {
			getCommentList() {
				axios.post('/holidayDessert/getCommentList')
				.then(response => {
					this.commentList = response.data.result;
					this.$nextTick(() => {
						this.checkWidth();
					});
				})
				.catch(error => {
					console.log(error);
					this.warning("執行失敗");
				});
			},
			initializeOwlCarousel() {
				var commentMessage = $('.comment-message');
            	// 销毁之前的实例，防止重复初始化
            	commentMessage.trigger('destroy.owl.carousel');
            	commentMessage.find('.owl-stage-outer').children().unwrap();
            	commentMessage.removeClass("owl-center owl-loaded owl-text-select-on");
				commentMessage.children().each(function(index) {
					$(this).attr('data-position', index);
				});
				commentMessage.owlCarousel({
					loop: true,
					nav: false,
					margin: 0,
					autoWidth: true,
					center: true,
					dots: false,
					responsiveClass: true,
					responsive: {
						0:		{items: 1},
						480:	{items: 2},
						768:	{items: 3},
						1024:	{items: 3}
					}
				});
				$(document).on('click', '.owl-item>.commenter-block', function() {
					commentMessage.trigger('to.owl.carousel', [$(this).data('position'), 300]);
				});
    		},
			destroyOwlCarousel() {
				$(this.$el).find(".owl-carousel").trigger('destroy.owl.carousel');
			},
			checkWidth() {
				if (window.innerWidth < 480) {
					this.destroyOwlCarousel();
					$(this.$el).find(".comment-message").removeClass("owl-carousel owl-theme");
				} else {
					this.initializeOwlCarousel();
					$(this.$el).find(".comment-message").addClass("owl-carousel owl-theme");
				}
			},
			handleResize() {
				this.checkWidth();
			}
		}
	});
});


