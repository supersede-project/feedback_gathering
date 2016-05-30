define(["require", "exports", 'path', '../config'], function (require, exports, path_1, config_1) {
    "use strict";
    return function watchDev(gulp, plugins) {
        return function () {
            plugins.watch(path_1.join(config_1.APP_SRC, '**/*'), function () { return gulp.start('build.dev'); });
        };
    };
});
//# sourceMappingURL=watch.dev.js.map