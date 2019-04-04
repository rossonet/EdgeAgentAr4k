export default {
  data () {
    return {
      selected: []
    }
  },
  template: [[${template}]],
  /*
   * components: { StatsCard, ChartCard, NavTabsCard, NavTabsTable, OrderedTable },
   */
  created () {
  },
  beforeDestroy () {
  },
  methods: {
    onSelect: function (items) {
      this.selected = items
    }
  }
}