# RPGLE to Java Type Mappings

## Numeric Types

| RPGLE Type | DDS | Java Type | Notes |
|------------|-----|-----------|-------|
| Packed Decimal | P | BigDecimal | Financial data, exact precision |
| Zoned Decimal | S | BigDecimal | Financial data |
| Binary | B | Integer or Long | Depending on size |
| Integer | I | Integer | 4-byte integer |
| Unsigned | U | Long | Java doesn't have unsigned |

### Precision Guidelines
- For packed decimal (n,d): Use `@Column(precision=n, scale=d)`
- For money: Always BigDecimal with appropriate scale
- For quantities: BigDecimal or Integer depending on decimals

## Character Types

| RPGLE Type | DDS | Java Type | Notes |
|------------|-----|-----------|-------|
| Character | A | String | Fixed or variable length |
| Varchar | VARLEN | String | Variable length |
| Graphic | G | String | Unicode characters |

## Date/Time Types

| RPGLE Type | DDS | Java Type | Notes |
|------------|-----|-----------|-------|
| Date | L | LocalDate | Use @Temporal for legacy |
| Time | T | LocalTime | |
| Timestamp | Z | LocalDateTime | |
| Legacy (YYMMDD) | 6,0 | String or LocalDate | Convert in service layer |
| Legacy (MMDDYY) | 6,0 | String or LocalDate | Convert in service layer |
| Legacy (YYYYMMDD) | 8,0 | String or LocalDate | Convert in service layer |

## Indicator Type

| RPGLE Type | Java Type | Notes |
|------------|-----------|-------|
| Indicator (*INxx) | Boolean | true/false |
| Indicator Array | boolean[] | For *IN array |

## Special Cases

### Null Handling
- RPG blank = Java null for Strings
- RPG zero = Consider if should be null for numbers
- Use `@Column(nullable = false)` only when field truly required

### Key Fields
- Primary key: Use `@Id`
- Composite key: Create `@Embeddable` class
- Generated key: Use `@GeneratedValue` if appropriate

### DB2 Specific
- For DB2 schemas: Use `@Table(name = "TABLENAME", schema = "SCHEMANAME")`
- For DB2 column names: Use `@Column(name = "COLNAME")`
