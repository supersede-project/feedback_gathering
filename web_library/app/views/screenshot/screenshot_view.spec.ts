import {ScreenshotView} from './screenshot_view';
import {Mechanism} from '../../models/mechanism';
import {screenshotType} from '../../js/config';
import Handlebars = require('handlebars');


describe('Screenshot View', () => {
    let screenshotView:ScreenshotView;
    let $ = $j;

    beforeEach(() => {
        var screenshotMechanism = new Mechanism(screenshotType, true);

        var screenshotMechanismTemplate = '{{#if screenshotMechanism.active}}' +
            '<section class="feedback-mechanism horizontal" id="screenshotType">' +
            '<article class="col col-left">' +
                '<button id="takeScreenshot" class="button small">take screenshot</button>' +
            '<div class="screenshot-operations">' +
            '<button id="screenshotDrawRect" class="screenshot-operation active">rectangle</button>' +
            '<button id="screenshotDrawFillRect" class="screenshot-operation">blacken</button>' +
            '<button id="screenshotDrawCircle" class="screenshot-operation">circle</button>' +
            '<button id="screenshotDrawArrow" class="screenshot-operation">arrow</button>' +
            '<button id="screenshotDrawFreehand" class="screenshot-operation">freehand</button>' +
            '<button id="screenshotCrop" class="screenshot-operation">crop</button>' +
            '<button id="screenshotDrawUndo" class="screenshot-operation">undo</button>' +
            '<button id="screenshotDrawRemove" class="screenshot-operation">remove</button>' +
            '</div>' +
            '</article>' +
        '<div id="screenshotPreview" class="col col-right">' +
            '</div>' +
            '<div class="clearfix"></div>' +
            '</section>' +
            '{{/if}}';

        var template = Handlebars.compile(screenshotMechanismTemplate);
        var context = {screenshotMechanism: screenshotMechanism};
        var html = template(context);
        $('body').append(html);

        var screenshotPreviewElement = $('#screenshotPreview');
        var screenshotCaptureButton = $('#takeScreenshot');
        var elementToCapture = $('#capture');

        screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreviewElement, screenshotCaptureButton,
            elementToCapture);
    });

    it('should find the required elements in the html', () => {
        expect(screenshotView.screenshotCaptureButton).toBeDefined();
        expect(screenshotView.screenshotCaptureButton.attr('id')).toEqual('takeScreenshot');
    });

});

