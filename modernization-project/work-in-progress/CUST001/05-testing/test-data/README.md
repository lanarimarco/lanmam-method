# Test Data for CUST001 Customer Inquiry

This directory contains test data setup scripts and utilities for testing the Customer Inquiry functionality.

## Files

### test-customer-data.sql
SQL script to populate the CUSTMAST table with comprehensive test data.

**Usage:**
```bash
# DB2
db2 -tvf test-customer-data.sql

# PostgreSQL
psql -d your_database -f test-customer-data.sql

# H2 (in-memory for tests)
# Automatically loaded via @Sql annotation in tests
```

**Test Data Categories:**

1. **Happy Path Scenarios** (CUSTNO 10001-10003, 12345)
   - Standard customers with various data
   - Used for successful inquiry tests

2. **Edge Cases** (CUSTNO 1, 99999, 10004-10005)
   - Minimum customer number (1)
   - Maximum customer number (99999)
   - Negative balance
   - Customer at credit limit

3. **Boundary Values** (CUSTNO 10006-10009)
   - Maximum balance (9999999.99)
   - Maximum length names, addresses, cities
   - Tests field length limits

4. **Data Precision** (CUSTNO 20001-20003)
   - Various decimal precision values
   - Tests BigDecimal handling

5. **Date Scenarios** (CUSTNO 30001-30003)
   - Recent orders
   - Old orders
   - NULL order dates

6. **Various States** (CUSTNO 40001-40003)
   - Different state codes
   - Regional diversity

### CustomerTestDataBuilder.java
Java test data builder for creating Customer entities and DTOs programmatically.

**Usage:**
```java
// Build a default test customer
Customer customer = CustomerTestDataBuilder.aCustomer().build();

// Build ACME Corporation test customer
Customer acme = CustomerTestDataBuilder.anAcmeCustomer().build();

// Build custom customer
Customer custom = CustomerTestDataBuilder.aCustomer()
    .withCustomerNumber(99999)
    .withCustomerName("Custom Corp")
    .withBalance(new BigDecimal("2500.00"))
    .build();

// Build CustomerInquiryDTO
CustomerInquiryDTO dto = CustomerTestDataBuilder.anAcmeCustomer().buildDTO();

// Build request DTO (only customer number)
CustomerInquiryDTO request = CustomerTestDataBuilder.aCustomer()
    .withCustomerNumber(12345)
    .buildRequestDTO();
```

**Pre-built Templates:**
- `aCustomer()` - Default test customer
- `anAcmeCustomer()` - ACME Corporation (matches CUSTNO 12345)
- `aMinimalCustomer()` - Minimum required fields
- `aMaximalCustomer()` - Maximum values for all fields
- `anOverdrawnCustomer()` - Customer with negative balance

## Test Scenarios Coverage

### Validation Tests
- Zero customer number (0)
- Null customer number
- Invalid customer number

### Happy Path Tests
- Valid customer found (CUSTNO 12345)
- All fields populated correctly
- Proper decimal precision

### Not Found Tests
- Non-existent customer number (99999)
- Deleted customer
- Never-existed customer

### Boundary Tests
- Minimum customer number (1)
- Maximum customer number (99999)
- Maximum balance (9999999.99)
- Minimum balance (0.00)
- Negative balance (-500.00)

### Data Integrity Tests
- Decimal precision (9P 2 fields)
- Date formatting (YYYYMMDD to LocalDate)
- String length limits
- NULL handling

## Integration with Tests

### Unit Tests (Service/Repository)
Use `CustomerTestDataBuilder` to create mock data:
```java
@Test
void testFindCustomer() {
    Customer customer = CustomerTestDataBuilder.anAcmeCustomer().build();
    when(repository.findByCustomerNumber(12345))
        .thenReturn(Optional.of(customer));
    // ... test logic
}
```

### Integration Tests (Controller)
Load SQL data using Spring's `@Sql`:
```java
@Test
@Sql("/test-customer-data.sql")
void testControllerWithRealData() {
    // ... test logic with real database
}
```

### Repository Tests (JPA)
Uses in-memory H2 database with test data:
```java
@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Customer customer = CustomerTestDataBuilder.anAcmeCustomer().build();
        entityManager.persist(customer);
        entityManager.flush();
    }
}
```

## Maintenance

When adding new test scenarios:
1. Add SQL records to `test-customer-data.sql`
2. Update this README with new test data description
3. Consider adding pre-built template to `CustomerTestDataBuilder`
4. Update test documentation in `test-results.md`
