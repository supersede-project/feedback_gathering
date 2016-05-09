"use strict";
var path_1 = require('path');
var yargs_1 = require('yargs');
var ENVIRONMENTS = {
    DEVELOPMENT: 'dev',
    PRODUCTION: 'prod'
};
exports.ENV = getEnvironment();
exports.PROJECT_ROOT = path_1.normalize(path_1.join(__dirname, '..'));
exports.APP_SRC = 'app';
exports.TOOLS_DIR = 'tools';
exports.APP_DEST = "dist/" + exports.ENV;
exports.TMP_DIR = 'tmp';
exports.TEST_DEST = 'test';
function getEnvironment() {
    var base = yargs_1.argv['_'];
    var prodKeyword = !!base.filter(function (o) { return o.indexOf(ENVIRONMENTS.PRODUCTION) >= 0; }).pop();
    if (base && prodKeyword || yargs_1.argv['env'] === ENVIRONMENTS.PRODUCTION) {
        return ENVIRONMENTS.PRODUCTION;
    }
    else {
        return ENVIRONMENTS.DEVELOPMENT;
    }
}
//# sourceMappingURL=config.js.map