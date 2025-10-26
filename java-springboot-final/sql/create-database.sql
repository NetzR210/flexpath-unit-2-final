-- Create and use the database
CREATE DATABASE IF NOT EXISTS web_shop;
USE web_shop;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS users, roles, products, orders, order_items;

-- Create users table
CREATE TABLE users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
);

-- Create roles table
CREATE TABLE roles (
    username VARCHAR(255) NOT NULL,
    role VARCHAR(250) NOT NULL,
    PRIMARY KEY (username, role),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Create products table
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price DECIMAL(10, 2)
);

-- Create orders table
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Create order_items table
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Insert admin user
INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$tBTfzHzjmQVKza3VSa5lsOX6/iL93xPVLlLXYg2FhT6a.jb1o6VDq');

-- Assign ADMIN role to admin
INSERT INTO roles (username, role)
VALUES ('admin', 'ADMIN');

-- Insert test-admin user for test compatibility
INSERT INTO users (username, password)
VALUES ('test-admin', '$2a$10$tBTfzHzjmQVKza3VSa5lsOX6/iL93xPVLlLXYg2FhT6a.jb1o6VDq');

-- Assign ADMIN role to test-admin
INSERT INTO roles (username, role)
VALUES ('test-admin', 'ADMIN');

-- Insert sample products
INSERT INTO products (name, price) VALUES ('Apple', 0.99);
INSERT INTO products (name, price) VALUES ('Banana', 0.59);
INSERT INTO products (name, price) VALUES ('Cherry', 1.99);
INSERT INTO products (name, price) VALUES ('Date', 2.99);
INSERT INTO products (name, price) VALUES ('Elderberry', 3.99);

-- Insert sample orders for admin
INSERT INTO orders (username) VALUES ('admin');
INSERT INTO orders (username) VALUES ('admin');
INSERT INTO orders (username) VALUES ('admin');
INSERT INTO orders (username) VALUES ('admin');
INSERT INTO orders (username) VALUES ('admin');
