import DashboardLayout from '@/pages/Layout/DashboardLayout.vue'
// import Dashboard from '@/pages/Dashboard.vue'
import UserProfile from '@/pages/UserProfile.vue'
import TableList from '@/pages/TableList.vue'
import Typography from '@/pages/Typography.vue'
import Icons from '@/pages/Icons.vue'
import Maps from '@/pages/Maps.vue'
import Notifications from '@/pages/Notifications.vue'
const Dashboard = () => import(/* webpackIgnore: true */'/ar4k/dashboard.vue')
const Terminal = () => import(/* webpackIgnore: true */'/ar4k/terminal.vue')
const Swagger = () => import(/* webpackIgnore: true */'/ar4k/swagger.vue')
// console.log(httpVueLoader)
// import httpVueLoader from 'http-vue-loader'
// const routesImport = () => import(/* webpackIgnore: true */ '/ar4k/routes.js').then(m => m.default)

// generare in automatico
// const routes = new VueRouter(routesImport)

const routes = [
  {
    path: '/',
    component: DashboardLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'terminal',
        name: 'Terminal',
        component: Terminal,
        meta: {
          hideFooter: true
        }
      },
      {
        path: 'swagger',
        name: 'Swagger',
        component: Swagger,
        meta: {
          hideFooter: true
        }
      },
      {
        path: 'user',
        name: 'User Profile',
        component: UserProfile
      },
      {
        path: 'table',
        name: 'Table List',
        component: TableList
      },
      {
        path: 'typography',
        name: 'Typography',
        component: Typography
      },
      {
        path: 'icons',
        name: 'Icons',
        component: Icons
      },
      {
        path: 'maps',
        name: 'Maps',
        meta: {
          hideFooter: true
        },
        component: Maps

      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: Notifications
      }
    ]
  }
]

export default routes
