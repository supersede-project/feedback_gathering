"use strict";
var path_1 = require('path');
var config_1 = require('../config');
module.exports = function buildSassProd(gulp, plugins, option) {
    return function () {
        return gulp.src(path_1.join(config_1.APP_SRC, '**', '*.scss'))
            .pipe(plugins.sass().on('error', plugins.sass.logError))
            .pipe(gulp.dest(config_1.APP_DEST));
    };
};
//# sourceMappingURL=build.sass.prod.js.map