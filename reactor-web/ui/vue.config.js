// vue.config.js
module.exports = {
  lintOnSave: true,
  baseUrl: process.env.NODE_ENV === 'production' ? '/static/' : '/'
}
