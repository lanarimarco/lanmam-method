# Physical Files DDS Directory

Place your DDS source for physical files (database tables) here.

## Purpose
Physical files in AS/400/IBM i define the database table structures. They specify:
- Field names, types, and lengths
- Primary keys and unique constraints
- Field-level validations and defaults
- Text descriptions for fields and files
- Data types (character, numeric, date, time, etc.)

## File Naming
- Typically use `.dds` or `.pf` extensions
- Example: `CUSTMAST.dds` (customer master file)
- Often named with suffix PF or F, or descriptive names like MAST (master)

## Conversion Notes
During conversion:
- Physical files are converted to JPA entities
- Fields become entity attributes with appropriate Java types
- Keys become @Id or unique constraints
- Field validations may become @Column constraints or Bean Validation annotations
- Relationships (foreign keys) are identified and modeled as JPA relationships
- The database agent (Phase 2) handles this conversion

## Common DDS to Java Type Mappings
- **A (Character)** → String
- **S (Zoned Decimal)** → BigDecimal
- **P (Packed Decimal)** → BigDecimal
- **B (Binary)** → Integer or Long
- **L (Date)** → LocalDate
- **T (Time)** → LocalTime
- **Z (Timestamp)** → LocalDateTime

See [type-mappings.md](../../../agents/02-database-agent/type-mappings.md) for complete mapping rules.
