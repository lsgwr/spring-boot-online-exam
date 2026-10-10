import { createRouter, createWebHistory } from 'vue-router'
import { useSession } from './session'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: () => import('./views/Login.vue'), meta: { guest: true } },
    { path: '/register', component: () => import('./views/Register.vue'), meta: { guest: true } },
    {
      path: '/',
      component: () => import('./layouts/Shell.vue'),
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', component: () => import('./views/Dashboard.vue') },
        { path: 'hall', component: () => import('./views/Hall.vue') },
        { path: 'exam/:id', component: () => import('./views/TakeExam.vue') },
        { path: 'records', component: () => import('./views/Records.vue') },
        { path: 'records/:id', component: () => import('./views/RecordDetail.vue') },
        { path: 'questions', component: () => import('./views/Questions.vue'), meta: { staff: true } },
        { path: 'papers', component: () => import('./views/Papers.vue'), meta: { staff: true } },
        { path: 'grades', component: () => import('./views/Grades.vue'), meta: { staff: true } },
        { path: 'subjects', component: () => import('./views/Subjects.vue'), meta: { admin: true } },
        { path: 'users', component: () => import('./views/Users.vue'), meta: { admin: true } }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const session = useSession()
  if (to.meta.guest) return session.token ? '/dashboard' : true
  if (!session.token) return '/login'
  if (to.meta.admin && !session.admin) return '/dashboard'
  if (to.meta.staff && !session.staff) return '/dashboard'
  return true
})

export default router
