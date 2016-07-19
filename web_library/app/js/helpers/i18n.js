define(["require", "exports", 'i18next'], function (require, exports, i18next) {
    "use strict";
    var I18nHelper = (function () {
        function I18nHelper() {
        }
        I18nHelper.initializeI18n = function (resources, options) {
            i18next.init({
                resources: resources,
                debug: false,
                fallbackLng: options.lang,
                lng: options.lang,
                load: 'currentOnly'
            });
        };
        return I18nHelper;
    }());
    exports.I18nHelper = I18nHelper;
});
//# sourceMappingURL=i18n.js.map