define(["require", "exports", 'path', '../config', '../utils'], function (require, exports, path_1, config_1, utils_1) {
    "use strict";
    return function buildJSDev(gulp, plugins) {
        var tsProject = utils_1.tsProjectFn(plugins);
        return function () {
            var src = [
                'typings/browser.d.ts',
                path_1.join(config_1.APP_SRC, '**/*.ts'),
                '!' + path_1.join(config_1.APP_SRC, '**/*.spec.ts')
            ];
            var result = gulp.src(src)
                .pipe(plugins.plumber())
                .pipe(plugins.sourcemaps.init())
                .pipe(plugins.typescript(tsProject));
            return result.js
                .pipe(plugins.sourcemaps.write())
                .pipe(plugins.template(utils_1.templateLocals()))
                .pipe(gulp.dest(config_1.APP_DEST));
        };
    };
});
