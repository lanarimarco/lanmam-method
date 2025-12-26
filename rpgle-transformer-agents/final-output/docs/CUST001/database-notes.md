# Database Layer - CUST001

**Created**: 2025-12-19
**Agent**: Database Agent
**Program**: CUST001 (Customer Inquiry)

---

## Overview

This document describes the database layer conversion from RPGLE DDS files to JPA entities and Spring Data repositories for the CUST001 customer inquiry program.

## Files Converted

### Physical Files
| DDS File | Entity Class | Repository | Description |
|----------|--------------|------------|-------------|
| CUSTMAST.dds | Customer.java | CustomerRepository.java | Customer master file |

### Logical Files
| DDS File | Purpose | Implementation |
|----------|---------|----------------|
| None | N/A | No logical files used by CUST001 |

---

## Entity: Customer

**Source**: CUSTMAST.dds
**Table**: CUSTMAST
**Primary Key**: CUSTNO (customerNumber)

### Field Mappings

| DDS Field | DDS Type | Java Field | Java Type | Notes |
|-----------|----------|------------|-----------|-------|
| CUSTNO | 5P 0 | customerNumber | BigDecimal | Primary key, 5-digit packed decimal |
| CUSTNAME | 30A | customerName | String | Customer name, max 30 characters |
| ADDR1 | 30A | address | String | Address line 1, max 30 characters |
| CITY | 20A | city | String | City name, max 20 characters |
| STATE | 2A | state | String | State code, 2 characters |
| ZIP | 5P 0 | zipCode | BigDecimal | 5-digit zip code as packed decimal |
| PHONE | 12A | phone | String | Phone number, max 12 characters |
| BALANCE | 9P 2 | balance | BigDecimal | Account balance with 2 decimal places |
| CREDITLIM | 9P 2 | creditLimit | BigDecimal | Credit limit with 2 decimal places |
| LASTORDER | 8P 0 | lastOrderDate | String | Last order date in YYYYMMDD format |

### Type Conversion Decisions

#### Packed Decimal Fields
- **CUSTNO (5P 0)**: Converted to `BigDecimal` to preserve exact precision. Primary key field.
- **ZIP (5P 0)**: Converted to `BigDecimal` to handle leading zeros and maintain compatibility with DB2.
- **BALANCE (9P 2)**: Converted to `BigDecimal` for exact monetary precision (9 digits, 2 decimal places).
- **CREDITLIM (9P 2)**: Converted to `BigDecimal` for exact monetary precision.
- **LASTORDER (8P 0)**: Stored as `String` to preserve YYYYMMDD format. Service layer should convert to `LocalDate` when needed.

**Rationale**: `BigDecimal` is used for all packed decimal fields to ensure exact precision matching DB2 behavior, especially important for monetary values and keys.

#### Character Fields
All character fields (A type) are mapped directly to Java `String` with appropriate length constraints via `@Column(length=n)`.

#### Date Fields
- **LASTORDER**: Stored as String (8 characters) in YYYYMMDD format
- **Conversion Strategy**: The service layer should handle conversion between String and LocalDate
- **Reason**: Maintains compatibility with legacy DB2 storage format while allowing modern date handling in business logic

### JPA Annotations

```java
@Entity
@Table(name = "CUSTMAST")
public class Customer {
    @Id
    @Column(name = "CUSTNO", precision = 5, scale = 0)
    private BigDecimal customerNumber;
    // ... other fields
}
```

- `@Entity`: Marks class as JPA entity
- `@Table(name = "CUSTMAST")`: Maps to existing DB2 table
- `@Id`: Designates primary key field
- `@Column(name = "...")`: Preserves original DB2 column names
- `precision` and `scale`: Matches DDS packed decimal specifications

---

## Repository: CustomerRepository

**Entity**: Customer
**Primary Key Type**: BigDecimal

### Methods Provided

#### 1. findByCustomerNumber
```java
Optional<Customer> findByCustomerNumber(BigDecimal customerNumber);
```
- **RPGLE Equivalent**: `CHAIN CUSTNO CUSTMAST`
- **Purpose**: Direct keyed lookup by customer number
- **Returns**: Optional containing customer if found, empty if not found
- **Usage**: Main lookup method for customer inquiry

#### 2. existsByCustomerNumber
```java
boolean existsByCustomerNumber(BigDecimal customerNumber);
```
- **Purpose**: Quick validation check without loading full entity
- **Returns**: true if customer exists, false otherwise
- **Usage**: Can be used for validation before operations

#### 3. Inherited JPA Methods
From `JpaRepository<Customer, BigDecimal>`:
- `findById(BigDecimal id)`: Alternative to findByCustomerNumber
- `save(Customer entity)`: For create/update operations
- `delete(Customer entity)`: For delete operations
- `findAll()`: Retrieve all customers (use with caution on large datasets)

### RPGLE Operation Mappings

| RPGLE Operation | Repository Method | Notes |
|----------------|-------------------|-------|
| CHAIN CUSTNO CUSTMAST | findByCustomerNumber(custNo) | Returns Optional<Customer> |
| %Found(CUSTMAST) | optional.isPresent() | Check if Optional is populated |
| %Error | Spring Data handles exceptions | DataAccessException hierarchy |

---

## Schema Compatibility

### Assumptions
- DB2 table `CUSTMAST` exists with the exact field structure defined in CUSTMAST.dds
- All column names match the DDS field names exactly
- Data types in DB2 are compatible with JPA mappings (DECIMAL, CHAR/VARCHAR)

### No Schema Changes Required
This conversion maintains 100% compatibility with the existing DB2 schema. No DDL changes are needed.

### Future Considerations
If modernizing the database schema in the future:
1. **ZIP code**: Could be changed to String/VARCHAR for better handling of leading zeros
2. **LASTORDER**: Could be migrated to a proper DATE or TIMESTAMP column
3. **CUSTNO**: Could use auto-increment INTEGER if business rules allow

---

## Quality Checklist

- [x] All fields from DDS mapped to entity
- [x] Correct JPA annotations used (@Entity, @Table, @Id, @Column)
- [x] Primary key properly defined (CUSTNO)
- [x] Column names match DB2 names exactly
- [x] Precision and scale specified for BigDecimal fields
- [x] Field lengths specified for String fields
- [x] Relationships properly mapped (N/A - no relationships in this table)
- [x] Repository methods match RPGLE operations (CHAIN → findByCustomerNumber)
- [x] Proper null handling considerations documented

---

## Testing Considerations

### Unit Testing
- Test entity creation with valid data
- Test equals/hashCode with same and different customer numbers
- Verify toString output format

### Repository Testing
- Test findByCustomerNumber with existing customer
- Test findByCustomerNumber with non-existent customer (should return empty Optional)
- Test existsByCustomerNumber for both existing and non-existent customers
- Verify BigDecimal comparison works correctly for customer number lookups

### Integration Testing
- Verify connection to actual DB2 database
- Test retrieval of real customer data
- Verify all fields map correctly from database to entity
- Test date field YYYYMMDD format handling

---

## Known Limitations

1. **Date Handling**: LASTORDER is stored as String, requiring manual conversion in service layer
2. **No Validation**: Entity has no bean validation annotations (@NotNull, @Size, etc.) - add in service/controller layer as needed
3. **No Relationships**: Customer entity is standalone with no JPA relationships to other entities (none exist yet)

---

## Next Steps (For Subsequent Agents)

### For Conversion Agent (Phase 3)
- Use `customerRepository.findByCustomerNumber(custNo)` to replace CHAIN operations
- Check `optional.isPresent()` or use `optional.ifPresent()` for %Found logic
- Convert BigDecimal customerNumber from user input (may need Integer → BigDecimal conversion)
- Handle Optional.empty() case for "customer not found" error message

### For UI Agent (Phase 4)
- Display customer number as integer in UI (convert BigDecimal to String/Integer)
- Format balance and creditLimit as currency (use proper locale formatting)
- Display lastOrderDate with proper date formatting (convert YYYYMMDD String to formatted date)
- Apply input masks for phone and zip fields

### For Testing Agent (Phase 5)
- Create test data with valid customer records in test database
- Test boundary values: customer number 0, 99999
- Test BigDecimal precision for monetary fields
- Test date format validation for YYYYMMDD

---

## References

- Source DDS: `/source-rpgle/dds/physical-files/CUSTMAST.dds`
- Analysis Document: `/work-in-progress/CUST001/01-analysis/CUST001-analysis.md`
- Type Mappings: `/agents/02-database-agent/type-mappings.md`
- Naming Conventions: `/agents/02-database-agent/naming-conventions.md`
- Maven Structure: `/common-patterns/maven-project-structure.md`
