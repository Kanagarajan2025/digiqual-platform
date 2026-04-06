# DIGIQUAL Platform

A centralised management system for DIGIQUAL — a UK-based awarding body providing syllabus and accreditation through partner institutes.

## Tech Stack
- **Frontend:** React 18 + Vite + Tailwind CSS
- **Backend:** Java 17 + Spring Boot 3 + Spring Security + JWT
- **Database:** PostgreSQL

## Roles
- 🛡️ Super Admin — full platform control
- 🏫 Partner Institute — manage batches & students
- 🎓 Student — access courses & certificate

## Getting Started

### Backend
```bash
cd backend
# update application.properties with your PostgreSQL credentials
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# open http://localhost:5173
```

## Project Structure
```
digiqual-platform/
├── backend/          ← Spring Boot 3 + Java 17
└── frontend/         ← React 18 + Vite + Tailwind CSS
```

## Features (MVP)
- [x] Login Page — Super Admin / Partner / Student
- [ ] Super Admin Dashboard
- [ ] Partner Institute Dashboard
- [ ] Student Portal
- [ ] Certificate Issuance & Verification
- [ ] Public Certificate Verification Page