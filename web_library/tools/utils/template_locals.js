define(["require", "exports", '../config'], function (require, exports, CONFIG) {
    "use strict";
    function templateLocals() {
        return CONFIG;
    }
    exports.templateLocals = templateLocals;
});
