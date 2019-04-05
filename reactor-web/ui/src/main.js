// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App'
// http method
import VueResource from 'vue-resource'

// router setup
import routes from './routes/routes'

// Plugins
import GlobalComponents from './globalComponents'
import GlobalDirectives from './globalDirectives'
import Notifications from './components/NotificationPlugin'

// MaterialDashboard plugin
import MaterialDashboard from './material-dashboard'

import Chartist from 'chartist'

import HttpVueLoader from 'http-vue-loader'

// const DashboardTxt = import(/* webpackIgnore: true */ '/ar4k/dashboard.js')
// const TerminalTxt = import(/* webpackIgnore: true */ '/ar4k/terminal.js')
// import Axios from './http-common.js'

import JQuery from 'jquery'
// import VueTerminal from 'vue-terminal'

import VueQriously from 'vue-qriously'

window.jQuery = JQuery
window.$ = JQuery
// window.vueTerminal = VueTerminal

Vue.use(VueRouter)
Vue.use(MaterialDashboard)
Vue.use(GlobalComponents)
Vue.use(GlobalDirectives)
Vue.use(Notifications)
Vue.use(VueResource)
Vue.use(HttpVueLoader)
Vue.use(VueQriously)

Vue.http.options.emulateJSON = false
// window.vueDashboard = Vue.component('Dashboard', DashboardTxt)
// window.vueTerminal = Vue.component('Terminal', TerminalTxt)
// configure router
const router = new VueRouter({
  routes, // short for routes: routes
  linkExactActiveClass: 'nav-item active'
})

// global library setup
Object.defineProperty(Vue.prototype, '$Chartist', {
  get () {
    return this.$root.Chartist
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  data: {
    Chartist: Chartist
  }
})
