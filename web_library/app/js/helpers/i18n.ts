import i18next = require('i18next');


export class I18nHelper {

    /**
     * Sets the default language and the translations.
     *
     * @param resources
     *  The translations given as a json in the following form
     *  {lng: {translation: {key1: value1, key2, value2}}. lng2: ... }
     * @param options
     *  Containing the key 'lang' to set the language
     *
     */
    static initializeI18n = function(resources, options) {
        i18next.init({
            resources: resources,
            debug: false,
            fallbackLng: options.lang,
            lng: options.lang,
            load: 'currentOnly'
        });
    };
}