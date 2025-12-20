# React Project Structure Convention

## IMPORTANT: All React Artifacts Must Follow Standard Project Structure

When creating any React-related artifacts (components, hooks, services, utilities, tests), you **MUST** organize them according to the standard React project layout for scalability and maintainability.

---

## Standard React Directory Structure

```
project-root/
├── public/                  # Static assets served directly
│   ├── index.html
│   ├── favicon.ico
│   └── assets/
│       └── images/
├── src/
│   ├── components/          # Reusable UI components
│   │   ├── common/          # Shared components (Button, Input, Modal, etc.)
│   │   │   ├── Button/
│   │   │   │   ├── Button.tsx
│   │   │   │   ├── Button.test.tsx
│   │   │   │   ├── Button.module.css
│   │   │   │   └── index.ts
│   │   │   └── Input/
│   │   └── layout/          # Layout components (Header, Footer, Sidebar)
│   │       ├── Header/
│   │       ├── Footer/
│   │       └── Sidebar/
│   ├── pages/               # Page-level components (routes)
│   │   ├── Home/
│   │   │   ├── Home.tsx
│   │   │   ├── Home.test.tsx
│   │   │   ├── Home.module.css
│   │   │   └── index.ts
│   │   ├── Dashboard/
│   │   └── Profile/
│   ├── features/            # Feature-based modules (optional)
│   │   ├── auth/
│   │   │   ├── components/
│   │   │   ├── hooks/
│   │   │   ├── services/
│   │   │   └── types/
│   │   └── orders/
│   ├── hooks/               # Custom React hooks
│   │   ├── useAuth.ts
│   │   ├── useFetch.ts
│   │   └── useLocalStorage.ts
│   ├── services/            # API calls and external services
│   │   ├── api/
│   │   │   ├── customer.service.ts
│   │   │   ├── order.service.ts
│   │   │   └── auth.service.ts
│   │   └── http/
│   │       └── client.ts
│   ├── store/               # State management (Redux, Zustand, etc.)
│   │   ├── slices/
│   │   │   ├── authSlice.ts
│   │   │   └── orderSlice.ts
│   │   ├── hooks.ts
│   │   └── store.ts
│   ├── types/               # TypeScript type definitions
│   │   ├── customer.types.ts
│   │   ├── order.types.ts
│   │   └── common.types.ts
│   ├── utils/               # Utility/helper functions
│   │   ├── formatters.ts
│   │   ├── validators.ts
│   │   └── constants.ts
│   ├── styles/              # Global styles and themes
│   │   ├── globals.css
│   │   ├── variables.css
│   │   └── theme.ts
│   ├── routes/              # Routing configuration
│   │   ├── AppRoutes.tsx
│   │   ├── PrivateRoute.tsx
│   │   └── routes.config.ts
│   ├── config/              # Application configuration
│   │   ├── env.ts
│   │   └── app.config.ts
│   ├── assets/              # Application assets
│   │   ├── images/
│   │   ├── fonts/
│   │   └── icons/
│   ├── App.tsx              # Root component
│   ├── App.test.tsx
│   ├── index.tsx            # Entry point
│   └── vite-env.d.ts / react-app-env.d.ts
├── .env                     # Environment variables
├── .env.example
├── package.json
├── tsconfig.json
├── vite.config.ts / craco.config.js
└── README.md
```

---

## Directory Organization Rules

### Rule 1: Component Directory Structure

**Each component should have its own directory with:**
- Component file (`.tsx` or `.jsx`)
- Test file (`.test.tsx` or `.test.jsx`)
- Styles (`.module.css`, `.scss`, or styled-components in the same file)
- Index file (`index.ts`) for clean exports

**Example:**
```
src/components/common/Button/
├── Button.tsx           # Component implementation
├── Button.test.tsx      # Component tests
├── Button.module.css    # Component styles
└── index.ts             # Export: export { Button } from './Button';
```

### Rule 2: Import Path Convention

Use the index file for clean imports:

**With index.ts:**
```typescript
import { Button } from '@/components/common/Button';
```

**Without index.ts (avoid):**
```typescript
import { Button } from '@/components/common/Button/Button';
```

### Rule 3: Separation of Concerns

| Directory | Purpose | Examples |
|-----------|---------|----------|
| `components/` | Presentational, reusable UI | Button, Input, Card |
| `pages/` | Route-level components | HomePage, Dashboard |
| `features/` | Feature modules with logic | auth/, orders/ |
| `hooks/` | Custom React hooks | useAuth, useFetch |
| `services/` | API calls, external services | customerService |
| `store/` | Global state management | Redux slices |
| `utils/` | Pure utility functions | formatDate, validateEmail |
| `types/` | TypeScript definitions | interfaces, types |

---

## File Naming Conventions

### Components
- **PascalCase** for component files: `UserProfile.tsx`, `OrderList.tsx`
- **PascalCase** for component directories: `UserProfile/`, `OrderList/`

### Hooks
- **camelCase** starting with `use`: `useAuth.ts`, `useFetchData.ts`

### Services
- **camelCase** with `.service` suffix: `customer.service.ts`, `auth.service.ts`

### Types
- **camelCase** with `.types` suffix: `customer.types.ts`, `common.types.ts`

### Utilities
- **camelCase**: `formatters.ts`, `validators.ts`

### Tests
- Same name as file + `.test`: `Button.test.tsx`, `useAuth.test.ts`

---

## Component Organization Patterns

### Pattern 1: Common Components (Atomic Design)

```
src/components/common/
├── atoms/              # Smallest building blocks
│   ├── Button/
│   ├── Input/
│   └── Label/
├── molecules/          # Simple combinations
│   ├── FormField/
│   ├── SearchBar/
│   └── Card/
└── organisms/          # Complex combinations
    ├── Header/
    ├── DataTable/
    └── Form/
```

### Pattern 2: Feature-Based Organization

```
src/features/
├── auth/
│   ├── components/
│   │   ├── LoginForm/
│   │   └── RegisterForm/
│   ├── hooks/
│   │   └── useAuth.ts
│   ├── services/
│   │   └── auth.service.ts
│   ├── types/
│   │   └── auth.types.ts
│   └── index.ts
└── orders/
    ├── components/
    │   ├── OrderList/
    │   └── OrderDetail/
    ├── hooks/
    │   └── useOrders.ts
    └── services/
        └── orders.service.ts
```

---

## TypeScript Integration

### Type Definition Example

**File: `src/types/customer.types.ts`**
```typescript
export interface Customer {
  id: string;
  name: string;
  email: string;
  createdAt: Date;
}

export interface CustomerDTO {
  id: string;
  name: string;
  email: string;
}

export type CustomerStatus = 'active' | 'inactive' | 'pending';
```

### Import and Usage

```typescript
import { Customer, CustomerStatus } from '@/types/customer.types';

const customer: Customer = {
  id: '1',
  name: 'John Doe',
  email: 'john@example.com',
  createdAt: new Date()
};
```

---

## Service Layer Structure

### API Service Example

**File: `src/services/api/customer.service.ts`**
```typescript
import { httpClient } from '../http/client';
import { Customer, CustomerDTO } from '@/types/customer.types';

export const customerService = {
  getAll: async (): Promise<Customer[]> => {
    const response = await httpClient.get<Customer[]>('/customers');
    return response.data;
  },

  getById: async (id: string): Promise<Customer> => {
    const response = await httpClient.get<Customer>(`/customers/${id}`);
    return response.data;
  },

  create: async (data: CustomerDTO): Promise<Customer> => {
    const response = await httpClient.post<Customer>('/customers', data);
    return response.data;
  },

  update: async (id: string, data: CustomerDTO): Promise<Customer> => {
    const response = await httpClient.put<Customer>(`/customers/${id}`, data);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await httpClient.delete(`/customers/${id}`);
  }
};
```

---

## Custom Hooks Pattern

### Hook Example

**File: `src/hooks/useFetch.ts`**
```typescript
import { useState, useEffect } from 'react';

interface UseFetchResult<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

export const useFetch = <T,>(url: string): UseFetchResult<T> => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await fetch(url);
        const json = await response.json();
        setData(json);
      } catch (err) {
        setError(err as Error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [url]);

  return { data, loading, error };
};
```

---

## Test File Organization

### Component Test Example

**File: `src/components/common/Button/Button.test.tsx`**
```typescript
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from './Button';

describe('Button', () => {
  it('renders with text', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });

  it('calls onClick handler when clicked', () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Click me</Button>);
    fireEvent.click(screen.getByText('Click me'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });
});
```

### Test Files Placement

**Option 1: Co-located (Recommended)**
```
src/components/common/Button/
├── Button.tsx
├── Button.test.tsx        # Test next to component
└── index.ts
```

**Option 2: Separate `__tests__` directory**
```
src/components/common/Button/
├── __tests__/
│   └── Button.test.tsx
├── Button.tsx
└── index.ts
```

---

## Path Aliases Configuration

Configure path aliases in `tsconfig.json` for cleaner imports:

```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"],
      "@/components/*": ["src/components/*"],
      "@/hooks/*": ["src/hooks/*"],
      "@/services/*": ["src/services/*"],
      "@/types/*": ["src/types/*"],
      "@/utils/*": ["src/utils/*"]
    }
  }
}
```

**Usage:**
```typescript
// Instead of: import { Button } from '../../../components/common/Button';
import { Button } from '@/components/common/Button';
import { useAuth } from '@/hooks/useAuth';
import { customerService } from '@/services/api/customer.service';
```

---

## Environment Configuration

### Environment Files

```
.env                    # Default environment variables
.env.local              # Local overrides (not committed)
.env.development        # Development-specific
.env.production         # Production-specific
.env.test               # Test-specific
```

### Example `.env` file

```bash
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=ERP System
VITE_ENABLE_ANALYTICS=false
```

### Usage in Code

**File: `src/config/env.ts`**
```typescript
export const env = {
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL,
  appName: import.meta.env.VITE_APP_NAME,
  enableAnalytics: import.meta.env.VITE_ENABLE_ANALYTICS === 'true'
};
```

---

## Common Mistakes to Avoid

❌ **WRONG**: Flat component structure
```
src/components/
├── Button.tsx
├── Input.tsx
├── Header.tsx
└── Footer.tsx
```

✅ **CORRECT**: Organized component directories
```
src/components/
├── common/
│   ├── Button/
│   └── Input/
└── layout/
    ├── Header/
    └── Footer/
```

❌ **WRONG**: Mixing concerns in one file
```typescript
// UserProfile.tsx with API calls, business logic, and UI
```

✅ **CORRECT**: Separation of concerns
```typescript
// services/user.service.ts - API calls
// hooks/useUser.ts - Business logic
// components/UserProfile/UserProfile.tsx - UI only
```

❌ **WRONG**: Relative imports everywhere
```typescript
import { Button } from '../../../components/common/Button';
```

✅ **CORRECT**: Use path aliases
```typescript
import { Button } from '@/components/common/Button';
```

❌ **WRONG**: No index files
```typescript
import { Button } from '@/components/common/Button/Button';
```

✅ **CORRECT**: Clean exports with index files
```typescript
import { Button } from '@/components/common/Button';
```

---

## Step-by-Step Process for Creating Components

When you need to create a React component, follow these steps:

1. **Determine the component type**
   - Common reusable component → `src/components/common/`
   - Layout component → `src/components/layout/`
   - Page component → `src/pages/`
   - Feature component → `src/features/{feature-name}/components/`

2. **Create the component directory**
   ```bash
   mkdir -p src/components/common/Button
   ```

3. **Create the component files**
   - Component: `Button.tsx`
   - Test: `Button.test.tsx`
   - Styles: `Button.module.css` (if needed)
   - Export: `index.ts`

4. **Write the component**
   ```typescript
   // Button.tsx
   import styles from './Button.module.css';

   interface ButtonProps {
     children: React.ReactNode;
     onClick?: () => void;
     variant?: 'primary' | 'secondary';
   }

   export const Button: React.FC<ButtonProps> = ({
     children,
     onClick,
     variant = 'primary'
   }) => {
     return (
       <button
         className={styles[variant]}
         onClick={onClick}
       >
         {children}
       </button>
     );
   };
   ```

5. **Create the index file**
   ```typescript
   // index.ts
   export { Button } from './Button';
   export type { ButtonProps } from './Button';
   ```

---

## Agent-Specific Instructions

### For ALL Agents Creating React Code:

1. **ALWAYS** create components in their own directories
2. **ORGANIZE** by type: common components, pages, features
3. **SEPARATE** concerns: UI, logic, API calls, types
4. **USE** TypeScript for type safety
5. **CREATE** index files for clean exports
6. **CONFIGURE** path aliases for cleaner imports
7. **CO-LOCATE** tests with components
8. **FOLLOW** naming conventions consistently

### When Working in `/work-in-progress/{PROGRAM}/`:

During development phases (ui, integration, testing), you may create files in phase-specific directories. However, ensure that:

- Components still follow the directory structure conventions
- When files are moved to `/final-output/`, they MUST follow the standard React structure
- Document the directory structure used in your phase notes

---

## State Management Structure

### Redux Toolkit Example

```
src/store/
├── slices/
│   ├── authSlice.ts
│   ├── orderSlice.ts
│   └── customerSlice.ts
├── hooks.ts             # Typed useDispatch and useSelector
├── store.ts             # Store configuration
└── middleware/
    └── logger.ts
```

### Zustand Example

```
src/store/
├── useAuthStore.ts
├── useOrderStore.ts
└── useCustomerStore.ts
```

---

## Final Output Structure

All final React artifacts in `/final-output/` **MUST** strictly follow this structure:

```
final-output/
├── public/
├── src/
│   ├── components/
│   ├── pages/
│   ├── hooks/
│   ├── services/
│   ├── types/
│   ├── utils/
│   ├── App.tsx
│   └── index.tsx
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

This ensures that:
- The project can be built successfully
- IDEs can properly recognize the project structure
- Tests can be discovered and run automatically
- The application can be deployed correctly
- Other developers can navigate the codebase easily

---

## Quick Reference

**Creating a Common Component:**
```
Directory: src/components/common/Button/
Files: Button.tsx, Button.test.tsx, Button.module.css, index.ts
```

**Creating a Page:**
```
Directory: src/pages/Dashboard/
Files: Dashboard.tsx, Dashboard.test.tsx, Dashboard.module.css, index.ts
```

**Creating a Custom Hook:**
```
File: src/hooks/useAuth.ts
Test: src/hooks/useAuth.test.ts
```

**Creating a Service:**
```
File: src/services/api/customer.service.ts
Test: src/services/api/customer.service.test.ts
```

**Creating Types:**
```
File: src/types/customer.types.ts
```

---

**Remember**: Following this structure ensures maintainability, scalability, and consistency across the React application.
