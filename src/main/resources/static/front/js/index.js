$(function() {
	function initializeOwlCarousel() {
		$(".owl-carousel").owlCarousel({
			loop: true, // 循環播放
			nav: false, // 顯示點點
			margin: 0,             // 设置为0，以免插件添加额外的间距
			autoWidth: true,       // 启用自动宽度
			center: true,
			dots: false,
			responsiveClass: true,
			responsive: {
				0: {
					items: 1 // 螢幕大小為 0~600 顯示 1 個項目
				},
				480: {
					items: 2 // 螢幕大小為 480~768 顯示 2 個項目
				},
				768: {
					items: 3 // 螢幕大小為 768 以上 顯示 3 個項目
				},
				1024: {
					items: 3 // 螢幕大小為 1024 以上 顯示 3 個項目
				}
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

var popularCarousel = $('.popular-carousel');
popularCarousel.children().each(function(index) {
	$(this).attr('data-position', index);
});
popularCarousel.owlCarousel({
	center: true,
	nav: false, // 顯示點點
	dots: false,
	loop: true,
	responsiveClass: true,
	responsive: {
		0: {
			items: 2
		},
		480: {
			items: 2
		},
		768: {
			items: 3
		},
		1024: {
			items: 5
		},
		1600: {
			items: 7
		}
	}
});
$(document).on('click', '.owl-item>.popular-item', function() {
	var speed = 300;
	popularCarousel.trigger('to.owl.carousel', [$(this).data('position'), speed]);
});
