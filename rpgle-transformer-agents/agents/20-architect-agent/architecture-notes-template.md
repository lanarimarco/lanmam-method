# Architecture Notes for {PROGRAM}

**Architect Agent**  
**Date**: {DATE}

---

## Inputs Received

### Essential Inputs
- [ ] Analysis document: `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`
  - Status: FOUND / MISSING (BLOCKER)
- [ ] RPGLE source: `/source-rpgle/programs/{PROGRAM}.rpgle`
  - Status: FOUND / MISSING (BLOCKER)
- [ ] DDS physical files: `/source-rpgle/dds/physical-files/`
  - Status: FOUND / MISSING (BLOCKER)
  - Files found: {list}

### Recommended Inputs
- [ ] Project settings: `project-settings.md`
  - Status: FOUND / MISSING (using defaults)
- [ ] Display files: `/source-rpgle/dds/display-files/`
  - Status: FOUND / MISSING
  - Files found: {list}
- [ ] Type mappings: `type-mappings.md`
  - Status: FOUND / MISSING (using standard mappings)

### Optional Inputs
- [ ] Logical files: `/source-rpgle/dds/logical-files/`
  - Status: FOUND / MISSING
- [ ] Naming conventions: `naming-conventions.md`
  - Status: FOUND / MISSING (using Java standards)

---

## Existing Artifacts Analysis

### Search Performed
- Location: `/src/main/java/`
- Date: {date}

### Artifacts Found

#### Entities
| Entity | Table | Path | Decision |
|--------|-------|------|----------|
| | | | EXTEND / NONE |

#### Repositories
| Repository | Path | Decision |
|------------|------|----------|
| | | EXTEND / NONE |

#### Services
| Service | Path | Decision |
|---------|------|----------|
| | | EXTEND / NONE |

#### Controllers
| Controller | Base Path | Path | Decision |
|------------|-----------|------|----------|
| | /api/{} | | EXTEND / NONE |

#### DTOs
| DTO | Path | Decision |
|-----|------|----------|
| | | REUSE / EXTEND / NONE |

---

## Architecture Decisions

### Decision 1: {Title}
- **Context**: {what situation led to this decision}
- **Options Considered**:
  1. {option 1}
  2. {option 2}
- **Decision**: {what was decided}
- **Rationale**: {why this decision was made}
- **Consequences**: {impact of the decision}

### Decision 2: {Title}
- **Context**: 
- **Options Considered**:
- **Decision**: 
- **Rationale**: 
- **Consequences**: 

---

## Assumptions Made

| # | Assumption | Reason | Risk if Wrong |
|---|------------|--------|---------------|
| 1 | | | |
| 2 | | | |

---

## Type Mapping Decisions

### Non-Standard Mappings
| DDS Field | DDS Type | Java Type | Reason |
|-----------|----------|-----------|--------|
| | | | |

### Special Handling Required
| Field | Handling | Notes |
|-------|----------|-------|
| | | |

---

## Issues Encountered

### Issue 1: {Title}
- **Description**: {what happened}
- **Impact**: {how it affects the architecture}
- **Resolution**: {how it was resolved}
- **Status**: RESOLVED / NEEDS REVIEW / BLOCKED

### Issue 2: {Title}
- **Description**: 
- **Impact**: 
- **Resolution**: 
- **Status**: 

---

## Deviations from Standard Patterns

### Deviation 1: {Title}
- **Standard Pattern**: {what the normal approach would be}
- **Actual Design**: {what was designed instead}
- **Reason**: {why the deviation was necessary}
- **Documentation**: {where this is documented}

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| | LOW/MED/HIGH | LOW/MED/HIGH | |

---

## Recommendations for Review

1. {recommendation}
2. {recommendation}

---

## Questions for Project Manager

| # | Question | Context | Impact |
|---|----------|---------|--------|
| 1 | | | |
