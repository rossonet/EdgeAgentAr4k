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
  template: /*[[${template}]]*/ '<template>loading...</template>',
  /*
   * components: { StatsCard, ChartCard, NavTabsCard, NavTabsTable, OrderedTable },
   */
  created () {
  },
  beforeDestroy () {
    clearInterval(this.pollStatus)
    clearInterval(this.pollHealth)
    clearInterval(this.pollBeans)
    clearInterval(this.pollThread)
    clearInterval(this.pollInfo)
  },
  methods: {
   
  }
}