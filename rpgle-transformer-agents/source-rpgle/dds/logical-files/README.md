# Logical Files DDS Directory

Place your DDS source for logical files (views) here.

## Purpose
Logical files in AS/400/IBM i define alternate views or access paths to physical files. They can:
- Define alternate keys and sort orders
- Select/omit specific records
- Join multiple physical files
- Define calculated fields

## File Naming
- Typically use `.dds` or `.lf` extensions
- Example: `CUSTLNAME.dds` (customers sorted by last name)

## Conversion Notes
During conversion:
- Simple logical files may map to JPA repository query methods
- Complex logical files with joins may become JPA entity relationships or database views
- Select/omit logic may become WHERE clauses in repository methods
