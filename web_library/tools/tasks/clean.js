define(["require", "exports", 'async', 'gulp-util', 'chalk', 'del', '../config'], function (require, exports, async, util, chalk, del, config_1) {
    "use strict";
    function cleanAll(done) {
        async.parallel([
            cleanDist,
            cleanTest,
            cleanTmp
        ], done);
    }
    function cleanDist(done) {
        del(config_1.APP_DEST).then(function (paths) {
            util.log('Deleted', chalk.yellow(paths && paths.join(', ') || '-'));
            done();
        });
    }
    function cleanTest(done) {
        del(config_1.TEST_DEST).then(function (paths) {
            util.log('Deleted', chalk.yellow(paths && paths.join(', ') || '-'));
            done();
        });
    }
    function cleanTmp(done) {
        del(config_1.TMP_DIR).then(function (paths) {
            util.log('Deleted', chalk.yellow(paths && paths.join(', ') || '-'));
            done();
        });
    }
    return function clean(gulp, plugins, option) {
        return function (done) {
            switch (option) {
                case 'all':
                    cleanAll(done);
                    break;
                case 'dist':
                    cleanDist(done);
                    break;
                case 'test':
                    cleanTest(done);
                    break;
                case 'tmp':
                    cleanTmp(done);
                    break;
                default: done();
            }
        };
    };
});
