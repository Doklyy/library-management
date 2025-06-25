-- Create database if not exists
CREATE DATABASE IF NOT EXISTS library_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Use the database
USE library_management;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    permissions VARCHAR(255),
    priority INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Insert default roles
INSERT INTO roles (role_name, permissions, priority) VALUES
('ADMIN', 'ALL', 1),
('LIBRARIAN', 'MANAGE_BOOKS,MANAGE_BORROWS', 2),
('USER', 'BORROW_BOOKS,VIEW_BOOKS', 3)
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20) UNIQUE,
    address TEXT,
    identity_card VARCHAR(50),
    identity_number VARCHAR(50),
    dob DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    avatar VARCHAR(255),
    borrow_limit INT DEFAULT 5,
    last_login_date DATETIME,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insert default admin user if not exists
INSERT INTO users (username, password, email, full_name, role_id, is_active)
SELECT 'admin', '$2a$10$VxVpZ2hp2QCYtxJ2m8YtYOQgrnJ0Gl8.j5t3FpFDXFl/bXb9mV7e2', 'admin@library.com', 'System Administrator',
       (SELECT id FROM roles WHERE role_name = 'ADMIN'), true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin'); 