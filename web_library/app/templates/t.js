define(["require", "exports", 'i18next'], function (require, exports, i18n) {
    "use strict";
    module.exports = function (i18n_key) {
        var result = i18n.t(i18n_key);
        return result;
    };
});
//# sourceMappingURL=t.js.map