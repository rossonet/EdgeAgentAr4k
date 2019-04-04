window.jQuery = import('/webjars/jquery/dist/jquery.min.js');
const NavTabsEnviroment = () => import('/ar4k/dashtable/env');
const NavTabsConfiguration = () => import('/ar4k/dashtable/conf');
const NavTabsWebMapping = () => import('/ar4k/dashtable/web');
const NavTabsLogger = () => import('/ar4k/dashtable/logger');
const NavTabsMetrics = () => import('/ar4k/dashtable/metrics');
const NavTabsKeyStores = () => import('/ar4k/dashtable/keystores');
const NavTabsBlockChain = () => import('/ar4k/dashtable/blockchain');

export default {
  data () {
    return {
      poll: null
    }
  },
  template: [[${template}]],
  components: { NavTabsEnviroment,NavTabsConfiguration,NavTabsWebMapping,NavTabsLogger,NavTabsMetrics,NavTabsKeyStores,NavTabsBlockChain },
  /*
   * components: { StatsCard, ChartCard, NavTabsCard, NavTabsTable, OrderedTable },
   */
  created () {
    this.poll = setInterval(function() {
      jQuery.ajax({
        url : "/control/health",
        dataType : "json",
        success : function(data) {
          $("#hw_ram").html(Math.round((data.details.ar4k.details['hardware-runtime-total-memory']-data.details.ar4k.details['hardware-runtime-free-memory'])/(1024*1024))+" / "+Math.round(data.details.ar4k.details['hardware-runtime-total-memory']/(1024*1024))+"<small> MB</small>");
          $("#hw_status").html(data.status);
          $("#hw_disk").html(parseFloat((data.details.diskSpace.details['total']-data.details.diskSpace.details['free'])/(1024*1024*1024)).toFixed(2)+" / "+parseFloat(data.details.diskSpace.details['total']/(1024*1024*1024)).toFixed(2)+"<small> GB</small>");
          $("#hw_cpu").html(parseFloat(100*data.details.ar4k.details['system-load-average']/(data.details.ar4k.details['pi-Processor'])).toFixed(1)+"<small> %</small><br/><small>"+data.details.ar4k.details['pi-CPU-Model-Name']+"</small>");
          $("#ar4k_net_hostname").html(data.details.ar4k.details['operating-system'].networkParams['hostName']);
          $("#ar4k_net_ip").html(data.details.ar4k.details['pi-IP-Addresses-0']+"  "+(data.details.ar4k.details['pi-IP-Addresses-1']||'')+"  "+(data.details.ar4k.details['pi-IP-Addresses-2']||'')+"  "+(data.details.ar4k.details['pi-IP-Addresses-3']||''));
          $("#ar4k_net_dns").html(data.details.ar4k.details['operating-system'].networkParams['dnsServers'].join(" "));
          $("#ar4k_net_domain").html("<small>"+data.details.ar4k.details['operating-system'].networkParams['domainName']+"</small>");
          $("#ar4k_net_gw").html(data.details.ar4k.details['operating-system'].networkParams['ipv4DefaultGateway']);
        }
      });
      jQuery.ajax({
        url : "/ar4k/status",
        dataType : "json",
        success : function(data) {
          $("#ar4k_config_label").html(data['run-config'].name+"<br/><small>"+data['run-config'].description+"</small>");
          $("#ar4k_runlevel").html(data['run-level']);
          $("#ar4k_services_count").html(data['run-services'].length);
        }
      });
      jQuery.ajax({
        url : "/control/beans",
        dataType : "json",
        success : function(data) {
          $("#ar4k_beans_count").html(Object.keys(data.contexts.application.beans).length);
        }
      });
      jQuery.ajax({
        url : "/control/threaddump",
        dataType : "json",
        success : function(data) {
          $("#ar4k_threads_count").html(data.threads.length);
        }
      });
      jQuery.ajax({
        url : "/control/info",
        dataType : "json",
        success : function(data) {
          if (typeof data.git !== "undefined"){
          $("#ar4k_build").html(data.app.name+" "+(data.git.build.version||'dev')+" ("+(data.git.build.time||'no git info')+")<br/><small>"+data.app.description+"</small");
          $("#ar4k_builder").html((data.git.build.host||'')+"<br/><small>"+(data.git.build.user.name||'')+"<br/>("+(data.git.build.user.email||'')+")</small>");
          $("#ar4k_commit").html("<small>"+(data.git.commit.time||'now')+"<br/>"+(data.git.commit.message['short']||'')+"</small><br/>"+(data.git.commit.id['abbrev']||''));
          $("#ar4k_author").html((data.git.commit.user.name||'you'));
          }
        }
      });
    }, 4200);
  },
  beforeDestroy () {
    clearInterval(this.poll)
  },
  methods: {
   
  }
}