# DIGIQUAL Platform - Getting Started Guide

## Overview
This guide will help you set up and run the DIGIQUAL platform locally.

## System Requirements
- **Java**: 17 or higher
- **Maven**: 3.8 or higher
- **Node.js**: 16 or higher
- **npm**: 8 or higher
- **PostgreSQL**: 12 or higher

## Database Setup

### 1. Create PostgreSQL Database
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE digiqual_db;

# Exit psql
\q
```

### 2. Configure Backend Database Connection
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/digiqual_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Backend Setup & Run

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application (runs on http://localhost:8080)
mvn spring-boot:run
```

### Verify Backend
```bash
# In another terminal, check health
curl http://localhost:8080/api/auth/health
# Expected: "DIGIQUAL API is running"
```

## Frontend Setup & Run

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server (runs on http://localhost:5173)
npm run dev
```

### Access Application
- Frontend: [http://localhost:5173](http://localhost:5173)
- Backend API: [http://localhost:8080/api](http://localhost:8080/api)

## Login with Demo Credentials

The application supports three roles:

### 1. **Super Admin**
- **Email**: `admin@digiqual.com`
- **Password**: `Admin@123`
- **Access**: Full platform control

### 2. **Partner Institute**
- **Email**: `partner@digiqual.com`
- **Password**: `Partner@123`
- **Access**: Manage batches and students

### 3. **Student**
- **Email**: `student@digiqual.com`
- **Password**: `Student@123`
- **Access**: View courses and certificates

## Project Structure

```
digiqual-platform/
├── backend/                    ← Spring Boot API
│   ├── src/main/java/com/digiqual/
│   │   ├── config/            ← Security configuration
│   │   ├── controller/        ← REST endpoints
│   │   ├── dto/               ← Data Transfer Objects
│   │   ├── entity/            ← JPA entities
│   │   ├── repository/        ← Data access layer
│   │   └── service/           ← Business logic
│   └── pom.xml               ← Maven dependencies
│
└── frontend/                   ← React + Vite + Tailwind
    ├── src/
    │   ├── api/               ← API integration
    │   ├── pages/             ← Page components
    │   ├── components/        ← Reusable components
    │   └── App.jsx
    └── package.json          ← NPM dependencies
```

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Authentication**: JWT (JSON Web Tokens)
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Icons**: Lucide React

## Common Issues & Solutions

### Issue: Backend fails to start
```bash
# Solution: Make sure PostgreSQL is running and database is created
# Check database connection in application.properties
```

### Issue: CORS error when calling API from frontend
```
# Solution: CORS is already configured for http://localhost:5173
# If using different port, update SecurityConfig.java
```

### Issue: "Unknown database 'digiqual_db'"
```bash
# Solution: Create the database manually
psql -U postgres -c "CREATE DATABASE digiqual_db;"
```

## Next Steps

1. ✅ **Login Page Complete** - Modern, responsive UI for all 3 roles
2. 📋 **TODO**: Test authentication with actual database users
3. 📋 **TODO**: Build Admin Dashboard
4. 📋 **TODO**: Build Partner Institute Dashboard
5. 📋 **TODO**: Build Student Portal
6. 📋 **TODO**: Certificate System & PDF generation

## Development Notes

- **Hot Reload**: Both frontend (Vite) and backend (Spring Boot Dev Tools) support hot reload
- **API Base URL**: Frontend calls `http://localhost:8080/api`
- **JWT Token**: Stored in localStorage as `authToken`
- **Token Expiration**: 24 hours (configurable in `application.properties`)

## Support & Questions

For any issues or questions, refer to:
- Backend logs: Check console output
- Frontend console: Open browser DevTools (F12)
- Database logs: Check PostgreSQL logs

---

**© 2024 DIGIQUAL Ltd. | Building Excellence in Education**
