const path = require('path')
const serverData = require('./src/common/lib/function.js')

module.exports = {
    devServer: {
        https: true,
        port: 8083,
        open: true,
        proxy: {
            '/plugins': {
                target: serverData.getServerURL(),
                changeOrigin: true,
                // logLevel: "debug",
            },
            '/api/v4': {
                target: serverData.getServerURL(),
                changeOrigin: true,
                // logLevel: "debug",
            },
            '/api/v1': {
                target: serverData.getMbotcURL(),
                changeOrigin: true,
                // logLevel: "debug",
            },
        },
        headers: {
            Connection: 'keep-alive'
        },
        historyApiFallback: true,
        hot: true,
        contentBase: path.join(__dirname,'')
    },
    lintOnSave: false, 

}