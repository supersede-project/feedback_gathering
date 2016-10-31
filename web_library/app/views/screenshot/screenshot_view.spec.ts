import {ScreenshotView} from './screenshot_view';
import {mechanismTypes} from '../../js/config';
import Handlebars = require('handlebars');
import {ScreenshotMechanism} from '../../models/mechanisms/screenshot_mechanism';


describe('Screenshot View', () => {
    let screenshotView:ScreenshotView;
    let container:JQuery;
    let $ = $j;

    beforeEach(() => {
        var screenshotMechanism = new ScreenshotMechanism(1, mechanismTypes.screenshotType, true, 1, true, []);

        var screenshotMechanismTemplate = '<div id="capture">Capture this element' +
            '<div class="hide-1">hide</div><span id="hide2">not part of screenshot</span></div>' +
            '' +
            '{{#if screenshotMechanism.active}}' +
            '<section id="container" class="feedback-mechanism horizontal screenshot-type">' +
            '<article class="col col-left">' +
                '<button class="take-screenshot button small">take screenshot</button>' +
            '<div class="screenshot-operations">' +
            '<button class="screenshot-operation active screenshot-draw-rect">rectangle</button>' +
            '<button class="screenshot-operation screenshot-draw-fill-rect">blacken</button>' +
            '<button class="screenshot-operation screenshot-draw-circle">circle</button>' +
            '<button class="screenshot-operation screenshot-draw-arrow">arrow</button>' +
            '<button class="screenshot-operation screenshot-draw-freehand">freehand</button>' +
            '<button class="screenshot-operation screenshot-crop">crop</button>' +
            '<button class="screenshot-operation screenshot-draw-undo">undo</button>' +
            '<button class="screenshot-operation screenshot-draw-remove">remove</button>' +
            '<article class="selected-object-controls">' +
            '<p>Selection</p>' +
            '<a class="delete" href="#"><i class="material-icons">delete</i></a> &nbsp;' +
            '<a class="color" href="#"><i class="material-icons">format_color_fill</i></a>' +
        '<input class="text-size" type="text">' +
            '</article>' +
        '<article class="freehand-controls">' +
            '<p>Color</p>' +
            '<a class="freehand-color" href="#"><i class="material-icons">format_color_fill</i></a>' +
            '</article>' +
            '</div>' +
            '</article>' +
        '<div class="screenshot-preview col col-right">' +
            '</div>' +
            '<div class="clearfix"></div>' +
            '</section>' +
            '{{/if}}';

        var template = Handlebars.compile(screenshotMechanismTemplate);
        var context = {screenshotMechanism: screenshotMechanism};
        var html = template(context);
        $('body').append(html);

        container = $('#container');

        var screenshotPreviewElement = container.find('.screenshot-preview');
        var screenshotCaptureButton = container.find('.take-screenshot');
        var elementToCapture = $('#capture');

        screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreviewElement, screenshotCaptureButton,
            elementToCapture, container, 'dist/', ['.hide-1', '#hide2'], true);
        screenshotMechanism.screenshotView = screenshotView;
    });

    it('should find the required elements in the html', () => {
        expect(screenshotView.screenshotCaptureButton).toBeDefined();
        expect(screenshotView.screenshotCaptureButton.attr('class')).toEqual('take-screenshot button small');

        expect(screenshotView.screenshotPreviewElement).toBeDefined();
        expect(screenshotView.elementToCapture).toBeDefined();
    });

    it('should hide some elements that should not be part of the screenshot', () => {
        screenshotView.hideElements();
        expect($('.hide-1').css('display')).toBe('none');
        expect($('#hide2').css('display')).toBe('none');
    });

    it('should redisplay some elements that should not be part of the screenshot', () => {
        screenshotView.hideElements();
        expect($('.hide-1').css('display')).toBe('none');
        expect($('#hide2').css('display')).toBe('none');

        screenshotView.showElements();
        expect($('.hide-1').css('display')).not.toBe('none');
        expect($('#hide2').css('display')).not.toBe('none');
    });

    it('should reset the view', () => {
        screenshotView.reset();

        expect(screenshotView.screenshotPreviewElement.css('display')).toBe('none');
        expect(screenshotView.screenshotCanvas).toBeNull();
        expect($('.screenshot-operations').css('display')).toBe('none');
    });
});

