# UI Layer Notes - CUST001

**Date**: 2025-12-28
**Program**: CUST001 - Customer Inquiry
**Display File**: CUSTDSP.dds

---

## Overview

This document describes the conversion of the CUSTDSP display file to a modern React web interface. The UI preserves the business workflow of the original 5250 screen while providing a modern, responsive user experience.

---

## Files Created

### React Components
- `CustomerInquiry.tsx` - Main page component
  - Location: `src/pages/CustomerInquiry/CustomerInquiry.tsx`
  - Implements both PROMPT and DETAIL screen logic

- `CustomerInquiry.css` - Component styles
  - Location: `src/pages/CustomerInquiry/CustomerInquiry.css`
  - Responsive design with mobile support

- `index.ts` - Clean exports
  - Location: `src/pages/CustomerInquiry/index.ts`

### API Services
- `customer.service.ts` - API service for backend communication
  - Location: `src/services/api/customer.service.ts`
  - Calls GET `/api/customers/{customerNumber}` endpoint

- `index.ts` - Service exports
  - Location: `src/services/api/index.ts`

### Type Definitions
- `customer.types.ts` - TypeScript interfaces
  - Location: `src/types/customer.types.ts`
  - Defines Customer, CustomerErrorResponse, CustomerSearchState types

- `index.ts` - Type exports
  - Location: `src/types/index.ts`

---

## Display File to React Mapping

### PROMPT Screen → Search Form

**DDS PROMPT Record:**
```dds
A          R PROMPT
A                                  1  2'CUST001'
A                                  1 30'Customer Inquiry'
A                                  4  2'Customer Number:'
A            PCUSTNO        5Y 0B  4 20EDTCDE(Z)
A  90                              5  2'Error:'
A  90        PMSG          50A  O  5  9COLOR(RED)
A                                 23  2'F3=Exit'
```

**React Implementation:**
- Header with "CUST001" and "Customer Inquiry" title
- Form with customer number input field
- Error message display (conditional, shown only when error occurs)
- Search and Clear buttons
- Function key help text

**Key Mappings:**
- `PCUSTNO` (input field) → `customerNumber` state
- `PMSG` (error message) → `errorMessage` state
- `*IN90` (error indicator) → `searchState === 'error'` condition
- `F3` (Exit) → Navigation/close button
- Enter key → Form submit → Search button

### DETAIL Screen → Customer Info Display

**DDS DETAIL Record:**
```dds
A          R DETAIL
A                                  1  2'CUST001'
A                                  1 30'Customer Detail'
A                                  4  2'Customer Number:'
A            DCUSTNO        5Y 0O  4 20EDTCDE(Z)
A                                  5  2'Name:'
A            DCUSTNAME     30A  O  5 20
A                                  7  2'Address:'
A            DADDR1        30A  O  7 20
A                                  8  2'City:'
A            DCITY         20A  O  8 20
A                                  9  2'State:'
A            DSTATE         2A  O  9 20
A                                 10  2'Zip:'
A            DZIP           5Y 0O 10 20EDTCDE(Z)
A                                 12  2'Phone:'
A            DPHONE        12A  O 12 20
A                                 14  2'Balance:'
A            DBALANCE       9Y 2O 14 20EDTCDE(J)
A                                 23  2'F3=Exit  F12=Return'
```

**React Implementation:**
- Header with "CUST001" and "Customer Detail" title
- Read-only display of customer information in labeled rows
- "New Search" button (replaces F12)
- Function key help text

**Key Mappings:**
- All DDS output fields (`O`) → Read-only display (`<span>` elements)
- `EDTCDE(Z)` (zero suppression) → formatZipCode() with leading zeros
- `EDTCDE(J)` (currency formatting) → formatCurrency() with $ symbol
- `F12` (Return) → "New Search" button
- `F3` (Exit) → Navigation/close

---

## UI/UX Decisions

### Screen Flow
**Original 5250 Flow:**
1. Display PROMPT screen
2. User enters customer number, presses Enter
3. If found: Display DETAIL screen
4. If not found: Display PROMPT with error
5. Press F12 to return to PROMPT or F3 to exit

**React Flow:**
1. Display search form (PROMPT equivalent)
2. User enters customer number, clicks Search
3. Loading state during API call
4. If found: Replace form with customer details (DETAIL equivalent)
5. If not found: Show error message on form
6. Click "New Search" to return to search form

**Key Difference**: React uses component state to switch between "screens" rather than separate display formats.

### Visual Enhancements

1. **Modern Styling**:
   - Card-based layout instead of 24×80 grid
   - Rounded corners and subtle shadows
   - Color-coded elements (primary blue, error red, success green)

2. **Responsive Design**:
   - Mobile-friendly (stacks vertically on small screens)
   - Tablet optimization
   - Print-friendly styles

3. **Loading States**:
   - "Searching..." button text during API call
   - Disabled inputs and buttons during loading
   - Prevents multiple simultaneous requests

4. **Error Handling**:
   - Prominent error display with red background
   - Specific error messages from API
   - Accessible error announcements (ARIA role="alert")

5. **Accessibility (WCAG 2.1)**:
   - Proper form labels with `htmlFor`
   - Keyboard navigation support
   - Focus management (autoFocus on first field)
   - ARIA attributes for screen readers
   - Sufficient color contrast
   - Focus-visible outlines

### Data Formatting

| DDS Edit Code | JavaScript Implementation |
|---------------|---------------------------|
| EDTCDE(Z) - Zero suppress | `formatZipCode()` - padStart(5, '0') |
| EDTCDE(J) - Currency | `formatCurrency()` - Intl.NumberFormat with USD |

**Examples:**
- Zip Code: 1234 → "01234"
- Balance: 1250.75 → "$1,250.75"

---

## Component Architecture

### State Management

```typescript
// Form input
const [customerNumber, setCustomerNumber] = useState<string>('');

// API call state
const [searchState, setSearchState] = useState<CustomerSearchState>('idle');

// Results
const [customer, setCustomer] = useState<Customer | null>(null);
const [errorMessage, setErrorMessage] = useState<string>('');
```

**State Values:**
- `'idle'` - Initial state, no search performed
- `'loading'` - API call in progress
- `'success'` - Customer found and displayed
- `'error'` - Validation error or customer not found

### Component Structure

```
CustomerInquiry (main component)
├── Page Header
│   ├── Program Name (CUST001)
│   └── Page Title (conditional: Inquiry or Detail)
├── Prompt Section (when !customer)
│   ├── Search Form
│   │   ├── Customer Number Input
│   │   ├── Error Message (conditional)
│   │   ├── Search/Clear Buttons
│   │   └── Function Key Help
├── Detail Section (when customer)
│   ├── Customer Info Display
│   │   └── Info Rows (8 fields)
│   ├── New Search Button
│   └── Function Key Help
```

### Event Handlers

| Handler | Purpose | RPGLE Equivalent |
|---------|---------|------------------|
| `handleSearch()` | Form submission | Enter key on PROMPT |
| `performSearch()` | Execute API call | CHAIN operation |
| `handleNewSearch()` | Reset to search form | F12 key |
| `handleKeyDown()` | Keyboard shortcuts | Function key processing |

---

## API Integration

### Environment Configuration

The API base URL is configured via environment variable:

```bash
REACT_APP_API_URL=http://localhost:8080
```

**Important**: Do NOT include `/api` suffix in the URL. The service concatenates it:

```typescript
const url = `${API_BASE_URL}/api/customers/${customerNumber}`;
```

### API Call Flow

```
User Input → Validation → API Call → Response Handling
   ↓             ↓           ↓              ↓
"12345"     isValid?    GET /api/    200: Display
                        customers/   404: Show error
                        12345        400: Show error
```

### Error Handling

**Validation Errors** (Client-side):
- Customer number is empty/zero → "Customer number required"
- Customer number is negative → "Customer number must be positive"

**API Errors** (Server-side):
- 404 Not Found → Display error message from API
- 400 Bad Request → Display validation error from API
- 500 Server Error → Generic error message
- Network error → "Network error: Unable to connect to the server"

---

## Keyboard Shortcuts

| Key | Action | Implementation |
|-----|--------|----------------|
| Enter | Submit search | Form onSubmit event |
| F12 / Escape | Return to search (when on detail) | handleKeyDown event |
| Tab | Navigate fields | Native browser behavior |

**Note**: F3 (Exit) is typically handled by the parent application's navigation.

---

## Styling Approach

### CSS Organization

- **File**: `CustomerInquiry.css`
- **Methodology**: Component-scoped CSS with descriptive class names
- **No CSS-in-JS**: Keeping styles separate for maintainability

### Design System Alignment

Following the style guide:

| Element | Style |
|---------|-------|
| Primary Color | #0066cc (blue) |
| Error Color | #dc3545 (red) |
| Success Color | #28a745 (green) |
| Background | #f8f9fa (light gray) |
| Font Family | Segoe UI, Tahoma |
| Monospace (numbers) | Courier New |

### Responsive Breakpoints

- **Mobile** (<768px): Stacked layout, full-width buttons
- **Tablet** (768-1024px): Optimized layout
- **Desktop** (>1024px): Full layout with max-width 800px

---

## Testing Considerations

### Manual Testing Checklist

- [ ] Valid customer number displays correct information
- [ ] Invalid customer number (0) shows validation error
- [ ] Non-existent customer shows "not found" error
- [ ] "Search" button disabled during loading
- [ ] "New Search" returns to search form
- [ ] Form clears on "Clear" button
- [ ] Keyboard shortcuts work (Enter, F12/Escape)
- [ ] Mobile responsive design works
- [ ] Error messages are accessible
- [ ] Focus management works correctly
- [ ] Currency formatting displays correctly
- [ ] Zip code formatting displays with leading zeros

### Automated Testing (Phase 5)

**Unit Tests:**
- Test component rendering
- Test form validation logic
- Test state management
- Test formatting functions (formatCurrency, formatZipCode)

**Integration Tests:**
- Test API service calls
- Test error handling
- Test loading states

**E2E Tests:**
- Test complete user flow
- Test keyboard navigation
- Test responsive behavior

---

## Dependencies

### Required npm Packages

**Core:**
- `react` (^18.0.0) - React library
- `react-dom` (^18.0.0) - React DOM renderer
- `typescript` (^5.0.0) - TypeScript support

**Development:**
- `@types/react` - TypeScript types for React
- `@types/react-dom` - TypeScript types for React DOM

**Optional Enhancements:**
- None required for basic functionality
- Could add: react-router-dom (if multi-page app)
- Could add: axios (alternative to fetch)

All dependencies should be documented in `dependencies-to-add.txt` for the Integration Agent.

---

## Integration Notes for Phase 7

### Routing

Suggested route path:
```typescript
<Route path="/customer-inquiry" element={<CustomerInquiry />} />
```

Suggested navigation label:
```typescript
<Link to="/customer-inquiry">Customer Inquiry</Link>
```

### Environment Setup

Ensure `.env.development` and `.env.production` files include:

```bash
# Development
REACT_APP_API_URL=http://localhost:8080

# Production
REACT_APP_API_URL=https://api.yourcompany.com
```

### Integration Steps

1. Copy component files to `/final-output/frontend/src/pages/CustomerInquiry/`
2. Copy service files to `/final-output/frontend/src/services/api/`
3. Copy type files to `/final-output/frontend/src/types/`
4. Add route to `App.tsx`
5. Add navigation link to header/menu
6. Verify API_BASE_URL is configured correctly

---

## Known Limitations

### Current Limitations
None for this simple inquiry program.

### Future Enhancements

1. **Search by Name**: Add ability to search by customer name
2. **Recent Searches**: Remember recent customer lookups
3. **Print Button**: Export customer details to PDF
4. **Edit Customer**: Add link to customer maintenance screen
5. **Transaction History**: Show recent orders for customer
6. **Auto-complete**: Suggest customer numbers as user types

---

## Accessibility Features

### WCAG 2.1 Compliance

**Level A:**
- ✓ Semantic HTML elements
- ✓ Form labels associated with inputs
- ✓ Keyboard accessible
- ✓ Focus visible
- ✓ Error identification

**Level AA:**
- ✓ Color contrast ratios meet 4.5:1 minimum
- ✓ Focus indicators visible
- ✓ Error messages in text, not just color
- ✓ Resize text up to 200%

### Screen Reader Support

- Form fields have descriptive labels
- Error messages use ARIA `role="alert"`
- Loading states announced
- Buttons have clear text labels

### Keyboard Navigation

- Tab order is logical
- Enter submits form
- Escape/F12 returns to search
- Focus management when switching "screens"

---

## Quality Checklist

- [x] All display file fields represented in React component
- [x] Function keys mapped to buttons/actions appropriately
- [x] Field validations implemented (client-side)
- [x] Error messages displayed properly
- [x] Loading states handled (spinners, disabled states)
- [x] Responsive design implemented (mobile, tablet, desktop)
- [x] Accessible (WCAG compliant - labels, ARIA, keyboard navigation)
- [x] Follows style guide and naming conventions
- [x] TypeScript types properly defined and exported
- [x] API service properly structured with error handling
- [x] No hardcoded values (uses environment variables)
- [x] Clean exports using index.ts files
- [x] Props properly typed for components
- [x] State management is clear and minimal
- [x] Component is standalone and can be imported
- [x] No assumptions about App.tsx or routing structure
- [x] API service uses environment variable for base URL
- [x] Integration notes provided for routing

---

## References

- Display File DDS: `/source-rpgle/dds/display-files/CUSTDSP.dds`
- Analysis Document: `/work-in-progress/CUST001/01-analysis/CUST001-analysis.md`
- Conversion Notes: `/work-in-progress/CUST001/03-conversion/conversion-notes.md`
- DDS to React Mapping: `/agents/04-ui-agent/dds-to-react-mapping.md`
- Style Guide: `/agents/04-ui-agent/style-guide.md`
- React Structure: `/common-patterns/react-project-structure.md`

---

**Phase 4 Status**: ✅ COMPLETE
