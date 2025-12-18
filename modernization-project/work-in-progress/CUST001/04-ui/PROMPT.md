# Phase 4: UI - Program: CUST001

# UI Agent

## Purpose
Convert RPGLE 5250 display files to modern React web interfaces while preserving business workflow.

## Inputs Required
1. Display file DDS from `/source-rpgle/dds/display-files/`
2. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
3. DTOs from `/work-in-progress/{PROGRAM}/03-conversion/`
4. DDS to React mapping guide: `dds-to-react-mapping.md`
5. Style guide: `style-guide.md`

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/04-ui/`:
1. React component (e.g., `CustomerInquiryScreen.tsx`)
2. Type definitions (e.g., `types.ts`)
3. CSS/styled components
4. `ui-notes.md` - Document UI/UX decisions

## Process
1. Review display file DDS
2. Map fields to React components
3. Preserve business workflow (screen flow)
4. Enhance UX while maintaining functionality
5. Add responsive design
6. Implement accessibility features
7. Document any UX improvements

## Output Format
Use TypeScript with React functional components and hooks

## Quality Checklist
- [ ] All display file fields represented
- [ ] Function keys mapped appropriately
- [ ] Field validations implemented
- [ ] Error messages displayed properly
- [ ] Responsive design implemented
- [ ] Accessible (WCAG compliant)
- [ ] Follows style guide
