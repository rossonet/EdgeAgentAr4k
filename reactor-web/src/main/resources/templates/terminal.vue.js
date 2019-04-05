export default {
  data () {
    return {
      taskList: {
          // your tasks
      },
      commandList: {
          // your commands
      }
    }
  },
  template: [[${template}]],
  created () {
  },
  beforeDestroy () {
  },
  methods: {
    onCommand(data, resolve, reject){
      // typed command is available in data.text
      // don't forget to resolve or reject the Promise
      $.post("/ar4k/cmd",data.text, function(dataQuery, status){
        if (status==="success"){
          resolve({text:dataQuery})
        } else {
          reject(status)
        }
      })
    }
  }
}