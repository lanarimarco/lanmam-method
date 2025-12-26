# Integration Checklist

## Pre-Integration Verification
- [ ] Phase 6 review completed for this program
- [ ] Review report shows all quality checks passed
- [ ] No blocking issues in review report
- [ ] All required artifacts present in work-in-progress folder

## File Copy Verification
- [ ] All entity files copied to /final-output/src/main/java/.../entities/
- [ ] All repository files copied to /final-output/src/main/java/.../repositories/
- [ ] All service files copied to /final-output/src/main/java/.../services/
- [ ] All controller files copied to /final-output/src/main/java/.../controllers/
- [ ] All DTO files copied to /final-output/src/main/java/.../dto/
- [ ] All test files copied to /final-output/src/test/java/...
- [ ] Package structure preserved correctly
- [ ] No files overwritten unintentionally

## Package Structure Validation
- [ ] Java package declarations match folder paths
- [ ] No package naming conflicts with existing code
- [ ] All imports resolve correctly
- [ ] No circular dependencies introduced

## Build Verification
- [ ] Maven clean compile succeeds
- [ ] No compilation errors
- [ ] No missing dependencies
- [ ] All imports resolved

## Test Verification
- [ ] All unit tests copied
- [ ] All integration tests copied
- [ ] mvn test executes successfully
- [ ] All tests pass (or failures documented)
- [ ] Test coverage adequate
- [ ] No flaky tests

## Configuration
- [ ] application.yml updated if needed
- [ ] Database connection properties configured
- [ ] No hardcoded values in code
- [ ] Environment-specific settings externalized
- [ ] Logging configuration appropriate

## Frontend Integration

### React Application Shell (First Program Only)
- [ ] Check if /final-output/frontend/src/App.tsx exists
- [ ] If first program: React shell created from templates
- [ ] If first program: Directory structure created (components, pages, services, types, hooks, utils, styles)
- [ ] If first program: package.json created with base dependencies
- [ ] If first program: tsconfig.json configured with path aliases
- [ ] If first program: .env.development and .env.production created
- [ ] Shell creation documented in integration report

### Program Component Integration (All Programs)
- [ ] Program page components copied to /final-output/frontend/src/pages/
- [ ] Program API services copied to /final-output/frontend/src/services/api/
- [ ] Program type definitions copied to /final-output/frontend/src/types/
- [ ] Custom hooks copied (if any)
- [ ] Utility functions copied (if any)
- [ ] No file conflicts or overwrites

### App.tsx Updates
- [ ] Import statement added for program page component
- [ ] Route added in <Routes> with appropriate path
- [ ] Navigation link added to header (if applicable)
- [ ] Route path follows convention (e.g., /customer-inquiry)

### Dependency Management
- [ ] Checked dependencies-to-add.txt from UI phase
- [ ] New dependencies added to package.json (if needed)
- [ ] No version conflicts with existing dependencies
- [ ] Dependencies documented in integration report

### Build Verification
- [ ] npm install executes successfully
- [ ] No dependency resolution errors
- [ ] npm run build completes without errors
- [ ] Build artifacts generated in /build/
- [ ] No TypeScript errors
- [ ] No ESLint errors (warnings acceptable)

### Configuration
- [ ] REACT_APP_API_URL set in .env.development
- [ ] REACT_APP_API_URL set in .env.production
- [ ] Program-specific env variables added (if any)
- [ ] No hardcoded API URLs in component code

### Integration Notes
- [ ] Reviewed integration-notes.md from UI phase
- [ ] Route path follows suggested naming
- [ ] Navigation placement appropriate
- [ ] Any special integration requirements handled

## Documentation
- [ ] integration-report.md created
- [ ] All phase notes copied to docs/{PROGRAM}/
- [ ] deployment-guide.md created
- [ ] File mappings documented
- [ ] Conflicts and resolutions documented
- [ ] Configuration changes documented

## Multi-Program Considerations
- [ ] No duplicate entity classes
- [ ] No conflicting repository method names
- [ ] Shared vs program-specific DTOs identified
- [ ] Cross-program dependencies documented

## Code Quality
- [ ] No duplicated code between programs
- [ ] Service layer properly organized
- [ ] Controllers follow REST conventions
- [ ] Exception handling consistent
- [ ] Logging consistent across program

## Security
- [ ] No sensitive data in code or config
- [ ] Input validation present
- [ ] SQL injection prevented (JPA)
- [ ] Authentication/authorization considered

## Performance
- [ ] No obvious performance issues
- [ ] Database queries optimized
- [ ] No N+1 query problems
- [ ] Caching considered where appropriate

## Deployment Readiness
- [ ] Deployment guide complete and clear
- [ ] Database setup documented
- [ ] Configuration requirements documented
- [ ] Smoke test procedures defined
- [ ] Rollback procedures defined
- [ ] Environment requirements documented

## Project Tracking
- [ ] conversion-status.md updated
- [ ] Program marked as integrated
- [ ] Any issues logged in issues-log.md
- [ ] Team notified of integration completion

## Final Verification
- [ ] Integration report reviewed
- [ ] All checklist items completed
- [ ] No outstanding issues
- [ ] Ready for human review
- [ ] Ready for UAT deployment

## Sign-off
- **Integrated by**: [Agent/Human Name]
- **Date**: [Date]
- **Status**: Ready for UAT / Needs Review / Has Issues
- **Notes**: [Any important notes]
