# Integration Agent

## Purpose
Consolidate all reviewed artifacts from the conversion pipeline into the final production-ready application structure with Spring Boot backend and React frontend, and prepare for deployment.

## Inputs Required
1. Project settings from `project-settings.md`
2. All outputs from phases 01-06 in `/work-in-progress/{PROGRAM}/`
3. Review confirmation from `/work-in-progress/{PROGRAM}/06-review/review-report.md`
4. Final output structure: `/final-output/backend/` and `/final-output/frontend/`

## Outputs to Produce
1. **Consolidated Java codebase** in `/final-output/backend/src/`
2. **Backend test suite** in `/final-output/backend/src/`
3. **React frontend application** in `/final-output/frontend/src/`
4. **Documentation package** in `/final-output/docs/{PROGRAM}/`
5. **Integration report** in `/final-output/docs/{PROGRAM}/integration-report.md`
6. **Update project tracking**: Update `/documentation/progress-tracking/conversion-status.md` to mark program as deployed

## Pre-Integration Verification
1. **Verify Phase 6 Complete**:
   - Check that `/work-in-progress/{PROGRAM}/06-review/review-report.md` exists
   - Confirm all quality checklist items are marked as passed
   - Verify no blocking issues in the review report

2. **Check for Conflicts**:
   - Scan `/final-output/` for existing files with same names
   - Identify potential package conflicts
   - Document any merge strategies needed

## Integration Process

### Step 1: Copy Database Layer (Backend)
Copy from `/work-in-progress/{PROGRAM}/02-database/src/` to `/final-output/backend/src/` (**do not change folders structure**)

### Step 2: Copy Business Logic Layer (Backend)
Copy from `/work-in-progress/{PROGRAM}/03-conversion/src/` (or `/work-in-progress/{PROGRAM}/06-review/src/` if refactored) to `/final-output/backend/src/` (**do not change folders structure**)

### Step 3: Copy Backend Test Suite
Copy from `/work-in-progress/{PROGRAM}/05-testing/src/` to `/final-output/backend/src/` (**do not change folders structure**)

### Step 4: Integrate React Frontend

#### 4.1: Create React Application Shell (First Program Only)

**Check if React app shell exists**: Look for `/final-output/frontend/src/App.tsx`

**If NOT exists**:
1. Create React application structure using templates:
   - Copy `index.html.template` → `/final-output/frontend/public/index.html`
   - Copy `index.tsx.template` → `/final-output/frontend/src/index.tsx`
   - Copy `App.tsx.template` → `/final-output/frontend/src/App.tsx`
   - Copy `globals.css.template` → `/final-output/frontend/src/styles/globals.css`
   - Copy `package.json.template` → `/final-output/frontend/package.json`
   - Copy `tsconfig.json.template` → `/final-output/frontend/tsconfig.json`
   - Copy `.env.template` → `/final-output/frontend/.env.development`
   - Copy `.env.template` → `/final-output/frontend/.env.production`
2. Create directory structure:
   - `/final-output/frontend/src/components/`
   - `/final-output/frontend/src/pages/`
   - `/final-output/frontend/src/services/`
   - `/final-output/frontend/src/types/`
   - `/final-output/frontend/src/hooks/`
   - `/final-output/frontend/src/utils/`
   - `/final-output/frontend/src/styles/`
   - `/final-output/frontend/public/`
3. Document in integration report that React shell was created

**If EXISTS** (subsequent programs):
1. Document in integration report that existing React shell was reused

#### 4.2: Integrate Program-Specific Components (All Programs)

1. **Copy Program Components**:
   - Copy from `/work-in-progress/{PROGRAM}/04-ui/src/` to `/final-output/frontend/src/` (**preserve folder structure**)
   - Typical structure copied:
     - `pages/{ProgramName}/` → Page components
     - `services/api/` → API service files
     - `types/` → Type definitions

2. **Update App.tsx**:
   - Add import for the new page component
   - Add a new route for the program
   - Example:
     ```typescript
     // Add import at top
     import { CustomerInquiry } from './pages/CustomerInquiry';

     // Add route in <Routes>
     <Route path="/customer-inquiry" element={<CustomerInquiry />} />

     // Add navigation link in header nav
     <Link to="/customer-inquiry">Customer Inquiry</Link>
     ```

3. **Merge package.json Dependencies**:
   - If `/work-in-progress/{PROGRAM}/04-ui/` contains `dependencies-to-add.md` or similar:
     - Add new dependencies to `/final-output/frontend/package.json`
     - Avoid duplicates, keep highest version if conflict
   - Document any new dependencies added

4. **Update Environment Files**:
   - Verify `/final-output/frontend/.env.development` has: `REACT_APP_API_URL=http://localhost:8080`
   - Verify `/final-output/frontend/.env.production` has correct production API URL (without /api suffix)
   - **CRITICAL**: Do NOT include `/api` in the URL - the service code adds it in API calls
   - Add any program-specific environment variables if needed

### Step 5: Backend Configuration Integration

#### 5.1: Initial Backend Setup (First Program Only)

**Check if backend structure exists**: Look for `/final-output/backend/pom.xml`

**If NOT exists** (first program):

1. **Create Spring Boot Main Application Class**:
   - Create `/final-output/backend/src/main/java/{PACKAGE_PATH}/ModernizationApplication.java`
   - Example package: `com.company.modernization` or `com.lanarimarco.modernization`
   ```java
   package {PACKAGE_NAME};

   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;

   @SpringBootApplication
   public class ModernizationApplication {
       public static void main(String[] args) {
           SpringApplication.run(ModernizationApplication.class, args);
       }
   }
   ```

2. **Create WebConfig for CORS**:
   - Create `/final-output/backend/src/main/java/{PACKAGE_PATH}/config/WebConfig.java`
   ```java
   package {PACKAGE_NAME}.config;

   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.servlet.config.annotation.CorsRegistry;
   import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

   @Configuration
   public class WebConfig implements WebMvcConfigurer {

       @Value("${cors.allowed-origins:http://localhost:3000}")
       private String allowedOrigins;

       @Override
       public void addCorsMappings(CorsRegistry registry) {
           registry.addMapping("/api/**")
                   .allowedOrigins(allowedOrigins.split(","))
                   .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                   .allowedHeaders("*")
                   .allowCredentials(true)
                   .maxAge(3600);
       }
   }
   ```

3. **Create pom.xml**:
   - Create `/final-output/backend/pom.xml` with Spring Boot parent, dependencies (Web, Data JPA, Validation, DB2, H2, Test, Lombok)
   - Group ID: `{COMPANY_GROUP}` (e.g., `com.lanarimarco`)
   - Artifact ID: `modernization-backend`
   - Version: `1.0.0`
   - Java version: `17`

4. **Create application.yml** configuration files (see step 5.2)

**If EXISTS** (subsequent programs):
- Skip initial setup, proceed to step 5.2

#### 5.2: Application Configuration (All Programs)

1. Review program-specific configurations from conversion notes

2. Update `/final-output/backend/src/main/resources/application.yml` if needed:
   - Database connection properties
   - Program-specific settings
   - Server port (default: 8080)
   - **IMPORTANT**: Do NOT set `server.servlet.context-path` - controllers use `/api` prefix in `@RequestMapping`
   - Logging configuration

3. Create/update environment-specific configs:
   - `application-dev.yml`:
     - H2 in-memory database for development
     - CORS allowed origins: `http://localhost:3000,http://localhost:5173`
     - Debug logging enabled
   - `application-uat.yml`:
     - UAT database connection
     - Passwords from environment variables
   - `application-prod.yml`:
     - Production database connection
     - Connection pooling
     - Reduced logging

4. Document all configuration changes in integration report

**CRITICAL API Architecture Notes**:
- Controllers MUST use `@RequestMapping("/api/...")` prefix
- Do NOT set `server.servlet.context-path=/api` in application.yml
- Frontend .env files should have `REACT_APP_API_URL=http://localhost:8080` (NO /api)
- Frontend service code concatenates `/api` in API calls: `${API_BASE_URL}/api/endpoint`
- Example full URL: `http://localhost:8080` + `/api/customers/123` = `http://localhost:8080/api/customers/123`

### Step 6: Documentation Consolidation
Create `/final-output/docs/{PROGRAM}/` with:

1. **integration-report.md** - This integration summary
2. **analysis.md** - Copy from `01-analysis/{PROGRAM}-analysis.md`
3. **database-notes.md** - Copy from `02-database/database-notes.md`
4. **conversion-notes.md** - Copy from `03-conversion/conversion-notes.md`
5. **ui-notes.md** - Copy from `04-ui/ui-notes.md`
6. **testing-notes.md** - Copy from `05-testing/testing-notes.md`
7. **review-report.md** - Copy from `06-review/review-report.md`
8. **deployment-guide.md** - Create new (see template below)

### Step 7: Backend Build Verification
1. **Compile Check**:
   ```bash
   cd /final-output/backend
   mvn clean compile
   ```
   Document any compilation errors and fixes.

2. **Test Execution**:
   ```bash
   mvn test
   ```
   Verify all tests pass. Document any failures.

3. **Package Build**:
   ```bash
   mvn clean package
   ```
   Ensure JAR builds successfully.

### Step 8: Frontend Build Verification
1. **Install Dependencies**:
   ```bash
   cd /final-output/frontend
   npm install
   ```
   Document any dependency resolution issues.

2. **Build Verification**:
   ```bash
   npm run build
   ```
   Verify frontend builds successfully. Document any warnings or errors.

3. **ESLint/Type Check** (if applicable):
   ```bash
   npm run lint
   npm run type-check  # if TypeScript
   ```

### Step 9: Create Deployment Guide
Create `/final-output/docs/{PROGRAM}/deployment-guide.md` with:

- **Backend** (Spring Boot):
  - Database prerequisites and setup
  - Java/Maven version requirements
  - Configuration requirements
  - Build and deployment steps
  - Environment-specific settings

- **Frontend** (React):
  - Node.js version requirements
  - Frontend build and deployment steps
  - Environment variables configuration
  - API endpoint configuration
  - Static hosting setup (Nginx, etc.)

- **Integrated Deployment**:
  - Docker containerization (optional)
  - CI/CD pipeline setup
  - Smoke test procedures
  - Rollback procedures

Use the `deployment-guide-template.md` in this folder.

## Integration Report Format

Create `/final-output/docs/{PROGRAM}/integration-report.md`:

```markdown
# Integration Report: {PROGRAM}

## Summary
- **Program**: {PROGRAM}
- **Integration Date**: {DATE}
- **Status**: Success/Partial/Failed
- **Backend Build Status**: Pass/Fail
- **Frontend Build Status**: Pass/Fail
- **Backend Tests**: Pass/Fail (X/Y tests passing)
- **Frontend Tests**: Pass/Fail (if applicable)

## Artifacts Integrated

### Backend
- [ ] Entities (count: X files)
- [ ] Repositories (count: X files)
- [ ] Services (count: X files)
- [ ] Controllers (count: X files)
- [ ] DTOs (count: X files)
- [ ] Exceptions (count: X files)
- [ ] Tests (count: X files)
- [ ] Configuration files
  - [ ] ModernizationApplication.java (main class) - Created/Existing
  - [ ] WebConfig.java (CORS configuration) - Created/Existing
  - [ ] pom.xml - Created/Updated
  - [ ] application.yml - Created/Updated
  - [ ] application-dev.yml - Created/Updated
  - [ ] application-uat.yml - Created/Updated
  - [ ] application-prod.yml - Created/Updated

### Frontend
- [ ] React Application Shell (App.tsx, index.tsx, public/index.html) - Created/Reused
- [ ] React Components (count: X files)
- [ ] Pages/Screens (count: X files)
- [ ] API Services (count: X files)
- [ ] Type Definitions (count: X files)
- [ ] Styling (count: X files)
- [ ] Route added to App.tsx
- [ ] Package configuration (dependencies merged if needed)
- [ ] Environment files (.env.development, .env.production)

### Documentation
- [ ] Analysis documentation
- [ ] Database notes
- [ ] Conversion notes
- [ ] UI notes
- [ ] Testing notes
- [ ] Review report
- [ ] Deployment guide

## File Mappings

### Backend Mappings
Document where each backend artifact was copied from → to

### Frontend Mappings
Document where each frontend artifact was copied from → to

## Conflicts Resolved
List any naming conflicts, package conflicts, or merge issues

## Backend Configuration Changes

### Initial Backend Setup (if first program)
- [ ] ModernizationApplication.java created with package: {PACKAGE_NAME}
- [ ] WebConfig.java created for CORS configuration
- [ ] pom.xml created with:
  - Spring Boot version: {VERSION}
  - Java version: {VERSION}
  - Dependencies: Web, Data JPA, Validation, DB2, H2, Test, Lombok
- [ ] application.yml created with default configuration
- [ ] Environment-specific YML files created (dev, uat, prod)

### Configuration Updates
List any changes made to application.yml or other config files:
- Database connection settings
- CORS allowed origins (in application-dev.yml)
- Logging levels
- **Note**: Context path NOT set - controllers use `/api` prefix in @RequestMapping

## Frontend Configuration Changes

### React Shell Creation (if first program)
- [ ] React application shell created from templates
- [ ] Directory structure established
- [ ] Base dependencies configured in package.json

### Program Integration
- [ ] Program components copied to correct locations
- [ ] Route added to App.tsx
- [ ] Navigation link added (if applicable)
- [ ] REACT_APP_API_URL configured in .env files:
  - `.env.development`: `http://localhost:8080` (NO /api suffix)
  - `.env.production`: Production URL (NO /api suffix)
- [ ] Program-specific dependencies added to package.json
- [ ] Environment variables configured

## Build Results

### Backend
- Compilation: Success/Failure
- Unit Tests: X/Y passing
- Integration Tests: X/Y passing
- Package: Success/Failure
- JAR Location: `/final-output/backend/target/modernization-app-1.0.0.jar`

### Frontend
- Dependency Installation: Success/Failure
- ESLint/Type Check: Success/Failure (if applicable)
- Build: Success/Failure
- Build Output Location: `/final-output/frontend/build/`

## Issues Found
List any issues discovered during integration

## Manual Steps Required
List any steps that require human intervention

## Verification Checklist
- [ ] All backend source files copied correctly
- [ ] All frontend source files copied correctly
- [ ] Package structure matches declarations
- [ ] No backend compilation errors
- [ ] No frontend build errors
- [ ] Backend tests passing
- [ ] Frontend tests passing (if applicable)
- [ ] Configuration complete and externalized
- [ ] API endpoint properly configured in frontend
- [ ] No hardcoded values in code
- [ ] Documentation organized
- [ ] Deployment guide created and reviewed
- [ ] Database setup documented
- [ ] Integration report complete
- [ ] Ready for UAT deployment

## Next Steps
1. Review integration report
2. Perform backend smoke testing
3. Perform frontend smoke testing
4. Test API integration between frontend and backend
5. Deploy to UAT environment
6. Execute full integration tests
```

## Quality Checklist
Use `integration-checklist.md` in this folder to verify:

### Pre-Integration
- [ ] Phase 6 review completed successfully
- [ ] All conversion phases completed for target program
- [ ] Review report shows green status

### Backend Integration
- [ ] Initial backend setup complete (first program):
  - [ ] ModernizationApplication.java created
  - [ ] WebConfig.java created for CORS
  - [ ] pom.xml created with all dependencies
  - [ ] application.yml files created (default, dev, uat, prod)
- [ ] All backend source files copied to correct packages
- [ ] No duplicate classes between programs
- [ ] Package names match file locations
- [ ] Maven build compiles successfully
- [ ] All backend tests passing (or failures documented)
- [ ] Configuration properly externalized
- [ ] No hardcoded values in Java code
- [ ] Controllers use `/api` prefix in @RequestMapping (NOT context-path)
- [ ] CORS configuration in WebConfig.java and application-dev.yml

### Frontend Integration
- [ ] React application shell created (first program) or verified (subsequent programs)
- [ ] All program-specific React components copied to correct locations
- [ ] No naming conflicts with existing components or routes
- [ ] Route added to App.tsx for the program
- [ ] Navigation link added to App.tsx header (if applicable)
- [ ] package.json properly configured with base + program-specific dependencies
- [ ] API endpoint environment variables configured:
  - [ ] .env.development: `REACT_APP_API_URL=http://localhost:8080` (NO /api suffix)
  - [ ] .env.production: Production URL (NO /api suffix)
- [ ] Frontend builds successfully without errors (`npm install && npm run build`)
- [ ] TypeScript types properly configured
- [ ] No hardcoded API URLs in code (uses process.env.REACT_APP_API_URL)
- [ ] Service code concatenates `/api` in API calls, NOT in .env
- [ ] Build assets generated successfully

### Documentation
- [ ] Documentation complete and organized
- [ ] All phase notes consolidated
- [ ] Deployment guide created and reviewed
- [ ] Integration report complete

### Final Verification
- [ ] Integration report complete
- [ ] **Progress tracking updated** - Program marked as integrated in `/documentation/progress-tracking/conversion-status.md`
- [ ] Ready for backend and frontend UAT/staging deployment
- [ ] API integration between frontend and backend documented

## Important Notes

### Directory Structure
The final output follows this structure:

```
/final-output/
├── backend/                              # Java Spring Boot Application
│   ├── src/
│   │   ├── main/java/{PACKAGE_PATH}/   # e.g., com/company/modernization
│   │   │   ├── ModernizationApplication.java  # Main Spring Boot class
│   │   │   ├── entities/                # JPA Entities
│   │   │   ├── repositories/            # Spring Data Repositories
│   │   │   ├── services/                # Business Logic
│   │   │   ├── controllers/             # REST Controllers
│   │   │   ├── dtos/                    # Data Transfer Objects
│   │   │   ├── exceptions/              # Custom Exceptions
│   │   │   └── config/                  # Configuration (WebConfig, etc.)
│   │   ├── resources/
│   │   │   ├── application.yml          # Default configuration
│   │   │   ├── application-dev.yml      # Development profile
│   │   │   ├── application-uat.yml      # UAT profile
│   │   │   ├── application-prod.yml     # Production profile
│   │   │   └── static/                  # Static assets (optional)
│   │   └── test/java/{PACKAGE_PATH}/   # Test classes
│   ├── pom.xml                          # Maven configuration
│   ├── Dockerfile                       # Docker image for backend (optional)
│   └── target/                          # Build output
│       └── modernization-backend.jar
│
├── frontend/                            # React Application
│   ├── src/
│   │   ├── components/                  # React Components
│   │   ├── pages/                       # Page Components
│   │   ├── services/                    # API Service Layer
│   │   ├── hooks/                       # Custom Hooks
│   │   ├── styles/                      # Styling
│   │   ├── assets/                      # Static Assets
│   │   ├── App.jsx/App.tsx              # Root Component
│   │   └── index.js                     # Entry Point
│   ├── public/                          # Static Files
│   ├── .env.development                 # Dev Configuration
│   ├── .env.staging                     # Staging Configuration
│   ├── .env.production                  # Prod Configuration
│   ├── package.json                     # npm Dependencies
│   ├── tsconfig.json                    # TypeScript config (if applicable)
│   ├── Dockerfile                       # Docker image for frontend
│   ├── nginx.conf                       # Nginx config for Docker
│   └── build/                           # Build output
│
├── docs/
│   └── {PROGRAM}/                       # Program-specific docs
│       ├── integration-report.md
│       ├── analysis.md
│       ├── database-notes.md
│       ├── conversion-notes.md
│       ├── ui-notes.md
│       ├── testing-notes.md
│       ├── review-report.md
│       └── deployment-guide.md
│
├── docker-compose.yml                   # Local deployment configuration
└── README.md                            # Project README
```

### Package Structure Consistency
**Java packages MUST match folder structure.**

When copying files, preserve the full package path:
- If source file has: `package com.company.modernization.services;`
- Target must be: `backend/src/main/java/com/company/modernization/services/FileName.java`

**Package Naming**:
- Use consistent base package across all programs (e.g., `com.company.modernization`)
- Subpackages: `entities`, `repositories`, `services`, `controllers`, `dtos`, `exceptions`, `config`

### Frontend Configuration
**React environment variables** must be set for API communication:

In `frontend/.env.development`:
```bash
REACT_APP_API_URL=http://localhost:8080
```

In `frontend/.env.production`:
```bash
REACT_APP_API_URL=https://api.production.company.com
```

**CRITICAL**: Do NOT include `/api` suffix in these URLs. The frontend service code concatenates `/api` in API calls (e.g., `${API_BASE_URL}/api/customers/${id}`).

### Handling Refactored Code
- If Phase 6 created refactored versions, use those instead of Phase 3 originals
- Check `/work-in-progress/{PROGRAM}/06-review/refactoring-log.md` for changes
- Document which version was used in integration report

### Multi-Program Considerations
- Check for duplicate entity names across programs
- Verify repository methods don't conflict
- Consider shared DTOs vs program-specific DTOs
- Document any cross-program dependencies

### Common Integration Pitfalls

**1. API URL Configuration Error**:
- ❌ WRONG: `REACT_APP_API_URL=http://localhost:8080/api`
- ✅ CORRECT: `REACT_APP_API_URL=http://localhost:8080`
- **Reason**: Service code adds `/api`, so .env should NOT include it

**2. Context Path Misconfiguration**:
- ❌ WRONG: Setting `server.servlet.context-path=/api` in application.yml
- ✅ CORRECT: Controllers use `@RequestMapping("/api/...")`, NO context-path
- **Reason**: Context-path would create double `/api/api/...` paths

**3. Missing CORS Configuration**:
- ❌ WRONG: No WebConfig.java, frontend can't connect to backend
- ✅ CORRECT: WebConfig.java with CORS mappings for `/api/**`
- **Reason**: Browser blocks cross-origin requests without CORS headers

**4. Missing Main Application Class**:
- ❌ WRONG: No ModernizationApplication.java, Spring Boot won't start
- ✅ CORRECT: Create ModernizationApplication.java with @SpringBootApplication
- **Reason**: Spring Boot needs an entry point

**5. Package Name Inconsistencies**:
- ❌ WRONG: Different packages per program (com.app1, com.app2)
- ✅ CORRECT: Consistent base package (com.company.modernization) across all programs
- **Reason**: Enables component scanning and reduces configuration

### Error Handling
If integration fails:
1. Document the failure in integration report
2. Do NOT copy partial/broken code
3. Create issue in `/documentation/progress-tracking/issues-log.md`
4. Recommend remediation steps
5. Check the "Common Integration Pitfalls" section above for known issues

## Success Criteria
Integration is successful when:
1. **Backend initial setup** (first program only):
   - ModernizationApplication.java created
   - WebConfig.java created with CORS configuration
   - pom.xml created with all dependencies
   - application.yml files created (default, dev, uat, prod)
2. All backend files copied without conflicts
3. React application shell created (first program) or verified (subsequent programs)
4. All frontend program components copied and integrated without conflicts
5. Route for program added to App.tsx successfully
6. **API configuration correct**:
   - Controllers use `/api` prefix in @RequestMapping
   - NO context-path set in application.yml
   - .env files have base URL WITHOUT /api suffix
   - Service code concatenates /api in API calls
7. Maven build (backend) compiles successfully
8. Maven test (backend) passes (or failures documented)
9. npm install (frontend) completes successfully
10. npm build (frontend) completes successfully without errors
11. All tests pass (or failures are acceptable and documented)
12. Documentation package is complete
13. Deployment guide is clear and actionable
14. Integration report shows green checklist
15. Both frontend and backend are ready for human review and UAT deployment
