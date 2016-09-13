define(["require", "exports", 'i18next', '../../services/mocks/mocks_loader'], function (require, exports, i18next, mocks_loader_1) {
    "use strict";
    var I18nHelper = (function () {
        function I18nHelper() {
        }
        I18nHelper.initializeI18n = function (options, resources) {
            var language = options.lang;
            if (resources == null || resources == undefined) {
                var resources = {};
                resources[language] = {
                    translation: mocks_loader_1.readJSON(options.distPath + 'locales/' + language + '/translation.json')
                };
            }
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