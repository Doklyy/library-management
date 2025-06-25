-- Sample data for testing statistics APIs

-- Insert roles
INSERT INTO roles (role_name, permissions, priority) VALUES 
('ADMIN', 'ALL', 1),
('USER', 'READ', 2);

-- Insert users
INSERT INTO users (username, password, full_name, email, role_id) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 'admin@library.com', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'User One', 'user1@library.com', 2);

-- Insert categories
INSERT INTO categories (name, description, is_active) VALUES 
('Fiction', 'Fiction books', true),
('Non-Fiction', 'Non-fiction books', true),
('Science', 'Science books', true),
('History', 'History books', true),
('Technology', 'Technology books', true);

-- Insert books
INSERT INTO books (title, author, isbn, description, publication_year, publisher, language, page_count, price, quantity, available_quantity, category_id, is_active) VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'A classic American novel', 1925, 'Scribner', 'English', 180, 15.99, 10, 8, 1, true),
('To Kill a Mockingbird', 'Harper Lee', '978-0446310789', 'A story of racial injustice', 1960, 'Grand Central', 'English', 281, 12.99, 8, 6, 1, true),
('1984', 'George Orwell', '978-0451524935', 'Dystopian novel', 1949, 'Signet', 'English', 328, 9.99, 12, 10, 1, true),
('The Art of War', 'Sun Tzu', '978-0140439199', 'Ancient Chinese military treatise', -500, 'Penguin', 'English', 160, 8.99, 5, 3, 2, true),
('A Brief History of Time', 'Stephen Hawking', '978-0553380163', 'Cosmology for general readers', 1988, 'Bantam', 'English', 256, 18.99, 6, 4, 3, true),
('The Origin of Species', 'Charles Darwin', '978-0451529060', 'On the origin of species', 1859, 'Signet', 'English', 576, 7.99, 4, 2, 3, true),
('The History of Rome', 'Livy', '978-0140448092', 'Ancient Roman history', -27, 'Penguin', 'English', 480, 14.99, 7, 5, 4, true),
('Clean Code', 'Robert C. Martin', '978-0132350884', 'Software development best practices', 2008, 'Prentice Hall', 'English', 464, 44.99, 15, 12, 5, true),
('Design Patterns', 'Gang of Four', '978-0201633610', 'Software design patterns', 1994, 'Addison-Wesley', 'English', 416, 49.99, 10, 8, 5, true);

-- Insert posts
INSERT INTO posts (title, content, user_id, is_active) VALUES 
('Welcome to Our Library', 'Welcome to our new library management system!', 1, true),
('Reading Tips', 'Here are some tips for effective reading...', 1, true),
('Book Review: The Great Gatsby', 'A detailed review of this classic novel...', 2, true),
('Technology Trends', 'Latest trends in technology and programming...', 1, true),
('History Corner', 'Interesting historical facts and stories...', 2, true);

-- Insert comments (likes will be simulated by comment count)
INSERT INTO comments (content, user_id, post_id, is_active) VALUES 
('Great post!', 2, 1, true),
('Very helpful tips', 1, 2, true),
('Excellent review', 1, 3, true),
('I agree with this', 2, 3, true),
('Amazing insights', 1, 4, true),
('Fascinating history', 2, 5, true),
('Love this content', 1, 5, true),
('More like this please', 2, 4, true),
('Well written', 1, 1, true),
('Informative post', 2, 2, true); 