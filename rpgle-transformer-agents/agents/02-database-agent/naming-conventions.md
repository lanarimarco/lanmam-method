# Database Naming Conventions

## Entity Class Names
- Use singular, PascalCase
- Example: `Customer` (not `Customers`, not `CUSTMAST`)
- Should represent business concept, not file name

## Field Names
- Use camelCase in Java
- Example: `customerNumber` (not `CUSNUM`)
- Be descriptive and clear

## Repository Names
- Pattern: `{Entity}Repository`
- Example: `CustomerRepository`

## Table/Column Annotations
- Keep original DB2 names in annotations
- Example: `@Table(name = "CUSTMAST")`
- Example: `@Column(name = "CUSNUM")`

## Package Structure
- Entities: `com.company.modernization.entities`
- Repositories: `com.company.modernization.repositories`

## Examples

### Physical File: CUSTMAST
```java
@Entity
@Table(name = "CUSTMAST")
public class Customer {

    @Id
    @Column(name = "CUSNUM", precision = 7, scale = 0)
    private BigDecimal customerNumber;

    @Column(name = "CUSNAM", length = 30)
    private String customerName;

    @Column(name = "CUSADR", length = 50)
    private String address;

    @Column(name = "CUSCTY", length = 20)
    private String city;

    @Column(name = "CUSSTS", length = 2)
    private String state;

    @Column(name = "CUSZIP", length = 10)
    private String zipCode;

    @Column(name = "CUSBAL", precision = 11, scale = 2)
    private BigDecimal balance;

    // Getters, setters, constructors
}
```
