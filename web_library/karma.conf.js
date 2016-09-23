// Karma configuration
// Generated on Mon May 09 2016 22:12:57 GMT+0200 (CEST)

module.exports = function (config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine-jquery', 'jasmine', 'requirejs'],

        // list of files / patterns to load in the browser
        files: [
            'test-main.js',
            { pattern: 'node_modules/handlebars/dist/*.js', included: false },
            { pattern: 'node_modules/i18next/*.js', included: false },
            { pattern: 'app/templates/*.handlebars', included: false, watched: true, served: true },
            { pattern: 'app/templates/*.handlebars.js', included: false, watched: true, served: true },
            { pattern: 'app/**/*.js', included: false },
            { pattern: 'app/services/mocks/**/*.json', included: false }
        ],

        proxies: {
            '/app/js/lib/html2canvas.js': '/base/app/js/lib/html2canvas.js',
            '/app/js/lib/fabric.js': '/base/app/js/lib/fabric.js',
            '/app/js/lib/spectrum.js': '/base/app/js/lib/spectrum.js',
            '/app/js/lib/customiseControls.js': '/base/app/js/lib/customiseControls.js',
            '/app/js/lib/jquery.star-rating-svg.js': '/base/app/js/lib/jquery.star-rating-svg.js',
            '/app/templates/feedback_dialog.handlebars': '/base/app/templates/feedback_dialog.handlebars',
            '/app/templates/intermediate_dialog.handlebars': '/base/app/templates/intermediate_dialog.handlebars'
        },

        // list of files to exclude
        exclude: [
        ],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
            'app/**/!(*spec|html2canvas|jquery\.star-rating-svg|spectrum|fabric|customiseControl|Fr.voice|libmp3lame.min|mp3Worker|recorder|recorderWorker).js': ['coverage'],
            'app/templates/*.handlebars': ['handlebars']
        },

        handlebarsPreprocessor: {
            templates: "Handlebars.templates",
            amd: true
        },

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['mocha', 'coverage'],


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['Chrome', 'PhantomJS2'],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false,

        // Concurrency level
        // how many browser should be started simultaneous
        concurrency: Infinity,


        coverageReporter: {
            dir: 'coverage/',
            reporters: [
                { type: 'text-summary' },
                { type: 'json', subdir: '.', file: 'coverage-final.json' },
                { type: 'html' }
            ]
        }
    })
};
