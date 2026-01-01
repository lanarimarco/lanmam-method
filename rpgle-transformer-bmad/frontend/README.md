# RPGLE Transformer Frontend

React + TypeScript + Vite frontend for RPGLE to Spring Boot transformation tool.

## Project Overview

This frontend provides a modern web interface for the RPGLE transformation system, featuring:
- Customer inquiry screens transformed from CUST001 RPGLE program
- Full TypeScript type safety with Zod validation
- React Query for server state management
- Tailwind CSS for styling

## Development Setup

### Prerequisites
- Node.js 18+ and npm
- Backend running on `http://localhost:8080`
- PostgreSQL database (via Docker Compose)

### Install Dependencies
```bash
npm install
```

### Run Development Server
```bash
npm run dev
```
Frontend will be available at `http://localhost:5173`

## Testing

### Unit & Component Tests (Vitest)
```bash
# Run tests in watch mode
npm run test

# Run tests with coverage
npm run test:coverage
```

### End-to-End Tests (Playwright)

E2E tests validate the complete user journey from frontend to backend to database.

#### Run E2E Tests (Headless)
```bash
# Start backend and database
cd ..
docker-compose up -d

# Run E2E tests
cd frontend
npm run test:e2e
```

#### Run E2E Tests (UI Mode for debugging)
```bash
npm run test:e2e:ui
```

#### E2E Test Requirements
- Backend must be running at `http://localhost:8080/api/v1`
- Frontend dev server runs automatically via Playwright config
- Database must have test data seeded

#### View E2E Test Reports
After running tests:
```bash
npx playwright show-report
```

## Code Quality

### Linting
```bash
npm run lint
```

### Formatting
```bash
npm run format
```

## Architecture

```
src/
├── features/           # Feature modules (customers, etc.)
├── components/ui/      # Shared UI components
├── api/                # API client (generated from OpenAPI)
└── App.tsx             # Root component
```

## RPGLE Traceability

All components include inline comments referencing original RPGLE sources:
- Display files (`.dspf`)
- Program files (`.rpgle`)
- DDS physical files (`.dds`)

See source-rpgle/ directory for original sources.

---

## React + Vite Template Information

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) (or [oxc](https://oxc.rs) when used in [rolldown-vite](https://vite.dev/guide/rolldown)) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...

      // Remove tseslint.configs.recommended and replace with this
      tseslint.configs.recommendedTypeChecked,
      // Alternatively, use this for stricter rules
      tseslint.configs.strictTypeChecked,
      // Optionally, add this for stylistic rules
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
