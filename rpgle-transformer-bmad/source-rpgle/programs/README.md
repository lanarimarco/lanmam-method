# RPGLE Programs Directory

Place your RPGLE program source files here.

## Purpose
This directory contains the RPGLE programs (business logic) to be converted to modern Java/Spring Boot applications.

## File Naming
- Typically use `.rpgle`, `.rpg`, or `.pgm` extensions
- Example: `CUST001.rpgle` (customer inquiry program)
- Follow your organization's naming conventions

## Program Types
Common RPGLE program types you may place here:
- **Interactive programs** - Programs that interact with users via 5250 displays
- **Batch programs** - Background processing programs
- **Service programs** - Reusable procedures (similar to libraries)
- **SQL programs** - Programs using embedded SQL

## Conversion Notes
During the 7-phase conversion workflow:

1. **Phase 1 (Analysis)** - Analyzes the RPGLE program structure, business logic, data flows, and dependencies
2. **Phase 2 (Database)** - Uses file references to create JPA entities and repositories
3. **Phase 3 (Conversion)** - Converts RPGLE business logic to Java services and REST controllers
4. **Phase 4 (UI)** - Converts display file interactions to React components
5. **Phase 5 (Testing)** - Generates unit and integration tests
6. **Phase 6 (Review)** - Reviews and refactors the generated code
7. **Phase 7 (Integration)** - Consolidates and prepares for deployment

## Dependencies
Make sure to also include in the appropriate directories:
- **Display files** used by the program → `source-rpgle/dds/display-files/`
- **Physical files** accessed → `source-rpgle/dds/physical-files/`
- **Logical files** accessed → `source-rpgle/dds/logical-files/`
- **Copybooks** included → `source-rpgle/copybooks/`

## Getting Started
To convert a program:
```bash
cd scripts
./run-full-pipeline.sh PROGRAM_NAME
```

See the main [README.md](../../README.md) and [WORKFLOW-GUIDE.md](../../WORKFLOW-GUIDE.md) for detailed instructions.
