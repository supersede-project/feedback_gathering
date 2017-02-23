"use strict";
var gulpLoadPlugins = require('gulp-load-plugins');
var plugins = gulpLoadPlugins();
var _tsProject;
function makeTsProject(options) {
    if (!_tsProject) {
        var config = Object.assign({
            typescript: require('typescript')
        }, options);
        _tsProject = plugins.typescript.createProject('tsconfig.json', config);
    }
    return _tsProject;
}
exports.makeTsProject = makeTsProject;
