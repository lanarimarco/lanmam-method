# Database Layer Conversion Notes - CUST001

**Date**: 2025-12-28
**Program**: CUST001 - Customer Inquiry
**Source File**: CUSTMAST.dds

---

## Overview

This document describes the conversion of the CUSTMAST physical file to a JPA entity and repository for use in the modernized Java application.

---

## Files Created

### Entity Classes
- `Customer.java` - JPA entity representing CUSTMAST physical file
  - Location: `src/main/java/com/lanarimarco/modernization/entities/Customer.java`
  - Package: `com.lanarimarco.modernization.entities`

### Repository Interfaces
- `CustomerRepository.java` - Spring Data JPA repository
  - Location: `src/main/java/com/lanarimarco/modernization/repositories/CustomerRepository.java`
  - Package: `com.lanarimarco.modernization.repositories`

---

## Type Mapping Decisions

### Field Mappings

| DDS Field | DDS Type | Java Field | Java Type | Rationale |
|-----------|----------|------------|-----------|-----------|
| CUSTNO | 5P 0 | customerNumber | Integer | Primary key, packed decimal with no decimals fits Integer range (0-99999) |
| CUSTNAME | 30A | customerName | String | Direct character to String mapping |
| ADDR1 | 30A | address1 | String | Direct character to String mapping |
| CITY | 20A | city | String | Direct character to String mapping |
| STATE | 2A | state | String | Direct character to String mapping |
| ZIP | 5P 0 | zipCode | Integer | Packed decimal, US zip code format |
| PHONE | 12A | phoneNumber | String | Direct character to String mapping |
| BALANCE | 9P 2 | balance | BigDecimal | Financial data requires exact precision |
| CREDITLIM | 9P 2 | creditLimit | BigDecimal | Financial data requires exact precision |
| LASTORDER | 8P 0 | lastOrderDate | Integer | Stored as YYYYMMDD numeric format |

### Primary Key
- **Field**: CUSTNO → customerNumber
- **Type**: Integer (5 digits max)
- **Annotation**: `@Id`
- **Reasoning**: Single-column primary key, naturally defined in DDS

---

## Design Decisions

### 1. Numeric Type Choices

**Customer Number and Zip Code (5P 0)**
- **Decision**: Use `Integer` instead of `BigDecimal`
- **Rationale**:
  - Values are whole numbers (scale = 0)
  - Range 0-99999 fits comfortably in Integer
  - More memory efficient than BigDecimal
  - Simpler arithmetic operations

**Balance and Credit Limit (9P 2)**
- **Decision**: Use `BigDecimal`
- **Rationale**:
  - Financial data requires exact decimal precision
  - Avoids floating-point rounding errors
  - Industry best practice for monetary values

**Last Order Date (8P 0)**
- **Decision**: Use `Integer` for storage, convert to `LocalDate` in service layer
- **Rationale**:
  - Maintains compatibility with legacy YYYYMMDD format
  - Database stores as numeric 8-digit value (e.g., 20251228)
  - Service/DTO layer can convert to proper LocalDate when needed
  - Avoids database schema changes during initial conversion

### 2. Field Inclusion

**Unused Fields**
- **Decision**: Include CREDITLIM and LASTORDER even though CUST001 doesn't use them
- **Rationale**:
  - Represents complete database schema
  - Other programs may use these fields
  - Allows for future functionality expansion
  - Maintains data integrity across system

### 3. Column Naming

**Database Column Names**
- **Decision**: Preserve original DB2 names in `@Column(name = "...")` annotations
- **Rationale**:
  - No database schema changes required
  - Works with existing DB2 database
  - Java code uses descriptive camelCase names
  - Clear mapping between Java and database

**Java Field Names**
- **Decision**: Use descriptive camelCase naming (e.g., `customerNumber` not `custno`)
- **Rationale**:
  - Follows Java naming conventions
  - More readable and self-documenting
  - Improves maintainability

### 4. Nullability

**All Fields**
- **Decision**: Allow nullable fields except for primary key
- **Rationale**:
  - DDS doesn't specify NOT NULL constraints explicitly
  - Safer to allow nulls during initial conversion
  - Can add `nullable = false` constraints later after data validation
  - Primary key (customerNumber) explicitly marked `nullable = false`

---

## Repository Design

### Custom Methods

#### findByCustomerNumber()
- **Purpose**: Find customer by customer number
- **Maps to**: RPGLE `CHAIN PCUSTNO CUSTMAST` operation
- **Return**: `Optional<Customer>` - empty if not found
- **Usage**: Primary lookup method for CUST001 program

#### existsByCustomerNumber()
- **Purpose**: Check if customer exists without loading entity
- **Use Case**: Validation scenarios
- **Return**: boolean
- **Performance**: More efficient than findBy when only existence check needed

### Inherited Methods from JpaRepository
The repository also provides standard CRUD operations:
- `findById(Integer id)` - same as findByCustomerNumber
- `findAll()` - retrieve all customers
- `save(Customer customer)` - create or update
- `deleteById(Integer id)` - delete by ID
- `count()` - count total customers

---

## Lombok Annotations

**Annotations Used**:
- `@Getter` / `@Setter` - Generate getters and setters
- `@NoArgsConstructor` - Default constructor (required by JPA)
- `@AllArgsConstructor` - Constructor with all fields
- `@Builder` - Builder pattern for object creation
- `@ToString` - Generate toString method
- `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` - Equality based on ID only

**Benefits**:
- Reduces boilerplate code
- Improves maintainability
- Standard practice in Spring Boot applications

---

## Package Structure

Following Maven and Spring Boot conventions:

```
02-database/
└── src/
    └── main/
        └── java/
            └── com/
                └── lanarimarco/
                    └── modernization/
                        ├── entities/
                        │   └── Customer.java
                        └── repositories/
                            └── CustomerRepository.java
```

Package naming:
- Base package: `com.lanarimarco.modernization` (from project-settings.md)
- Subpackages:
  - `entities` - JPA entities
  - `repositories` - Spring Data repositories

---

## Dependencies Required

The following dependencies are needed in `pom.xml`:

```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- DB2 JDBC Driver -->
    <dependency>
        <groupId>com.ibm.db2</groupId>
        <artifactId>jcc</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## Database Configuration Notes

### For application.yml

```yaml
spring:
  datasource:
    url: jdbc:db2://<host>:<port>/<database>
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.ibm.db2.jcc.DB2Driver

  jpa:
    database-platform: org.hibernate.dialect.DB2Dialect
    hibernate:
      ddl-auto: validate  # Use validate to prevent schema changes
    show-sql: false       # Set true for debugging
    properties:
      hibernate:
        format_sql: true
```

**Notes**:
- Use `ddl-auto: validate` to ensure schema matches entity definitions without modifying database
- Credentials should be externalized to environment variables
- DB2Dialect ensures proper SQL generation for DB2 database

---

## Testing Considerations

### Unit Tests (to be created in Phase 5)
- Test repository methods with H2 in-memory database
- Verify findByCustomerNumber returns correct customer
- Test behavior when customer not found (returns empty Optional)
- Verify existsByCustomerNumber for existing and non-existing customers

### Integration Tests
- Test against actual DB2 database (or DB2 Docker container)
- Verify field mappings are correct
- Test transaction management
- Verify precision of BigDecimal fields

---

## Known Limitations and Future Enhancements

### Current Limitations
1. **Date Format**: LASTORDER stored as Integer (YYYYMMDD) instead of proper date type
   - Service layer must handle conversion to LocalDate
   - Consider database migration to proper DATE column in future

2. **Phone Number Format**: Stored as String with no validation
   - Consider adding format validation in service layer
   - May want to standardize to E.164 format

3. **Zip Code**: Only supports 5-digit US zip codes
   - International zip codes would require different approach
   - Consider String type if international support needed

### Future Enhancements
1. Add Bean Validation annotations (@NotNull, @Size, @Pattern)
2. Consider adding audit fields (createdDate, modifiedDate, createdBy, modifiedBy)
3. Add indexes for frequently queried fields (if not already in database)
4. Consider soft delete pattern instead of physical deletion

---

## Quality Checklist

- [x] All fields from DDS mapped to entity
- [x] Correct JPA annotations used
- [x] Primary key properly defined (@Id on customerNumber)
- [x] Column names match DB2 names
- [x] Relationships properly mapped (N/A - no relationships in CUSTMAST)
- [x] Repository methods match RPGLE operations
- [x] Type mappings follow type-mappings.md guidelines
- [x] Naming conventions follow naming-conventions.md
- [x] Package structure follows Maven conventions
- [x] Documentation complete

---

## References

- Source DDS: `/source-rpgle/dds/physical-files/CUSTMAST.dds`
- Analysis Document: `/work-in-progress/CUST001/01-analysis/CUST001-analysis.md`
- Type Mappings Guide: `/agents/02-database-agent/type-mappings.md`
- Naming Conventions: `/agents/02-database-agent/naming-conventions.md`
- Maven Structure: `/common-patterns/maven-project-structure.md`

---

## Conversion Agent Notes

When implementing Phase 3 (Business Logic Conversion):

1. **Service Layer**: Create `CustomerInquiryService` that uses `CustomerRepository.findByCustomerNumber()`
2. **Validation**: Check that customerNumber > 0 before repository call
3. **Error Handling**: Map empty Optional to "Customer not found" response
4. **DTO Mapping**: Create `CustomerDTO` that includes only fields used by CUST001:
   - customerNumber, customerName, address1, city, state, zipCode, phoneNumber, balance
   - Exclude: creditLimit, lastOrderDate (not used by program)
5. **Date Conversion**: If lastOrderDate is added to DTO later, convert Integer to LocalDate in mapper

---

**Phase 2 Status**: ✅ COMPLETE
