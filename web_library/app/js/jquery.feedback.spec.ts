import {feedbackPluginModule} from './jquery.feedback';
import Handlebars = require('handlebars');



xdescribe('jQuery Feedback Plugin', () => {
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

    it('should assign the default color to the element if no color defined in the options', () => {
        expect(feedbackButton.css('color')).toEqual('rgb(255, 255, 255)');
    });

    it('should assign the defined color in the options to the element', () => {
        expect(styledFeedbackButton.css('color')).toEqual('rgb(0, 0, 0)');
    });
});


