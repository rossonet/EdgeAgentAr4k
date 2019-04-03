// const path = require('path')
// vue.config.js
module.exports = {
  lintOnSave: true,
  runtimeCompiler: true,
  // publicPath: process.env.NODE_ENV === 'production' ? '/static/' : '/',
  publicPath: '/static/',

  /*
  chainWebpack: config => {
    if (config.plugins.has('extract-css')) {
      const extractCSSPlugin = config.plugin('extract-css')
      extractCSSPlugin && extractCSSPlugin.tap(() => [{
        filename: '[name].css',
        chunkFilename: '[name].css'
      }])
    }
  },
  configureWebpack: {
    output: { filename: '[name].js', chunkFilename: '[name].js' }
  }
  */
  /*
  configureWebpack: {
    resolve: {
      alias: {
        Components: path.resolve(__dirname, 'src/components/'),
        Pages: path.resolve(__dirname, 'src/pages/'),
        Src: path.resolve(__dirname, 'src/'),
        Axios: path.resolve(__dirname, 'axios')
      }
    }
  },
  */
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
