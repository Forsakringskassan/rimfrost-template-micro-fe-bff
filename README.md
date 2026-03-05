# Rimfrost Micro Frontend BFF Template

A comprehensive template for building Backend-for-Frontend (BFF) servers for Rimfrost micro frontends. This template provides a production-ready Express.js server written in TypeScript with built-in support for data transformation, error handling, and development workflows.

## Table of Contents

- [Overview](#overview)
- [What is a BFF?](#what-is-a-bff)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Developing Your BFF](#developing-your-bff)
- [API Design](#api-design)
- [Data Transformation](#data-transformation)
- [Error Handling](#error-handling)
- [Testing](#testing)
- [Deployment](#deployment)
- [Best Practices](#best-practices)

## Overview

This is a template for creating a **Backend-for-Frontend (BFF)** service specific to a micro frontend in the Rimfrost task management system. 

### Key Features

- ✅ **Express.js Framework** - Fast, lightweight Node.js web framework
- ✅ **TypeScript** - Full type safety and excellent IDE support
- ✅ **Hot Reload Development** - Automatic server restart on code changes with `tsx --watch`
- ✅ **CORS Handling** - Pre-configured CORS headers for frontend communication
- ✅ **Request Logging** - Built-in request logging for debugging
- ✅ **Data Transformation** - Examples for converting backend data (snake_case → camelCase)
- ✅ **Error Handling** - Robust error handling with proper HTTP status codes
- ✅ **ESLint & Prettier** - Code quality tools for consistency
- ✅ **Environment Configuration** - Support for `.env` files and environment variables

## What is a BFF?

A **Backend-for-Frontend (BFF)** is a specialized API server that sits between your frontend application and backend services. It serves several important purposes:

### Responsibilities

1. **Data Transformation** - Convert backend responses to frontend-friendly formats
   - Backend returns snake_case → Frontend needs camelCase
   - Extract only needed fields
   - Restructure data hierarchies

2. **API Aggregation** - Combine multiple backend calls into single endpoints
   - Frontend calls one endpoint
   - BFF internally calls 3 backend services
   - BFF returns combined, deduplicated data

3. **Business Logic** - Implement task-specific logic that doesn't belong in the frontend
   - Validation before sending to backend
   - Authorization checks
   - Conditional data fetching

4. **Error Handling & Resilience** - Graceful error handling and fallback strategies
   - Implement retry logic
   - Provide meaningful error messages to frontend
   - Mock data fallback for development

5. **Security** - Protect sensitive operations
   - API key management (backend keys never exposed to frontend)
   - Authentication/authorization enforcement
   - Request validation and sanitization

### Why Separate from Frontend?

In a micro-frontend architecture:
- ✅ Each micro frontend has its own dedicated BFF
- ✅ Frontend code stays lean and focused on UI
- ✅ Backend services remain unchanged
- ✅ Easy to add, update, or deprecate micro frontends
- ✅ Independent scaling and deployment

## Architecture

### System Overview

```
┌──────────────────────────────────────────┐
│  Micro Frontend (Vue App)                │
│  - User Interface                        │
│  - Component Logic                       │
│  - State Management (Pinia)              │
└──────────────────────────────────────────┘
            ↓ HTTP Requests
┌──────────────────────────────────────────┐
│  This BFF Service (Express.js)           │
│  - Data transformation                   │
│  - Business logic                        │
│  - Error handling                        │
│  - Validation                            │
└──────────────────────────────────────────┘
            ↓ HTTP Requests
┌──────────────────────────────────────────┐
│  Backend API Services                    │
│  - Database operations                   │
│  - Core business logic                   │
│  - Data persistence                      │
└──────────────────────────────────────────┘
```

### Data Flow Example

```
Frontend Request:
GET /api/regel/my-task/12345

↓

BFF Processing:
1. Validate request (kundbehovsflodeId: 12345)
2. Call backend: GET /backend-service/rule/12345
3. Receive: { rule_id: 1, rule_name: "test", ... }
4. Transform: { ruleId: 1, ruleName: "test", ... }
5. Respond: JSON { ruleId: 1, ruleName: "test", ... }

↓

Frontend Receives:
{ ruleId: 1, ruleName: "test", ... } ✓
```

## Getting Started

### Prerequisites

- Node.js 24+ (adjust `@tsconfig/node24` in `package.json` if using a different version)
- npm or yarn
- Basic understanding of Express.js and TypeScript
- A micro frontend that will consume this BFF

### Installation

1. **Clone or create from template**:
   ```bash
   git clone <your-template-repo-url>
   cd your-micro-bff
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Create `.env` file** from the example:
   ```bash
   cp .env.example .env
   ```

4. **Edit `.env` with your configuration**:
   ```env
   PORT=9002
   NODE_ENV=development
   BACKEND_BASE_URL=http://localhost:8890
   ```

### Development

Start the development server with hot reload:
```bash
npm run dev
```

The BFF will be available at `http://localhost:9002` (configurable via `PORT` env var).

Changes to your TypeScript files will automatically restart the server.

### Building for Production

Build TypeScript to JavaScript:
```bash
npm run build
```

Run the production build:
```bash
npm start
```

### Available Scripts

```bash
npm run dev              # Start dev server with hot reload
npm run build            # Compile TypeScript to JavaScript
npm start                # Run production server (requires build first)
npm run type-check       # Check TypeScript without building
npm run lint             # Run ESLint
npm run lint:fix         # Fix ESLint issues automatically
npm run format           # Format code with Prettier
npm run format:check     # Check code formatting
```

## Project Structure

```
.
├── index.ts                              # Main application file (Express setup, routes)
├── utils/
│   ├── transformBackendResponse.ts      # Data transformation utilities
│   └── [your-utils].ts                  # Add more utilities here
├── types.d.ts                           # TypeScript type definitions
├── package.json                         # Dependencies and scripts
├── tsconfig.json                        # TypeScript configuration
├── eslint.config.js                     # ESLint rules
├── .env.example                         # Example environment variables
├── .env                                 # Your actual environment variables (not in git)
├── .gitignore                           # Git ignore rules
├── dist/                                # Compiled JavaScript (created by npm run build)
└── node_modules/                        # Dependencies (created by npm install)
```

### Key Files Explained

**index.ts** - Main application file containing:
- Express app initialization
- Middleware setup (CORS, JSON parsing, logging)
- Route definitions
- Server startup

**utils/transformBackendResponse.ts** - Example data transformation utilities:
```typescript
// Snake case to camel case converter
export function snakeToCamelCase(obj: any): any {
  // Implementation converts { rule_id: 1 } → { ruleId: 1 }
}

// Add more transformation functions for your data
export function transformTaskData(backendData: any) {
  return {
    id: backendData.task_id,
    title: backendData.task_title,
    status: backendData.task_status,
  };
}
```

## Developing Your BFF

### Anatomy of a BFF Endpoint

Here's what a typical BFF endpoint looks like:

```typescript
// GET endpoint to fetch task data
app.get('/api/regel/:regeltyp/:id', async (req, res) => {
  try {
    // 1. Extract and validate parameters
    const { regeltyp, id } = req.params;
    
    if (!id || id.length === 0) {
      return res.status(400).json({ error: 'Invalid task ID' });
    }

    // 2. Build backend URL
    const backendUrl = `${process.env.BACKEND_BASE_URL}/regel/${regeltyp}/${id}`;

    // 3. Call backend service
    const response = await fetch(backendUrl, {
      method: 'GET',
      headers: {
        // Forward auth headers if present
        ...(req.headers.authorization && { 
          authorization: req.headers.authorization 
        }),
      },
    });

    // 4. Handle backend errors
    if (!response.ok) {
      const error = await response.text();
      console.error(`Backend error: ${error}`);
      return res.status(response.status).json({ 
        error: 'Failed to fetch data from backend' 
      });
    }

    // 5. Parse backend response
    const backendData = await response.json();

    // 6. Transform data for frontend
    const transformedData = transformTaskData(backendData);

    // 7. Send response
    return res.json(transformedData);
    
  } catch (error) {
    console.error('Error in GET /api/regel/:regeltyp/:id', error);
    return res.status(500).json({ 
      error: 'Internal server error',
      message: error instanceof Error ? error.message : 'Unknown error'
    });
  }
});
```

### Creating New Endpoints

1. **Define the route and HTTP method**:
   ```typescript
   app.post('/api/rule/update/:id', async (req, res) => {
     // Implementation
   });
   ```

2. **Extract request data**:
   ```typescript
   const { id } = req.params;           // From URL
   const { title, description } = req.body;  // From body
   const token = req.headers.authorization; // From headers
   ```

3. **Validate input**:
   ```typescript
   if (!title || title.length === 0) {
     return res.status(400).json({ error: 'Title is required' });
   }
   ```

4. **Call backend**:
   ```typescript
   const response = await fetch(backendUrl, {
     method: 'POST',
     headers: { 'Content-Type': 'application/json' },
     body: JSON.stringify({ title, description }),
   });
   ```

5. **Handle response**:
   ```typescript
   if (!response.ok) {
     return res.status(response.status).json({ error: 'Backend error' });
   }
   const data = await response.json();
   ```

6. **Transform and return**:
   ```typescript
   return res.json(transformTaskData(data));
   ```

## API Design

### Best Practices for BFF APIs

#### 1. **RESTful Naming**
```typescript
// ✅ Good
GET    /api/rules/:ruleId           // Get single rule
POST   /api/rules                    // Create new rule
PUT    /api/rules/:ruleId            // Update entire rule
PATCH  /api/rules/:ruleId            // Partial update
DELETE /api/rules/:ruleId            // Delete rule

// ❌ Avoid
GET    /api/getRule/:ruleId
GET    /api/createRule
```

#### 2. **Consistent Response Format**

Success response:
```typescript
res.json({
  data: { /* actual data */ },
  status: 'success',
  timestamp: new Date().toISOString()
});
```

Error response:
```typescript
res.status(400).json({
  error: 'Invalid input',
  message: 'Task ID is required',
  timestamp: new Date().toISOString()
});
```

#### 3. **Appropriate HTTP Status Codes**
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (missing auth)
- `403` - Forbidden (not allowed)
- `404` - Not Found
- `500` - Internal Server Error
- `502` - Bad Gateway (backend unreachable)
- `503` - Service Unavailable

#### 4. **Environment-Specific URLs**

Use environment variables to support different environments:
```env
# .env.development
BACKEND_BASE_URL=http://localhost:8890

# .env.production
BACKEND_BASE_URL=https://api.production.example.com
```

## Data Transformation

### Why Transform Data?

- **Frontend convenience** - Use JavaScript conventions (camelCase)
- **API stability** - Decouple frontend from backend API changes
- **Efficiency** - Remove unnecessary fields
- **Consistency** - Format dates, numbers, etc. consistently

### Example Transformations

#### Snake Case to Camel Case
```typescript
// Backend sends: { user_id: 1, user_name: "John" }
// Frontend needs: { userId: 1, userName: "John" }

function snakeToCamel(str: string): string {
  return str.replace(/_([a-z])/g, (match, letter) => letter.toUpperCase());
}

function transformObject(obj: Record<string, any>): Record<string, any> {
  const transformed: Record<string, any> = {};
  for (const [key, value] of Object.entries(obj)) {
    transformed[snakeToCamel(key)] = value;
  }
  return transformed;
}
```

#### Restructuring Data
```typescript
// Backend response
{
  "rule_id": 1,
  "rule_metadata": {
    "created_date": "2024-01-01",
    "created_by": "admin"
  }
}

// Transform for frontend
function transformRuleData(data: any) {
  return {
    id: data.rule_id,
    createdDate: new Date(data.rule_metadata.created_date),
    createdBy: data.rule_metadata.created_by,
  };
}
```

#### Filtering Fields
```typescript
// Backend sends full user object with sensitive fields
// BFF sends only what frontend needs

function transformUserData(backendUser: any) {
  return {
    id: backendUser.user_id,
    name: backendUser.name,
    email: backendUser.email,
    // ❌ Don't include: password_hash, api_key, internal_id
  };
}
```

#### Formatting Dates
```typescript
function transformTaskData(data: any) {
  return {
    ...data,
    dueDate: new Date(data.due_date).toISOString(),
    createdAt: new Date(data.created_at).toISOString(),
  };
}
```

## Error Handling

### Error Handling Strategy

Your BFF should handle errors at multiple levels:

```typescript
// 1. Parameter validation
if (!id) {
  return res.status(400).json({ error: 'ID is required' });
}

// 2. Backend communication
const response = await fetch(backendUrl);
if (!response.ok) {
  return res.status(502).json({ error: 'Backend service unavailable' });
}

// 3. Data transformation
try {
  const data = await response.json();
  const transformed = transformData(data);
  res.json(transformed);
} catch (error) {
  res.status(500).json({ error: 'Data processing error' });
}
```

### Common Errors and Responses

```typescript
// 400 - Bad Request
res.status(400).json({
  error: 'Invalid request',
  message: 'Task ID must be a positive number'
});

// 401 - Unauthorized
res.status(401).json({
  error: 'Unauthorized',
  message: 'Missing or invalid authentication token'
});

// 404 - Not Found
res.status(404).json({
  error: 'Not found',
  message: 'Task with ID 99999 does not exist'
});

// 502 - Bad Gateway
res.status(502).json({
  error: 'Service unavailable',
  message: 'Backend service is currently unavailable'
});

// 500 - Internal Server Error
res.status(500).json({
  error: 'Internal server error',
  message: error instanceof Error ? error.message : 'Unknown error'
});
```

### Development Fallback Strategy

For development, you might implement a fallback to mock data:

```typescript
async function fetchWithFallback(url: string) {
  try {
    const response = await fetch(url);
    if (response.ok) {
      return await response.json();
    }
  } catch (error) {
    console.warn('Backend unavailable, using mock data');
  }
  
  // Return mock data
  return {
    id: '1',
    title: 'Mock Task',
    status: 'pending'
  };
}
```

## Testing

### Manual Testing

1. **Start your BFF**:
   ```bash
   npm run dev
   ```

2. **Test health endpoint**:
   ```bash
   curl http://localhost:9002/api/health
   ```

3. **Test your endpoint** (with example parameters):
   ```bash
   curl http://localhost:9002/api/regel/manual/12345 \
     -H "Authorization: Bearer YOUR_TOKEN" \
     -H "Content-Type: application/json"
   ```

### Using Thunder Client / Postman

Create requests to test:
- GET requests for fetching data
- POST requests for creating data
- PUT/PATCH requests for updating
- DELETE requests for removing data

### Testing Tips

- ✅ Test with valid data
- ✅ Test with invalid/missing parameters
- ✅ Test with invalid authorization
- ✅ Test what happens when backend is down
- ✅ Test with large payloads
- ✅ Test with special characters in strings

## Deployment

### Build for Production

```bash
npm run build
```

This creates a `dist/` directory with compiled JavaScript files.

### Prerequisites for Production

- Node.js 24+ running on your server
- Environment variables set in your deployment environment:
  ```env
  PORT=9002
  NODE_ENV=production
  BACKEND_BASE_URL=https://your-backend-api.com
  ```

### Running in Production

```bash
npm start
```

### Docker Deployment (Optional)

Create a `Dockerfile` if deploying with Docker:

```dockerfile
FROM node:24-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --production

# Build TypeScript
RUN npm run build

# Copy compiled code
COPY dist ./dist

# Start server
CMD ["npm", "start"]
```

### Environment-Specific Configuration

Use different `.env` files or environment variables for each deployment:

```bash
# Development
BACKEND_BASE_URL=http://localhost:8890

# Staging
BACKEND_BASE_URL=https://staging-api.example.com

# Production
BACKEND_BASE_URL=https://api.production.example.com
```

## Best Practices

### 1. **Error Handling**
- ✅ Always use try-catch for async operations
- ✅ Log errors with context (what failed, why, when)
- ✅ Return meaningful error messages to frontend
- ✅ Use appropriate HTTP status codes
- ❌ Don't expose sensitive internal details in errors

### 2. **Performance**
- ✅ Use parallel requests for independent calls:
  ```typescript
  const [users, tasks] = await Promise.all([
    fetch(usersUrl),
    fetch(tasksUrl)
  ]);
  ```
- ✅ Cache responses when appropriate
- ✅ Set timeouts for backend calls
- ❌ Don't make unnecessary multiple requests

### 3. **Security**
- ✅ Validate all input from frontend
- ✅ Never log sensitive data (passwords, tokens)
- ✅ Forward authentication headers securely
- ✅ Use HTTPS in production
- ✅ Implement rate limiting for public endpoints
- ❌ Don't hardcode secrets in code

### 4. **Code Organization**
- ✅ Keep routes in `index.ts`
- ✅ Move transformation logic to `utils/`
- ✅ Create separate utility files for different domains
- ✅ Use TypeScript interfaces for data structures
- ❌ Don't put all logic in one massive function

### 5. **Logging**
- ✅ Log important operations (requests, errors, warnings)
- ✅ Include timestamps and request IDs
- ✅ Use consistent log levels (info, warn, error)
- ❌ Don't log in production (or use minimal logging)
- ❌ Don't log sensitive data

### 6. **Documentation**
- ✅ Document each endpoint's purpose
- ✅ Document required request parameters
- ✅ Document response format
- ✅ Add comments for complex transformations
- ✅ Keep this README updated

## Example: Complete Task Data Endpoint

Here's a complete, well-structured endpoint example:

```typescript
interface TaskRequest {
  ruleType: string;
  kundbehovsflodeId: string;
}

interface TransformedTask {
  id: string;
  title: string;
  description: string;
  status: 'pending' | 'completed' | 'failed';
  createdAt: string;
}

/**
 * Fetch task data for a specific rule type and customer flow
 * GET /api/task/:ruleType/:kundbehovsflodeId
 */
app.get('/api/task/:ruleType/:kundbehovsflodeId', async (req, res) => {
  try {
    // Validate inputs
    const { ruleType, kundbehovsflodeId } = req.params;
    
    if (!ruleType || !kundbehovsflodeId) {
      return res.status(400).json({
        error: 'Missing required parameters',
        required: ['ruleType', 'kundbehovsflodeId']
      });
    }

    // Build backend URL
    const backendUrl = `${process.env.BACKEND_BASE_URL}/rule/${ruleType}/${kundbehovsflodeId}`;
    
    console.log(`Fetching task from: ${backendUrl}`);

    // Call backend
    const response = await fetch(backendUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(req.headers.authorization && { 
          authorization: req.headers.authorization 
        }),
      },
    });

    // Handle backend errors
    if (!response.ok) {
      const error = await response.text();
      console.error(`Backend error (${response.status}): ${error}`);
      
      return res.status(502).json({
        error: 'Failed to fetch task data',
        status: response.status
      });
    }

    // Parse response
    const backendData = await response.json();

    // Transform data
    const transformed: TransformedTask = {
      id: backendData.task_id,
      title: backendData.task_title,
      description: backendData.task_description,
      status: transformStatus(backendData.task_status),
      createdAt: new Date(backendData.created_at).toISOString(),
    };

    // Return transformed data
    return res.json(transformed);

  } catch (error) {
    console.error('Unexpected error fetching task:', error);
    return res.status(500).json({
      error: 'Internal server error',
      message: error instanceof Error ? error.message : 'Unknown error'
    });
  }
});

function transformStatus(backendStatus: string): 'pending' | 'completed' | 'failed' {
  const mapping: Record<string, 'pending' | 'completed' | 'failed'> = {
    'in_progress': 'pending',
    'completed': 'completed',
    'error': 'failed',
  };
  return mapping[backendStatus] || 'pending';
}
```

## Troubleshooting

### Port Already in Use

```bash
# Change port in .env
PORT=9003 npm run dev
```

### Backend Connection Error

**Problem**: "Cannot reach backend at http://localhost:8890"

Solutions:
- Check that backend is running
- Update `BACKEND_BASE_URL` in `.env`
- Check firewall rules

### TypeScript Errors

```bash
npm run type-check    # Check types without building
npm run lint:fix      # Auto-fix linting issues
```

### Hot Reload Not Working

- Ensure you're running with `npm run dev` (not `npm start`)
- Check that files are being saved
- Restart the development server if needed

## Resources

- [Express.js Documentation](https://expressjs.com/)
- [Node.js Documentation](https://nodejs.org/docs/)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
- [Fetch API Documentation](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [REST API Best Practices](https://restfulapi.net/)

## Contributing

When adding features to this template, please:
- Add TypeScript types for new functions
- Include error handling for all async operations
- Add comments for complex logic
- Test endpoints before committing
- Update this README if adding new patterns

## License

[Add your license information here]

## 💡 Tips

- **Development**: Använd `npm run dev` för snabb utveckling med auto-reload
- **Type Safety**: Definiera TypeScript-interfaces för din data i separata filer
- **Error Logging**: Överväg att lägga till ett logging-library i produktion (t.ex. Winston, Pino)
- **Validation**: Lägg till request/response validation (t.ex. Zod, Joi)
- **Testing**: Lägg till test-framework (t.ex. Jest, Vitest)

## 📝 License

ISC