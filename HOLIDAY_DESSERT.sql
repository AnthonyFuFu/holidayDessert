CREATE DATABASE IF NOT EXISTS HOLIDAY_DESSERT;
USE HOLIDAY_DESSERT;

DROP TABLE IF EXISTS PRODUCT_PIC;
DROP TABLE IF EXISTS MESSAGE;
DROP TABLE IF EXISTS PROMOTION_DETAIL;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS PRODUCT_COLLECTION;
DROP TABLE IF EXISTS MEMBER_COUPON;
DROP TABLE IF EXISTS PROMOTION;
DROP TABLE IF EXISTS COUPON;
DROP TABLE IF EXISTS MAIN_ORDER;
DROP TABLE IF EXISTS CART;
DROP TABLE IF EXISTS RECEIPT_INFORMATION;
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS AUTHORITY;
DROP TABLE IF EXISTS EMP_FUNCTION;
DROP TABLE IF EXISTS EMPLOYEE;
DROP TABLE IF EXISTS BANNER;
DROP TABLE IF EXISTS NEWS;

-- ================== CREATE TABLE(會員）================== --

CREATE TABLE MEMBER(
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
INSERT INTO MEMBER(MEM_NAME, MEM_ACCOUNT, MEM_PASSWORD, MEM_GENDER, MEM_PHONE, MEM_EMAIL,MEM_ADDRESS, MEM_BIRTHDAY)
VALUES  ('傅寶貝', 'FU830917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
		('嘉寶貝', 'WU861125', '861125', 'f', '0988000000','zoe861125@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1997-11-25','%Y-%m-%d')),
        ('傅貝', 'FU30917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
        ('傅寶', 'FU83917', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d')),
        ('傅', 'FU83097', '830917', 'm', '0999000000','s9017611@gmail.com','台北市中正區博愛路36號',STR_TO_DATE('1994-09-17','%Y-%m-%d'))
        ;

-- 常用收貨資訊 --
create table RECEIPT_INFORMATION(
RCP_ID int auto_increment not null primary key,
MEM_ID int not null,
RCP_NAME varchar(45) not null,
RCP_CVS varchar(255) ,
RCP_ADDRESS varchar(255) ,
RCP_PHONE varchar(10) not null,
constraint RECEIPT_INFORMATION_MEMBER_fk foreign key (MEM_ID) references MEMBER(MEM_ID)
);
insert into RECEIPT_INFORMATION(MEM_ID,RCP_NAME,RCP_CVS,RCP_PHONE)
values ('201','傅寶貝','全家中壢中山店','0999000000');

-- ================== CREATE TABLE(管理員相關）================== --
        
-- 管理員 --
CREATE TABLE EMPLOYEE(
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
INSERT INTO EMPLOYEE(EMP_NAME, EMP_PHONE, EMP_ACCOUNT, EMP_PASSWORD, EMP_LEVEL)  
VALUES  ('傅寶貝', '0912345678','holidaydessert101', 'emppassword1','0'),
		('嘉寶貝', '0987654321','holidaydessert102', 'emppassword2','0');

-- 功能-- 
CREATE TABLE EMP_FUNCTION(
FUNC_ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
FUNC_NAME VARCHAR(40) NOT NULL
);
INSERT INTO EMP_FUNCTION(FUNC_NAME)  
VALUES  ('客服聊天紀錄管理'),
		('商品管理');
        
-- 管理員權限 --
 CREATE TABLE AUTHORITY(
EMP_ID INT NOT NULL,
FUNC_ID INT NOT NULL,
CONSTRAINT AUTHORITY_EMPLOYEE_FK FOREIGN KEY (EMP_ID) references EMPLOYEE(EMP_ID),
CONSTRAINT AUTHORITY_EMP_FUNCTION_FK FOREIGN KEY (FUNC_ID) references EMP_FUNCTION(FUNC_ID), 
CONSTRAINT PK_AUTHORITY_EMP_ID_FUNC_ID PRIMARY KEY (EMP_ID, FUNC_ID)
);
INSERT INTO AUTHORITY(EMP_ID, FUNC_ID)
VALUES  (101, 1),
		(101,2);

-- 最新消息 --
create table NEWS(
NEWS_ID int auto_increment not null primary key,
NEWS_NAME varchar(50),
NEWS_CONTENT varchar(500),
NEWS_STATUS TINYINT(1) NOT NULL default '0',
NEWS_TIME datetime not null default current_timestamp
)auto_increment=101;

insert into NEWS(NEWS_ID,NEWS_NAME,NEWS_CONTENT)
VALUES ('101','慶祝父親節','慶祝父親節，全館88折'),
	   ('102','歡慶中元節','歡慶中元節，全館9折'),
       ('103','光棍節快樂','光棍節快樂，全館1.1折起');

-- BANNER --
create table BANNER(
BAN_ID	int auto_increment not null primary key,
NEWS_ID	 int not null,
BAN_PIC longblob,
CONSTRAINT BANNER_NEWS_FK FOREIGN KEY (NEWS_ID) references NEWS(NEWS_ID)
);

insert into BANNER(NEWS_ID)
VALUES ('101');

-- ================== CREATE TABLE(客服相關）================== --

-- 客服聊天紀錄 --
create table MESSAGE(
	MSG_ID int auto_increment not null primary key,
	EMP_ID int not null,
	MEM_ID int not null,
	MSG_CONTENT varchar(3000),
	MSG_TIME datetime default current_timestamp on update current_timestamp not null,
	MSG_DIRECTION tinyint(1) not null,
    MSG_PIC longblob,
    constraint MESSAGE_EMPLOYEE_fk foreign key (EMP_ID) references EMPLOYEE(EMP_ID),
    constraint MESSAGE_MEMBER_fk foreign key (MEM_ID) references MEMBER(MEM_ID)
);

insert into MESSAGE (EMP_ID,MEM_ID,MSG_CONTENT,MSG_DIRECTION)
value(101,201,"HI",0),
	 (101,201,"Hello",0);

-- 商品分類 --
create table PRODUCT_COLLECTION(
	PDC_ID int auto_increment not null primary key,
    PDC_NAME varchar(10) not null
);

insert into PRODUCT_COLLECTION(PDC_NAME)
values ('可麗路');
-- 商品 --
create table PRODUCT(
	PD_ID int auto_increment not null primary key,
    PDC_ID int not null,
    PD_NAME varchar(30) not null unique, 
    PD_PRICE int not null,
    PD_DESCRIPTION varchar(200),
    PD_STATUS tinyint(1) not null default 0,
    constraint PRODUCT_PRODUCT_COLLECTION_fk FOREIGN KEY (PDC_ID) REFERENCES PRODUCT_COLLECTION(PDC_ID)
)auto_increment=4001;
insert into product(PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_STATUS)
values (1, '奶茶風味可麗露', 300, 'NNNNNNNNN', 0);
       
-- 商品圖片 --
create table PRODUCT_PIC (
	PD_PIC_ID int auto_increment not null primary key,
    PD_ID int,
    PD_PIC longblob,
    constraint PRODUCT_PIC_PRODUCT_fk foreign key (PD_ID) references PRODUCT(PD_ID)
);
insert into PRODUCT_PIC(PD_ID)
values (4001);
       

-- ================== CREATE TABLE(優惠相關）================== --

-- 活動優惠方案 --
 create table PROMOTION(
PM_ID int not null primary key auto_increment,
PM_NAME varchar(45) not null,
PM_DESCRIPTION varchar(255),
PM_DISCOUNT decimal(3,2) not null,
PM_STATUS tinyint(1) not null
);

insert into PROMOTION(PM_NAME,PM_DESCRIPTION,PM_DISCOUNT,PM_STATUS)
values('中秋節特價','所有常溫品項79折',0.79,1);
 
-- 優惠活動明細 --
create table PROMOTION_DETAIL(
PMD_ID int auto_increment not null primary key,
PD_ID int not null,
PM_ID int not null,
PMD_START datetime,
PMD_END datetime,
PMD_PD_DISCOUNT_PRICE int ,
constraint PROMOTION_DETAIL_PROMOTION_fk foreign key (PM_ID) references PROMOTION(PM_ID),
constraint PROMOTION_DETAIL_PRODUCT_fk foreign key (PD_ID) references PRODUCT(PD_ID));

insert into PROMOTION_DETAIL(PM_ID,PD_ID,PMD_START,PMD_END,PMD_PD_DISCOUNT_PRICE)
values(1,4001,str_to_date('2022-08-20','%Y-%m-%d'), str_to_date('2022-08-31','%Y-%m-%d'),399);

-- 優惠券種類 --
create table COUPON(
	CP_ID int auto_increment not null primary key,
    CP_NAME varchar(45) not null,
    CP_DISCOUNT int not null,
    CP_STATUS tinyint not null,
    CP_PIC longblob
);
insert into COUPON(CP_NAME, CP_DISCOUNT, CP_STATUS)
values ('50元購物金', 50, 1),
       ('10元折價券', 10, 0);
 
-- 會員優惠券 --
create table MEMBER_COUPON(
	MEM_CP_ID int auto_increment not null primary key,
    MEM_ID int not null,
    CP_ID int not null,
    MEM_CP_START datetime not null,
    MEM_CP_END datetime not null,
    MEM_CP_STATUS tinyint not null,
    MEM_CP_RECORD datetime,
    constraint MEMBER_COUPON_MEMBER_fk foreign key (MEM_ID) references MEMBER(MEM_ID),
    constraint MEMBER_COUPON_COUPON_fk foreign key (CP_ID) references COUPON(CP_ID)
);
insert into MEMBER_COUPON(MEM_ID, CP_ID, MEM_CP_START,MEM_CP_END, MEM_CP_STATUS, MEM_CP_RECORD)
values (201, 1, str_to_date('2022-08-31','%Y-%m-%d'),str_to_date('2022-10-31','%Y-%m-%d'), 1, null);

-- ================== CREATE TABLE(商品相關）================== --

-- 購物車-- 
create table CART(
	MEM_ID int not null,
    PD_ID int not null,
    CART_PD_QUANTITY int not null,
    constraint CART_MEMBER_fk foreign key (MEM_ID) references MEMBER(MEM_ID),
    constraint CART_PRODUCT_fk foreign key (PD_ID) references PRODUCT(PD_ID),
    primary key(MEM_ID, PD_ID)
);
insert into CART(MEM_ID, PD_ID, CART_PD_QUANTITY)
values (201, 4001, 3);

-- 訂單 --
create table MAIN_ORDER(
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
    constraint MAIN_ORDER_MEMBER_fk foreign key (MEM_ID) references MEMBER(MEM_ID),
    constraint MAIN_ORDER_MEMBER_COUPON_fk foreign key (MEM_CP_ID) references MEMBER_COUPON(MEM_CP_ID)
);
insert into MAIN_ORDER(MEM_ID, MEM_CP_ID, ORD_SUBTOTAL, ORD_TOTAL, ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_DELIVERY_FEE ,ORD_ADDRESS)
values (201, null, 270, 270, 'leoblue', '0987654321', 1, 1,'100', '桃園');
       
-- 訂單明細 --
create table ORDER_DETAIL(
	ORD_ID int not null,
    PD_ID int not null,
    ORDD_NUMBER int not null,
    ORDD_PRICE int not null,
    ORDD_DISCOUNT_PRICE int not null,
    constraint ORDER_DETAIL_MAIN_ORDER_fk foreign key (ORD_ID) references MAIN_ORDER(ORD_ID),
    constraint ORDER_DETAIL_PRODUCT_fk foreign key (PD_ID) references PRODUCT(PD_ID),
	primary key(ORD_ID, PD_ID)
);
insert into ORDER_DETAIL(ORD_ID, PD_ID, ORDD_NUMBER, ORDD_PRICE, ORDD_DISCOUNT_PRICE)
values (1, 4001, 3, 300, 270);

-- 公司資訊 --
create table COMPANY_INFORMATION(
	COM_ID int auto_increment not null primary key,
    COM_NAME varchar(45) not null,
    COM_ADDRESS varchar(45) not null,
    COM_PHONE varchar(10) not null,
    COM_MEMO varchar(500) not null
);
insert into COMPANY_INFORMATION(COM_NAME, COM_ADDRESS, COM_PHONE, COM_MEMO)
values ('假日甜點', '桃園市楊梅區甡甡路646號', '0981023222', '享受假日時光');