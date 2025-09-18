<<<<<<< HEAD
# BE_PTIT_FOOD
=======
# FoodPTIT Backend

Backend project cho ứng dụng FoodPTIT sử dụng Spring Boot, MySQL, JWT, Stripe, Cloudinary và Spring Mail.

## 1. Công nghệ sử dụng
- Java 17+
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA + MySQL
- Stripe API
- Cloudinary
- Spring Mail
- Docker (tuỳ chọn)

## 2. Cấu hình cơ sở dữ liệu
Cấu hình trong `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.connection.characterEncoding=UTF-8
spring.jpa.properties.hibernate.connection.useUnicode=true
spring.jpa.properties.hibernate.connection.CharSet=UTF-8
spring.jpa.properties.hibernate.connection.collation=utf8mb4_unicode_ci
````

## 3. Cài đặt & chạy project

### 3.1 Cài đặt

1. Clone repo:

   ```bash
   git clone <repo-url>
   cd backend
   ```
2. Cài đặt dependencies Maven:

   ```bash
   mvn clean install
   ```

### 3.2 Biến môi trường

Tạo file `.env` (hoặc khai báo trên Render) với các biến sau:

```env
PORT=8080
DB_URL=jdbc:mysql://<host>:3306/foodptit
DB_USER=<db_user>
DB_PASSWORD=<db_password>

JWT_SECRET_KEY=<secret_key>
FE_URL=http://localhost:5173

MAIL_USER=<email_user>
MAIL_PASSWORD=<email_password>
MAIL_SENDER=<mail_sender>

CLOUD_NAME=<cloudinary_name>
CLOUD_KEY=<cloudinary_key>
CLOUD_SECRET=<cloudinary_secret>

STRIPE_KEY=<stripe_api_key>
```

### 3.3 Chạy project

* Chạy trực tiếp:

```bash
mvn spring-boot:run
```

* Hoặc build jar và chạy:

```bash
mvn clean package
java -jar target/foodptit-0.0.1-SNAPSHOT.jar
```

Backend sẽ chạy trên cổng được khai báo trong biến `PORT`.

## 4. API

* Authentication: `/api/auth/login`, `/api/auth/register`, `/api/auth/refresh-token`
* Admin: `/api/v1/admin/**` (role ADMIN)
* Các endpoint khác: `/api/**` (yêu cầu xác thực)

## 5. Lưu ý

* CORS: chỉ cho phép `FE_URL` truy cập, cookie httpOnly được bật.
* JWT: sử dụng access token + refresh token.
* Khi deploy lên Render, đảm bảo khai báo tất cả biến môi trường cần thiết.

```

>>>>>>> 4c48884 (done)
