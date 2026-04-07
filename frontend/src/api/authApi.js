import { createServiceClient, resolveServiceBaseUrl } from './serviceClient'

const AUTH_API_BASE_URL = resolveServiceBaseUrl(
  import.meta.env.VITE_AUTH_API_BASE_URL,
  'http://localhost:8091/api'
)

const authApi = createServiceClient(AUTH_API_BASE_URL)

export const login = async (email, password, role) => {
  try {
    const response = await authApi.post('/auth/login', {
      email,
      password,
      role,
    })
    
    if (response.data.success) {
      localStorage.setItem('authToken', response.data.token)
      localStorage.setItem('userEmail', email)
      localStorage.setItem('userRole', role)
    }
    
    return response.data
  } catch (error) {
    return {
      success: false,
      message: error.response?.data?.message || 'Login failed. Please try again.',
    }
  }
}

export const logout = () => {
  localStorage.removeItem('authToken')
  localStorage.removeItem('userEmail')
  localStorage.removeItem('userRole')
}

export default authApi
