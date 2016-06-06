define(["require", "exports", 'path', '../config'], function (require, exports, path_1, config_1) {
    "use strict";
    return function tslint(gulp, plugins) {
        return function () {
            var src = [
                path_1.join(config_1.APP_SRC, '**/*.ts'),
                '!' + path_1.join(config_1.APP_SRC, '**/*.d.ts'),
                path_1.join(config_1.TOOLS_DIR, '**/*.ts'),
                '!' + path_1.join(config_1.TOOLS_DIR, '**/*.d.ts')
            ];
            return gulp.src(src)
                .pipe(plugins.tslint())
                .pipe(plugins.tslint.report(plugins.tslintStylish, {
                emitError: true,
                sort: true,
                bell: true
            }));
        };
    };
});
