<template>
  <div class="content">
    <div class="md-layout">
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
    </div>
    <!-- { NavTabsEnviroment,NavTabsConfiguration,NavTabsWebMapping,NavTabsLogger,NavTabsMetrics,NavTabsKeyStores,NavTabsBlockChain } -->
    <div
      class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100">
      <nav-tabs-card> <template slot="content"
        md-alignment="centered"> <md-tabs md-sync-route
        class="md-warning" md-alignment="centered"> <md-tab
        id="tab-environment" md-label="Enviroment" md-icon="label_outline">
      <div id="nav-tabs-enviroment"></div> </md-tab> <md-tab
        id="tab-configurations" md-label="Configurations" md-icon="tune">
      <div id="nav-tabs-configuration"></div> </md-tab> <md-tab
        id="tab-web-mappings" md-label="Web Mappings" md-icon="call_split">
      <div id="nav-tabs-web-mapping"></div> </md-tab> <md-tab
        id="tab-loggers" md-label="Loggers" md-icon="hearing"> <div id="nav-tabs-logger"></div>
      </md-tab> <md-tab id="tab-metrics" md-label="Metrics" md-icon="av_timer">
      <div id="nav-tabs-metrics"></div> </md-tab> <md-tab id="tab-keystores"
        md-label="Keystores" md-icon="vpn_key"> <div id="nav-tabs-key-stores"></div>
      </md-tab> <md-tab id="tab-blockchains" md-label="Blockchains"
        md-icon="monetization_on"> <div id="nav-tabs-block-chain"></div>
      </md-tab> </md-tabs> </template> </nav-tabs-card>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      poll: null
    }
  },
  created () {
    this.poll = setInterval(function () {
      window.jQuery.ajax({
        url: '/ar4k/dashtable/env.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-enviroment').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/conf.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-configuration').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/web.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-web-mapping').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/logger.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-logger').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/metrics.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-metrics').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/blockchain.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-block-chain').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/ar4k/dashtable/keystores.html',
        dataType: 'html',
        success: function (data) {
          $('#nav-tabs-key-stores').html(data)
        }
      }),
      window.jQuery.ajax({
        url: '/control/health',
        dataType: 'json',
        success: function (data) {
          $('#hw_ram').html(Math.round((data.details.ar4k.details['hardware-runtime-total-memory'] - data.details.ar4k.details['hardware-runtime-free-memory']) / (1024 * 1024)) + ' / ' + Math.round(data.details.ar4k.details['hardware-runtime-total-memory'] / (1024 * 1024)) + '<small> MB</small>')
          $('#hw_status').html(data.status)
          $('#hw_disk').html(parseFloat((data.details.diskSpace.details['total'] - data.details.diskSpace.details['free']) / (1024 * 1024 * 1024)).toFixed(2) + ' / ' + parseFloat(data.details.diskSpace.details['total'] / (1024 * 1024 * 1024)).toFixed(2) + '<small> GB</small>')
          $('#hw_cpu').html(parseFloat(100 * data.details.ar4k.details['system-load-average'] / (data.details.ar4k.details['pi-Processor'])).toFixed(1) + '<small> %</small><br/><small>' + data.details.ar4k.details['pi-CPU-Model-Name'] + '</small>')
          $('#ar4k_net_hostname').html(data.details.ar4k.details['operating-system'].networkParams['hostName'])
          $('#ar4k_net_ip').html(data.details.ar4k.details['pi-IP-Addresses-0'] + '  ' + (data.details.ar4k.details['pi-IP-Addresses-1'] || '') + '  ' + (data.details.ar4k.details['pi-IP-Addresses-2'] || '') + '  ' + (data.details.ar4k.details['pi-IP-Addresses-3'] || ''))
          $('#ar4k_net_dns').html(data.details.ar4k.details['operating-system'].networkParams['dnsServers'].join(' '))
          $('#ar4k_net_domain').html('<small>' + data.details.ar4k.details['operating-system'].networkParams['domainName'] + '</small>')
          $('#ar4k_net_gw').html(data.details.ar4k.details['operating-system'].networkParams['ipv4DefaultGateway'])
        }
      })
      window.jQuery.ajax({
        url: '/ar4k/status',
        dataType: 'json',
        success: function (data) {
          $('#ar4k_config_label').html(data['run-config'].name + '<br/><small>' + data['run-config'].description + '</small>')
          $('#ar4k_runlevel').html(data['run-level'])
          $('#ar4k_services_count').html(data['run-services'].length)
        }
      })
      window.jQuery.ajax({
        url: '/control/beans',
        dataType: 'json',
        success: function (data) {
          $('#ar4k_beans_count').html(Object.keys(data.contexts.application.beans).length)
        }
      })
      window.jQuery.ajax({
        url: '/control/threaddump',
        dataType: 'json',
        success: function (data) {
          $('#ar4k_threads_count').html(data.threads.length)
        }
      })
      window.jQuery.ajax({
        url: '/control/info',
        dataType: 'json',
        success: function (data) {
          if (typeof data.git !== 'undefined') {
            $('#ar4k_build').html(data.app.name + ' ' + (data.git.build.version || 'dev') + ' (' + (data.git.build.time || 'no git info') + ')<br/><small>' + data.app.description + '</small')
            $('#ar4k_builder').html((data.git.build.host || '') + '<br/><small>' + (data.git.build.user.name || '') + '<br/>(' + (data.git.build.user.email || '') + ')</small>')
            $('#ar4k_commit').html('<small>' + (data.git.commit.time || 'now') + '<br/>' + (data.git.commit.message['short'] || '') + '</small><br/>' + (data.git.commit.id['abbrev'] || ''))
            $('#ar4k_author').html((data.git.commit.user.name || 'you'))
          }
        }
      })
    }, 4200)
  },
  beforeDestroy () {
    clearInterval(this.poll)
  },
  methods: {
  }
}
</script>
