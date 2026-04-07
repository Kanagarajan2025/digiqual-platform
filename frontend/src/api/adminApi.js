import { createServiceClient, resolveServiceBaseUrl } from './serviceClient'

const adminApi = createServiceClient(
  resolveServiceBaseUrl(import.meta.env.VITE_ADMIN_API_BASE_URL, 'http://localhost:8092/api')
)

export async function getAdminDashboard() {
  const response = await adminApi.get('/admin/dashboard')
  return response.data
}

export async function approveBatch(batchId) {
  await adminApi.post(`/admin/batches/${batchId}/approve`)
}

export async function returnBatch(batchId, reason) {
  await adminApi.post(`/admin/batches/${batchId}/return`, { reason })
}

export async function approvePartner(partnerId) {
  await adminApi.post(`/admin/partners/${partnerId}/approve`)
}

export async function suspendPartner(partnerId) {
  await adminApi.post(`/admin/partners/${partnerId}/suspend`)
}

export async function updateCertificateStage(certificateCode, stage) {
  await adminApi.patch(`/admin/certificates/${certificateCode}/stage`, { stage })
}

export async function approveStudent(enrollmentId) {
  await adminApi.post(`/admin/students/${enrollmentId}/approve`)
}

export async function rejectStudent(enrollmentId) {
  await adminApi.post(`/admin/students/${enrollmentId}/reject`)
}
