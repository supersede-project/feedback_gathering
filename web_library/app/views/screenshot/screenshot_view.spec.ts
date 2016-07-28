import {ScreenshotView} from './screenshot_view';
import {Mechanism} from '../../models/mechanism';
import {screenshotType} from '../../js/config';
import Handlebars = require('handlebars');


describe('Screenshot View', () => {
    let screenshotView:ScreenshotView;
    let $ = $j;

    beforeEach(() => {
        var screenshotMechanism = new Mechanism(1, screenshotType, true);

        var screenshotMechanismTemplate = '<div id="capture">Capture this element' +
            '<div class="hide-1">hide</div><span id="hide2">not part of screenshot</span></div>' +
            '' +
            '{{#if screenshotMechanism.active}}' +
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
            elementToCapture, [$('.hide-1'), $('#hide2')]);
    });

    it('should find the required elements in the html', () => {
        expect(screenshotView.screenshotCaptureButton).toBeDefined();
        expect(screenshotView.screenshotCaptureButton.attr('id')).toEqual('takeScreenshot');

        expect(screenshotView.screenshotPreviewElement).toBeDefined();
        expect(screenshotView.elementToCapture).toBeDefined();
    });

    it('should have the same width/height ratio for the element to capture and the canvas after capturing', () => {

    });

    it('should capture an image when the screenshot capture button is clicked', (done) => {
        expect(screenshotView.screenshotPreviewElement.html()).toEqual('');

        screenshotView.screenshotCaptureButton.click();

        setTimeout(function() {
            expect(screenshotView.screenshotPreviewElement.html()).not.toEqual('');
            expect(screenshotView.screenshotPreviewElement.html().indexOf('canvas')).not.toEqual(-1);
            done();
        }, 1000);
    });

    it('should return the screenshot as a binary', (done) => {
        expect(screenshotView.getScreenshotAsBinary()).toBeNull();

        screenshotView.screenshotCaptureButton.click();

        setTimeout(function() {
            expect(screenshotView.getScreenshotAsBinary()).not.toBeNull();
            done();
        }, 1000);
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
        expect($('#screenshotDrawRect').hasClass('active')).toBeTruthy();
        expect(screenshotView.drawingMode).toEqual('rectDrawingMode');
    });

    it('should update the canvas states for the undo stack', (done) => {
        screenshotView.screenshotCaptureButton.click();

        setTimeout(function() {
            expect(screenshotView.canvasState.src).not.toBeNull();

            var offset = screenshotView.screenshotPreviewElement.offset();
            var eventDown = jQuery.Event( "mousedown", {
                which: 1,
                pageX: offset.left,
                pageY: offset.top
            });
            screenshotView.screenshotPreviewElement.trigger(eventDown);

            var eventMove = jQuery.Event( "mousemove", {
                which: 1,
                pageX: offset.left,
                pageY: offset.top
            });
            screenshotView.screenshotPreviewElement.trigger(eventMove);

            var eventUp = jQuery.Event( "mouseup", {
                which: 1,
                pageX: offset.left,
                pageY: offset.top
            });
            screenshotView.screenshotPreviewElement.trigger(eventUp);

            done();
        }, 1000);
    });

    it('should provide an undo operation', () => {

    });

    it('should initialize the screenshot operations', () => {

    });

    it('should disable all screenshot operations', () => {

    });

});

