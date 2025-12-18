-- Test Data Setup for CUST001 Customer Inquiry Tests
-- This script creates test customer records in the CUSTMAST table
-- for unit testing, integration testing, and manual testing

-- =============================================================================
-- TEST CUSTOMERS - HAPPY PATH SCENARIOS
-- =============================================================================

-- Test Customer 1: Standard customer with all fields populated
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (12345, 'ACME Corporation', '123 Main Street', 'New York', 'NY', 10001, '212-555-1234', 1500.50, 5000.00, '2025-12-15');

-- Test Customer 2: Customer with high balance
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10001, 'Global Industries Inc', '456 Oak Avenue', 'Chicago', 'IL', 60601, '312-555-5678', 25000.00, 50000.00, '2025-12-10');

-- Test Customer 3: Customer with zero balance
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10002, 'Smith & Associates', '789 Elm Street', 'Los Angeles', 'CA', 90001, '213-555-9012', 0.00, 10000.00, '2025-11-20');

-- Test Customer 4: Customer with minimal data
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10003, 'Jones Co', '321 Pine Road', 'Houston', 'TX', 77001, '713-555-3456', 500.00, 2000.00, NULL);

-- =============================================================================
-- TEST CUSTOMERS - EDGE CASES
-- =============================================================================

-- Edge Case 1: Minimum customer number (1)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (1, 'First Customer', '100 First Street', 'Boston', 'MA', 2101, '617-555-0001', 100.00, 1000.00, '2025-01-01');

-- Edge Case 2: Maximum customer number (99999)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (99999, 'Last Customer', '999 Last Avenue', 'Seattle', 'WA', 98101, '206-555-9999', 999.99, 9999.99, '2025-12-31');

-- Edge Case 3: Customer with negative balance
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10004, 'Overdrawn Customer', '555 Debt Lane', 'Miami', 'FL', 33101, '305-555-7890', -500.00, 5000.00, '2025-12-01');

-- Edge Case 4: Customer at credit limit
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10005, 'Maxed Out Industries', '777 Credit Road', 'Denver', 'CO', 80201, '303-555-2468', 10000.00, 10000.00, '2025-12-18');

-- =============================================================================
-- TEST CUSTOMERS - BOUNDARY VALUES
-- =============================================================================

-- Boundary Test 1: Maximum balance (9P 2 = 9999999.99)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10006, 'Big Balance Corp', '888 Money Street', 'Dallas', 'TX', 75201, '214-555-8888', 9999999.99, 9999999.99, '2025-12-17');

-- Boundary Test 2: Long names (30 characters max)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10007, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ12', '999 Alphabet Drive', 'Phoenix', 'AZ', 85001, '602-555-1111', 1000.00, 5000.00, '2025-12-16');

-- Boundary Test 3: Long address (30 characters max)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10008, 'Long Address Co', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ12', 'San Francisco', 'CA', 94101, '415-555-2222', 2500.00, 7500.00, '2025-12-14');

-- Boundary Test 4: Long city (20 characters max)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (10009, 'Long City Corp', '111 City Street', 'ABCDEFGHIJKLMNOPQR', 'NY', 10002, '212-555-3333', 3000.00, 8000.00, '2025-12-13');

-- =============================================================================
-- TEST CUSTOMERS - DATA PRECISION
-- =============================================================================

-- Decimal Precision Test 1: Various decimal values
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (20001, 'Decimal Test 1', '101 Precision Ave', 'Austin', 'TX', 78701, '512-555-0101', 1234.56, 5000.00, '2025-12-12');

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (20002, 'Decimal Test 2', '102 Precision Ave', 'Austin', 'TX', 78702, '512-555-0102', 0.01, 1000.00, '2025-12-11');

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (20003, 'Decimal Test 3', '103 Precision Ave', 'Austin', 'TX', 78703, '512-555-0103', 0.99, 2000.00, '2025-12-10');

-- =============================================================================
-- TEST CUSTOMERS - DATE SCENARIOS
-- =============================================================================

-- Date Test 1: Recent order date
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (30001, 'Recent Order Co', '201 Today Street', 'Portland', 'OR', 97201, '503-555-2001', 1500.00, 5000.00, '2025-12-18');

-- Date Test 2: Old order date
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (30002, 'Old Order Inc', '202 Yesterday Rd', 'Portland', 'OR', 97202, '503-555-2002', 800.00, 3000.00, '2020-01-15');

-- Date Test 3: No order date (NULL)
INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (30003, 'Never Ordered LLC', '203 Never Lane', 'Portland', 'OR', 97203, '503-555-2003', 0.00, 1000.00, NULL);

-- =============================================================================
-- TEST CUSTOMERS - VARIOUS STATES
-- =============================================================================

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (40001, 'California Co', '301 West Ave', 'San Diego', 'CA', 92101, '619-555-3001', 2000.00, 6000.00, '2025-12-05');

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (40002, 'Florida Inc', '302 South Blvd', 'Tampa', 'FL', 33601, '813-555-3002', 1800.00, 5500.00, '2025-12-06');

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
VALUES (40003, 'Washington LLC', '303 North St', 'Spokane', 'WA', 99201, '509-555-3003', 2200.00, 7000.00, '2025-12-07');

-- =============================================================================
-- COMMIT TRANSACTION
-- =============================================================================

COMMIT;
