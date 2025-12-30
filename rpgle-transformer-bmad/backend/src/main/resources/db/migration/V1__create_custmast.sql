-- ===========================================================================
-- Flyway Migration: V1__create_custmast.sql
-- Table: CUSTMAST (Customer Master File)
--
-- Original DDS Physical File: source-rpgle/dds/physical-files/CUSTMAST.dds
-- Record Format: CUSTREC
--
-- This migration creates the CUSTMAST table preserving the original AS/400
-- data structure with SQL column names matching DDS field names.
--
-- DDS Field Mapping:
-- - CUSTNO (5P 0) -> BIGINT NOT NULL PRIMARY KEY
-- - CUSTNAME (30A) -> VARCHAR(30) NOT NULL
-- - ADDR1 (30A) -> VARCHAR(30)
-- - CITY (20A) -> VARCHAR(20)
-- - STATE (2A) -> CHAR(2)
-- - ZIP (5P 0) -> INTEGER
-- - PHONE (12A) -> VARCHAR(12)
-- - BALANCE (9P 2) -> DECIMAL(9,2)
-- - CREDITLIM (9P 2) -> DECIMAL(9,2)
-- - LASTORDER (8P 0) -> INTEGER
-- ===========================================================================

CREATE TABLE CUSTMAST (
    -- CUSTNO (5P 0) - Customer Number - Primary Key
    -- DDS Field: CUSTNO - Packed decimal 5 digits, 0 decimal places
    CUSTNO BIGINT NOT NULL PRIMARY KEY,

    -- CUSTNAME (30A) - Customer Name
    -- DDS Field: CUSTNAME - Alphanumeric 30 characters
    CUSTNAME VARCHAR(30) NOT NULL,

    -- ADDR1 (30A) - Address Line 1
    -- DDS Field: ADDR1 - Alphanumeric 30 characters
    ADDR1 VARCHAR(30),

    -- CITY (20A) - City
    -- DDS Field: CITY - Alphanumeric 20 characters
    CITY VARCHAR(20),

    -- STATE (2A) - State Code
    -- DDS Field: STATE - Alphanumeric 2 characters (fixed width)
    STATE CHAR(2),

    -- ZIP (5P 0) - Zip Code
    -- DDS Field: ZIP - Packed decimal 5 digits, 0 decimal places
    ZIP INTEGER,

    -- PHONE (12A) - Phone Number
    -- DDS Field: PHONE - Alphanumeric 12 characters
    PHONE VARCHAR(12),

    -- BALANCE (9P 2) - Account Balance
    -- DDS Field: BALANCE - Packed decimal 9 digits, 2 decimal places
    BALANCE DECIMAL(9,2),

    -- CREDITLIM (9P 2) - Credit Limit
    -- DDS Field: CREDITLIM - Packed decimal 9 digits, 2 decimal places
    CREDITLIM DECIMAL(9,2),

    -- LASTORDER (8P 0) - Last Order Date (YYYYMMDD format)
    -- DDS Field: LASTORDER - Packed decimal 8 digits, 0 decimal places
    LASTORDER INTEGER
);

-- Index on customer name for search performance
CREATE INDEX IDX_CUSTMAST_NAME ON CUSTMAST(CUSTNAME);
