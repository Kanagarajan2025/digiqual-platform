# DIGIQUAL Platform

A centralised management system for **DIGIQUAL** — a UK-based awarding body providing syllabus and accreditation through partner institutes.

## Tech Stack
- **Frontend:** React 18 + Vite + Tailwind CSS
- **Backend:** Java 17 + Spring Boot 3 + Spring Security + JWT
- **Database:** PostgreSQL

## Roles
| Role | Access |
|------|--------|
| Super Admin | Full platform control — partners, students, certificates |
| Partner Institute | Manage batches, enrol students, submit for approval |
| Student | View profile, access course materials, download certificate |

## Getting Started

### Backend
```bash
cd backend
# Update src/main/resources/application.properties with your DB credentials
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# Opens at http://localhost:5173
```

## Feature Progress
	[x] ✅ Login Page (Beautiful, Modern UI - All 3 roles)
- [ ] Super Admin Dashboard
- [ ] Partner Institute Dashboard
- [ ] Student Portal
- [ ] Certificate Issuance & PDF Generation
- [ ] Public Certificate Verification Page

## 🚀 Quick Start

### 1. Setup Database
\`\`\`bash
createdb digiqual_db  # PostgreSQL
\`\`\`

### 2. Start Backend
\`\`\`bash
cd backend
mvn spring-boot:run
\`\`\`

### 3. Start Frontend (New Terminal)
\`\`\`bash
cd frontend
npm install
npm run dev
\`\`\`

### 4. Login with Demo Credentials
**Admin**: admin@digiqual.com / Admin@123
**Partner**: partner@digiqual.com / Partner@123
**Student**: student@digiqual.com / Student@123

## 📖 Documentation
- [SETUP.md](SETUP.md) - Detailed setup instructions
- [IMPLEMENTATION.md](IMPLEMENTATION.md) - What was built & how it works

## Project Structure
```
digiqual-platform/
├── backend/          ← Spring Boot 3 + Java 17
└── frontend/         ← React 18 + Vite + Tailwind CSS
```

---
© 2024 DIGIQUAL Ltd. United Kingdom.