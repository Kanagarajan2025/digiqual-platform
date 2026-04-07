import { createServiceClient, resolveServiceBaseUrl } from './serviceClient'

const studentApi = createServiceClient(
  resolveServiceBaseUrl(import.meta.env.VITE_STUDENT_API_BASE_URL, 'http://localhost:8094/api')
)

export async function getMyStudentProfile() {
  const response = await studentApi.get('/student/me')
  return response.data
}

export async function getStudentDashboard() {
  const response = await studentApi.get('/student/dashboard')
  return response.data
}

export async function verifyCertificate(studentId, surname, dob) {
  const response = await studentApi.get('/student/public/verify', {
    params: { studentId, surname, dob },
  })
  return response.data
}
