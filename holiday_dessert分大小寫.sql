CREATE DATABASE IF NOT EXISTS holiday_dessert;
USE holiday_dessert;

DROP TABLE IF EXISTS product_pic;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS chat_room;
DROP TABLE IF EXISTS promotion_detail;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_collection;
DROP TABLE IF EXISTS member_coupon;
DROP TABLE IF EXISTS promotion;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS main_order;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS receipt_information;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS emp_function;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS banner;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS company_information;
DROP TABLE IF EXISTS order_detail;
-- ================== CREATE TABLE(會員）================== --

CREATE TABLE member(
MEM_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
MEM_NAME VARCHAR(45) NOT NULL,
MEM_ACCOUNT VARCHAR(50) NOT NULL,
MEM_PASSWORD VARCHAR(20) NOT NULL,
MEM_GENDER CHAR(1),
MEM_PHONE VARCHAR(20),
MEM_EMAIL VARCHAR(50) NOT NULL,
MEM_ADDRESS VARCHAR(100),
MEM_BIRTHDAY DATE,
MEM_STATUS INT(1) DEFAULT(1),
MEM_VERIFICATION_STATUS INT(1) DEFAULT(1),
MEM_VERIFICATION_CODE VARCHAR(100),
MEM_GOOGLE_UID VARCHAR(100),
CONSTRAINT unikey_MEM_ACCOUNT unique(MEM_ACCOUNT)
);
INSERT INTO member(MEM_NAME, MEM_ACCOUNT, MEM_PASSWORD, MEM_GENDER, MEM_PHONE, MEM_EMAIL,MEM_ADDRESS, MEM_BIRTHDAY,MEM_STATUS,MEM_VERIFICATION_STATUS,MEM_GOOGLE_UID)
VALUES  ('傅勝宏', 'FU830917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'),1,1,NULL),
		('嘉', 'WU861125', '861125', 'f', '0988000000','zoe861125@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1997-11-25','%Y-%m-%d'),1,1,NULL),
        ('貝', 'FU30917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'),1,1,NULL),
        ('傅', 'FU83917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'),1,1,NULL),
        ('傅', 'FU83097', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'),1,1,NULL)
        ;

-- 常用收貨資訊 --
create table receipt_information(
RCP_ID int auto_increment not null primary key,
MEM_ID int not null,
RCP_NAME varchar(45) not null,
RCP_CVS varchar(255) ,
RCP_ADDRESS varchar(255) ,
RCP_PHONE varchar(20) not null,
constraint receipt_information_member_fk foreign key (MEM_ID) references member(MEM_ID)
);
insert into receipt_information(MEM_ID,RCP_NAME,RCP_CVS,RCP_PHONE)
values ('1','傅勝宏','全家中壢中山店','0999000000');

-- ================== CREATE TABLE(管理員相關）================== --

-- 部門 --
CREATE TABLE department(
DEPT_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
DEPT_NAME VARCHAR(30) NOT NULL,
DEPT_LOC VARCHAR(100)
);
INSERT INTO department(DEPT_NAME, DEPT_LOC)
VALUES  ('資訊部', '桃園市'),
		('業務部', '桃園市');

-- 管理員 --
CREATE TABLE employee(
EMP_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
DEPT_ID INT(10),
EMP_NAME VARCHAR(20) NOT NULL,
EMP_PHONE VARCHAR(20) NOT NULL,
EMP_JOB VARCHAR(30) NOT NULL,
EMP_SALARY INT(10) NOT NULL,
EMP_PICTURE VARCHAR(300),
EMP_IMAGE VARCHAR(100),
EMP_ACCOUNT VARCHAR(20) NOT NULL,
EMP_PASSWORD VARCHAR(20) NOT NULL,
EMP_EMAIL VARCHAR(40) NOT NULL,
EMP_LEVEL INT(2) NOT NULL,
EMP_STATUS INT(1) NOT NULL default '1',
EMP_HIREDATE date not null DEFAULT (CURRENT_DATE),
EMP_THEME VARCHAR(40),
CONSTRAINT unikey_EMP_ACCOUNT unique(EMP_ACCOUNT),
CONSTRAINT employee_department_fk foreign key (DEPT_ID) references department(DEPT_ID)
);
INSERT INTO employee(EMP_NAME, DEPT_ID, EMP_PHONE, EMP_JOB, EMP_SALARY, EMP_PICTURE, EMP_IMAGE, EMP_ACCOUNT, EMP_PASSWORD, EMP_EMAIL, EMP_LEVEL, EMP_STATUS) 
VALUES  ('傅勝宏', '1', '0912345678', '軟體工程師', '46250','holidayDessert/admin/upload/images/employee/user.jpg','user.jpg','holidaydessert101', 'emppassword1','s9017688@yahoo.com.tw','0','1'),
		('嘉嘉', '2', '0987654321', '行銷+美編', '31500','holidayDessert/admin/upload/images/employee/user.jpg','user.jpg','holidaydessert102', 'emppassword2','zoe861125@gmail.com','0','1');
        
-- 功能-- 
CREATE TABLE emp_function(
FUNC_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
FUNC_NAME VARCHAR(40) NOT NULL,
FUNC_LAYER VARCHAR(5),
FUNC_PARENT_ID VARCHAR(5),
FUNC_LINK VARCHAR(100),
FUNC_STATUS INT(1) NOT NULL default '1',
FUNC_ICON VARCHAR(50)
);
INSERT INTO emp_function(FUNC_NAME,FUNC_LAYER,FUNC_PARENT_ID,FUNC_LINK,FUNC_STATUS,FUNC_ICON)
VALUES ('公司管理','1','0','/holidayDessert/admin/company/list',1,'business'),
	('員工管理','1','0','/holidayDessert/admin/employee/list',1,'group_add'),
	('會員管理','1','0','/holidayDessert/admin/member/list',1,'people'),
	('訂單管理','1','0','/holidayDessert/admin/order/list',1,'payment'),
	('商品管理','1','0','/holidayDessert/admin/product/list',1,'shop'),
	('優惠活動管理','1','0','/holidayDessert/admin/promotion/list',1,'event'),
	('優惠券管理','1','0','/holidayDessert/admin/coupon/list',1,'card_giftcard'),
	('最新消息管理','1','0','/holidayDessert/admin/news/list',1,'fiber_new');

-- 管理員權限 --
 CREATE TABLE authority(
EMP_ID INT NOT NULL,
FUNC_ID INT NOT NULL,
AUTH_STATUS INT(1) NOT NULL default '1',
CONSTRAINT authority_employee_FK FOREIGN KEY (EMP_ID) references employee(EMP_ID),
CONSTRAINT authority_emp_function_FK FOREIGN KEY (FUNC_ID) references emp_function(FUNC_ID), 
CONSTRAINT PK_authority_emp_ID_FUNC_ID PRIMARY KEY (EMP_ID, FUNC_ID)
);

INSERT INTO authority(EMP_ID, FUNC_ID, AUTH_STATUS)
VALUES  (1,1,1),
		(1,2,1),
		(1,3,1),
		(1,4,1),
		(1,5,1),
		(1,6,1),
		(1,7,1),
		(1,8,1),
        (2,1,0),
		(2,2,0),
		(2,3,1),
		(2,4,1),
		(2,5,1),
		(2,6,1),
		(2,7,1),
		(2,8,1);

-- 最新消息 --
create table news(
NEWS_ID int auto_increment not null primary key,
PM_ID int,
NEWS_NAME varchar(50),
NEWS_CONTENT varchar(500),
NEWS_STATUS INT(1) NOT NULL default '1',
NEWS_START datetime,
NEWS_END datetime,
NEWS_CREATE datetime not null default current_timestamp
);

insert into news(PM_ID,NEWS_ID,NEWS_NAME,NEWS_CONTENT,NEWS_START,NEWS_END)
VALUES (null,'1','慶祝父親節','慶祝父親節，全館88折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59')),
	   (2,'2','歡慶中元節','歡慶中元節，全館9折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59')),
       (null,'3','中秋節快樂','中秋節快樂，全館79折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'));

-- BANNER --
create table banner(
BAN_ID	int auto_increment not null primary key,
NEWS_ID	 int not null,
BAN_PICTURE VARCHAR(300),
BAN_IMAGE VARCHAR(100),
CONSTRAINT banner_news_FK FOREIGN KEY (NEWS_ID) references news(NEWS_ID)
);

insert into banner(NEWS_ID)
VALUES ('1');

-- ================== CREATE TABLE(客服相關）================== --
-- 聊天室 --
create table chat_room(
	ROOM_ID int auto_increment not null primary key,
	ROOM_URL VARCHAR(300),
    ROOM_STATUS INT(1) not null,
    ROOM_UPDATE_STATUS INT(1) not null,
    ROOM_LAST_UPDATE datetime
);
insert into chat_room (ROOM_URL,ROOM_STATUS,ROOM_UPDATE_STATUS,ROOM_LAST_UPDATE)
value('chatRoomUrl',1,0,NOW());

-- 客服聊天紀錄 --
create table message(
	MSG_ID int auto_increment not null primary key,
	EMP_ID int not null,
	MEM_ID int not null,
	ROOM_ID int not null,
	MSG_CONTENT varchar(3000),
	MSG_TIME datetime default current_timestamp on update current_timestamp not null,
	MSG_DIRECTION INT(1) not null,
    MSG_PICTURE VARCHAR(300),
    MSG_IMAGE VARCHAR(100),
    constraint message_employee_fk foreign key (EMP_ID) references employee(EMP_ID),
    constraint message_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint message_chat_room_fk foreign key (ROOM_ID) references chat_room(ROOM_ID)
);

insert into message (EMP_ID,MEM_ID,ROOM_ID,MSG_CONTENT,MSG_DIRECTION)
value(1,1,1,"HI",0),
	 (1,1,1,"Hello",0);

-- 商品分類 --
create table product_collection(
	PDC_ID int auto_increment not null primary key,
    PDC_NAME varchar(10) not null,
    PDC_KEYWORD varchar(200),
    PDC_STATUS INT(2) not null default 1
);

insert into product_collection(PDC_NAME,PDC_KEYWORD)
values ('可麗路','巧克力 起司 抹茶')
	  ,('可頌','巧克力 楓糖 蜂蜜 紅豆');
-- 商品 --
create table product(
	PD_ID int auto_increment not null primary key,
    PDC_ID int not null,
    PD_NAME varchar(30) not null unique, 
    PD_PRICE int not null,
    PD_DESCRIPTION varchar(200),
    PD_DISPLAY_QUANTITY int,
    PD_STATUS INT(2) not null default 1,
    PD_IS_DEL INT(1) not null default 0,
    PD_CREATE_BY varchar(20) ,
	PD_CREATE_TIME datetime ,
	PD_UPDATE_BY varchar(20) ,
	PD_UPDATE_TIME datetime,
    constraint product_product_collection_fk FOREIGN KEY (PDC_ID) REFERENCES product_collection(PDC_ID)
);
insert into product(PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, PD_STATUS, PD_IS_DEL,
					PD_CREATE_BY,PD_CREATE_TIME,PD_UPDATE_BY,PD_UPDATE_TIME)
values (1, '奶茶風味可麗露', 300, '奶茶風味可麗露最好吃', 2, 1, 0,'嘉',NOW(),'嘉',NOW()),
       (1, '抹茶風味可麗露', 500, '抹茶風味可麗露最好吃', 2, 1, 0,'嘉',NOW(),'嘉',NOW());

-- 商品圖片 --
create table product_pic (
	PD_PIC_ID int auto_increment not null primary key,
    PD_ID int,
    PD_PIC_SORT int,
    PD_PICTURE VARCHAR(300),
    PD_IMAGE VARCHAR(100),
    constraint product_pic_product_fk foreign key (PD_ID) references product(PD_ID)
);
insert into product_pic(PD_ID, PD_PIC_SORT)
values (1, 1),(1, 2),(1, 3),(2, 1),(2, 3),(2, 2);

-- ================== CREATE TABLE(優惠相關）================== --

-- 活動優惠方案 --
 create table promotion(
PM_ID int not null primary key auto_increment,
PM_NAME varchar(45) not null,
PM_DESCRIPTION varchar(255),
PM_DISCOUNT decimal(3,2),
PM_REGULARLY INT(1) not null,
PM_STATUS INT(1) not null,
PM_START datetime,
PM_END datetime
);

insert into promotion(PM_NAME,PM_DESCRIPTION,PM_DISCOUNT,PM_REGULARLY,PM_STATUS,PM_START,PM_END)
values('單品特價','特價商品最優惠',1.00,1,1,null,null),
	  ('中秋節特價','全館79折',0.79,1,1,CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'));

-- 優惠活動明細 --
create table promotion_detail(
PMD_ID int auto_increment not null primary key,
PD_ID int not null,
PM_ID int not null,
PMD_START datetime,
PMD_END datetime,
PMD_PD_DISCOUNT_PRICE int ,
constraint promotion_detail_promotion_fk foreign key (PM_ID) references promotion(PM_ID),
constraint promotion_detail_product_fk foreign key (PD_ID) references product(PD_ID));

insert into promotion_detail(PM_ID,PD_ID,PMD_START,PMD_END,PMD_PD_DISCOUNT_PRICE)
values(2,1,CONCAT(CURDATE(), ' 00:00:00'), str_to_date('2022-08-31','%Y-%m-%d'),237);

-- 優惠券種類 --
create table coupon(
	CP_ID int auto_increment not null primary key,
    CP_NAME varchar(45) not null,
    CP_DISCOUNT int not null,
    CP_STATUS INT(1) not null,
    CP_PICTURE VARCHAR(300),
    CP_IMAGE VARCHAR(100)
);
insert into coupon(CP_NAME, CP_DISCOUNT, CP_STATUS)
values ('50元購物金', 50, 1),
       ('10元折價券', 10, 0);
       
-- 會員優惠券 --
create table member_coupon(
	MEM_CP_ID int auto_increment not null primary key,
    MEM_ID int not null,
    CP_ID int not null,
    MEM_CP_START datetime not null,
    MEM_CP_END datetime not null,
    MEM_CP_STATUS INT(1) not null,
    MEM_CP_RECORD datetime,
    constraint member_coupon_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint member_coupon_coupon_fk foreign key (CP_ID) references coupon(CP_ID)
);
insert into member_coupon(MEM_ID, CP_ID, MEM_CP_START,MEM_CP_END, MEM_CP_STATUS, MEM_CP_RECORD)
values (1, 1, CONCAT(CURDATE(), ' 00:00:00'),str_to_date('2022-10-31','%Y-%m-%d'), 1, null);

-- ================== CREATE TABLE(商品相關）================== --

-- 購物車-- 
create table cart(
	MEM_ID int not null,
    PD_ID int not null,
    CART_PD_QUANTITY int not null,
    constraint cart_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint cart_product_fk foreign key (PD_ID) references product(PD_ID),
    primary key(MEM_ID, PD_ID)
);
insert into cart(MEM_ID, PD_ID, CART_PD_QUANTITY)
values (1, 1, 3);

-- 訂單 --
create table main_order(
	ORD_ID int auto_increment not null primary key,
    MEM_ID int not null,
    MEM_CP_ID int,
    ORD_SUBTOTAL int not null,
    ORD_TOTAL int not null,
    ORD_STATUS INT(1) not null default 0,
    ORD_CREATE datetime default current_timestamp,
    ORD_RECIPIENT varchar(20) not null,
    ORD_RECIPIENT_PHONE varchar(20) not null,
    ORD_PAYMENT INT(2) not null,
    ORD_DELIVERY INT(2) not null,
    ORD_ADDRESS varchar(100) not null,
    ORD_NOTE varchar(100),
    ORD_DELIVERY_FEE int not null,
    constraint main_order_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint main_order_member_coupon_fk foreign key (MEM_CP_ID) references member_coupon(MEM_CP_ID)
);
insert into main_order(MEM_ID, MEM_CP_ID, ORD_SUBTOTAL, ORD_TOTAL, ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_DELIVERY_FEE ,ORD_ADDRESS)
values (1, null, 270, 270, 'leoblue', '0987654321', 1, 1,'100', '桃園');
       
-- 訂單明細 --
create table order_detail(
	ORD_ID int not null,
    PD_ID int not null,
    ORDD_NUMBER int not null,
    ORDD_PRICE int not null,
    ORDD_DISCOUNT_PRICE int not null,
    constraint order_detail_main_order_fk foreign key (ORD_ID) references main_order(ORD_ID),
    constraint order_detail_product_fk foreign key (PD_ID) references product(PD_ID),
	primary key(ORD_ID, PD_ID)
);
insert into order_detail(ORD_ID, PD_ID, ORDD_NUMBER, ORDD_PRICE, ORDD_DISCOUNT_PRICE)
values (1, 1, 3, 300, 270);

-- 公司資訊 --
create table company_information(
	COM_ID int auto_increment not null primary key,
    COM_NAME varchar(45) not null,
    COM_ADDRESS varchar(45) not null,
    COM_PHONE varchar(20) not null,
    COM_MEMO varchar(500) not null
);
insert into company_information(COM_NAME, COM_ADDRESS, COM_PHONE, COM_MEMO)
values ('假日甜點', '桃園市楊梅區甡甡路646號', '0981023222', '享受假日時光');

-- 表單 --
create table form(
FORM_ID int auto_increment not null primary key,
FORM_PHONE varchar(20),
FORM_EMAIL varchar(40) NOT NULL,
FORM_CONTENT varchar(500),
FORM_CREATE_BY varchar(50),
FORM_CREATE_TIME datetime not null default current_timestamp
);

insert into form(FORM_PHONE,FORM_EMAIL,FORM_CONTENT,FORM_CREATE_BY)
values ('0911064756','s9017611@gmail.com','我想購買 怎麼聯繫您們','傅勝宏'),
	   ('0911064756','s9017688@gmail.com','我想購買','AnthonyFuFu');
