import http from './http.js'

export const fetchProjectPage = (params) => http.get('/api/projects/page', { params })

export const fetchProjectDetail = (id) => http.get(`/api/projects/${id}`)

export const createProject = (data) => http.post('/api/projects', data)
