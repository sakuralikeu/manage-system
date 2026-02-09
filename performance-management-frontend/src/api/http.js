import axios from 'axios'

const http = axios.create({
  baseURL: '/',
  timeout: 10000
})

http.interceptors.response.use(
  (resp) => resp,
  (error) => Promise.reject(error)
)

export default http
