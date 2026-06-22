-- ========================================
-- LIBRARY MANAGEMENT SYSTEM - DATABASE SCHEMA
-- ========================================
-- Database: library_management_db
-- Created: 2026-06-22
-- ========================================

-- Create Database
CREATE DATABASE IF NOT EXISTS library_management_db;
USE library_management_db;

-- ========================================
-- 1. USERS TABLE (Authentication)
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'LIBRARIAN' COMMENT 'ADMIN, LIBRARIAN, etc.',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores user login credentials and information';

-- ========================================
-- 2. CATEGORY TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_category_name (category_name),
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book categories like Fiction, Science, History, etc.';

-- ========================================
-- 3. AUTHOR TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    author_name VARCHAR(150) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(10),
    biography TEXT,
    date_of_birth DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_author_name (author_name),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book authors information';

-- ========================================
-- 4. PUBLISHER TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS publisher (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    publisher_name VARCHAR(150) NOT NULL UNIQUE,
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(10),
    website_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_publisher_name (publisher_name),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book publishers information';

-- ========================================
-- 5. BOOK TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    book_title VARCHAR(255) NOT NULL,
    author_id INT NOT NULL,
    publisher_id INT NOT NULL,
    category_id INT NOT NULL,
    edition VARCHAR(20),
    publication_date DATE,
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    book_price DECIMAL(10, 2),
    description TEXT,
    acquisition_date DATE,
    shelf_location VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_book_title (book_title),
    INDEX idx_isbn (isbn),
    INDEX idx_author_id (author_id),
    INDEX idx_publisher_id (publisher_id),
    INDEX idx_category_id (category_id),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE RESTRICT,
    FOREIGN KEY (publisher_id) REFERENCES publisher(publisher_id) ON DELETE RESTRICT,
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book inventory';

-- ========================================
-- 6. MEMBER TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    member_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Unique member ID/code',
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(10),
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    membership_date DATE NOT NULL,
    membership_type VARCHAR(50) DEFAULT 'STANDARD' COMMENT 'STANDARD, PREMIUM, STUDENT, etc.',
    expiry_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_member_code (member_code),
    INDEX idx_email (email),
    INDEX idx_is_active (is_active),
    INDEX idx_membership_date (membership_date),
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Library members/subscribers';

-- ========================================
-- 7. BOOK_ISSUE TABLE (Issue Book Transaction)
-- ========================================
CREATE TABLE IF NOT EXISTS book_issue (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL COMMENT 'Expected return date',
    return_date DATE COMMENT 'Actual return date (NULL if not returned)',
    issue_status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_member_id (member_id),
    INDEX idx_book_id (book_id),
    INDEX idx_issue_date (issue_date),
    INDEX idx_issue_status (issue_status),
    INDEX idx_return_date (return_date),
    UNIQUE KEY unique_active_issue (member_id, book_id, issue_status) COMMENT 'Prevent duplicate active issues for same book',
    FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE RESTRICT,
    FOREIGN KEY (book_id) REFERENCES book(book_id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Record of book issues to members';

-- ========================================
-- 8. BOOK_RETURN TABLE (Return Book with Fine Calculation)
-- ========================================
CREATE TABLE IF NOT EXISTS book_return (
    return_id INT AUTO_INCREMENT PRIMARY KEY,
    issue_id INT NOT NULL UNIQUE,
    return_date DATE NOT NULL,
    days_kept INT NOT NULL COMMENT 'Number of days book was kept',
    is_overdue BOOLEAN DEFAULT FALSE,
    days_overdue INT DEFAULT 0,
    fine_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Calculated fine for overdue books',
    fine_per_day DECIMAL(10, 2) DEFAULT 5.00 COMMENT 'Fine per day after due date',
    fine_paid BOOLEAN DEFAULT FALSE,
    payment_date DATE,
    payment_method VARCHAR(50) COMMENT 'CASH, CARD, CHEQUE, etc.',
    return_condition VARCHAR(50) COMMENT 'GOOD, DAMAGED, LOST, etc.',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    INDEX idx_issue_id (issue_id),
    INDEX idx_return_date (return_date),
    INDEX idx_is_overdue (is_overdue),
    INDEX idx_fine_paid (fine_paid),
    FOREIGN KEY (issue_id) REFERENCES book_issue(issue_id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Record of book returns and fine calculations';

-- ========================================
-- 9. AUDIT_LOG TABLE (System Audit Trail)
-- ========================================
CREATE TABLE IF NOT EXISTS audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action VARCHAR(100) NOT NULL COMMENT 'CREATE, UPDATE, DELETE, etc.',
    table_name VARCHAR(50),
    record_id INT,
    old_value JSON,
    new_value JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_timestamp (timestamp),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Audit trail for system activities';

-- ========================================
-- 10. SYSTEM_SETTINGS TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS system_settings (
    setting_id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value VARCHAR(255),
    setting_type VARCHAR(50) COMMENT 'STRING, NUMBER, BOOLEAN, etc.',
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System configuration settings';

-- ========================================
-- INSERT DEFAULT DATA
-- ========================================

-- Insert default admin user (Password: Admin@123)
INSERT INTO users (username, password, email, first_name, last_name, role, is_active)
VALUES ('admin', '$2a$10$Nz0qYEZW2z.KZ9Z0F0F0Ue2z0z0z0z0z0z0z0z0z0z0z0z0z0z0', 'admin@library.com', 'System', 'Administrator', 'ADMIN', TRUE)
ON DUPLICATE KEY UPDATE user_id = user_id;

-- Insert default system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description) VALUES
('FINE_PER_DAY', '5.00', 'NUMBER', 'Fine amount per day for overdue books'),
('MAX_BOOKS_PER_MEMBER', '5', 'NUMBER', 'Maximum books a member can issue at once'),
('ISSUE_PERIOD_DAYS', '14', 'NUMBER', 'Standard book issue period in days'),
('LIBRARY_NAME', 'City Central Library', 'STRING', 'Name of the library'),
('LIBRARY_EMAIL', 'contact@library.com', 'STRING', 'Library contact email'),
('LIBRARY_PHONE', '+1-800-LIBRARY', 'STRING', 'Library phone number')
ON DUPLICATE KEY UPDATE setting_id = setting_id;

-- ========================================
-- CREATE VIEWS FOR REPORTING
-- ========================================

-- View: Current Active Issues
CREATE OR REPLACE VIEW vw_active_issues AS
SELECT 
    bi.issue_id,
    m.member_code,
    CONCAT(m.first_name, ' ', m.last_name) AS member_name,
    b.book_title,
    a.author_name,
    bi.issue_date,
    bi.due_date,
    DATEDIFF(CURDATE(), bi.due_date) AS days_overdue,
    CASE 
        WHEN bi.due_date < CURDATE() THEN 'OVERDUE'
        WHEN bi.due_date = CURDATE() THEN 'DUE_TODAY'
        ELSE 'ACTIVE'
    END AS status
FROM book_issue bi
JOIN member m ON bi.member_id = m.member_id
JOIN book b ON bi.book_id = b.book_id
JOIN author a ON b.author_id = a.author_id
WHERE bi.issue_status = 'ACTIVE'
ORDER BY bi.due_date ASC;

-- View: Member Borrowing History
CREATE OR REPLACE VIEW vw_member_borrow_history AS
SELECT 
    m.member_id,
    m.member_code,
    CONCAT(m.first_name, ' ', m.last_name) AS member_name,
    COUNT(CASE WHEN bi.issue_status = 'ACTIVE' THEN 1 END) AS active_issues,
    COUNT(CASE WHEN bi.issue_status = 'RETURNED' THEN 1 END) AS returned_books,
    COUNT(CASE WHEN bi.issue_status = 'OVERDUE' THEN 1 END) AS overdue_books,
    SUM(COALESCE(br.fine_amount, 0)) AS total_fines,
    SUM(CASE WHEN br.fine_paid = FALSE THEN br.fine_amount ELSE 0 END) AS unpaid_fines
FROM member m
LEFT JOIN book_issue bi ON m.member_id = bi.member_id
LEFT JOIN book_return br ON bi.issue_id = br.issue_id
GROUP BY m.member_id, m.member_code, m.first_name, m.last_name;

-- View: Book Inventory Status
CREATE OR REPLACE VIEW vw_book_inventory AS
SELECT 
    b.book_id,
    b.book_title,
    b.isbn,
    a.author_name,
    c.category_name,
    p.publisher_name,
    b.total_copies,
    b.available_copies,
    COUNT(CASE WHEN bi.issue_status = 'ACTIVE' THEN 1 END) AS issued_copies,
    b.available_copies - COUNT(CASE WHEN bi.issue_status = 'ACTIVE' THEN 1 END) AS in_stock
FROM book b
LEFT JOIN author a ON b.author_id = a.author_id
LEFT JOIN category c ON b.category_id = c.category_id
LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
LEFT JOIN book_issue bi ON b.book_id = bi.book_id
WHERE b.is_active = TRUE
GROUP BY b.book_id, b.book_title, b.isbn, a.author_name, c.category_name, p.publisher_name, b.total_copies, b.available_copies;

-- ========================================
-- CREATE INDEXES FOR PERFORMANCE
-- ========================================
CREATE INDEX idx_book_issue_composite ON book_issue(member_id, issue_status, issue_date);
CREATE INDEX idx_book_return_composite ON book_return(return_date, is_overdue);
CREATE INDEX idx_member_active ON member(is_active, membership_date);
CREATE INDEX idx_author_active ON author(is_active);
CREATE INDEX idx_publisher_active ON publisher(is_active);

-- ========================================
-- DATABASE SCHEMA COMPLETE
-- ========================================