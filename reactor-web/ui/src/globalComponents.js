import DropDown from './components/Dropdown.vue'

import {
  StatsCard,
  // ChartCard,
  NavTabsCard
  // NavTabsTable,
  // OrderedTable
} from '@/components'

import DashboardLayout from '@/pages/Layout/DashboardLayout.vue'
// import Dashboard from '@/pages/Dashboard.vue'
import UserProfile from '@/pages/UserProfile.vue'
import TableList from '@/pages/TableList.vue'
import Typography from '@/pages/Typography.vue'
import Icons from '@/pages/Icons.vue'
import Maps from '@/pages/Maps.vue'
import Notifications from '@/pages/Notifications.vue'
import VueTerminal from 'vue-terminal-ui'

window.vueTerminal = VueTerminal

// import { AXIOS } from './http-common.js'

// const Dashboard = () => import(/* webpackIgnore: true */ '/ar4k/dashboard.js')
// const Terminal = () => import(/* webpackIgnore: true */ '/ar4k/terminal.js')

/**
 * You can register global components here and use them as a plugin in your main Vue instance
 */

const GlobalComponents = {
  install (Vue) {
    // Vue.component('vueTerminal', VueTerminal)
    Vue.component('drop-down', DropDown)
    Vue.component('statsCard', StatsCard)
    // Vue.component('chartCard', ChartCard)
    Vue.component('navTabsCard', NavTabsCard)
    // Vue.component('navTabsTable', NavTabsTable)
    // Vue.component('orderedTable', OrderedTable)
    // Vue.component('AXIOS', AXIOS)
    // Vue.component('Dashboard', Dashboard)
    // Vue.component('Terminal', Terminal)
    Vue.component('Notifications', Notifications)
    Vue.component('Maps', Maps)
    Vue.component('Icons', Icons)
    Vue.component('Typography', Typography)
    Vue.component('TableList', TableList)
    Vue.component('UserProfile', UserProfile)
    Vue.component('DashboardLayout', DashboardLayout)
  }
}

export default GlobalComponents
