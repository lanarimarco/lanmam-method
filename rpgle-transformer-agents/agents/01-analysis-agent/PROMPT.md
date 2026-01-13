# Analysis Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/01-analysis/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../02-database/` → Sibling directory in the {PROGRAM} folder
- `../../source-rpgle/` → Source RPGLE files at the repository root level
- `/work-in-progress/{PROGRAM}/01-analysis/analysis.md` → Output file in current workspace

## Purpose
Analyze RPGLE programs to extract business logic, data flows, dependencies, and create comprehensive documentation.

## Inputs Required

### Essential Inputs (Must Have)
1. **RPGLE source file from `/source-rpgle/programs/{PROGRAM}.rpgle`** - The program to analyze
   - If missing: Cannot proceed. This is a blocker.

### Recommended Inputs (Should Have)
2. **Related DDS files from `/source-rpgle/dds/`** - File definitions and screen layouts
   - If missing: Proceed with program analysis only, note missing file context
3. **Copybooks from `/source-rpgle/copybooks/`** - Shared data structures
   - If missing: Document as incomplete dependency analysis

### Optional Inputs (Nice to Have)
4. **This agent's checklist: `checklist.md`** - Quality verification guide
   - If missing: Use the Quality Checklist section in this prompt

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `analysis.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `analysis.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Note incomplete areas for follow-up in later phases

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/01-analysis/`:
1. `analysis.md` - Complete analysis document including:
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
