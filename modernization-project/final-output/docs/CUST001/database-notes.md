# Database Conversion Notes - CUST001

**Conversion Date**: 2025-12-17
**Source File**: CUSTMAST.dds
**Program**: CUST001 (Customer Inquiry)

---

## Entity Created

### Customer Entity (`Customer.java`)
- **Package**: `com.smeup.erp.entities`
- **Table**: `CUSTMAST`
- **Primary Key**: `customerNumber` (Integer)

## Field Mappings

| DDS Field | Type | Java Field | Java Type | Notes |
|-----------|------|------------|-----------|-------|
| CUSTNO | 5P 0 | customerNumber | Integer | Primary key, 5-digit packed decimal |
| CUSTNAME | 30A | customerName | String | Customer name, max 30 characters |
| ADDR1 | 30A | address1 | String | Address line 1, max 30 characters |
| CITY | 20A | city | String | City name, max 20 characters |
| STATE | 2A | state | String | 2-character state code |
| ZIP | 5P 0 | zipCode | Integer | 5-digit zip code |
| PHONE | 12A | phone | String | Phone number, max 12 characters |
| BALANCE | 9P 2 | balance | BigDecimal | Account balance with 2 decimals |
| CREDITLIM | 9P 2 | creditLimit | BigDecimal | Credit limit with 2 decimals |
| LASTORDER | 8P 0 | lastOrderDate | LocalDate | Last order date (YYYYMMDD format) |

## Type Conversion Decisions

### Numeric Types
1. **CUSTNO (5P 0)**: Converted to `Integer` instead of `BigDecimal`
   - Rationale: Customer numbers are integer identifiers without decimals
   - The 5P 0 format indicates 5 digits with no decimal places
   - Integer is more appropriate for ID fields in Java

2. **ZIP (5P 0)**: Converted to `Integer`
   - Rationale: 5-digit numeric zip codes
   - Packed decimal with no decimals is best represented as Integer
   - Alternative: Could use String to preserve leading zeros, but Integer is cleaner for 5-digit US zips

3. **BALANCE and CREDITLIM (9P 2)**: Converted to `BigDecimal`
   - Rationale: Monetary values require exact precision
   - BigDecimal prevents floating-point rounding errors
   - Preserves 2 decimal places as specified in DDS

### Date Types
4. **LASTORDER (8P 0)**: Converted to `LocalDate`
   - Rationale: DDS stores date as YYYYMMDD in packed decimal format
   - LocalDate provides proper date semantics in Java
   - Note: Conversion logic will be needed in service layer to convert between YYYYMMDD integer and LocalDate
   - JPA attribute converter may be needed for automatic conversion

### Character Types
5. **All character fields (A)**: Converted to `String`
   - Standard mapping for DB2 character fields
   - Length preserved in @Column annotation

## Repository Created

### CustomerRepository (`CustomerRepository.java`)
- **Package**: `com.smeup.erp.repositories`
- **Extends**: `JpaRepository<Customer, Integer>`

### Methods Implemented

1. **findByCustomerNumber(Integer customerNumber)**
   - Maps to: RPGLE CHAIN operation on CUSTMAST by CUSTNO
   - Returns: `Optional<Customer>` (handles %Found/%NotFound pattern)
   - Purpose: Main lookup operation for customer inquiry

2. **existsByCustomerNumber(Integer customerNumber)**
   - Utility method for validation
   - Returns: boolean indicating if customer exists
   - Purpose: Quick existence check without loading full entity

## RPGLE Operation Mappings

| RPGLE Operation | JPA Equivalent | Notes |
|----------------|----------------|-------|
| CHAIN CUSTMAST | findByCustomerNumber() | Keyed read by primary key |
| %Found | Optional.isPresent() | Check if record exists |
| %NotFound | Optional.isEmpty() | Check if record not found |

## Schema Considerations

### Nullable Fields
- All fields are nullable by default (no @Column(nullable = false) specified)
- Rationale: RPGLE allows blank/zero values in these fields
- Business logic validation should be handled in service layer

### No Relationships
- CUSTMAST is a standalone master file
- No foreign keys or relationships identified in CUST001 program
- Future programs may reveal relationships to add

### Index Recommendations
- Primary key index on CUSTNO exists (defined in DDS with K)
- Additional indexes may be needed based on query patterns from other programs

## Data Migration Notes

### Date Field Conversion
The LASTORDER field requires special handling during data migration:
```java
// Pseudo-code for conversion
int yyyymmdd = 20231225; // From RPGLE
LocalDate date = LocalDate.of(
    yyyymmdd / 10000,        // year
    (yyyymmdd / 100) % 100,  // month
    yyyymmdd % 100           // day
);
```

Consider creating a JPA AttributeConverter:
```java
@Converter
public class YyyymmddToLocalDateConverter implements AttributeConverter<LocalDate, Integer> {
    // Implementation to auto-convert between Integer and LocalDate
}
```

### Packed Decimal Preservation
- DB2 packed decimal fields map correctly to Integer/BigDecimal
- Ensure database column types are DECIMAL with correct precision and scale
- CUSTNO: DECIMAL(5,0)
- ZIP: DECIMAL(5,0)
- BALANCE: DECIMAL(9,2)
- CREDITLIM: DECIMAL(9,2)
- LASTORDER: DECIMAL(8,0) or DATE type if converting at DB level

## Testing Recommendations

1. **Entity Tests**:
   - Verify all getters/setters work correctly
   - Test equals/hashCode with same and different customer numbers
   - Test toString output

2. **Repository Tests**:
   - Test findByCustomerNumber with existing customer
   - Test findByCustomerNumber with non-existent customer (returns empty Optional)
   - Test existsByCustomerNumber for both cases

3. **Integration Tests**:
   - Verify JPA annotations work with actual DB2 database
   - Test data persistence and retrieval
   - Verify precision is maintained for monetary fields
   - Test date conversion for LASTORDER field

## Quality Checklist

- [x] All fields from DDS mapped to entity
- [x] Correct JPA annotations used (@Entity, @Table, @Column, @Id)
- [x] Primary key properly defined (CUSTNO)
- [x] Column names match DB2 names (CUSTNO, CUSTNAME, etc.)
- [x] Relationships properly mapped (none for this entity)
- [x] Repository methods match RPGLE operations (CHAIN → findByCustomerNumber)
- [x] Proper Java naming conventions (camelCase fields, PascalCase class)
- [x] Package structure matches folder structure
- [x] Documentation complete

## Next Steps for Conversion Agent

When implementing the business logic for CUST001:

1. Use `CustomerRepository.findByCustomerNumber()` to replace CHAIN operation
2. Check `Optional.isPresent()` to replace %Found indicator
3. Handle validation: customer number must be non-zero (> 0)
4. Map error indicator *IN90 to exception handling or validation result
5. Consider creating a CustomerService layer for business logic
6. May need DTO for UI layer to separate entity from display logic
