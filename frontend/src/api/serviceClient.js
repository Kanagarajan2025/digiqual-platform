import axios from 'axios'

export function resolveServiceBaseUrl(serviceBaseUrl, directDefaultBaseUrl) {
  const gatewayBaseUrl = import.meta.env.VITE_API_GATEWAY_BASE_URL

  if (serviceBaseUrl) {
    return serviceBaseUrl
  }

  if (gatewayBaseUrl) {
    return gatewayBaseUrl
  }

  return directDefaultBaseUrl
}

export function createServiceClient(baseURL) {
  const client = axios.create({
    baseURL,
    headers: {
      'Content-Type': 'application/json',
    },
  })

  client.interceptors.request.use((config) => {
    const token = localStorage.getItem('authToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })

  return client
}
