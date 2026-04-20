import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import CategoryDetail from '../views/CategoryDetail.vue'
import AdminLoginView from '../views/admin/LoginView.vue'
import AdminDashboardView from '../views/admin/DashboardView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/category/:id',
      name: 'category-detail',
      component: CategoryDetail,
      props: true
    },
    {
      path: '/admin/login',
      name: 'admin-login',
      component: AdminLoginView,
      meta: { hideMusicPlayer: true }
    },
    {
      path: '/admin',
      name: 'admin-dashboard',
      component: AdminDashboardView,
      meta: { hideMusicPlayer: true }
    }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to) => {
  if (to.path.startsWith('/admin') && to.name !== 'admin-login') {
    const loggedIn = localStorage.getItem('portfolio_admin_logged_in') === 'true'
    if (!loggedIn) {
      return { name: 'admin-login' }
    }
  }
})

export default router
