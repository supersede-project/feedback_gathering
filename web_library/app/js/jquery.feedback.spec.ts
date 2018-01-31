import {feedbackPluginModule} from './jquery.feedback';
import Handlebars = require('handlebars');



describe('jQuery Feedback Plugin', () => {
    let $ = $j;
    let feedbackPlugin;
    let feedbackButton;

    beforeEach(() => {
        $('body').append('<a id="feedbackEntryPoint" href="#">Feedback</a>');
        feedbackPlugin = feedbackPluginModule($, window, document);
        feedbackButton = $('#feedbackEntryPoint');
    });

    it('should assign the default color to the element if no color defined in the options', () => {
        feedbackButton.feedbackPlugin({
            'distPath': 'base/app/',
            'userId': '99999999',
            'lang': 'en',
            'fallbackLang': 'de',
            'dialogCSSClass': 'my-custom-dialog-class',
            'colorPickerCSSClass': 'my-color-picker',
            'defaultStrokeWidth': 4
        });

        expect(feedbackButton.css('color')).toEqual('rgb(0, 0, 238)');
    });

    it('should assign the defined color in the options to the element', () => {
        feedbackButton.feedbackPlugin({
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

        expect(feedbackButton.css('color')).toEqual('rgb(0, 0, 238)');
    });
});


