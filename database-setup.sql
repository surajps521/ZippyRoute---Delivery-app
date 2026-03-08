-- ZippyRoute Database Setup Script
-- Run this script in PostgreSQL to create the database and tables

-- Create database
CREATE DATABASE zippyroute_db;

-- Connect to the database
\c zippyroute_db;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    address TEXT,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

-- Create menu items table
CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100),
    image_url VARCHAR(500),
    rating DECIMAL(3,2) DEFAULT 4.5,
    review_count INTEGER DEFAULT 0,
    available BOOLEAN NOT NULL DEFAULT true,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    delivery_address TEXT,
    phone_number VARCHAR(15),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) DEFAULT 0,
    delivery_fee DECIMAL(10,2) DEFAULT 50,
    discount DECIMAL(10,2) DEFAULT 0,
    notes TEXT,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create order items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    special_instructions TEXT,
    created_at BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Create order tracking table
CREATE TABLE order_tracking (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    current_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    delivery_person_name VARCHAR(255),
    delivery_person_phone VARCHAR(15),
    estimated_delivery_time VARCHAR(50),
    status_history TEXT,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_menu_items_category ON menu_items(category);
CREATE INDEX idx_menu_items_available ON menu_items(available);
CREATE INDEX idx_order_tracking_order_id ON order_tracking(order_id);

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE zippyroute_db TO postgres;

-- Display confirmation
\dt

-- Insert sample categories (if needed)
-- These will be auto-created when menu items are added

-- Sample menu items (optional - uncomment to add during setup)
/*
INSERT INTO menu_items (name, description, price, category, available, created_at, updated_at) VALUES
('Margherita Pizza', 'Fresh tomato, basil, and mozzarella', 299, 'Pizza', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Pepperoni Pizza', 'Loaded with pepperoni and cheese', 349, 'Pizza', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Veggie Burger', 'Fresh vegetables with wholegrain bun', 199, 'Burgers', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Chicken Burger', 'Grilled chicken breast with special sauce', 229, 'Burgers', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Biryani', 'Fragrant basmati rice with spices', 249, 'Indian', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Butter Chicken', 'Chicken in creamy tomato sauce', 319, 'Indian', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Caesar Salad', 'Crisp romaine with parmesan and croutons', 179, 'Salads', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Greek Salad', 'Feta cheese, olives and tomatoes', 189, 'Salads', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Chocolate Brownie', 'Fudgy chocolate with nuts', 99, 'Desserts', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Ice Cream Sundae', 'Vanilla ice cream with toppings', 129, 'Desserts', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Coca Cola', 'Chilled cola beverage 250ml', 49, 'Beverages', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
('Fresh Lemonade', 'Freshly squeezed with ice', 79, 'Beverages', true, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000, EXTRACT(EPOCH FROM NOW())::BIGINT * 1000);
*/
