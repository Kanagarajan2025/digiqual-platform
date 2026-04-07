import { Navigate } from 'react-router-dom'
import { getStoredUser } from '../utils/auth'

export default function ProtectedRoute({ children, allowedRole }) {
  const user = getStoredUser()
  const fallbackRoute = user.role ? `/${user.role.toLowerCase()}-dashboard` : '/'

  if (!user.token) {
    return <Navigate to="/" replace />
  }

  if (allowedRole && user.role !== allowedRole) {
    return <Navigate to={fallbackRoute} replace />
  }

  return children
}