import {feedbackPluginModule} from './jquery.feedback';
import Handlebars = require('handlebars');


describe('jQuery Feedback Plugin', () => {
    let $ = $j;

    beforeEach(() => {
        feedbackPluginModule($, window, document);
    });

    it('should ...', () => {

    });
});

