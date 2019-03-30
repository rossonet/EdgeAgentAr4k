<template>
<div class="content">
  <div class="md-layout">
    <!--
      <div class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-33">
        <chart-card
          :chart-data="dailySalesChart.data"
          :chart-options="dailySalesChart.options"
          :chart-type="'Line'"
          data-background-color="blue">
          <template slot="content">
            <h4 class="title">Daily Sales</h4>
              <p class="category">
                <span class="text-success"><i class="fas fa-long-arrow-alt-up"></i> 55% </span> increase in today sales.
              </p>
          </template>

          <template slot="footer">
            <div class="stats">
              <md-icon>access_time</md-icon>
              updated 4 minutes ago
            </div>
          </template>
        </chart-card>
      </div>
      <div class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-33">
        <chart-card
          :chart-data="emailsSubscriptionChart.data"
          :chart-options="emailsSubscriptionChart.options"
          :chart-responsive-options="emailsSubscriptionChart.responsiveOptions"
          :chart-type="'Bar'"
          data-background-color="red">
          <template slot="content">
            <h4 class="title">Email Subscription</h4>
              <p class="category">
                Last Campaign Performance
              </p>
          </template>

          <template slot="footer">
            <div class="stats">
              <md-icon>access_time</md-icon>
              updated 10 days ago
            </div>
          </template>
        </chart-card>
      </div>
      <div class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-33">
        <chart-card
          :chart-data="dataCompletedTasksChart.data"
          :chart-options="dataCompletedTasksChart.options"
          :chart-type="'Line'"
          data-background-color="green">
          <template slot="content">
            <h4 class="title">Completed Tasks</h4>
              <p class="category">
                Last Campaign Performance
              </p>
          </template>

          <template slot="footer">
            <div class="stats">
              <md-icon>access_time</md-icon>
              campaign sent 26 minutes ago
            </div>
          </template>
        </chart-card>
      </div>
      -->
    <div
      class="md-layout-item md-medium-size-50 md-xsmall-size-100 md-size-25">
      <stats-card data-background-color="green"> <template
        slot="header"> <md-icon>developer_board</md-icon> </template> <template
        slot="content">
      <p class="category">Configuration</p>
      <h6 class="title" id="ar4k_config_label">
        <small>loading...</small>
      </h6>
      <p class="category">Services</p>
      <h4 class="title" id="ar4k_services_count">
        <small>loading...</small>
      </h4>
      <p class="category">Beans</p>
      <h4 class="title" id="ar4k_beans_count">
        <small>loading...</small>
      </h4>
      <p class="category">Threads</p>
      <h4 class="title" id="ar4k_threads_count">
        <small>loading...</small>
      </h4>
      <!-- <h3 class="title">$34,245</h3> --> </template> <template slot="footer">
      <div class="md-layout md-gutter">
        <div class="md-layout-item md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/ar4k/status" style="padding: 4px;"
            class="md-button md-dense md-raised md-success"
            target="_json">JSON STATUS</a>
        </div>
        <div class="md-layout-item md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/beans" style="padding: 4px;"
            class="md-button md-dense md-raised md-success"
            target="_json">JSON BEANS</a>
        </div>
        <div class="md-layout-item md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/conditions" style="padding: 4px;"
            class="md-button md-dense md-raised md-info" target="_json">JSON
            SPRING CONDITION</a>
        </div>
        <div class="md-layout-item md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/configprops" style="padding: 4px;"
            class="md-button md-dense md-raised md-primary"
            target="_json">JSON SPRING CONFIG PROPS</a>
        </div>
      </div>
      </template> </stats-card>
    </div>
    <div
      class="md-layout-item md-medium-size-50 md-xsmall-size-100 md-size-25">
      <stats-card data-background-color="orange"> <template
        slot="header"> <md-icon>memory</md-icon> </template> <template
        slot="content">
      <h5 class="title text-danger" id="hw_status">NaN</h5>
      <p class="category">Ram Memory Usage</p>
      <h4 class="title" id="hw_ram">
        <small>loading...</small>
      </h4>
      <p class="category">Disks Usage</p>
      <h4 class="title" id="hw_disk">
        <small>loading...</small>
      </h4>
      <p class="category">CPU Usage</p>
      <h4 class="title" id="hw_cpu">
        <small>loading...</small>
      </h4>
      </template> <template slot="footer">
      <div class="md-layout md-gutter">
        <div class="md-layout-item  md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/health" style="padding: 4px;"
            class="md-button md-dense md-raised md-primary"
            target="_json">JSON HEALTH</a>
        </div>
        <div class="md-layout-item  md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/threaddump" style="padding: 4px;"
            class="md-button md-dense md-raised md-info" target="_json">JSON
            THREADS</a>
        </div>
      </div>
      </template> </stats-card>
    </div>
    <div
      class="md-layout-item md-medium-size-50 md-xsmall-size-100 md-size-25">
      <stats-card data-background-color="red"> <template
        slot="header"> <md-icon>unarchive</md-icon> </template> <template
        slot="content">
      <p class="card-category">Build</p>
      <h5 class="title" id="ar4k_build">
        <small>loading...</small>
      </h5>
      <p class="category">Builder</p>
      <h6 class="title" id="ar4k_builder">
        <small>loading...</small>
      </h6>
      <p class="category">Commit</p>
      <h5 class="title" id="ar4k_commit">
        <small>loading...</small>
      </h5>
      <p class="category">Author</p>
      <h6 class="title" id="ar4k_author">
        <small>loading...</small>
      </h6>
      </template> <template slot="footer">
      <div class="md-layout md-gutter">
        <div class="md-layout-item  md-size-100"
          style="margin: 3px; text-align: right;">
          <a href="/control/info" style="padding: 4px;"
            class="md-button md-dense md-raised md-info" target="_json">JSON
            BUILD INFO</a>
        </div>
      </div>
      </template> </stats-card>
    </div>
    <div
      class="md-layout-item md-medium-size-50 md-xsmall-size-100 md-size-25">
      <stats-card data-background-color="blue"> <template
        slot="header"> <md-icon>device_hub</md-icon></template> <template
        slot="content">
      <p class="card-category">Hostname</p>
      <h4 class="title" id="ar4k_net_hostname">
        <small>loading...</small>
      </h4>
      <p class="category">IPV4 Address</p>
      <h4 class="title" id="ar4k_net_ip">
        <small>loading...</small>
      </h4>
      <p class="category">IPV4 Gateway</p>
      <h4 class="title" id="ar4k_net_gw">
        <small>loading...</small>
      </h4>
      <p class="category">DNS</p>
      <h4 class="title" id="ar4k_net_dns">
        <small>loading...</small>
      </h4>
      <p class="category">Domain</p>
      <h4 class="title" id="ar4k_net_domain">
        <small>loading...</small>
      </h4>
      </template> </stats-card>
    </div>
    <div
      class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100">
      <nav-tabs-card> <template slot="content"
        md-alignment="centered"> <md-tabs md-sync-route
        class="md-warning" md-alignment="centered"> <md-tab
        id="tab-environment" md-label="Enviroment"
        md-icon="label_outline"> <nav-tabs-table></nav-tabs-table>
      </md-tab> <md-tab id="tab-configurations" md-label="Configurations"
        md-icon="tune"> <nav-tabs-table></nav-tabs-table> </md-tab> <md-tab
        id="tab-web-mappings" md-label="Web Mappings"
        md-icon="call_split"> <nav-tabs-table></nav-tabs-table>
      </md-tab> <md-tab id="tab-loggerss" md-label="Loggers" md-icon="hearing">
      <nav-tabs-table></nav-tabs-table> </md-tab> <md-tab id="tab-metrics"
        md-label="Metrics" md-icon="av_timer"> <nav-tabs-table></nav-tabs-table>
      </md-tab> <md-tab id="tab-keystores" md-label="Keystores" md-icon="vpn_key">
      <nav-tabs-table></nav-tabs-table> </md-tab> <md-tab id="tab-blockchains"
        md-label="Blockchains" md-icon="monetization_on"> <nav-tabs-table></nav-tabs-table>
      </md-tab> </md-tabs> </template> </nav-tabs-card>
    </div>
  </div>
</div>
</template>

<script>
import {
  StatsCard,
  ChartCard,
  NavTabsCard,
  NavTabsTable,
  OrderedTable
} from '@/components'

import { AXIOS } from './http-common.js'

export default {
  data () {
    return {
      pollStatus: null,
      pollHealth: null,
      pollBeans: null,
      pollThread: null,
      pollInfo: null
    }
  },
  components: {
    StatsCard,
    ChartCard,
    NavTabsCard,
    NavTabsTable,
    OrderedTable
  },
  created () {
    this.callStatus()
    this.callHealth()
    this.callBeans()
    this.callThread()
    this.callInfo()
  },
  beforeDestroy () {
    clearInterval(this.pollStatus)
    clearInterval(this.pollHealth)
    clearInterval(this.pollBeans)
    clearInterval(this.pollThread)
    clearInterval(this.pollInfo)
  },
  methods: {
    // Fetches posts when the component is created.
    callStatus () {
      this.pollStatus = setInterval(
        AXIOS.get(`/ar4k/status`)
          .then(response => {
            console.log(response.data)
          })
          .catch(e => {
            console.log(e)
          }), 5000)
    },
    callHealth () {
      this.pollHealth = setInterval(
        AXIOS.get(`/control/health`)
          .then(response => {
            console.log(response.data)
          })
          .catch(e => {
            console.log(e)
          }), 5000)
    },
    callBeans () {
      this.pollBeans = setInterval(
        AXIOS.get(`/control/beans`)
          .then(response => {
            console.log(response.data)
          })
          .catch(e => {
            console.log(e)
          }), 5000)
    },
    callThread () {
      this.pollThread = setInterval(
        AXIOS.get(`/control/threaddump`)
          .then(response => {
            console.log(response.data)
          })
          .catch(e => {
            console.log(e)
          }), 5000)
    },
    callInfo () {
      this.pollInfo = setInterval(
        AXIOS.get(`/control/info`)
          .then(response => {
            console.log(response.data)
          })
          .catch(e => {
            console.log(e)
          }), 5000)
    }
  }
}
</script>
