const webpack = require('webpack');

module.exports = {
    entry: './app/js/jquery.feedback.js',
    output: {
        path: './dist',
        filename: 'jquery.feedback.min.js'
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            },
            output: {
                comments: false
            }
        })
    ],
    module: {
        loaders: [
            {test: /\.handlebars$/, loader: "handlebars-loader"}
        ]
    }
};