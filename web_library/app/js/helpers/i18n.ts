import {readJSONAsync} from '../../services/mocks/mocks_loader';
import i18next = require('i18next');
import ResourceStore = i18next.ResourceStore;


export class I18nHelper {

    /**
     * Sets the default language and the translations.
     *
     * @param options
     *  Containing the key 'lang' to set the language
     * @param afterInitialization
     *  Function to execute after the i18next has loaded its language
     * @param resources
     *  Hash containing languages and their translations, e.g.:
     *  'de' : {
     *      'translation': {
     *          'key1': 'value1',
     *          'key2': 'value2'
     *      }
     *  }
     */
    static initializeI18n = function (options, afterInitialization:(language:string) => void, resources?:{}) {
        I18nHelper.getLanguage(options, function(language) {
            if (resources == null || resources == undefined) {
                resources = {};
                let url = options.distPath + 'locales/' + language + '/translation.json';
                readJSONAsync(url, function (data) {
                    data = I18nHelper.overrideLocales(data, options.localesOverride);
                    resources[language] = {
                        translation: data
                    };
                    i18next.init({
                        resources: <ResourceStore>resources,
                        debug: false,
                        fallbackLng: options.fallbackLang,
                        lng: options.lang,
                        load: 'currentOnly'
                    });
                    afterInitialization(language);
                });
            } else {
                i18next.init({
                    resources: <ResourceStore>resources,
                    debug: false,
                    fallbackLng: options.fallbackLang,
                    lng: options.lang,
                    load: 'currentOnly'
                });
                afterInitialization(language);
            }
        });
    };

    /**
     *
     * @param data
     *  Default locale object.
     * @param customLocales
     *  Custom locale object to override the default translations.
     * @returns {any}
     *  The overriden translation object
     */
    static overrideLocales = function (data, customLocales):{} {
        if(customLocales && customLocales.dialog) {
            data = jQuery.extend(true, data, customLocales.dialog);
        }
        return data;
    };

    static getLanguage = function (options, callbackLang:(language:string) => void):void {
        let url = options.distPath + 'locales/' + options.lang + '/translation.json';
        readJSONAsync(url, function(data) {
            if(data === null) {
                callbackLang(options.fallbackLang);
            } else {
                callbackLang(options.lang);
            }
        });
    };
}