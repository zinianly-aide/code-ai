-- Insert test data for user table
INSERT INTO "user" (username, email, password, full_name, age) VALUES
('john_doe', 'john.doe@example.com', 'password123', 'John Doe', 30),
('jane_smith', 'jane.smith@example.com', 'password456', 'Jane Smith', 25),
('bob_johnson', 'bob.johnson@example.com', 'password789', 'Bob Johnson', 35),
('alice_williams', 'alice.williams@example.com', 'password101', 'Alice Williams', 28),
('charlie_brown', 'charlie.brown@example.com', 'password202', 'Charlie Brown', 32);

-- Insert test data for product table
INSERT INTO product (name, description, price, category, stock_quantity) VALUES
('Laptop Computer', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 'Electronics', 50),
('Wireless Mouse', 'Ergonomic wireless mouse with USB receiver', 29.99, 'Accessories', 200),
('Coffee Maker', '12-cup programmable coffee maker with thermal carafe', 89.99, 'Appliances', 30),
('Running Shoes', 'Lightweight running shoes with cushioning technology', 149.99, 'Sports', 75),
('Bluetooth Headphones', 'Noise-cancelling wireless headphones with 30-hour battery', 199.99, 'Electronics', 40),
('Yoga Mat', 'Non-slip yoga mat with carrying strap', 39.99, 'Sports', 100),
('Desk Lamp', 'LED desk lamp with adjustable brightness and color temperature', 79.99, 'Furniture', 25),
('Water Bottle', 'Insulated stainless steel water bottle, 32oz', 24.99, 'Sports', 150);

-- Insert test data for order_table
INSERT INTO order_table (user_id, order_number, total_amount, status) VALUES
(1, 'ORD-2024-001', 1329.98, 'COMPLETED'),
(2, 'ORD-2024-002', 269.97, 'SHIPPED'),
(3, 'ORD-2024-003', 149.99, 'PENDING'),
(1, 'ORD-2024-004', 89.99, 'COMPLETED'),
(4, 'ORD-2024-005', 199.99, 'PROCESSING');

-- Insert test data for order_item
INSERT INTO order_item (order_id, product_id, quantity, unit_price, total_price) VALUES
(1, 1, 1, 1299.99, 1299.99),
(1, 2, 1, 29.99, 29.99),
(2, 3, 1, 89.99, 89.99),
(2, 6, 2, 39.99, 79.98),
(2, 8, 1, 24.99, 24.99),
(2, 7, 1, 79.99, 79.99),
(3, 4, 1, 149.99, 149.99),
(4, 3, 1, 89.99, 89.99),
(5, 5, 1, 199.99, 199.99);