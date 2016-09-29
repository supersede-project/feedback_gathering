import i18next = require('i18next');
import {readJSON} from '../../services/mocks/mocks_loader';


export class I18nHelper {

    /**
     * Sets the default language and the translations.
     *
     * @param options
     *  Containing the key 'lang' to set the language
     *
     */
    static initializeI18n = function(options, resources?) {
        var language = I18nHelper.getLanguage(options);

        if(resources == null || resources == undefined) {
            var resources = {};
            resources[language] = {
                translation: readJSON(options.distPath + 'locales/' + language + '/translation.json')
            };
        }

        i18next.init({
            resources: resources,
            debug: false,
            fallbackLng: options.fallbackLang,
            lng: options.lang,
            load: 'currentOnly'
        });
    };

    // TODO maybe remove this and use i18next method instead
    static getLanguage = function(options): string {
        if(readJSON(options.distPath + 'locales/' + options.lang + '/translation.json') !== null) {
            return options.lang;
        } else {
            return options.fallbackLang;
        }
    };
}