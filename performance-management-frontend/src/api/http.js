import axios from 'axios'

const http = axios.create({
  baseURL: '/',
  timeout: 10000
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (resp) => {
    if (resp?.data?.code && resp.data.code !== 200) {
      ElMessage.error(resp.data.message || '请求失败')
      return Promise.reject(resp)
    }
    return resp.data
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('refreshToken')
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    const message = error?.response?.data?.message || '网络错误，请稍后再试'
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default http
