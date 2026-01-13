# UI Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/04-ui/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../01-analysis/` → Analysis from phase 1
- `../03-conversion/` → Java conversion from phase 3
- `../../source-rpgle/` → Source RPGLE files at the repository root level
- `/work-in-progress/{PROGRAM}/04-ui/src/pages/` → React components in current workspace

## Purpose
Convert RPGLE 5250 display files to modern React web interfaces while preserving business workflow.

## Inputs Required

### Essential Inputs (Must Have)
1. **Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`** - Business context and workflow
   - If missing: Cannot understand screen flow and business logic. This is a blocker.
2. **Display file DDS from `/source-rpgle/dds/display-files/`** - Screen layouts and fields
   - If missing: Cannot proceed. This is a blocker.
3. **DTOs from `/work-in-progress/{PROGRAM}/03-conversion/`** - Data structures for API integration
   - If missing: Cannot create proper TypeScript types. This is a blocker.

### Recommended Inputs (Should Have)
4. **Project settings from `project-settings.md`** - Component naming, structure conventions
   - If missing: Use standard React conventions (PascalCase for components, camelCase for variables)
5. **DDS to React mapping guide: `dds-to-react-mapping.md`** - Field type conversions
   - If missing: Use standard HTML5 input types (text, number, date, etc.)
6. **Common patterns from `/common-patterns/`** - React best practices
   - If missing: Use standard React patterns (functional components, hooks)

### Optional Inputs (Nice to Have)
7. **Style guide: `style-guide.md`** - CSS/UI conventions
   - If missing: Use clean, accessible styles with standard CSS
8. **Templates from this agent folder** - React component templates
   - If missing: Generate components using standard React patterns

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `ui-notes.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `ui-notes.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Use reasonable defaults and standard practices when guidelines are missing
- Ensure accessibility (WCAG) even without explicit style guide

## Outputs to Produce

### CRITICAL: Pre-Check for Existing Artifacts (MANDATORY)
**This check is MANDATORY before any code generation. Failure to perform this check will result in duplicate code and conflicts.**

#### Step 1: Search for Existing Artifacts
Search `/final-output/frontend/src/` for:
1. **Existing Page Components** in `pages/` handling similar business logic or entities
2. **Existing API Services** in `services/api/` with similar REST endpoint calls
3. **Existing Type Definitions** in `types/` for the same entities
4. **Existing Hooks** in `hooks/` for similar data fetching or state management
5. **Existing Utilities** in `utils/` for similar validation or transformation logic

#### Step 2: Analyze Existing Artifacts
For **each artifact found**, analyze:
- **Page Components**: 
  - What business functionality do they already implement?
  - Do they handle the same entity/domain as the current display file?
  - Do they use similar screen layouts or workflows?
  - Can new functionality be added to the existing component?
- **API Services**:
  - What REST endpoints do they already call?
  - Do they interact with the same backend controller?
  - Can new API methods be added to the existing service?
- **Type Definitions**:
  - What data structures are already defined?
  - Can they be reused or extended for the current display file?
- **Hooks**:
  - What custom logic do they already encapsulate?
  - Can they be reused or extended?
- **Utilities**:
  - What validation or transformation logic exists?
  - Can it be reused or extended?

#### Step 3: Decision Matrix
Based on your analysis, follow this decision matrix:

| Scenario | Action | Example |
|----------|--------|---------|
| **Existing page component handles the SAME entity** | **EXTEND the existing component** - Add new functionality, fields, or sections to the existing component | `CustomerInquiry.tsx` exists → Add new sections like address management, contact info |
| **Existing API service calls related endpoints** | **EXTEND the existing service** - Add new API methods to the existing service | `customer.service.ts` exists → Add methods like `updateCustomerAddress()`, `deleteCustomer()` |
| **Existing type represents the SAME entity** | **REUSE or extend the type** - Use existing type or create a specialized variant if needed | `CustomerDTO` exists → Reuse it, or create `CustomerDetailDTO extends CustomerDTO` |
| **Existing hook handles similar data** | **EXTEND the existing hook** - Add new functionality to the existing hook | `useCustomerData` exists → Add new parameters or return values |
| **Similar validation logic exists** | **CONSOLIDATE into existing utility** - Refactor to combine similar operations | `customerValidation.ts` exists → Add new validation functions |
| **Completely different domain/entity** | **CREATE NEW artifacts** - Only create new components for truly distinct functionality | Converting `PRODUCT` display when only `CUSTOMER` components exist → Create new ProductPage, product.service.ts |
| **Different screen type (list vs detail)** | **CREATE NEW component but REUSE services/types** - Different views can share backend integration | Existing customer detail page → Create customer list page but reuse customer.service.ts |

#### Step 4: Document Your Decision
In `ui-notes.md`, **ALWAYS document**:
```markdown
## Artifact Reuse Analysis

### Existing Artifacts Found
- Page Component: [Name and path]
- API Service: [Name and path]  
- Types: [Names and paths]
- Hooks: [Names and paths]
- Utilities: [Names and paths]

### Decision Made
- [ ] EXTENDED existing component: [Component name]
  - Added features: [list new features/sections]
- [ ] EXTENDED existing API service: [Service name]
  - Added methods: [list new API methods]
- [ ] REUSED existing types: [Type names]
- [ ] EXTENDED existing hooks: [Hook names]
- [ ] REUSED existing utilities: [Utility names]
- [ ] CREATED new artifacts (justify why): [explanation]

### Route Path Verification
- Existing routes checked: [list existing routes]
- Proposed new route: [new route path]
- Conflicts: NONE / [describe any conflicts and resolution]
```

### Artifacts to Create in Current Workspace
**IMPORTANT**: All artifacts are created in `/work-in-progress/{PROGRAM}/04-ui/src/` following React structure as defined in `/common-patterns/react-project-structure.md`

#### When EXTENDING Existing Artifacts:
**DO NOT modify files in `/final-output/`**. Instead:
1. **COPY** the existing artifact from `/final-output/frontend/src/` to current workspace with same directory structure
2. **EXTEND** the copied version with new features/methods
3. Document in `ui-notes.md` what was copied and what was added

**Files to create in current workspace**:
```
/work-in-progress/{PROGRAM}/04-ui/
├── src/
│   ├── pages/
│   │   └── [ProgramName]/
│   │       ├── [CopiedAndExtendedComponent].tsx
│   │       ├── [CopiedAndExtendedComponent].css
│   │       └── index.ts
│   ├── services/
│   │   └── api/
│   │       ├── [copiedAndExtended].service.ts
│   │       └── index.ts
│   ├── types/
│   │   ├── [newOrCopied].types.ts
│   │   └── index.ts
│   ├── hooks/ (if extending)
│   │   └── [copiedAndExtended].ts
│   └── utils/ (if extending)
│       └── [copiedAndExtended].ts
├── ui-notes.md
├── README.md
├── dependencies-to-add.txt (optional)
└── integration-notes.md (optional)
```

#### When CREATING New Artifacts (only when justified):
**Files to create in current workspace following React structure**:
```
/work-in-progress/{PROGRAM}/04-ui/
├── src/
│   ├── pages/
│   │   └── [ProgramName]/
│   │       ├── [New]Component.tsx
│   │       ├── [New]Component.css
│   │       └── index.ts
│   ├── services/
│   │   └── api/
│   │       ├── [new].service.ts
│   │       └── index.ts
│   ├── types/
│   │   ├── [new].types.ts
│   │   └── index.ts
│   ├── hooks/ (if needed)
│   │   └── use[New].ts
│   └── utils/ (if needed)
│       └── [new]Validation.ts
├── ui-notes.md
├── README.md
├── dependencies-to-add.txt (optional)
└── integration-notes.md (optional)
```

**Critical Rules**:
- ✅ **DO**: Create all artifacts in current workspace following React structure
- ✅ **DO**: Copy existing artifacts from `/final-output/` to current workspace as starting point
- ✅ **DO**: Extend the copied versions with new functionality
- ❌ **DO NOT**: Modify files in `/final-output/` directly
- ❌ **DO NOT**: Create duplicate components for the same entity
- ✅ **DO**: Follow directory structure from `/common-patterns/react-project-structure.md`

**IMPORTANT**: You are creating **program-specific components only**, NOT a full React application. The Integration Agent (Phase 7) will create the React application shell (App.tsx, index.tsx, index.html) and integrate your components into it.

### Required Documentation (Always):

**Documentation**:
- `ui-notes.md` - **MUST include Artifact Reuse Analysis** (see Step 4 above), UI/UX decisions, component structure, and integration notes
- `README.md` - Component overview, props documentation, usage instructions

### Required Artifacts (New or Extended):

**Note**: The following lists apply whether you are creating NEW artifacts or extending EXISTING ones copied from `/final-output/`.

1. **Page Component** (`pages/{ProgramName}/`):
   - Main component file (e.g., `CustomerInquiry.tsx`)
   - Component styles (e.g., `CustomerInquiry.css` or `.module.css`)
   - Index file for clean exports (`index.ts`)

2. **API Service** (`services/api/`):
   - Service file (e.g., `customer.service.ts`)
   - Index file (`index.ts`)

3. **Type Definitions** (`types/`):
   - Type/interface definitions (e.g., `customer.types.ts`)
   - Index file (`index.ts`)

### Optional Outputs (if needed):

5. **Custom Hooks** (`hooks/`):
   - Program-specific hooks (e.g., `useCustomerData.ts`)

6. **Utility Functions** (`utils/`):
   - Program-specific utilities (e.g., `customerValidation.ts`)

7. **Shared Components** (`components/common/`):
   - Only if creating reusable components for this program

### Configuration Files (Reference Only):

Create these files in `/work-in-progress/{PROGRAM}/04-ui/` (NOT in src/):
- `dependencies-to-add.txt` - List any additional npm packages needed beyond base React
- `integration-notes.md` - Notes for Integration Agent about routing, navigation, etc.

**Example Structure**:
```
/work-in-progress/{PROGRAM}/04-ui/
├── src/
│   ├── pages/
│   │   └── CustomerInquiry/
│   │       ├── CustomerInquiry.tsx
│   │       ├── CustomerInquiry.css
│   │       └── index.ts
│   ├── services/
│   │   └── api/
│   │       ├── customer.service.ts
│   │       └── index.ts
│   └── types/
│       ├── customer.types.ts
│       └── index.ts
├── ui-notes.md
├── README.md
├── dependencies-to-add.txt (optional)
└── integration-notes.md (optional)
```

## Process

1. **MANDATORY: Perform artifact pre-check** (see "CRITICAL: Pre-Check for Existing Artifacts" section)
   - Search for existing page components, API services, types, hooks, and utilities in `/final-output/frontend/src/`
   - Analyze what they already implement
   - Make extend vs. create decision using the decision matrix
   - Document your decision in `ui-notes.md`

2. **If extending existing artifacts**:
   - **COPY** the existing files from `/final-output/frontend/src/` to current workspace
   - Maintain the same directory structure in current workspace
   - Use copied files as starting point for extensions

3. **Analyze Display File**:
   - Review display file DDS from `/source-rpgle/dds/display-files/`
   - Identify all fields, indicators, function keys
   - Understand screen flow and business logic

4. **Design Component Structure**:
   - Map DDS fields to React form inputs
   - Identify reusable sub-components (if needed)
   - Plan state management approach
   - Design API integration points

5. **Create or Extend Page Component**:
   - **If extending**: Enhance the COPIED component in current workspace with new fields, sections, or functionality
   - **If creating new**: Build main page component in `src/pages/{ProgramName}/`
   - Implement form fields, buttons, displays
   - Add client-side validation
   - Preserve business workflow (screen flow)
   - Enhance UX while maintaining functionality

6. **Create or Extend API Service**:
   - **If extending**: Add new API methods to the COPIED service in current workspace
   - **If creating new**: Build service layer in `src/services/api/`
   - Map RPGLE operations to REST endpoints
   - Handle request/response transformation
   - Implement error handling

7. **Define or Reuse TypeScript Types**:
   - **If reusing**: Copy and extend existing types from `/final-output/frontend/src/types/`
   - **If creating new**: Create interfaces matching DTOs from Phase 3
   - Add form state types
   - Define API response types

8. **Add Styling**:
   - Create component-specific CSS
   - Implement responsive design
   - Follow style guide conventions
   - Ensure accessibility (WCAG compliant)

9. **Document**:
   - Write ui-notes.md with **Artifact Reuse Analysis** (mandatory) and UI/UX decisions
   - Create README.md with component usage
   - Add integration-notes.md with routing suggestions
   - List any additional dependencies needed

## Output Format
Use TypeScript with React functional components and hooks

## Quality Checklist

### Pre-Check and Reuse
- [ ] **CRITICAL: Artifact pre-check completed and documented**
- [ ] **CRITICAL: No duplicate components created for the same entity**
- [ ] **CRITICAL: No duplicate API services created for the same endpoints**
- [ ] **CRITICAL: Existing artifacts copied to current workspace** (not modified in `/final-output/`)
- [ ] Decision to extend vs. create documented in `ui-notes.md`
- [ ] Route path conflicts checked and documented

### Component Quality
- [ ] All display file fields represented in React component
- [ ] Function keys mapped to buttons/actions appropriately
- [ ] Field validations implemented (client-side)
- [ ] Error messages displayed properly
- [ ] Loading states handled (spinners, disabled states)
- [ ] Responsive design implemented (mobile, tablet, desktop)
- [ ] Accessible (WCAG compliant - labels, ARIA, keyboard navigation)
- [ ] Follows style guide and naming conventions

### Code Quality
- [ ] TypeScript types properly defined and exported
- [ ] API service properly structured with error handling
- [ ] No hardcoded values (use constants or env variables)
- [ ] Clean exports using index.ts files
- [ ] Props properly typed for components
- [ ] State management is clear and minimal

### Integration Readiness
- [ ] Component is standalone and can be imported
- [ ] No assumptions about App.tsx or routing structure
- [ ] API service uses environment variable for base URL (REACT_APP_API_URL)
- [ ] All dependencies documented in dependencies-to-add.txt
- [ ] Integration notes provided for routing (suggested route path)
- [ ] README.md explains how to integrate component

### Documentation
- [ ] **ui-notes.md includes Artifact Reuse Analysis section** (mandatory)
- [ ] ui-notes.md explains UI/UX decisions and DDS mappings
- [ ] ui-notes.md documents what was copied and what was added (if extending)
- [ ] README.md provides component overview and usage
- [ ] integration-notes.md suggests route path and navigation label
- [ ] Any special setup or configuration documented
