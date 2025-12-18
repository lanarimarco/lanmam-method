# Phase 1: Analysis - Program: CUST001

# Analysis Agent

## Purpose
Analyze RPGLE programs to extract business logic, data flows, dependencies, and create comprehensive documentation.

## Inputs Required
1. RPGLE source file from `/source-rpgle/programs/{PROGRAM}.rpgle`
2. Related DDS files from `/source-rpgle/dds/`
3. Any copybooks from `/source-rpgle/copybooks/`
4. This agent's checklist: `checklist.md`

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/01-analysis/`:
1. `{PROGRAM}-analysis.md` - Complete analysis document including:
   - Business purpose and logic flow
   - All dependencies (programs, files, copybooks)
   - Data flow diagram (Mermaid format)
   - Complexity assessment
   - Special considerations
   - Conversion notes

## Process
1. Read the RPGLE source code completely
2. Identify the program's business purpose
3. Map all file operations (database, display, printer)
4. Document all external program calls
5. Identify all indicators and their meanings
6. Extract business rules and validation logic
7. Assess complexity (LOW/MEDIUM/HIGH)
8. Note any special considerations (date handling, packed decimals, etc.)

## Output Format
Use the template in `analysis-template.md`

## Quality Checklist
Before marking analysis complete, verify:
- [ ] Business purpose clearly stated
- [ ] All input/output parameters documented
- [ ] All files (DB, display, printer) identified
- [ ] All external calls mapped
- [ ] Data flow documented
- [ ] Complexity assessed
- [ ] Special considerations noted
- [ ] Dependencies list is complete
