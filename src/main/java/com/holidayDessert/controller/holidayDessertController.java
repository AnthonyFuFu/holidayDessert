package com.holidayDessert.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidayDessert.model.Authority;
import com.holidayDessert.model.Banner;
import com.holidayDessert.model.Cart;
import com.holidayDessert.model.CompanyInformation;
import com.holidayDessert.model.Coupon;
import com.holidayDessert.model.EmpFunction;
import com.holidayDessert.model.Employee;
import com.holidayDessert.model.MainOrder;
import com.holidayDessert.model.Member;
import com.holidayDessert.model.MemberCoupon;
import com.holidayDessert.model.Message;
import com.holidayDessert.model.News;
import com.holidayDessert.model.OrderDetail;
import com.holidayDessert.model.Product;
import com.holidayDessert.model.ProductCollection;
import com.holidayDessert.model.ProductPic;
import com.holidayDessert.model.Promotion;
import com.holidayDessert.model.PromotionDetail;
import com.holidayDessert.model.ReceiptInformation;
import com.holidayDessert.service.AuthorityService;
import com.holidayDessert.service.BannerService;
import com.holidayDessert.service.CartService;
import com.holidayDessert.service.CompanyInformationService;
import com.holidayDessert.service.CouponService;
import com.holidayDessert.service.EmpFunctionService;
import com.holidayDessert.service.EmployeeService;
import com.holidayDessert.service.MainOrderService;
import com.holidayDessert.service.MemberCouponService;
import com.holidayDessert.service.MemberService;
import com.holidayDessert.service.MessageService;
import com.holidayDessert.service.NewsService;
import com.holidayDessert.service.OrderDetailService;
import com.holidayDessert.service.ProductCollectionService;
import com.holidayDessert.service.ProductPicService;
import com.holidayDessert.service.ProductService;
import com.holidayDessert.service.PromotionDetailService;
import com.holidayDessert.service.PromotionService;
import com.holidayDessert.service.impl.ReceiptInformationService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/")
public class holidayDessertController {

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
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {

		Authority authority = new Authority();
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		System.out.println(authorityList);

		Banner banner = new Banner();
		List<Map<String, Object>> bannerList = bannerService.list(banner);
		System.out.println(bannerList);

		Cart cart = new Cart();
		List<Map<String, Object>> cartList = cartService.list(cart);
		System.out.println(cartList);

		CompanyInformation companyInformation = new CompanyInformation();
		List<Map<String, Object>> companyInformationList = companyInformationService.list(companyInformation);
		System.out.println(companyInformationList);

		Coupon coupon = new Coupon();
		List<Map<String, Object>> couponList = couponService.list(coupon);
		System.out.println(couponList);

		EmpFunction empFunction = new EmpFunction();
		List<Map<String, Object>> empFunctionList = empFunctionService.list(empFunction);
		System.out.println(empFunctionList);

		Employee employee = new Employee();
		List<Map<String, Object>> employeeList = employeeService.list(employee);
		System.out.println(employeeList);

		MainOrder mainOrder = new MainOrder();
		List<Map<String, Object>> mainOrderList = mainOrderService.list(mainOrder);
		System.out.println(mainOrderList);

		Member member = new Member();
		List<Map<String, Object>> memberList = memberService.list(member);
		System.out.println(memberList);

		MemberCoupon memberCoupon = new MemberCoupon();
		List<Map<String, Object>> memberCouponList = memberCouponService.list(memberCoupon);
		System.out.println(memberCouponList);

		Message message = new Message();
		List<Map<String, Object>> messageList = messageService.list(message);
		System.out.println(messageList);

		News news = new News();
		List<Map<String, Object>> newsList = newsService.list(news);
		System.out.println(newsList);

		OrderDetail orderDetail = new OrderDetail();
		List<Map<String, Object>> orderDetailList = orderDetailService.list(orderDetail);
		System.out.println(orderDetailList);

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

		return "front/holidayDessert/index";

	}

}
