define(["require", "exports", 'i18next', '../../services/mocks/mocks_loader'], function (require, exports, i18next, mocks_loader_1) {
    "use strict";
    var I18nHelper = (function () {
        function I18nHelper() {
        }
        I18nHelper.initializeI18n = function (options) {
            var language = options.lang;
            var resources = {};
            resources[language] = {
                translation: mocks_loader_1.readJSON('dist/locales/' + language + '/translation.json')
            };
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