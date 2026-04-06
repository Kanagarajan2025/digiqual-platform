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
- [x] Login Page (All roles — Super Admin, Partner, Student)
- [ ] Super Admin Dashboard
- [ ] Partner Institute Dashboard
- [ ] Student Portal
- [ ] Certificate Issuance & PDF Generation
- [ ] Public Certificate Verification Page

## Project Structure
```
digiqual-platform/
├── backend/          ← Spring Boot 3 + Java 17
└── frontend/         ← React 18 + Vite + Tailwind CSS
```

---
© 2024 DIGIQUAL Ltd. United Kingdom.