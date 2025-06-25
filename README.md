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
- POST `/api/users` - Đăng ký tài khoản mới

### Users
- GET `/api/users` - Lấy danh sách users
- GET `/api/users/{id}` - Lấy thông tin user theo ID
- PUT `/api/users/{id}` - Cập nhật thông tin user
- DELETE `/api/users/{id}` - Xóa user
