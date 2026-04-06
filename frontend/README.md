# Frontend Project Structure

## Directory Structure
```
frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── App.jsx
│   └── main.jsx
├── public/
├── index.html
└── package.json
```

## Running the Frontend
1. Make sure you have [Node.js](https://nodejs.org/) installed.
2. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ## Prerequisites
   - Node.js 16+ and npm/yarn
 
   ## Installation & Setup
   ```bash
   cd frontend
   npm install
   ```
 
   ## Development
   ```bash
   npm run dev
   # Opens at http://localhost:5173
   ```
 
   ## Build for Production
   ```bash
   npm run build
   # Creates optimized build in dist/
   ```
 
   ## Project Structure
   ```
   frontend/
   ├── src/
   │   ├── components/       ← Reusable React components
   │   ├── pages/            ← Page components
   │   ├── api/              ← API integration
   │   ├── App.jsx           ← Main app component
   │   ├── main.jsx          ← App entry point
   │   └── index.css         ← Global styles
   ├── index.html            ← HTML entry point
   ├── vite.config.js        ← Vite configuration
   └── tailwind.config.js    ← Tailwind configuration
   ```
 
   ## Features
   - Modern, responsive login page
   - Support for 3 roles: Student, Partner, Admin
   - JWT-based authentication
   - Beautiful gradient UI with glassmorphism

- Tailwind CSS