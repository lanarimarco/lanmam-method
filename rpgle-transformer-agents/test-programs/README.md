# Test Programs for Workflow Validation

This folder contains sample RPGLE programs that can be used to test and validate the conversion pipeline.

## Purpose

Use these test programs to:
- Validate the conversion workflow before processing real programs
- Train team members on the conversion process
- Test different LLM configurations
- Benchmark conversion quality and completeness
- Document best practices and lessons learned

## Available Test Programs

### CUST001 - Customer Inquiry (Simple)

**Complexity**: LOW
**Type**: Interactive inquiry program
**Lines of Code**: ~70

**Features:**
- Single file read (CUSTMAST)
- Display file interaction (CUSTDSP)
- Simple validation
- Basic error handling
- Typical inquiry pattern

**Good for testing:**
- Analysis phase - simple program structure
- Database layer - basic entity and repository
- Conversion - straightforward service/controller
- UI - simple form and detail screen
- Testing - easy to create comprehensive tests

**Files:**
- `programs/CUST001.rpgle` - Main program
- `dds/physical-files/CUSTMAST.dds` - Customer master file
- `dds/display-files/CUSTDSP.dds` - Display file with 2 formats

### Expected Conversion Output

After running the full pipeline on CUST001, you should get:

```
work-in-progress/CUST001/
├── 01-analysis/
│   └── CUST001-analysis.md          # Program analysis document
├── 02-database/
│   ├── entities/
│   │   └── Customer.java            # JPA entity
│   └── repositories/
│       └── CustomerRepository.java  # Spring Data repository
├── 03-conversion/
│   ├── services/
│   │   └── CustomerInquiryService.java
│   ├── controllers/
│   │   └── CustomerInquiryController.java
│   └── dtos/
│       ├── CustomerInquiryRequest.java
│       └── CustomerInquiryResponse.java
├── 04-ui/
│   └── components/
│       └── CustomerInquiryScreen.tsx
├── 05-testing/
│   ├── unit-tests/
│   │   └── CustomerInquiryServiceTest.java
│   └── integration-tests/
│       └── CustomerInquiryControllerIT.java
└── 06-review/
    └── review-report.md
```

## How to Test the Workflow

### Step 1: Copy Test Program to Source Folder

```bash
# Copy CUST001 to the source folder
cp test-programs/programs/CUST001.rpgle source-rpgle/programs/
cp test-programs/dds/physical-files/CUSTMAST.dds source-rpgle/dds/physical-files/
cp test-programs/dds/display-files/CUSTDSP.dds source-rpgle/dds/display-files/
```

### Step 2: Run the Pipeline

```bash
cd scripts
./run-full-pipeline.sh CUST001
```

### Step 3: Choose Your Workflow

**Option A: Single Session**
```bash
# Copy the master prompt
cat ../work-in-progress/CUST001/MASTER-PIPELINE-PROMPT.md | pbcopy

# Paste into your LLM (Claude Code, ChatGPT, etc.)
```

**Option B: Phase by Phase**
```bash
# Phase 1: Analysis
cat ../work-in-progress/CUST001/01-analysis/PROMPT.md | pbcopy
# Run in LLM, review output

# Phase 2: Database
cat ../work-in-progress/CUST001/02-database/PROMPT.md | pbcopy
# Run in LLM, review output

# Continue for phases 3-6...
```

### Step 4: Validate Results

Check that all expected files were created:

```bash
# Check analysis
ls -la work-in-progress/CUST001/01-analysis/CUST001-analysis.md

# Check database layer
ls -la work-in-progress/CUST001/02-database/entities/
ls -la work-in-progress/CUST001/02-database/repositories/

# Check conversion
ls -la work-in-progress/CUST001/03-conversion/services/
ls -la work-in-progress/CUST001/03-conversion/controllers/
ls -la work-in-progress/CUST001/03-conversion/dtos/

# Check UI
ls -la work-in-progress/CUST001/04-ui/components/

# Check tests
ls -la work-in-progress/CUST001/05-testing/unit-tests/
ls -la work-in-progress/CUST001/05-testing/integration-tests/

# Check review
ls -la work-in-progress/CUST001/06-review/review-report.md
```

### Step 5: Review Quality

Review the generated code for:
- ✅ Correctness - Does it match RPGLE behavior?
- ✅ Completeness - Are all features converted?
- ✅ Code Quality - Does it follow Spring Boot best practices?
- ✅ Tests - Do they pass? Is coverage adequate?
- ✅ Documentation - Are notes files complete?

## Validation Checklist

Use this checklist when testing with CUST001:

### Analysis Phase
- [ ] Business purpose clearly identified
- [ ] All files (CUSTMAST, CUSTDSP) documented
- [ ] Data flow diagram created
- [ ] Complexity rated as LOW
- [ ] Dependencies complete

### Database Phase
- [ ] Customer entity created with all fields
- [ ] Primary key (CUSTNO) properly annotated
- [ ] CustomerRepository created
- [ ] findById method available
- [ ] Type mappings correct (packed decimal → BigDecimal)

### Conversion Phase
- [ ] CustomerInquiryService created
- [ ] findCustomer method implements CHAIN logic
- [ ] CustomerInquiryController created
- [ ] GET endpoint for customer lookup
- [ ] DTOs properly defined
- [ ] Error handling for "not found" case

### UI Phase
- [ ] React component created
- [ ] Search form (matches PROMPT format)
- [ ] Detail display (matches DETAIL format)
- [ ] Error message display
- [ ] TypeScript types defined

### Testing Phase
- [ ] Unit tests for service
- [ ] Test for customer found case
- [ ] Test for customer not found case
- [ ] Integration test for controller
- [ ] Tests actually pass

### Review Phase
- [ ] Review report created
- [ ] No critical issues
- [ ] Code quality acceptable
- [ ] Ready for integration

## Expected Conversion Time

With a capable LLM (Claude Opus/Sonnet, GPT-4):
- **Single session (master prompt)**: 5-15 minutes
- **Phase-by-phase**: 20-30 minutes total (3-5 min per phase)

## Success Criteria

A successful test run should produce:
1. ✅ All 6 phases completed
2. ✅ All expected files created
3. ✅ Code compiles (Java)
4. ✅ Tests pass
5. ✅ Business logic matches RPGLE
6. ✅ No critical issues in review

## Adding More Test Programs

To add additional test programs:

1. Create RPGLE program in `programs/`
2. Create associated DDS files in appropriate `dds/` folders
3. Document in this README:
   - Complexity level
   - Features being tested
   - Expected outputs
4. Add to validation checklist

### Recommended Additional Test Cases

**CUST002 - Customer Maintenance (Medium)**
- CRUD operations
- Update and delete logic
- More complex validation
- Subfile handling

**ORD001 - Order Entry (High)**
- Multi-file processing
- Calculations and business rules
- Complex screen flow
- Batch processing elements

## Cleanup

To clean up after testing:

```bash
# Remove work-in-progress
rm -rf work-in-progress/CUST001

# Remove from source folders (if needed)
rm source-rpgle/programs/CUST001.rpgle
rm source-rpgle/dds/physical-files/CUSTMAST.dds
rm source-rpgle/dds/display-files/CUSTDSP.dds
```

## Notes

- Test programs are intentionally simple and well-documented
- They represent common AS/400 patterns
- Use them to calibrate your conversion process
- Document any improvements or issues in `common-patterns/lessons-learned.md`

---

**Best Practice**: Always test with CUST001 before processing production programs!
