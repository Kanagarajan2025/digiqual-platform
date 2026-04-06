import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

const authApi = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add token to requests if it exists
authApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

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
