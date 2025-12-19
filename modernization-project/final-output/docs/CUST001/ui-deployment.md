# UI Deployment Guide - CUST001 Customer Inquiry

## Overview

The CUST001 Customer Inquiry UI is built as a **separate React/TypeScript application** that communicates with the Spring Boot backend via REST API. This architecture allows for:
- Independent deployment and scaling of frontend and backend
- Modern single-page application (SPA) experience
- Flexibility in hosting (CDN, static hosting, etc.)

## UI Technology Stack

- **Framework**: React 18+
- **Language**: TypeScript
- **Build Tool**: Vite/Create React App
- **Styling**: CSS modules
- **HTTP Client**: Axios/Fetch API

## UI Components Location

The UI source files are located in:
```
/work-in-progress/CUST001/04-ui/
├── components/
│   └── CustomerInquiryScreen.tsx
├── types/
│   └── types.ts
├── styles/
│   └── CustomerInquiry.css
└── ui-notes.md (implementation notes)
```

## API Endpoint Configuration

The React application must be configured to connect to the Spring Boot backend API.

### API Base URL

**Development**:
```typescript
const API_BASE_URL = 'http://localhost:8080/api/v1/customers';
```

**Production**:
```typescript
const API_BASE_URL = 'https://erp.smeup.com/api/v1/customers';
```

### API Endpoints Used

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/inquire` | POST | Customer inquiry operation |
| `/inquire/init` | GET | Initialize screen (optional) |
| `/inquire/health` | GET | Health check |

### Sample API Configuration

Create an `api/config.ts` file:
```typescript
// api/config.ts
const getApiBaseUrl = (): string => {
  const env = process.env.NODE_ENV;

  if (env === 'production') {
    return process.env.REACT_APP_API_URL || 'https://erp.smeup.com/api/v1/customers';
  }

  return process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1/customers';
};

export const API_CONFIG = {
  BASE_URL: getApiBaseUrl(),
  ENDPOINTS: {
    INQUIRE: '/inquire',
    INIT: '/inquire/init',
    HEALTH: '/inquire/health'
  },
  TIMEOUT: 10000 // 10 seconds
};
```

## Deployment Options

### Option 1: Static Hosting (Recommended)

Deploy the built React app to a static hosting service:

**Build the app**:
```bash
cd /work-in-progress/CUST001/04-ui
npm run build
```

**Deploy to**:
- **AWS S3 + CloudFront**
- **Netlify**
- **Vercel**
- **Azure Static Web Apps**
- **GitHub Pages**

**Nginx configuration** (if self-hosting):
```nginx
server {
    listen 80;
    server_name customer-inquiry.smeup.com;

    root /var/www/customer-inquiry/build;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # API proxy (optional - if CORS is an issue)
    location /api/ {
        proxy_pass http://backend-server:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### Option 2: Embedded in Spring Boot (Not Recommended)

While possible to embed the React build in Spring Boot's static resources, this is **not recommended** because:
- Defeats the purpose of microservices architecture
- Requires backend rebuild for UI changes
- Harder to scale independently

If embedding is required:
1. Build the React app: `npm run build`
2. Copy build files to `src/main/resources/static/`
3. Access via: `http://localhost:8080/index.html`

## CORS Configuration

The Spring Boot backend must allow requests from the React app origin.

**Backend configuration** (`application.properties`):
```properties
# Development
app.cors.allowed-origins=http://localhost:3000,http://localhost:5173

# Production
app.cors.allowed-origins=https://customer-inquiry.smeup.com
```

The controller uses this configuration:
```java
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerInquiryController {
    // ...
}
```

## Environment Variables

Create `.env` files for different environments:

**.env.development**:
```
REACT_APP_API_URL=http://localhost:8080/api/v1/customers
REACT_APP_ENV=development
```

**.env.production**:
```
REACT_APP_API_URL=https://erp.smeup.com/api/v1/customers
REACT_APP_ENV=production
```

## Building the UI Application

### Development Mode

```bash
cd /work-in-progress/CUST001/04-ui

# Install dependencies
npm install

# Start development server
npm run dev

# Access at: http://localhost:3000 (or 5173 for Vite)
```

### Production Build

```bash
cd /work-in-progress/CUST001/04-ui

# Install dependencies
npm install

# Build for production
npm run build

# Output directory: build/ or dist/
```

## Testing the Integration

1. **Start the backend**:
   ```bash
   cd /final-output
   mvn spring-boot:run
   ```
   Backend runs on: `http://localhost:8080`

2. **Start the frontend**:
   ```bash
   cd /work-in-progress/CUST001/04-ui
   npm run dev
   ```
   Frontend runs on: `http://localhost:3000`

3. **Test the inquiry**:
   - Open browser to `http://localhost:3000`
   - Enter a customer number (e.g., 12345)
   - Click "Inquire"
   - Verify customer data is displayed

## Deployment Checklist

- [ ] UI code built successfully (`npm run build`)
- [ ] Environment variables configured for target environment
- [ ] API base URL points to correct backend
- [ ] CORS configured on backend to allow UI origin
- [ ] Static hosting configured (if using CDN/static host)
- [ ] SSL/HTTPS configured for production
- [ ] Error handling tested (network errors, 404s, etc.)
- [ ] Loading states working correctly
- [ ] Browser compatibility tested (Chrome, Firefox, Safari, Edge)

## Monitoring and Troubleshooting

### Common Issues

**Issue: CORS errors in browser console**
- Solution: Verify `app.cors.allowed-origins` in backend includes UI origin

**Issue: Network errors / API not reachable**
- Solution: Check `REACT_APP_API_URL` environment variable
- Solution: Verify backend is running and accessible

**Issue: 404 on refresh (SPA routing)**
- Solution: Configure server to always serve `index.html` for unknown routes

### Browser DevTools

Monitor API calls in browser DevTools:
1. Open DevTools (F12)
2. Go to Network tab
3. Look for calls to `/api/v1/customers/inquire`
4. Check request/response headers and body

## Security Considerations

1. **HTTPS Only**: Always use HTTPS in production
2. **API Keys**: If using API keys, store in environment variables (never commit to git)
3. **Input Validation**: Backend validates all inputs (already implemented)
4. **Error Messages**: Don't expose sensitive information in error messages
5. **Rate Limiting**: Consider adding rate limiting on API endpoints

## Performance Optimization

1. **Code Splitting**: Split React bundles for faster initial load
2. **CDN**: Use CDN for static assets (CSS, JS, images)
3. **Caching**: Configure proper cache headers for static files
4. **Compression**: Enable gzip/brotli compression on server
5. **Lazy Loading**: Lazy load components not needed on initial render

## Rollback Plan

If UI deployment fails:
1. Revert to previous version in static hosting
2. Update DNS/CDN to point to previous version
3. Verify backend API is still compatible with old UI

## Next Steps

1. Set up CI/CD pipeline for automated builds and deployments
2. Configure monitoring and analytics (e.g., Google Analytics)
3. Set up error tracking (e.g., Sentry)
4. Implement automated E2E tests (e.g., Cypress, Playwright)

## Contact

For UI deployment issues, contact:
- Frontend Team: frontend-team@smeup.com
- DevOps Team: devops@smeup.com

---

**Document Version**: 1.0
**Last Updated**: 2025-12-18
**Deployment Strategy**: Separate React App (Option B)
