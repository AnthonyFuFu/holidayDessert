CREATE DATABASE IF NOT EXISTS holiday_dessert;
USE holiday_dessert;

DROP TABLE IF EXISTS product_pic;
DROP TABLE IF EXISTS message;
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
DROP TABLE IF EXISTS banner;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS company_information;
DROP TABLE IF EXISTS order_detail;
-- ================== CREATE TABLE(會員）================== --

CREATE TABLE member(
MEM_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
MEM_NAME VARCHAR(45) NOT NULL,
MEM_ACCOUNT VARCHAR(20) NOT NULL,
MEM_PASSWORD VARCHAR(20) NOT NULL,
MEM_GENDER CHAR(1) NOT NULL,
MEM_PHONE VARCHAR(10) NOT NULL,
MEM_EMAIL VARCHAR(40) NOT NULL,
MEM_ADDRESS VARCHAR(100),
MEM_BIRTHDAY DATE NOT NULL,
MEM_STATUS TINYINT(1) DEFAULT(0),
CONSTRAINT unikey_MEM_ACCOUNT unique(MEM_ACCOUNT)
)auto_increment=201;

INSERT INTO member(MEM_NAME, MEM_ACCOUNT, MEM_PASSWORD, MEM_GENDER, MEM_PHONE, MEM_EMAIL,MEM_ADDRESS, MEM_BIRTHDAY)
VALUES  ('傅寶貝', 'FU830917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
		('嘉寶貝', 'WU861125', '861125', 'f', '0988000000','zoe861125@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1997-11-25','%Y-%m-%d')),
        ('傅貝', 'FU30917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
        ('傅寶', 'FU83917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
        ('傅', 'FU83097', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'))
        ;

-- 常用收貨資訊 --
create table receipt_information(
RCP_ID int auto_increment not null primary key,
MEM_ID int not null,
RCP_NAME varchar(45) not null,
RCP_CVS varchar(255) ,
RCP_ADDRESS varchar(255) ,
RCP_PHONE varchar(10) not null,
constraint receipt_information_member_fk foreign key (MEM_ID) references member(MEM_ID)
);
insert into receipt_information(MEM_ID,RCP_NAME,RCP_CVS,RCP_PHONE)
values ('201','傅寶貝','全家中壢中山店','0999000000');

-- ================== CREATE TABLE(管理員相關）================== --
        
-- 管理員 --
CREATE TABLE employee(
EMP_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
EMP_NAME VARCHAR(20) NOT NULL,
EMP_PHONE varchar(10) NOT NULL,
EMP_PICTURE LONGBLOB,
EMP_ACCOUNT VARCHAR(20) NOT NULL,
EMP_PASSWORD VARCHAR(20) NOT NULL,
EMP_LEVEL TINYINT(1) NOT NULL,
EMP_STATUS TINYINT(1) NOT NULL default '0',
EMP_HIREDATE date not null DEFAULT (CURRENT_DATE),
CONSTRAINT unikey_EMP_ACCOUNT unique(EMP_ACCOUNT)
)auto_increment=101;
INSERT INTO employee(EMP_NAME, EMP_PHONE, EMP_ACCOUNT, EMP_PASSWORD, EMP_LEVEL)  
VALUES  ('傅寶貝', '0912345678','holidaydessert101', 'emppassword1','0'),
		('嘉寶貝', '0987654321','holidaydessert102', 'emppassword2','0');

-- 功能-- 
CREATE TABLE emp_function(
FUNC_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
FUNC_NAME VARCHAR(40) NOT NULL,
FUNC_LAYER VARCHAR(5),
FUNC_PARENT_ID VARCHAR(5),
FUNC_LINK VARCHAR(100),
FUNC_STATUS TINYINT(1) NOT NULL default '1',
FUNC_ICON VARCHAR(50)
);
INSERT INTO emp_function(FUNC_NAME,FUNC_LAYER,FUNC_PARENT_ID,FUNC_LINK)  
VALUES  ('客服聊天紀錄管理','1','0','/admin/message/list'),
		('商品管理','1','0','/admin/product/list');
        
-- 管理員權限 --
 CREATE TABLE authority(
EMP_ID INT NOT NULL,
FUNC_ID INT NOT NULL,
AUTH_STATUS TINYINT(1) NOT NULL default '1',
CONSTRAINT authority_employee_FK FOREIGN KEY (EMP_ID) references employee(EMP_ID),
CONSTRAINT authority_emp_function_FK FOREIGN KEY (FUNC_ID) references emp_function(FUNC_ID), 
CONSTRAINT PK_authority_emp_ID_FUNC_ID PRIMARY KEY (EMP_ID, FUNC_ID)
);
INSERT INTO authority(EMP_ID, FUNC_ID, AUTH_STATUS)
VALUES  (101, 1, 1),
		(101, 2, 1);

-- 最新消息 --
create table news(
NEWS_ID int auto_increment not null primary key,
PM_ID int,
NEWS_NAME varchar(50),
NEWS_CONTENT varchar(500),
NEWS_STATUS TINYINT(1) NOT NULL default '0',
NEWS_START datetime,
NEWS_END datetime,
NEWS_CREATE datetime not null default current_timestamp
)auto_increment=101;

insert into news(PM_ID,NEWS_ID,NEWS_NAME,NEWS_CONTENT,NEWS_START,NEWS_END)
VALUES (null,'101','慶祝父親節','慶祝父親節，全館88折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59')),
	   (2,'102','歡慶中元節','歡慶中元節，全館9折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59')),
       (null,'103','中秋節快樂','中秋節快樂，全館79折',CONCAT(CURDATE(), ' 00:00:00'),CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'));

-- BANNER --
create table banner(
BAN_ID	int auto_increment not null primary key,
NEWS_ID	 int not null,
BAN_PIC longblob,
CONSTRAINT banner_news_FK FOREIGN KEY (NEWS_ID) references news(NEWS_ID)
);

insert into banner(NEWS_ID)
VALUES ('101');

-- ================== CREATE TABLE(客服相關）================== --

-- 客服聊天紀錄 --
create table message(
	MSG_ID int auto_increment not null primary key,
	EMP_ID int not null,
	MEM_ID int not null,
	MSG_CONTENT varchar(3000),
	MSG_TIME datetime default current_timestamp on update current_timestamp not null,
	MSG_DIRECTION tinyint(1) not null,
    MSG_PIC longblob,
    constraint message_employee_fk foreign key (EMP_ID) references employee(EMP_ID),
    constraint message_member_fk foreign key (MEM_ID) references member(MEM_ID)
);

insert into message (EMP_ID,MEM_ID,MSG_CONTENT,MSG_DIRECTION)
value(101,201,"HI",0),
	 (101,201,"Hello",0);

-- 商品分類 --
create table product_collection(
	PDC_ID int auto_increment not null primary key,
    PDC_NAME varchar(10) not null,
    PDC_KEYWORD varchar(200),
    PDC_STATUS tinyint(1) not null default 1
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
    PD_STATUS tinyint(1) not null default 1,
    PD_IS_DEL tinyint(1) not null default 0,
    PD_CREATE_BY varchar(20) ,
	PD_CREATE_TIME datetime ,
	PD_UPDATE_BY varchar(20) ,
	PD_UPDATE_TIME datetime,
    constraint product_product_collection_fk FOREIGN KEY (PDC_ID) REFERENCES product_collection(PDC_ID)
)auto_increment=4001;
insert into product(PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, PD_STATUS, PD_IS_DEL,
					PD_CREATE_BY,PD_CREATE_TIME,PD_UPDATE_BY,PD_UPDATE_TIME)
values (1, '奶茶風味可麗露', 300, '奶茶風味可麗露最好吃', 2, 1, 0,'嘉寶貝',NOW(),'嘉寶貝',NOW()),
       (1, '抹茶風味可麗露', 500, '抹茶風味可麗露最好吃', 2, 1, 0,'嘉寶貝',NOW(),'嘉寶貝',NOW());

-- 商品圖片 --
create table product_pic (
	PD_PIC_ID int auto_increment not null primary key,
    PD_ID int,
    PD_PIC_SORT int,
    PD_PIC longblob,
    constraint product_pic_product_fk foreign key (PD_ID) references product(PD_ID)
);
insert into product_pic(PD_ID, PD_PIC_SORT)
values (4001, 1),(4001, 2),(4001, 3),(4002, 1),(4002, 3),(4002, 2);

-- ================== CREATE TABLE(優惠相關）================== --

-- 活動優惠方案 --
 create table promotion(
PM_ID int not null primary key auto_increment,
PM_NAME varchar(45) not null,
PM_DESCRIPTION varchar(255),
PM_DISCOUNT decimal(3,2),
PM_REGULARLY tinyint(1) not null,
PM_STATUS tinyint(1) not null,
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
values(2,4001,CONCAT(CURDATE(), ' 00:00:00'), str_to_date('2022-08-31','%Y-%m-%d'),237);

-- 優惠券種類 --
create table coupon(
	CP_ID int auto_increment not null primary key,
    CP_NAME varchar(45) not null,
    CP_DISCOUNT int not null,
    CP_STATUS tinyint not null,
    CP_PIC longblob
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
    MEM_CP_STATUS tinyint not null,
    MEM_CP_RECORD datetime,
    constraint member_coupon_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint member_coupon_coupon_fk foreign key (CP_ID) references coupon(CP_ID)
);
insert into member_coupon(MEM_ID, CP_ID, MEM_CP_START,MEM_CP_END, MEM_CP_STATUS, MEM_CP_RECORD)
values (201, 1, CONCAT(CURDATE(), ' 00:00:00'),str_to_date('2022-10-31','%Y-%m-%d'), 1, null);

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
values (201, 4001, 3);

-- 訂單 --
create table main_order(
	ORD_ID int auto_increment not null primary key,
    MEM_ID int not null,
    MEM_CP_ID int,
    ORD_SUBTOTAL int not null,
    ORD_TOTAL int not null,
    ORD_STATUS tinyint(1) not null default 0,
    ORD_CREATE datetime default current_timestamp,
    ORD_RECIPIENT varchar(20) not null,
    ORD_RECIPIENT_PHONE varchar(10) not null,
    ORD_PAYMENT tinyint(1) not null,
    ORD_DELIVERY tinyint(1) not null,
    ORD_ADDRESS varchar(100) not null,
    ORD_NOTE varchar(100),
    ORD_DELIVERY_FEE int not null,
    constraint main_order_member_fk foreign key (MEM_ID) references member(MEM_ID),
    constraint main_order_member_coupon_fk foreign key (MEM_CP_ID) references member_coupon(MEM_CP_ID)
);
insert into main_order(MEM_ID, MEM_CP_ID, ORD_SUBTOTAL, ORD_TOTAL, ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_DELIVERY_FEE ,ORD_ADDRESS)
values (201, null, 270, 270, 'leoblue', '0987654321', 1, 1,'100', '桃園');
       
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
values (1, 4001, 3, 300, 270);

-- 公司資訊 --
create table company_information(
	COM_ID int auto_increment not null primary key,
    COM_NAME varchar(45) not null,
    COM_ADDRESS varchar(45) not null,
    COM_PHONE varchar(10) not null,
    COM_MEMO varchar(500) not null
);
insert into company_information(COM_NAME, COM_ADDRESS, COM_PHONE, COM_MEMO)
values ('假日甜點', '桃園市楊梅區甡甡路646號', '0981023222', '享受假日時光');