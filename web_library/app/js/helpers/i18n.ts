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
    static initializeI18n = function(options) {
        var language = options.lang;

        var resources = {};
        resources[language] = {
            translation: readJSON('dist/locales/' + language + '/translation.json')
        };

        i18next.init({
            resources: resources,
            debug: false,
            fallbackLng: options.lang,
            lng: options.lang,
            load: 'currentOnly'
        });
    };
}