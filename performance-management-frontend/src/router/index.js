import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import http from '@/api/http.js'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const isPublic = (path) => path === '/login'

router.beforeEach(async (to) => {
  const token = localStorage.getItem('token')
  if (!token && !isPublic(to.path)) {
    return { path: '/login' }
  }
  if (token && isPublic(to.path)) {
    return { path: '/home' }
  }
  if (token && to.path === '/home') {
    try {
      await http.get('/api/auth/me')
    } catch (err) {
      return { path: '/login' }
    }
  }
  return true
})

export default router
