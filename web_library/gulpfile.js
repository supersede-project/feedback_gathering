"use strict";
var gulp = require('gulp');
var utils_1 = require('./tools/utils');
gulp.task('clean', utils_1.task('clean', 'all'));
gulp.task('clean.dist', utils_1.task('clean', 'dist'));
gulp.task('clean.test', utils_1.task('clean', 'test'));
gulp.task('clean.tmp', utils_1.task('clean', 'tmp'));
gulp.task('build.dev', function (done) {
    return utils_1.runSequence('clean.dist', 'tslint', 'build.sass.dev', 'build.js.dev', done);
});
//# sourceMappingURL=gulpfile.js.map