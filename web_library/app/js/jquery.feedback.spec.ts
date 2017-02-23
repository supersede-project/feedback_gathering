import {feedbackPluginModule, feedbackApp} from './jquery.feedback';
import Handlebars = require('handlebars');
import {applicationId, apiEndpointOrchestrator, apiEndpointRepository} from './config';



describe('jQuery Feedback Plugin', () => {
    let $ = $j;
    let feedbackPlugin;
    let feedbackButton;
    let styledFeedbackButton;

    beforeEach(() => {
        $('body').append('<a id="feedbackEntryPoint" href="#">Feedback</a>');
        $('body').append('<a id="styledFeedbackEntryPoint" href="#">Feedback</a>');
        feedbackPlugin = feedbackPluginModule($, window, document);
        feedbackButton = $('#feedbackEntryPoint');
        styledFeedbackButton = $('#styledFeedbackEntryPoint');

        feedbackButton.feedbackPlugin({
            'distPath': 'base/app/',
            'userId': '99999999',
            'lang': 'en',
            'fallbackLang': 'de',
            'dialogCSSClass': 'my-custom-dialog-class',
            'colorPickerCSSClass': 'my-color-picker',
            'defaultStrokeWidth': 4
        });

        styledFeedbackButton.feedbackPlugin({
            'color': '#000',
            'backgroundColor': '#ffffff',
            'distPath': 'base/app/',
            'userId': '99999999',
            'lang': 'en',
            'fallbackLang': 'de',
            'dialogCSSClass': 'my-custom-dialog-class',
            'colorPickerCSSClass': 'my-color-picker',
            'defaultStrokeWidth': 4
        });
    });

    xit('should assign the default color to the element if no color defined in the options', () => {
        expect(feedbackButton.css('color')).toEqual('rgb(255, 255, 255)');
    });

    xit('should assign the defined color in the options to the element', () => {
        expect(styledFeedbackButton.css('color')).toEqual('rgb(0, 0, 0)');
    });

    it('should use the predefined application id, orchestrator endpoint and repository endpoint if they are not passed in the options', () => {
        expect(feedbackApp.options.applicationId).toEqual(applicationId);
        expect(feedbackApp.options.apiEndpointRepository).toEqual(apiEndpointRepository);
        expect(feedbackApp.options.apiEndpointOrchestrator).toEqual(apiEndpointOrchestrator);
        expect(feedbackApp.applicationId).toEqual(applicationId);
    });

    it('should use the passed options', () => {
        feedbackButton.feedbackPlugin({
            'distPath': 'base/app/',
            'userId': '99999999',
            'lang': 'en',
            'fallbackLang': 'de',
            'dialogCSSClass': 'my-custom-dialog-class',
            'colorPickerCSSClass': 'my-color-picker',
            'defaultStrokeWidth': 4,
            'applicationId': 99999,
            'apiEndpointRepository': 'http://test-endpoint.com/repository/api/v1/',
            'apiEndpointOrchestrator': 'http://test-endpoint.com/orchestrator/api/v1/',
        });

        expect(feedbackApp.options.applicationId).toEqual(99999);
        expect(feedbackApp.options.apiEndpointRepository).toEqual('http://test-endpoint.com/repository/api/v1/');
        expect(feedbackApp.options.apiEndpointOrchestrator).toEqual('http://test-endpoint.com/orchestrator/api/v1/');
        expect(feedbackApp.applicationId).toEqual(99999);
    });
});


