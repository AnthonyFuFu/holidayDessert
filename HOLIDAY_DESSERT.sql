
	create database if not exists holiday_dessert;
	use holiday_dessert;

	drop table if exists product_pic;
	drop table if exists message;
	drop table if exists promotion_detail;
	drop table if exists product;
	drop table if exists product_collection;
	drop table if exists member_coupon;
	drop table if exists promotion;
	drop table if exists coupon;
	drop table if exists main_order;
	drop table if exists cart;
	drop table if exists receipt_information;
	drop table if exists member;
	drop table if exists authority;
	drop table if exists emp_function;
	drop table if exists employee;
	drop table if exists banner;
	drop table if exists news;

	-- ================== create table(會員）================== --

	create table member(
	mem_id int auto_increment not null primary key,
	mem_name varchar(45) not null,
	mem_account varchar(20) not null,
	mem_password varchar(20) not null,
	mem_gender char(1) not null,
	mem_phone varchar(10) not null,
	mem_email varchar(40) not null,
	mem_address varchar(100),
	mem_birthday date not null,
	mem_status tinyint(1) default(0),
	constraint unikey_mem_account unique(mem_account)
	)auto_increment=201;
	insert into member(mem_name, mem_account, mem_password, mem_gender, mem_phone, mem_email,mem_address, mem_birthday)
	values  ('傅寶貝', 'fu830917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',str_to_date('1994-09-17','%y-%m-%d')),
			('嘉寶貝', 'wu861125', '861125', 'f', '0988000000','zoe861125@gmail.com','台北市中正區博愛路36號',str_to_date('1997-11-25','%y-%m-%d')),
	        ('傅貝', 'fu30917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',str_to_date('1994-09-17','%y-%m-%d')),
	        ('傅寶', 'fu83917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',str_to_date('1994-09-17','%y-%m-%d')),
	        ('傅', 'fu83097', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',str_to_date('1994-09-17','%y-%m-%d'))
	        ;

	-- 常用收貨資訊 --
	create table receipt_information(
	rcp_id int auto_increment not null primary key,
	mem_id int not null,
	rcp_name varchar(45) not null,
	rcp_cvs varchar(255) ,
	rcp_address varchar(255) ,
	rcp_phone varchar(10) not null,
	constraint receipt_information_member_fk foreign key (mem_id) references member(mem_id)
	);
	insert into receipt_information(mem_id,rcp_name,rcp_cvs,rcp_phone)
	values ('201','傅寶貝','全家中壢中山店','0999000000');

	-- ================== create table(管理員相關）================== --
	        
	-- 管理員 --
	create table employee(
	emp_id int auto_increment not null primary key,
	emp_name varchar(20) not null,
	emp_phone varchar(10) not null,
	emp_picture longblob,
	emp_account varchar(20) not null,
	emp_password varchar(20) not null,
	emp_level tinyint(1) not null,
	emp_status tinyint(1) not null default '0',
	emp_hiredate date not null default (current_date),
	constraint unikey_emp_account unique(emp_account)
	)auto_increment=101;
	insert into employee(emp_name, emp_phone, emp_account, emp_password, emp_level)  
	values  ('傅寶貝', '0912345678','holidaydessert101', 'emppassword1','0'),
			('嘉寶貝', '0987654321','holidaydessert102', 'emppassword2','0');

	-- 功能-- 
	create table emp_function(
	func_id int auto_increment not null primary key,
	func_name varchar(40) not null
	);
	insert into emp_function(func_name)  
	values  ('客服聊天紀錄管理'),
			('商品管理');
	        
	-- 管理員權限 --
	 create table authority(
	emp_id int not null,
	func_id int not null,
	constraint authority_employee_fk foreign key (emp_id) references employee(emp_id),
	constraint authority_emp_function_fk foreign key (func_id) references emp_function(func_id), 
	constraint pk_authority_emp_id_func_id primary key (emp_id, func_id)
	);
	insert into authority(emp_id, func_id)
	values  (101, 1),
			(101,2);

	-- 最新消息 --
	create table news(
	news_id int auto_increment not null primary key,
	news_name varchar(50),
	news_content varchar(500),
	news_status tinyint(1) not null default '0',
	news_time datetime not null default current_timestamp
	)auto_increment=101;

	insert into news(news_id,news_name,news_content)
	values ('101','慶祝父親節','慶祝父親節，全館88折'),
		   ('102','歡慶中元節','歡慶中元節，全館9折'),
	       ('103','光棍節快樂','光棍節快樂，全館1.1折起');

	-- banner --
	create table banner(
	ban_id	int auto_increment not null primary key,
	news_id	 int not null,
	ban_pic longblob,
	constraint banner_news_fk foreign key (news_id) references news(news_id)
	);

	insert into banner(news_id)
	values ('101');

	-- ================== create table(客服相關）================== --

	-- 客服聊天紀錄 --
	create table message(
		msg_id int auto_increment not null primary key,
		emp_id int not null,
		mem_id int not null,
		msg_content varchar(3000),
		msg_time datetime default current_timestamp on update current_timestamp not null,
		msg_direction tinyint(1) not null,
	    msg_pic longblob,
	    constraint message_employee_fk foreign key (emp_id) references employee(emp_id),
	    constraint message_member_fk foreign key (mem_id) references member(mem_id)
	);

	insert into message (emp_id,mem_id,msg_content,msg_direction)
	value(101,201,"hi",0),
		 (101,201,"hello",0);

	-- 商品分類 --
	create table product_collection(
		pdc_id int auto_increment not null primary key,
	    pdc_name varchar(10) not null
	);

	insert into product_collection(pdc_name)
	values ('可麗路');
	-- 商品 --
	create table product(
		pd_id int auto_increment not null primary key,
	    pdc_id int not null,
	    pd_name varchar(30) not null unique, 
	    pd_price int not null,
	    pd_description varchar(200),
	    pd_status tinyint(1) not null default 0,
	    constraint product_product_collection_fk foreign key (pdc_id) references product_collection(pdc_id)
	)auto_increment=4001;
	insert into product(pdc_id, pd_name, pd_price, pd_description, pd_status)
	values (1, '奶茶風味可麗露', 300, 'nnnnnnnnn', 0);

	-- 商品圖片 --
	create table product_pic (
		pd_pic_id int auto_increment not null primary key,
	    pd_id int,
	    pd_pic longblob,
	    constraint product_pic_product_fk foreign key (pd_id) references product(pd_id)
	);
	insert into product_pic(pd_id)
	values (4001);
	       

	-- ================== create table(優惠相關）================== --

	-- 活動優惠方案 --
	 create table promotion(
	pm_id int not null primary key auto_increment,
	pm_name varchar(45) not null,
	pm_description varchar(255),
	pm_discount decimal(3,2) not null,
	pm_status tinyint(1) not null
	);

	insert into promotion(pm_name,pm_description,pm_discount,pm_status)
	values('中秋節特價','所有常溫品項79折',0.79,1);
	 
	-- 優惠活動明細 --
	create table promotion_detail(
	pmd_id int auto_increment not null primary key,
	pd_id int not null,
	pm_id int not null,
	pmd_start datetime,
	pmd_end datetime,
	pmd_pd_discount_price int ,
	constraint promotion_detail_promotion_fk foreign key (pm_id) references promotion(pm_id),
	constraint promotion_detail_product_fk foreign key (pd_id) references product(pd_id));

	insert into promotion_detail(pm_id,pd_id,pmd_start,pmd_end,pmd_pd_discount_price)
	values(1,4001,str_to_date('2022-08-20','%y-%m-%d'), str_to_date('2022-08-31','%y-%m-%d'),399);

	-- 優惠券種類 --
	create table coupon(
		cp_id int auto_increment not null primary key,
	    cp_name varchar(45) not null,
	    cp_discount int not null,
	    cp_status tinyint not null,
	    cp_pic longblob
	);
	insert into coupon(cp_name, cp_discount, cp_status)
	values ('50元購物金', 50, 1),
	       ('10元折價券', 10, 0);
	 
	-- 會員優惠券 --
	create table member_coupon(
		mem_cp_id int auto_increment not null primary key,
	    mem_id int not null,
	    cp_id int not null,
	    mem_cp_start datetime not null,
	    mem_cp_end datetime not null,
	    mem_cp_status tinyint not null,
	    mem_cp_record datetime,
	    constraint member_coupon_member_fk foreign key (mem_id) references member(mem_id),
	    constraint member_coupon_coupon_fk foreign key (cp_id) references coupon(cp_id)
	);
	insert into member_coupon(mem_id, cp_id, mem_cp_start,mem_cp_end, mem_cp_status, mem_cp_record)
	values (201, 1, str_to_date('2022-08-31','%y-%m-%d'),str_to_date('2022-10-31','%y-%m-%d'), 1, null);

	-- ================== create table(商品相關）================== --

	-- 購物車-- 
	create table cart(
		mem_id int not null,
	    pd_id int not null,
	    cart_pd_quantity int not null,
	    constraint cart_member_fk foreign key (mem_id) references member(mem_id),
	    constraint cart_product_fk foreign key (pd_id) references product(pd_id),
	    primary key(mem_id, pd_id)
	);
	insert into cart(mem_id, pd_id, cart_pd_quantity)
	values (201, 4001, 3);

	-- 訂單 --
	create table main_order(
		ord_id int auto_increment not null primary key,
	    mem_id int not null,
	    mem_cp_id int,
	    ord_subtotal int not null,
	    ord_total int not null,
	    ord_status tinyint(1) not null default 0,
	    ord_create datetime default current_timestamp,
	    ord_recipient varchar(20) not null,
	    ord_recipient_phone varchar(10) not null,
	    ord_payment tinyint(1) not null,
	    ord_delivery tinyint(1) not null,
	    ord_address varchar(100) not null,
	    ord_note varchar(100),
	    ord_delivery_fee int not null,
	    constraint main_order_member_fk foreign key (mem_id) references member(mem_id),
	    constraint main_order_member_coupon_fk foreign key (mem_cp_id) references member_coupon(mem_cp_id)
	);
	insert into main_order(mem_id, mem_cp_id, ord_subtotal, ord_total, ord_recipient, ord_recipient_phone, ord_payment, ord_delivery, ord_delivery_fee ,ord_address)
	values (201, null, 270, 270, 'leoblue', '0987654321', 1, 1,'100', '桃園');
	       
	-- 訂單明細 --
	create table order_detail(
		ord_id int not null,
	    pd_id int not null,
	    ordd_number int not null,
	    ordd_price int not null,
	    ordd_discount_price int not null,
	    constraint order_detail_main_order_fk foreign key (ord_id) references main_order(ord_id),
	    constraint order_detail_product_fk foreign key (pd_id) references product(pd_id),
		primary key(ord_id, pd_id)
	);
	insert into order_detail(ord_id, pd_id, ordd_number, ordd_price, ordd_discount_price)
	values (1, 4001, 3, 300, 270);

	-- 公司資訊 --
	create table company_information(
		com_id int auto_increment not null primary key,
	    com_name varchar(45) not null,
	    com_address varchar(45) not null,
	    com_phone varchar(10) not null,
	    com_memo varchar(500) not null
	);
	insert into company_information(com_name, com_address, com_phone, com_memo)
	values ('假日甜點', '桃園市楊梅區甡甡路646號', '0981023222', '享受假日時光');

