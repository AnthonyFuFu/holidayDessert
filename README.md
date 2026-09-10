<div align="center">

# 🍰 HolidayDessert（假日甜點）

以 Spring Boot 3 打造的甜點電商暨會員票券管理系統，包含前台購物網站與後台管理系統，並整合訊息佇列、全文搜尋、AI 與 OCR 等周邊服務。

[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.15-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.5-003545?logo=mariadb&logoColor=white)](https://mariadb.org/)
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?logo=redis&logoColor=white)](https://redis.io/)
[![Kafka](https://img.shields.io/badge/Kafka-Messaging-231F20?logo=apachekafka&logoColor=white)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)

</div>

---

## 📑 目錄

- [📖 專案簡介](#-專案簡介)
- [🛠 技術棧](#-技術棧)
- [🧩 功能模組](#-功能模組)
- [📂 專案結構](#-專案結構)
- [✅ 環境需求](#-環境需求)
- [⚙️ 設定檔](#️-設定檔)
- [🚀 啟動方式](#-啟動方式)
- [🐳 Docker 部署](#-docker-部署)
- [📘 API 文件](#-api-文件)
- [🔗 測試網址](#-測試網址)

---

## 📖 專案簡介

HolidayDessert 是一套以甜點電商為情境的全端練習／實戰專案，涵蓋：

- 🛍️ **前台（front）**：商品瀏覽、購物車、下單結帳、會員註冊登入（含 Google OAuth2）、優惠券、行銷活動、票券預約、即時聊天室、最新消息與留言互動。
- 🗂️ **後台（admin）**：員工／部門管理、商品管理、訂單管理、會員管理、優惠券／促銷活動管理、行事曆、公司資訊、最新消息、票券管理等 CRUD 後臺。
- 🔌 **周邊整合**：Kafka 訊息佇列（票券訂單非同步處理）、OpenSearch 全文搜尋（會員資料索引）、Redis 快取、Azure OpenAI（文字生成／圖像辨識）、Tesseract OCR、PDF／Excel 報表產製、Jasypt 敏感資料加解密。

---

## 🛠 技術棧

| 分類 | 技術 |
| --- | --- |
| 🧑‍💻 語言／框架 | Java 17、Spring Boot 3.5.15 |
| 🌐 Web／模板 | Spring MVC、Thymeleaf、WebSocket |
| 🗄️ 資料存取 | Spring Data JPA、Spring JDBC、MySQL / MariaDB、HikariCP |
| ⚡ 快取／訊息 | Redis、Apache Kafka、Confluent Avro Serializer |
| 🔍 搜尋引擎 | OpenSearch（搭配 Logstash 同步 `index_member`） |
| 🔐 安全性 | Spring Security、JWT（`jjwt` / `java-jwt`）、OAuth2 Client（Google 登入）、Jasypt |
| 🤖 AI／影像 | Azure OpenAI（Chat）、Azure AI Vision（Image Analysis）、Tess4J（OCR） |
| 📄 文件產製 | iTextPDF、Apache POI、JExcelAPI |
| 📘 API 文件 | springdoc-openapi（Swagger UI） |
| 🧰 建置工具 | Maven（`mvnw`）、Docker / Docker Compose |

---

## 🧩 功能模組

### 🛍️ 前台（`controller` / `templates/front`）
- 首頁、商品與活動展示（`IndexController`、`HolidayDessertController`）
- 會員登入註冊、Google 第三方登入（`FrontLoginController`、`MemberController`）
- 收藏清單（`CollectionController`）
- 表單／留言（`FormController`）
- 即時聊天室（`ChatRoomController`，WebSocket）
- 票券預約與 Kafka 非同步下單（`TicketController`、`kafka/*`）
- 公司資訊、通用 CRUD 查詢（`CompanyInformationController`、`CRUDController`）

### 🗂️ 後台（`controller/admin`）
- 員工／部門管理（`EmployeeManagement`）
- 商品管理（`ProductManagement`）
- 訂單管理（`OrderManagement`）
- 會員管理（`MemberManagement`）
- 優惠券／促銷活動管理（`CouponManagement`、`PromotionManagement`）
- 行事曆（`CalendarManagement`）
- 最新消息（`NewsManagement`）
- 票券管理（`TicketManagement`）
- 公司資訊管理（`CompanyManagement`）
- 後台登入（`LoginController`）

### 🧰 其他能力
- OCR 文字辨識（`OcrController`，Tess4J + tessdata）
- PDF 產生／下載（`PdfController`，iTextPDF）
- Azure OpenAI 聊天與圖像分析（`OpenAIController`）
- OpenSearch 查詢（`OpenSearchController`）
- JWT 簽發與驗證（`TokenController`）
- Jasypt 加解密工具（`JasyptController`）
- 高併發測試端點（`HighConcurrencyController`）
- 排程任務（`schedule/RunSchedule`）

---

## 📂 專案結構

```text
src/main/java/com/holidaydessert/
├── config/         # Spring 設定（Security、CORS、Redis、WebSocket、OpenSearch...）
├── constant/       # 常數定義
├── controller/     # 前台 Controller（含 admin/ 後台 Controller）
├── dao/            # 資料存取邏輯
├── filter/         # Servlet Filter（如 JWT 驗證）
├── interceptors/   # Spring MVC 攔截器
├── kafka/          # Kafka Producer / Consumer 設定
├── model/          # 實體與 DTO
├── repository/     # Spring Data JPA Repository
├── schedule/       # 排程任務
├── service/        # 商業邏輯層
└── utils/          # 共用工具類別

src/main/resources/
├── application.yml     # 多環境設定（local / r5-2600-windows / r5-2600-wsl 等 profile）
├── static/              # 前後台靜態資源（admin、front、member、load、assets）
└── templates/           # Thymeleaf 樣板（admin、front）
```

---

## ✅ 環境需求

- JDK 17
- Maven（可直接使用內附 `mvnw` / `mvnw.cmd`）
- MySQL 或 MariaDB
- Redis
- （選用）Apache Kafka + Confluent Schema Registry：票券訂單非同步流程
- （選用）OpenSearch + Logstash：會員資料全文搜尋（見 `index_member.conf`）

---

## ⚙️ 設定檔

主要設定位於 [src/main/resources/application.yml](src/main/resources/application.yml)，以 Spring Profile 區分環境，預設啟用 `local`：

```yaml
spring:
  profiles:
    active: local
```

依實際環境調整資料庫帳密、Redis 連線、上傳檔案路徑（`admin.upload.file.path`）、log 路徑（`logback.logdir`）與 Google OAuth2 用戶端資訊等欄位。專案內含資料庫結構檔 [holiday_dessert分大小寫.sql](holiday_dessert分大小寫.sql) 可用於初始化資料庫。

---

## 🚀 啟動方式

```powershell
# 安裝相依套件並打包
./mvnw.cmd clean package

# 啟動應用程式（預設 profile: local）
./mvnw.cmd spring-boot:run
```

應用程式預設監聽 `8080` 埠，Context Path 為 `/holidayDessert`。

---

## 🐳 Docker 部署

專案提供 [Dockerfile](Dockerfile) 與 [docker-compose.yml](docker-compose.yml)，可一併啟動應用程式與 MariaDB：

```powershell
docker-compose up -d
```

---

## 📘 API 文件

整合 springdoc-openapi，啟動後可透過以下網址查看 Swagger UI：

```
http://localhost:8080/holidayDessert/swagger-ui.html
```

---

## 🔗 測試網址

```
http://localhost:8080/holidayDessert/index.html
```
