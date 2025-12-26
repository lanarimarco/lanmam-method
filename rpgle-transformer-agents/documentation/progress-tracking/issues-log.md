# Issues and Blockers Log

**Last Updated**: {DATE}

---

## Active Issues

### Issue Template
**Issue #**:
**Date Reported**:
**Program**:
**Phase**:
**Severity**: [Critical / High / Medium / Low]
**Status**: [Open / In Progress / Resolved / Closed]
**Description**:
**Impact**:
**Assigned To**:
**Resolution**:
**Date Resolved**:

---

## Open Issues

(None currently)

---

## Resolved Issues

(None yet)

---

## Common Issues and Solutions

### Issue Pattern: Packed Decimal Precision Loss
**Programs Affected**: Multiple
**Solution**: Always use BigDecimal with proper scale
**Reference**: `/common-patterns/numeric-handling.md`

### Issue Pattern: Date Format Confusion
**Programs Affected**: Multiple
**Solution**: Use DateUtils class for conversions
**Reference**: `/common-patterns/date-time-conversion.md`

---

## Blockers

### Active Blockers
(None currently)

### Resolved Blockers
(None yet)

---

## Instructions for Logging Issues

**Primary Responsibility**: Project Manager (Human)

**When to Log**:
- Any conversion problem that takes >30 minutes to resolve
- Any blocking issue preventing progress
- Any recurring pattern seen across multiple programs
- Any significant deviation from expected conversion process

**How It Works**:
1. **Agents report problems** in their own output files (e.g., `conversion-notes.md`, `review-report.md`)
2. **You review** agent outputs and decide which issues warrant logging
3. **You add** the issue to this log using the Issue Template
4. **Agent provides solution** in their work (if they resolve it)
5. **You update** the issue as resolved and document the solution
6. **You identify patterns** and add reusable solutions to "Common Issues and Solutions"
7. **You update** `/common-patterns/lessons-learned.md` for future reference

**Agents' Role**:
- Agents should clearly **document problems** they encounter in their respective output files
- Agents should **propose solutions** when possible
- Agents should **flag significant issues** in their notes for human review
- Agents **DO NOT directly update** this issues-log.md file (you do that)

**Example Agent Problem Reporting**:
In `CUST003/03-conversion/conversion-notes.md`:
```
## Issues Encountered

### Packed Decimal Conversion Challenge
**Problem**: RPGLE uses 15,5 packed decimal but Java BigDecimal precision differs
**Attempted Solution**: Used BigDecimal with setScale(5, HALF_UP)
**Status**: Works but needs validation
**Recommendation**: This pattern should be documented for future conversions
```

Then you (human) decide whether to add this to issues-log.md.

**Who Logs**: **You (Project Manager)** based on agent reports and your observations
