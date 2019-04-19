<template>
  <div>
    <vue-terminal :task-list="taskList" :command-list="commandList"
      style="width:90%;margin:0 auto"></vue-terminal>
  </div>
</template>

<script>
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
  created () {
  },
  beforeDestroy () {
  },
  methods: {
    onCommand (data, resolve, reject) {
      // typed command is available in data.text
      // don't forget to resolve or reject the Promise
      window.jQuery.post('/ar4k/cmd', data.text, function (dataQuery, status) {
        if (status === 'success') {
          resolve({ text: dataQuery })
        } else {
          reject(status)
        }
      })
    }
  }
}
</script>
