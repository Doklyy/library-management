# Library Management System

## Giới thiệu
Đây là một hệ thống quản lý thư viện được xây dựng bằng Spring Boot, sử dụng JWT cho xác thực và phân quyền.

## Công nghệ sử dụng
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT Authentication
- MySQL
- Maven

## Cấu trúc project
```
src/main/java/com/library/
├── config/          # Cấu hình Spring Boot, Security
├── controller/      # REST Controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities
├── repository/     # JPA Repositories
├── security/       # JWT Security Configuration
├── service/        # Business Logic
└── exception/      # Custom Exception Handlers
```

## API Endpoints

### Authentication
- POST `/api/auth/login` - Đăng nhập
- POST `/api/auth/register` - Đăng ký tài khoản mới

### Users
- GET `/api/users` - Lấy danh sách users
- GET `/api/users/{id}` - Lấy thông tin user theo ID
- PUT `/api/users/{id}` - Cập nhật thông tin user
- DELETE `/api/users/{id}` - Xóa user

## Cài đặt và chạy project

1. Clone repository:
```bash
git clone https://github.com/Doklyy/library-management.git
```

2. Cấu hình database trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build và chạy project:
```bash
mvn clean install
mvn spring-boot:run
```

## Vấn đề đang gặp phải
Hiện tại đang gặp lỗi khi gọi API login:
```
NoResourceFoundException: No static resource auth/login
```

Lỗi này xảy ra do:
1. Request đang được gửi đến `/auth/login` thay vì `/api/auth/login`
2. Cấu hình security cần được điều chỉnh để xử lý cả hai pattern URL

## Hướng phát triển tiếp theo
1. Hoàn thiện các chức năng CRUD cho Books
2. Thêm chức năng mượn/trả sách
3. Thêm chức năng tìm kiếm và lọc sách
4. Thêm unit tests và integration tests
5. Tích hợp Swagger UI cho API documentation

## Liên hệ
Nếu có bất kỳ câu hỏi hoặc góp ý nào, vui lòng tạo issue trên GitHub repository. 