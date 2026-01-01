-- ===========================================================================
-- Flyway Migration: V2__insert_test_data.sql
-- Table: CUSTMAST (Customer Master File)
--
-- Purpose: Insert test data for development and E2E testing
-- Original DDS Physical File: source-rpgle/dds/physical-files/CUSTMAST.dds
--
-- Test Customers:
-- - 1001: ACME Corporation (standard test case)
-- - 1002: Global Industries Inc (multi-customer test)
-- - 1003: Tech Solutions LLC (additional test data)
-- ===========================================================================

INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER) VALUES
(1001, 'ACME Corporation', '123 Main Street', 'Springfield', 'IL', 62701, '217-555-0100', 1500.50, 10000.00, 20251201),
(1002, 'Global Industries Inc', '456 Oak Avenue', 'Chicago', 'IL', 60601, '312-555-0200', 2750.00, 25000.00, 20251215),
(1003, 'Tech Solutions LLC', '789 Elm Boulevard', 'Naperville', 'IL', 60540, '630-555-0300', 500.25, 5000.00, 20251220);
