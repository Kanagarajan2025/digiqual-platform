export function getStoredUser() {
  return {
    token: localStorage.getItem('authToken'),
    email: localStorage.getItem('userEmail'),
    role: localStorage.getItem('userRole'),
  }
}

export function clearStoredUser() {
  localStorage.removeItem('authToken')
  localStorage.removeItem('userEmail')
  localStorage.removeItem('userRole')
}