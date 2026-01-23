import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/Index.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/games',
        name: 'Games',
        component: () => import('@/views/Games.vue'),
        meta: { title: '游戏中心' }
      },
      {
        path: '/game/:id',
        name: 'GameDetail',
        component: () => import('@/views/GameDetail.vue'),
        meta: { title: '游戏详情' }
      },
      {
        path: '/news',
        name: 'News',
        component: () => import('@/views/News.vue'),
        meta: { title: '游戏资讯' }
      },
      {
        path: '/forum',
        name: 'Forum',
        component: () => import('@/views/Forum.vue'),
        meta: { title: '论坛社区' }
      },
      {
        path: '/chat',
        name: 'Chat',
        component: () => import('@/views/Chat.vue'),
        meta: { title: '聊天室' }
      },
      {
        path: '/user/profile',
        name: 'UserProfile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/admin',
    component: () => import('@/layout/Admin.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '管理后台' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'games',
        name: 'AdminGames',
        component: () => import('@/views/admin/Games.vue'),
        meta: { title: '游戏管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '游戏社区交流平台'
  
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && userInfo.role !== 2) {
    next('/')
  } else {
    next()
  }
})

export default router
