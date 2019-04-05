export default {
  data () {
    return {
      selected: []
    }
  },
  template: [[${template}]],
  methods: {
    onSelect: function (items) {
      this.selected = items
    }
  }
}