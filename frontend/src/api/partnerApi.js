import { createServiceClient, resolveServiceBaseUrl } from './serviceClient'

const partnerApi = createServiceClient(
  resolveServiceBaseUrl(import.meta.env.VITE_PARTNER_API_BASE_URL, 'http://localhost:8093/api')
)

export async function getPartnerDashboard() {
  const response = await partnerApi.get('/partner/dashboard')
  return response.data
}

export async function createBatch(payload) {
  await partnerApi.post('/partner/batches', payload)
}

export async function submitBatch(batchId) {
  await partnerApi.post(`/partner/batches/${batchId}/submit`)
}

export async function createEnrollment(payload) {
  await partnerApi.post('/partner/enrollments', payload)
}

export async function updateEnrollment(enrollmentId, payload) {
  await partnerApi.patch(`/partner/enrollments/${enrollmentId}`, payload)
}

export async function deleteEnrollment(enrollmentId) {
  await partnerApi.delete(`/partner/enrollments/${enrollmentId}`)
}

export async function deleteBatch(batchId) {
  await partnerApi.delete(`/partner/batches/${batchId}`)
}
