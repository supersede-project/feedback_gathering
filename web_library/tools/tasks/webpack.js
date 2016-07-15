define(["require", "exports"], function (require, exports) {
    "use strict";
    var exec = require('child_process').exec;
    return function webpack() {
        return function () {
            exec('webpack');
        };
    };
});
//# sourceMappingURL=webpack.js.map