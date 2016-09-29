define(["require", "exports", './i18n'], function (require, exports, i18n_1) {
    "use strict";
    describe('i18n Helper', function () {
        it('should determine the correct language depending on fallback and set language options', function () {
            var optionsBothAvailable = {
                'fallbackLang': 'de',
                'lang': 'en',
                'distPath': 'base/app/'
            };
            var langNotAvailable = {
                'fallbackLang': 'de',
                'lang': 'it',
                'distPath': 'base/app/'
            };
            expect(i18n_1.I18nHelper.getLanguage(optionsBothAvailable)).toEqual('en');
            expect(i18n_1.I18nHelper.getLanguage(langNotAvailable)).toEqual('de');
        });
    });
});
//# sourceMappingURL=i18n.spec.js.map