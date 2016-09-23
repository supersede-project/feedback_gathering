import {ScreenshotView} from './screenshot_view';
import {mechanismTypes} from '../../js/config';
import Handlebars = require('handlebars');
import {Mechanism} from '../../models/mechanisms/mechanism';


xdescribe('Screenshot View', () => {
    let screenshotView:ScreenshotView;
    let container:JQuery;
    let $ = $j;

    beforeEach(() => {
        var screenshotMechanism = new Mechanism(1, mechanismTypes.screenshotType, true);

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
            elementToCapture, container, 'dist/', [$('.hide-1'), $('#hide2')]);
    });

    it('should find the required elements in the html', () => {
        expect(screenshotView.screenshotCaptureButton).toBeDefined();
        expect(screenshotView.screenshotCaptureButton.attr('class')).toEqual('take-screenshot button small');

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

    it('should reset the view', (done) => {
        screenshotView.reset();

        expect(screenshotView.screenshotPreviewElement.css('display')).toBe('none');
        expect(screenshotView.screenshotCanvas).toBeNull();
        expect($('.screenshot-operations').css('display')).toBe('none');

        setTimeout(function() {
            expect(container.find('.screenshot-draw-rect').hasClass('active')).toBeTruthy();
            expect(screenshotView.drawingMode).toEqual('rectDrawingMode');
            done();
        }, 3000);
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

