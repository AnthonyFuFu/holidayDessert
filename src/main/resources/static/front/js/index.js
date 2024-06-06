$(function() {
	new Vue({
		el: '#newArrival',
		data: {
			// model 屬性
			popularList:[],
			newArrivalList:[]
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
					var speed = 300;
					popularCarousel.trigger('to.owl.carousel', [$(this).data('position'), speed]);
				});
    		}
		}
	});
	
	function initializeOwlCarousel() {
		$(".owl-carousel").owlCarousel({
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
	}
	function destroyOwlCarousel() {
		$(".owl-carousel").trigger('destroy.owl.carousel');
	}
	function checkWidth() {
		if ($(window).width() < 480) {
			destroyOwlCarousel();
			$(".comment-message").removeClass("owl-carousel owl-theme"); // 移除 Owl Carousel 的 class
		} else {
			initializeOwlCarousel();
			$(".comment-message").addClass("owl-carousel owl-theme"); // 添加 Owl Carousel 的 class
		}
	}
	checkWidth(); // 初次检查
	$(window).resize(function() {
		checkWidth(); // 窗口大小改变时再次检查
	});
});

