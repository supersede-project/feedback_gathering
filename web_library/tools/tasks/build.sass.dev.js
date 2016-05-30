define(["require", "exports", 'path', '../config'], function (require, exports, path_1, config_1) {
    "use strict";
    return function buildSassDev(gulp, plugins, option) {
        return function () {
            return gulp.src(path_1.join(config_1.APP_SRC, '**', '*.scss'))
                .pipe(plugins.sass().on('error', plugins.sass.logError))
                .pipe(gulp.dest(config_1.APP_DEST))
                .pipe(gulp.dest('app/'));
        };
    };
});
//# sourceMappingURL=build.sass.dev.js.map