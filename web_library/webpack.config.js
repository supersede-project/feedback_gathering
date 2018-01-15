const webpack = require('webpack');
const dev = true;
const NODE_ENV = process.env.NODE_ENV || 'development';
var plugins = [];
var ExtractTextPlugin = require("extract-text-webpack-plugin");


if (!dev) {
    plugins.push(new webpack.optimize.UglifyJsPlugin({
        compress: {
            warnings: false
        },
        output: {
            comments: false
        }
    }));
}

module.exports = [
  {
    entry: './app/js/jquery.feedback.js',
    output: {
        path: './dist',
        filename: 'jquery.feedback.min.js'
    },
    plugins: plugins,
    module: {
        loaders: [
            {test: /\.handlebars$/, loader: "handlebars-loader"},
            {test: /\.css$/, loader: "style-loader!css-loader"},
            {test: /\.json$/, loader: 'json', include: "app"}
        ]
    },
    resolve: {
        alias: {
            handlebars: 'handlebars/dist/handlebars.amd.min.js'
        }
    }
},{

        entry: './f2fcentral/src/index.js',
        output: {
            path: './dist',
            filename: 'f2fcentral.min.js'
        },
        resolve: {
            extensions: ['','.js', '.jsx']
        },
        plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify(NODE_ENV),
                'REACT_APP_BASE_URL': JSON.stringify("http://mt.ronnieschaniel.com:8080/feedback_repository/feedback_repository/")
            }
        })
    ],
        module: {
            loaders: [
                {
                    test: /\.js?$/,
                    exclude: /node_modules\/(?!react-tabs)/,
                    loader: 'babel-loader',
                    query: {
                        presets: ['es2015', 'react']
                    }
                },
                {
                    test: /\.css$/,
                    include: /node_modules/,
                    loader: 'style!css'

                },
                {
                    test: /\.css$/,
                    exclude: /node_modules/,
                    loader: 'style!css?modules&localIdentName=[name]---[local]---[hash:base64:5]'
                },
                {
                    test: /\.svg$/,
                    loader: 'svg-inline-loader'
                }
            ]
        }
    }];
