window.jQuery = import('/webjars/jquery/dist/jquery.min.js');

export default {
  data () {
    return {
      intro:"Type help to list the available commands\n\n"
    }
  },
  name: 'ar4k-terminal',
   components: { VueTerminal: window.vueTerminal },
  template: [[${template}]],
  created () {
    
  },
  beforeDestroy () {
  },
  methods: {
    onCliCommand(data, resolve, reject){
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