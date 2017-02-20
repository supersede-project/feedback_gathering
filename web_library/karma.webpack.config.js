var webpackConfig = require("./webpack.config.js");
var testArgs = require('yargs').argv;
var resolve = webpackConfig.resolve,
    loaders = webpackConfig.module.loaders,
    plugins = webpackConfig.plugins,
    getPostLoaders = function() {
        return testArgs.noCoverage ? [] : [

        ];
    };

module.exports = {
    module: {
        loaders: loaders,
        postLoaders: getPostLoaders()
    },
    resolve: resolve,
    plugins: plugins
};