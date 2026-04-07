import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute'
import AboutPage from './pages/AboutPage'
import AdminDashboard from './pages/AdminDashboard'
import CertificateVerificationPage from './pages/CertificateVerificationPage'
import ContactPage from './pages/ContactPage'
import CourseDetailPage from './pages/CourseDetailPage'
import CoursesPage from './pages/CoursesPage'
import HomePage from './pages/HomePage'
import LoginPage from './pages/LoginPage'
import NotFoundPage from './pages/NotFoundPage'
import PartnerDashboard from './pages/PartnerDashboard'
import StudentDashboard from './pages/StudentDashboard'
import './index.css'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/courses" element={<CoursesPage />} />
        <Route path="/courses/:slug" element={<CourseDetailPage />} />
        <Route path="/contact" element={<ContactPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/admin-login" element={<LoginPage defaultRole="ADMIN" />} />
        <Route path="/student-login" element={<LoginPage defaultRole="STUDENT" />} />
        <Route path="/partner-login" element={<LoginPage defaultRole="PARTNER" />} />
        <Route path="/certificate-verification" element={<CertificateVerificationPage />} />
        <Route
          path="/student-dashboard"
          element={
            <ProtectedRoute allowedRole="STUDENT">
              <StudentDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/partner-dashboard"
          element={
            <ProtectedRoute allowedRole="PARTNER">
              <PartnerDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin-dashboard"
          element={
            <ProtectedRoute allowedRole="ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route path="/legacy-login" element={<Navigate to="/login" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
