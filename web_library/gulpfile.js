define(["require", "exports", 'gulp', './tools/utils'], function (require, exports, gulp, utils_1) {
    "use strict";
    gulp.task('clean', utils_1.task('clean', 'all'));
    gulp.task('clean.dist', utils_1.task('clean', 'dist'));
    gulp.task('clean.test', utils_1.task('clean', 'test'));
    gulp.task('clean.tmp', utils_1.task('clean', 'tmp'));
    var exec = require('child_process').exec;
    gulp.task('build', function (done) {
        return utils_1.runSequence('tslint', 'build.sass.dev', exec('webpack'), done);
    });
});
