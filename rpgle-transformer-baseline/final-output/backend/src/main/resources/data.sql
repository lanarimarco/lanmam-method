-- Sample customer data for testing
INSERT INTO customer_master (customer_number, customer_name, address_line1, city, state, zip_code, phone_number, account_balance, credit_limit, last_order_date)
VALUES
    (10001, 'Acme Corporation', '123 Main Street', 'New York', 'NY', 10001, '212-555-0100', 15000.50, 50000.00, '2025-12-15'),
    (10002, 'TechStart Inc', '456 Innovation Drive', 'San Francisco', 'CA', 94102, '415-555-0200', 8500.75, 25000.00, '2025-12-10'),
    (10003, 'Global Traders LLC', '789 Commerce Blvd', 'Chicago', 'IL', 60601, '312-555-0300', 22000.00, 75000.00, '2025-12-20'),
    (10004, 'Premier Solutions', '321 Business Park', 'Boston', 'MA', 02101, '617-555-0400', 5000.25, 20000.00, '2025-11-28'),
    (10005, 'Midwest Manufacturing', '654 Industrial Way', 'Detroit', 'MI', 48201, '313-555-0500', 31500.00, 100000.00, '2025-12-18');
