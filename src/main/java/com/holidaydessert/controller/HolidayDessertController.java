package com.holidaydessert.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Banner;
import com.holidaydessert.model.Cart;
import com.holidaydessert.model.CompanyInformation;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.EmpFunction;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.MainOrder;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;
import com.holidaydessert.model.Message;
import com.holidaydessert.model.News;
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.model.Product;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.BannerService;
import com.holidaydessert.service.CartService;
import com.holidaydessert.service.CompanyInformationService;
import com.holidaydessert.service.CouponService;
import com.holidaydessert.service.EmpFunctionService;
import com.holidaydessert.service.EmployeeService;
import com.holidaydessert.service.MainOrderService;
import com.holidaydessert.service.MemberCouponService;
import com.holidaydessert.service.MemberService;
import com.holidaydessert.service.MessageService;
import com.holidaydessert.service.NewsService;
import com.holidaydessert.service.OrderDetailService;
import com.holidaydessert.service.ProductCollectionService;
import com.holidaydessert.service.ProductPicService;
import com.holidaydessert.service.ProductService;
import com.holidaydessert.service.PromotionDetailService;
import com.holidaydessert.service.PromotionService;
import com.holidaydessert.service.impl.ReceiptInformationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
@SessionAttributes("memberSession")
@CrossOrigin
//@RestController
@Api(tags = "首頁")
public class HolidayDessertController {
	
//	private final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerGroups.class);
	
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CompanyInformationService companyInformationService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private EmpFunctionService empFunctionService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MainOrderService mainOrderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberCouponService memberCouponService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private NewsService newsService;

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCollectionService productCollectionService;

	@Autowired
	private ProductPicService productPicService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private PromotionDetailService promotionDetailService;

	@Autowired
	private ReceiptInformationService receiptInformationService;

	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "查詢首頁", httpMethod = "GET", notes = "進行查詢首頁")
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId("101");
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		System.out.println(authorityList);
		
		// 公告圖片
		Banner banner = new Banner();
		banner.setNewsId("101");
		List<Map<String, Object>> bannerList = bannerService.frontRandList(banner);
		System.out.println(bannerList);
		
		// 購物車
		Cart cart = new Cart();
		cart.setMemId("201");
		List<Map<String, Object>> cartFrontList = cartService.frontList(cart);
		System.out.println(cartFrontList);
		cart.setSearchText("s9017611");
		List<Map<String, Object>> cartList = cartService.list(cart);
		Integer cartCount = cartService.count(cart);
		System.out.println(cartCount+":"+cartList);
		
		// 公司資訊
		CompanyInformation companyInformation = new CompanyInformation();
		companyInformation.setSearchText("X假日甜點");
		List<Map<String, Object>> companyInformationList = companyInformationService.list(companyInformation);
		Integer companyInformationCount = companyInformationService.getCount(companyInformation);
		System.out.println(companyInformationCount+":"+companyInformationList);
		List<Map<String, Object>> companyInformationFfrontList = companyInformationService.frontList(companyInformation);
		System.out.println(companyInformationFfrontList);
		
		// 優惠券
		Coupon coupon = new Coupon();
		coupon.setSearchText("50");
		List<Map<String, Object>> couponList = couponService.list(coupon);
		Integer couponCount = couponService.getCount(coupon);
		System.out.println(couponCount+":"+couponList);
		
		// 後臺功能
		EmpFunction empFunction = new EmpFunction();
		List<Map<String, Object>> empFunctionList = empFunctionService.list(empFunction);
		System.out.println(empFunctionList);

		Employee employee = new Employee();
		List<Map<String, Object>> employeeList = employeeService.list(employee);
		System.out.println(employeeList);

		MainOrder mainOrder = new MainOrder();
		// mainOrder.setSearchText("100");
		List<Map<String, Object>> mainOrderList = mainOrderService.list(mainOrder);
		Integer mainOrderCount = mainOrderService.getCount(mainOrder);
		System.out.println(mainOrderCount+":"+mainOrderList);

		MemberCoupon memberCoupon = new MemberCoupon();
		memberCoupon.setSearchText("50");
		List<Map<String, Object>> memberCouponList = memberCouponService.list(memberCoupon);
		Integer memberCouponCount = memberCouponService.getCount(memberCoupon);
		System.out.println(memberCouponCount+":"+memberCouponList);
		Member anthonyFu = new Member();
		anthonyFu.setMemId("201");
		memberCoupon.setMemId(anthonyFu.getMemId());
		List<Map<String, Object>> frontMemberCouponList = memberCouponService.listMemberCoupon(memberCoupon);
		System.out.println("anthonyFu:"+frontMemberCouponList);

		Member member = new Member();
		member.setSearchText("嘉");
		List<Map<String, Object>> memberList = memberService.list(member);
		Integer memberCount = memberService.getCount(member);
		System.out.println(memberCount+":"+memberList);

		Message message = new Message();
		List<Map<String, Object>> messageList = messageService.list(message);
		System.out.println(messageList);

		News news = new News();
		news.setSearchText("下");
		List<Map<String, Object>> newsList = newsService.list(news);
		Integer newsCount = newsService.getCount(news);
		System.out.println(newsCount+":"+newsList);
		news.setStart("1");
		news.setLength("2");
		List<Map<String, Object>> newsFrontList = newsService.frontList(news);
		System.out.println(newsFrontList);
		List<Map<String, Object>> newsRandList = newsService.frontRandList(news);
		System.out.println(newsRandList);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setSearchText("2 leo");
		List<Map<String, Object>> orderDetailList = orderDetailService.list(orderDetail);
		Integer orderDetailCount = orderDetailService.getCount(orderDetail);
		System.out.println(orderDetailCount+":"+orderDetailList);
		orderDetail.setOrdId("1");
		OrderDetail oneOrderDetail = orderDetailService.data(orderDetail);
		System.out.println(oneOrderDetail.toString());

		Product product = new Product();
		List<Map<String, Object>> productList = productService.list(product);
		System.out.println(productList);

		ProductCollection productCollection = new ProductCollection();
		List<Map<String, Object>> productCollectionList = productCollectionService.list(productCollection);
		System.out.println(productCollectionList);

		ProductPic productPic = new ProductPic();
		List<Map<String, Object>> productPicList = productPicService.list(productPic);
		System.out.println(productPicList);

		Promotion promotion = new Promotion();
		List<Map<String, Object>> promotionList = promotionService.list(promotion);
		System.out.println(promotionList);

		PromotionDetail promotionDetail = new PromotionDetail();
		List<Map<String, Object>> promotionDetailList = promotionDetailService.list(promotionDetail);
		System.out.println(promotionDetailList);

		ReceiptInformation receiptInformation = new ReceiptInformation();
		List<Map<String, Object>> receiptInformationList = receiptInformationService.list(receiptInformation);
		System.out.println(receiptInformationList);
		
		System.out.println("index");
		
		return "front/index";

	}
	
}
