"use strict";
var gulp = require('gulp');
var gulpLoadPlugins = require('gulp-load-plugins');
var merge = require('merge-stream');
var path_1 = require('path');
var config_1 = require('../config');
var utils_1 = require('../utils');
var plugins = gulpLoadPlugins();


module.exports = function () {
    var tsProject = utils_1.makeTsProject();
    var typings = gulp.src([
        'typings/browser.d.ts',
        config_1.TOOLS_DIR + '/manual_typings/**/*.d.ts'
    ]);
    var src = [
        path_1.join(config_1.APP_SRC, '**/*.ts'),
        '!' + path_1.join(config_1.APP_SRC, '**/*.spec.ts'),
        '!' + path_1.join(config_1.APP_SRC, '**/*.e2e.ts')
    ];
    var projectFiles = gulp.src(src); //.pipe(plugins.cached());
    var result = merge(typings, projectFiles)
        .pipe(plugins.plumber())
        .pipe(plugins.sourcemaps.init())
        .pipe(plugins.typescript(tsProject));
    return result.js
        .pipe(plugins.sourcemaps.write())
        .pipe(gulp.dest('.'));
};
