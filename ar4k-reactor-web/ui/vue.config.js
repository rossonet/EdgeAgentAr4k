// const path = require('path')
// vue.config.js

module.exports = {
  lintOnSave: true,
  runtimeCompiler: true,
  // publicPath: process.env.NODE_ENV === 'production' ? '/static/' : '/',
  publicPath: '/static/',
  devServer: {
    proxy: {
      '^/control': {
        target: 'http://127.0.0.1:9005',
        // ws: true,
        changeOrigin: true
      },
      '^/images': {
        target: 'http://127.0.0.1:9005',
        // ws: true,
        changeOrigin: true
      },
      '^/ar4k': {
        target: 'http://127.0.0.1:9005',
        // ws: true,
        changeOrigin: true
      },
      '^/swagger-ui': {
        target: 'http://127.0.0.1:9005',
        // ws: true,
        changeOrigin: true
      },
      '^/webjars': {
        target: 'http://127.0.0.1:9005',
        // ws: true,
        changeOrigin: true
      }
    }
  }
}
