import Handlebars = require('handlebars');
import './lib/rating/jquery.star-rating-svg.min.js';
import './jquery.validate';
import './jquery.validate_category';
import './jquery.fileupload';
import {applicationId, apiEndpointOrchestrator, apiEndpointRepository} from './config';
import {I18nHelper} from './helpers/i18n';
import i18n = require('i18next');
import {ApplicationService} from '../services/application_service';
import * as t from '../templates/t';
import * as compare from '../templates/compare';
import {FeedbackApp} from './feedback_app';
import {MockBackend} from '../services/backends/mock_backend';
import {QuestionDialogView} from '../views/dialog/question_dialog_view';
import * as i18next from 'i18next';
let mockData = require('json!../services/mocks/dev/application_senercon_20.json');


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
        if($.fn.droppable === undefined) {
            $.getScript('https://platform.supersede.eu/web_library/senercon/dist/jquery.ui.droppable.js');
        }
        let button = this;
        var options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
        let mockBackend:MockBackend = new MockBackend(mockData);

        I18nHelper.initializeI18n(options, function(language) {
            let applicationService = new ApplicationService(options.apiEndpointOrchestrator, language, mockBackend);
            feedbackApp = new FeedbackApp(applicationService, options.applicationId, options, button);
            feedbackApp.loadApplicationConfiguration();
        });

        return this;
    };

    $.fn.feedbackPlugin.defaults = {
        'color': '#ffffff',
        'lang': 'de',
        'fallbackLang': 'en',
        'localesOverride': null,
        'backgroundColor': '#7c9009',
        'distPath': 'dist/',
        'userId': '',
        'dialogCSSClass': 'feedback-dialog',
        'colorPickerCSSClass': 'color-picker',
        'defaultStrokeWidth': 4,
        'dialogPositionMy': 'center top',
        'dialogPositionAt': 'center top+30',
        'dialogPositionOf': window,
        'apiEndpointOrchestrator': apiEndpointOrchestrator,
        'apiEndpointRepository': apiEndpointRepository,
        'applicationId': applicationId
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