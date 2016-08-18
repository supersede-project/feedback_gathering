const webpack = require('webpack');
const dev = false;
var plugins = [];

if(!dev) {
    plugins.push(new webpack.optimize.UglifyJsPlugin({
        compress: {
            warnings: false
        },
        output: {
            comments: false
        }
    }));
}

module.exports = {
    entry: './app/js/jquery.feedback.js',
    output: {
        path: './dist',
        filename: 'jquery.feedback.min.js'
    },
    plugins: plugins,
    module: {
        loaders: [
            { test: /\.handlebars$/, loader: "handlebars-loader" },
            { test: /\.css$/, loader: "style-loader!css-loader" },
            { test: /\.json$/, loader: 'json', include: "app"}
        ]
    },
    resolve: {
        alias: {
            handlebars: 'handlebars/dist/handlebars.amd.min.js'
        }
    }
};