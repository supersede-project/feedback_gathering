import Handlebars = require('handlebars');
import './lib/rating/jquery.star-rating-svg.js';
import './jquery.validate';
import './jquery.validate_category';
import './jquery.fileupload';
import {applicationId, apiEndpointOrchestrator} from './config';
import {I18nHelper} from './helpers/i18n';
import i18n = require('i18next');
import {ApplicationService} from '../services/application_service';
import * as t from '../templates/t';
import * as compare from '../templates/compare';
import {FeedbackApp} from './feedback_app';
import {MockBackend} from '../services/backends/mock_backend';
var mockData = require('json!../services/mocks/dev/applications_mock.json');


export declare var feedbackApp:FeedbackApp;

export var feedbackPluginModule = function ($, window, document) {

    /**
     * @param options
     *  Client side configuration of the feedback library
     * @returns {jQuery}
     *
     * The feedbackPlugin() function can get applied to a HTML element. This element is then configured via the passed
     * options and the default options. If a click event on this element happens the configuration is fetched from the
     * server and the feedback mechanism is invoked.
     */
    $.fn.feedbackPlugin = function (options) {
        I18nHelper.initializeI18n(options);
        var language = I18nHelper.getLanguage(options);
        var options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
        var mockBackend:MockBackend = new MockBackend(mockData);
        var applicationService = new ApplicationService(apiEndpointOrchestrator, language); //, mockBackend);
        feedbackApp = new FeedbackApp(applicationService, applicationId, options, this);
        feedbackApp.loadApplicationConfiguration();

        return this;
    };

    $.fn.feedbackPlugin.defaults = {
        'color': '#fff',
        'lang': 'de',
        'backgroundColor': '#7c9009',
        'fallbackLang': 'en',
        'distPath': 'dist/',
        'userId': '',
        'dialogCSSClass': 'feedback-dialog',
        'colorPickerCSSClass': 'color-picker',
        'defaultStrokeWidth': 4,
        'dialogPositionMy': 'center top',
        'dialogPositionAt': 'center top+30',
        'dialogPositionOf': window
    };
};

(function ($, window, document) {
    feedbackPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config({
    "shim": {
        "feedbackPlugin": ["jquery"]
    }
});