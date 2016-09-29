import {I18nHelper} from './i18n';


describe('i18n Helper', () => {

    /* assumes that the only 'de' and 'en' are available */
    it('should determine the correct language depending on fallback and set language options', () => {
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

        expect(I18nHelper.getLanguage(optionsBothAvailable)).toEqual('en');
        expect(I18nHelper.getLanguage(langNotAvailable)).toEqual('de');
    });
});

