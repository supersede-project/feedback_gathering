"use strict";
var path_1 = require('path');
var config_1 = require('../config');
var gulpIgnore = require('gulp-ignore');
var condition = '_*.scss';
module.exports = function buildAssetsDev(gulp, plugins) {
    return function () {
        return gulp.src([
            path_1.join(config_1.APP_SRC, '**'),
            '!' + path_1.join(config_1.APP_SRC, '**', '*.ts')
        ])
            .pipe(gulpIgnore.exclude(condition))
            .pipe(gulp.dest(config_1.APP_DEST));
    };
};
//# sourceMappingURL=build.assets.dev.js.map