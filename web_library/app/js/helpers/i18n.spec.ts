import {I18nHelper} from './i18n';


xdescribe('i18n Helper', () => {
    //assumes that de and en are available, but ru not
    it('should determine the correct language depending on fallback and set language options', (done) => {
        let optionsBothAvailable = {
            'fallbackLang': 'de',
            'lang': 'en',
            'distPath': 'base/app/'
        };
        let langNotAvailable = {
            'fallbackLang': 'de',
            'lang': 'ru',
            'distPath': 'base/app/'
        };

        I18nHelper.getLanguage(optionsBothAvailable, language => {
            expect(language).toEqual('en');
        });

        I18nHelper.getLanguage(langNotAvailable, language => {
            expect(language).toEqual('de');
            done();
        });
    });
});