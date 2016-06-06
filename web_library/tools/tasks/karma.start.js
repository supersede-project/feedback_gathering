define(["require", "exports", 'karma', 'path'], function (require, exports, karma, path_1) {
    "use strict";
    return function karmaStart() {
        return function (done) {
            new karma.Server({
                configFile: path_1.join(process.cwd(), 'karma.conf.js'),
                singleRun: true
            }).start(done);
        };
    };
});
