import {ScreenshotViewDrawing} from './screenshot_view_drawing';
import {DataHelper} from '../../js/helpers/data_helper';
import '../../js/lib/screenshot/html2canvas_5_0_4.min.js';
import '../../js/lib/screenshot/html2canvas_5_0_4.svg.min.js';
import '../../js/lib/screenshot/rgbcolor.js';
import '../../js/lib/screenshot/StackBlur.js';
import '../../js/lib/screenshot/canvg.js';
import {Mechanism} from '../../models/mechanisms/mechanism';
import {CanvasState} from './canvas_state';
import {ScreenshotFeedback} from '../../models/feedbacks/screenshot_feedback';
import {MechanismView} from '../mechanism_view';
import IPoint = fabric.IPoint;
import {clickBlocked} from '../dialog/dialog_view';

const freehandDrawingMode:string = 'freehandDrawingMode';
const rectDrawingMode:string = 'rectDrawingMode';
const fillRectDrawingMode:string = 'fillRectDrawingMode';
const circleDrawingMode:string = 'circleDrawingMode';
const arrowDrawingMode:string = 'arrowDrawingMode';
const croppingMode:string = 'croppingMode';
const stickingMode:string = 'stickingMode';
const textMode:string = 'textMode';
const textMode2:string = 'textMode2';
const black:string = "#000000";
const defaultColor:string = black;
const canvasId:string = 'screenshotCanvas';
const defaultFontSize:number = 30;
const textTypeObjectIdentifier:string = 'i-text';
const cropperTypeObjectIdentifier:string = 'cropper';


export class ScreenshotView implements MechanismView {
    screenshotMechanism:Mechanism;
    screenshotPreviewElement:JQuery;
    screenshotCaptureButton:JQuery;
    elementToCapture:JQuery;
    elementsToHide:[string];
    screenshotCanvas:any;
    context:any;
    startX:number;
    startY:number;
    drawingMode:string;
    canvasState:HTMLImageElement;
    canvasStates:CanvasState[];
    canvasWidth:number;
    canvasHeight:number;
    screenshotViewDrawing:ScreenshotViewDrawing;
    canvasOriginalWidth:number;
    canvasOriginalHeight:number;
    container:JQuery;
    distPath:string;
    fabricCanvas;
    canvas;
    croppingIsActive:boolean;
    freehandActive:boolean;
    colorPickerCSSClass:string = 'color-picker';
    defaultStrokeWidth:number = 3;
    selectedObjectControls:any;
    hasBordersAndControls:boolean = true;
    panning:boolean = false;
    blockPanning:boolean = false;
    canvasMovementX:number = 0;
    canvasMovementY:number = 0;
    currentObjectInToolbar:any = null;
    croppingRect:any;

    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, container:JQuery, distPath:string, elementsToHide?:any, hasBordersAndControls?:boolean) {
        this.screenshotMechanism = screenshotMechanism;
        this.screenshotPreviewElement = screenshotPreviewElement;
        this.screenshotCaptureButton = screenshotCaptureButton;
        this.elementToCapture = elementToCapture;
        this.container = container;
        this.elementsToHide = elementsToHide;
        this.canvasState = null;
        this.canvasStates = [];
        this.distPath = distPath;
        this.screenshotViewDrawing = new ScreenshotViewDrawing();
        this.addCaptureEventToButton();
        this.croppingIsActive = false;
        this.freehandActive = false;
        this.hasBordersAndControls = (hasBordersAndControls === null || hasBordersAndControls === undefined) ? true : hasBordersAndControls;
    }

    checkAutoTake() {
        if (this.screenshotMechanism.getParameterValue('autoTake')) {
            this.makeScreenshot();
        }
    }

    replaceOptionsWithTemporarySpans() {
        jQuery('select').each(function() {
            var selectText = jQuery(this).find('option:selected').text();

            jQuery(this).data('original-color', jQuery(this).css('color'));
            jQuery(this).css('color', 'transparent');
            var textOverlay = jQuery('<span class="html2canvas-option">' + selectText + '</span>');
            textOverlay.css('position', 'absolute')
                .css('top', jQuery(this).offset().top - 30 + 'px')
                .css('left', jQuery(this).offset().left + 10 + 'px')
                .css('width', 'auto')
                .css('height', '25px')
                .css('display', 'block')
                .css('z-index', 9000);
            jQuery('body').append(textOverlay);
        });
    }

    removeTemporarySpans() {
        jQuery('.html2canvas-option').remove();
        jQuery('select').each(function() {
            var originalColor = jQuery(this).data('original-color');
            jQuery(this).css('color', originalColor);
        });
    }

    makeScreenshot() {
        if(this.screenshotMechanism.getParameterValue('screenshotUrl')) {
            this.generateScreenshotFromUrl();
        } else {
            this.generateScreenshot();
        }
    }

    generateScreenshotFromUrl() {
        let screenshotDelay = 4000;
        let myThis = this;

        myThis.addAndShowLoading();

        setTimeout(function() {
            let screenshotImageUrl = myThis.screenshotMechanism.getParameterValue('screenshotUrl') + "?anti_cache=" + new Date().getTime();

            console.log(screenshotImageUrl);

            let canvas: HTMLCanvasElement = <HTMLCanvasElement>(jQuery('<canvas width="478" height="251"></canvas>').get(0));
            myThis.canvas = canvas;
            myThis.screenshotPreviewElement.empty().append(canvas);
            myThis.screenshotPreviewElement.show();
            jQuery('.screenshot-preview canvas').attr('id', canvasId);

            fabric.util.loadImage(screenshotImageUrl, function(img) {
                myThis.removeLoading();
                let imgWidth = +img.width;
                let imgHeight = +img.height;

                let windowRatio = imgWidth / imgHeight;
                myThis.canvasWidth = myThis.screenshotPreviewElement.width() - 2;
                myThis.canvasHeight = (myThis.screenshotPreviewElement.width() / windowRatio) - 2;
                jQuery(canvas).prop('width', myThis.canvasWidth);
                jQuery(canvas).prop('height', myThis.canvasHeight);


                myThis.canvasState = img;
                myThis.screenshotCanvas = canvas;
                myThis.context = canvas.getContext("2d");
                img.onload = function () {
                    myThis.context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                };

                myThis.initFabric(img, canvas);
                myThis.initFreehandDrawing();
                myThis.initStickers();
                myThis.initScreenshotOperations();
                myThis.customizeControls();
                myThis.initZoom();

                let screenshotCaptureButtonActiveText = myThis.screenshotCaptureButton.data('active-text');
                myThis.screenshotCaptureButton.text(screenshotCaptureButtonActiveText);
            }, null, {crossOrigin: 'Anonymous'});
        }, screenshotDelay);
    }

    generateScreenshot() {
        var scrollPosition = this.container.offset().top;
        this.hideElements();
        this.addAndShowLoading();
        var myThis = this;

        setTimeout(function() {
            myThis.svgToCanvas();
            myThis.replaceOptionsWithTemporarySpans();

            html2canvas(myThis.elementToCapture, {
                useCORS: true,
                onrendered: function (canvas) {
                    setTimeout(function() {
                        console.log(myThis.container.offset().top);
                        jQuery('html, body').animate({
                            scrollTop: scrollPosition - 85
                        }, 0);

                        myThis.removeLoading();
                        myThis.removeTemporarySpans();
                        myThis.showElements();
                        myThis.showAllCanvasElements();
                        myThis.canvas = canvas;
                        myThis.screenshotPreviewElement.empty().append(canvas);
                        myThis.screenshotPreviewElement.show();
                        jQuery('.screenshot-preview canvas').attr('id', canvasId);

                        var windowRatio = myThis.elementToCapture.width() / myThis.elementToCapture.height();

                        // save the canvas content as imageURL
                        var data = canvas.toDataURL("image/png");
                        myThis.context = canvas.getContext("2d");
                        myThis.canvasOriginalWidth = canvas.width;
                        myThis.canvasOriginalHeight = canvas.height;

                        myThis.canvasWidth = myThis.screenshotPreviewElement.width() - 2;
                        myThis.canvasHeight = (myThis.screenshotPreviewElement.width() / windowRatio) - 2;

                        jQuery(canvas).prop('width', myThis.canvasWidth);
                        jQuery(canvas).prop('height', myThis.canvasHeight);

                        var img = new Image();
                        myThis.canvasState = img;
                        myThis.screenshotCanvas = canvas;
                        img.src = data;
                        img.onload = function () {
                            myThis.context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                        };

                        myThis.initFabric(img, canvas);
                        myThis.initFreehandDrawing();
                        myThis.initStickers();
                        myThis.initScreenshotOperations();
                        myThis.customizeControls();
                        myThis.initZoom();

                        let screenshotCaptureButtonActiveText = myThis.screenshotCaptureButton.data('active-text');
                        myThis.screenshotCaptureButton.text(screenshotCaptureButtonActiveText);
                    }, 200);
                }
            });
        }, 200);
    }

    addAndShowLoading() {
        let loadingCircle = jQuery('<div id="loadingCircle" class="loader"></div>');
        this.screenshotPreviewElement.css('min-width', '200px');
        this.screenshotPreviewElement.css('min-height', '200px');
        this.screenshotPreviewElement.css('display', 'block');
        this.screenshotPreviewElement.empty();
        this.screenshotPreviewElement.append(loadingCircle);
    }

    removeLoading() {
        this.screenshotPreviewElement.find('#loadingCircle').remove();
    }

    initFabric(img, canvas) {
        var myThis = this;
        this.fabricCanvas = new fabric.Canvas(canvasId);

        var pageScreenshotCanvas = new fabric.Image(img, {width: canvas.width, height: canvas.height});

        pageScreenshotCanvas.set('selectable', false);
        pageScreenshotCanvas.set('hoverCursor', 'default');
        this.fabricCanvas.add(pageScreenshotCanvas);

        this.selectedObjectControls = jQuery('#screenshotMechanism' + myThis.screenshotMechanism.id + ' .selected-object-controls');
        this.selectedObjectControls.hide();

        myThis.fabricCanvas.on('object:selected', function (e) {
            myThis.blockPanning = true;
            var selectedObject = e.target;

            selectedObject.bringToFront();

            myThis.fabricCanvas.uniScaleTransform = selectedObject.get('type') === 'fabricObject' || selectedObject.get('type') === 'fillRect';

            myThis.selectedObjectControls.show();
            if (selectedObject.get('type') === textTypeObjectIdentifier) {
                var textSizeInput = myThis.selectedObjectControls.find('.text-size');
                textSizeInput.show();
                textSizeInput.val(selectedObject.getFontSize());
                textSizeInput.off().on('keyup', function () {
                    selectedObject.setFontSize(jQuery(this).val());
                    myThis.fabricCanvas.renderAll();
                });
            } else {
                myThis.selectedObjectControls.find('.text-size').hide();
            }

            // prevent form submission on enter press
            myThis.selectedObjectControls.find('.text-size').on('keydown', function(event) {
                if(event.keyCode == 13) {
                    event.preventDefault();
                    return false;
                }
                return true;
            });

            if (selectedObject.get('type') === 'path-group') {
                for (var path of selectedObject.paths) {
                    if (path.getFill() != "") {
                        var currentObjectColor = path.getFill();
                        break;
                    }
                }
            } else if (selectedObject.get('type') === 'path' || selectedObject.get('type') === 'fabricObject') {
                var currentObjectColor = selectedObject.getStroke();
            } else if (selectedObject.get('type') === 'fillRect') {
                var currentObjectColor = selectedObject.getFill();
            } else {
                var currentObjectColor = selectedObject.getFill();
            }

            myThis.selectedObjectControls.find('.delete').off().on('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                if (selectedObject.get('customType') === 'arrow') {
                    if (selectedObject.line !== undefined) {
                        selectedObject.line.remove();
                    }
                    if (selectedObject.arrow !== undefined) {
                        selectedObject.arrow.remove();
                    }
                    if (selectedObject.circle !== undefined) {
                        selectedObject.circle.remove();
                    }
                }

                if (selectedObject) {
                    selectedObject.remove();
                }
            });

            myThis.selectedObjectControls.find('a.color').css('color', currentObjectColor);
            myThis.selectedObjectControls.find('a.color').off().spectrum({
                color: currentObjectColor,
                containerClassName: myThis.colorPickerCSSClass,
                showPaletteOnly: true,
                togglePaletteOnly: true,
                togglePaletteMoreText: 'more',
                togglePaletteLessText: 'less',
                palette: [
                    ["#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff"],
                    ["#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f"],
                    ["#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                    ["#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                    ["#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                    ["#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79"],
                    ["#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47"],
                    ["#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130"]
                ],
                change: function (color) {
                    var color = color.toHexString();
                    jQuery(this).css('color', color);

                    if (selectedObject.get('customType') === 'arrow') {
                        selectedObject.setFill(color);
                        selectedObject.setStroke(color);
                        if(selectedObject.line !== undefined) {
                            selectedObject.line.setFill(color);
                            selectedObject.line.setStroke(color);
                        }
                        if(selectedObject.arrow !== undefined) {
                            selectedObject.arrow.setFill(color);
                            selectedObject.arrow.setStroke(color);
                        }
                        if(selectedObject.circle !== undefined) {
                            selectedObject.circle.setFill(color);
                            selectedObject.circle.setStroke(color);
                        }
                    } else if (selectedObject.get('type') === 'path-group') {
                        for (var path of selectedObject.paths) {
                            if (path.getFill() != "") {
                                path.setFill(color);
                            }
                        }
                    } else if (selectedObject.get('type') === 'path' || selectedObject.get('type') === 'fabricObject') {
                        selectedObject.setStroke(color);
                    } else if (selectedObject.get('type') === 'fillRect') {
                        selectedObject.setStroke(color);
                        selectedObject.setFill(color);
                    } else {
                        selectedObject.setFill(color);
                    }

                    myThis.fabricCanvas.renderAll();
                },
                beforeShow: function (color) {
                    jQuery(this).spectrum("option", 'color', currentObjectColor);
                }
            });
        });

        myThis.fabricCanvas.on('selection:cleared', function () {
            var selectedObjectControls = jQuery('#screenshotMechanism' + myThis.screenshotMechanism.id + ' .selected-object-controls');
            selectedObjectControls.hide();
            selectedObjectControls.find('.delete').off();
            selectedObjectControls.find('.color').off();
            myThis.blockPanning = false;
        });

        myThis.fabricCanvas.on('object:added', function (object) {
            if (myThis.freehandActive) {
            }
        });

        myThis.fabricCanvas.on('object:scaling', function (e) {
            var object = e.target;
            if (object.type === 'fabricObject') {
                var o = e.target;
                if (!o.strokeWidthUnscaled && o.strokeWidth) {
                    o.strokeWidthUnscaled = o.strokeWidth;
                }
                if (o.strokeWidthUnscaled) {
                    o.strokeWidth = o.strokeWidthUnscaled / o.scaleX;
                }
            }
        });
    }

    initZoom() {
        var myThis = this;
        this.container.find('img.zoom-in').on('click', function() {
            myThis.fabricCanvas.zoomToPoint(new fabric.Point(myThis.fabricCanvas.width / 2, myThis.fabricCanvas.height / 2), myThis.fabricCanvas.getZoom() * 1.1);
        });
        this.container.find('img.zoom-out').on('click', function() {
            myThis.fabricCanvas.zoomToPoint(new fabric.Point(myThis.fabricCanvas.width / 2, myThis.fabricCanvas.height / 2), myThis.fabricCanvas.getZoom() / 1.1);
        });

        this.fabricCanvas.on('mouse:up', function (e) {
            myThis.panning = false;
            setTimeout(function () {
                clickBlocked = false;
            }, 100);
        });

        this.fabricCanvas.on('mouse:down', function (e) {
            myThis.panning = true;
            clickBlocked = true;
        });

        this.fabricCanvas.on('mouse:move', function (e) {
            if (!myThis.croppingIsActive && !myThis.freehandActive && !myThis.blockPanning && myThis.panning && e && e.e) {
                var delta = new fabric.Point(e.e.movementX, e.e.movementY);
                myThis.fabricCanvas.relativePan(delta);

                myThis.canvasMovementX += e.e.movementX;
                myThis.canvasMovementY += e.e.movementY;
            }
        });

        // retina and co
        setTimeout(function() {
            if (window.devicePixelRatio !== 1) {
                var zoom = 1/window.devicePixelRatio;
                myThis.fabricCanvas.setZoom(zoom);
            }
        }, 500);
    }

    initCrop() {
        var myThis = this;
        var pos = [0, 0];
        var canvasBoundingRect = document.getElementById(canvasId).getBoundingClientRect();
        pos[0] = canvasBoundingRect.left;
        pos[1] = jQuery(myThis.screenshotCanvas).parent().offset().top;

        var mousex = 0;
        var mousey = 0;
        var crop = false;

        myThis.croppingRect = new fabric.Rect({
            fill: 'transparent',
            originX: 'left',
            originY: 'top',
            stroke: '#333',
            strokeDashArray: [4, 4],
            type: cropperTypeObjectIdentifier,
            opacity: 1,
            width: 1,
            height: 1
        });

        this.croppingIsActive = true;

        myThis.croppingRect.visible = false;
        this.fabricCanvas.add(myThis.croppingRect);

        this.fabricCanvas.on("mouse:down", function (event) {
            if (!myThis.croppingIsActive) {
                return;
            }

            let x = event.e.pageX - myThis.screenshotPreviewElement.offset().left,
                y = event.e.pageY - myThis.screenshotPreviewElement.offset().top;
            let point:IPoint = new fabric.Point(x, y);
            let transformedPoint = myThis.transformClickPointToCoordinates(point);

            myThis.croppingRect.left = transformedPoint.x;
            myThis.croppingRect.top = transformedPoint.y;

            myThis.croppingRect.visible = true;
            mousex = transformedPoint.x;
            mousey = transformedPoint.y;
            crop = true;
            myThis.fabricCanvas.bringToFront(myThis.croppingRect);
        });

        this.fabricCanvas.on("mouse:move", function (event) {
            if (crop && myThis.croppingIsActive) {
                let x = event.e.pageX - myThis.screenshotPreviewElement.offset().left,
                    y = event.e.pageY - myThis.screenshotPreviewElement.offset().top;
                let point:IPoint = new fabric.Point(x, y);
                let transformedPoint = myThis.transformClickPointToCoordinates(point);

                myThis.croppingRect.width = transformedPoint.x - mousex;
                myThis.croppingRect.height = transformedPoint.y - mousey;
            }
            myThis.fabricCanvas.renderAll();
        });

        this.fabricCanvas.on("mouse:up", function (event) {
            crop = false;
        });

        this.container.find('.screenshot-crop-cancel').show().on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            myThis.cancelCropping();
        });

        this.container.find('.screenshot-crop-confirm').show().off().on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            myThis.cropTheCanvas(myThis.croppingRect);
            myThis.croppingIsActive = false;
            jQuery(this).hide();
            jQuery('.screenshot-crop-cancel').hide();
            myThis.setCanvasObjectsMovement(false);
        });
    }

    transformClickPointToCoordinates(clickPoint:IPoint):IPoint {
        var invertedMatrix = fabric.util.invertTransform(this.fabricCanvas.viewportTransform);
        return fabric.util.transformPoint(clickPoint, invertedMatrix);
    }

    transformCoordinatesToClickPoint(coordinates:IPoint):IPoint {
        return fabric.util.transformPoint(coordinates, this.fabricCanvas.viewportTransform);
    }

    cancelCropping() {
        this.croppingIsActive = false;
        this.container.find('.screenshot-crop-cancel').hide();
        jQuery('.screenshot-crop-confirm').hide();
        if(this.croppingRect) {
            this.croppingRect.remove();
        }
        this.setCanvasObjectsMovement(false);
    }

    cropTheCanvas(croppingRect) {
        let canvas = this.fabricCanvas;
        let oldZoom = canvas.getZoom();
        // reset zoom temporarily to 1 as it simplifies the calculation a lot
        canvas.setZoom(1);

        this.container.find('.screenshot-draw-undo').show();

        // Cropping canvas according to cropper rectangle
        let croppedTop = croppingRect.top + 1;
        let croppedLeft = croppingRect.left + 1;
        let croppWidth = croppingRect.width - 2;
        let croppHeight = croppingRect.height - 2;

        let reverseTransformedPoint:IPoint = this.transformCoordinatesToClickPoint(new fabric.Point(croppedLeft, croppedTop));
        croppedTop = reverseTransformedPoint.y;
        croppedLeft = reverseTransformedPoint.x;

        croppingRect.remove();
        canvas.renderAll.bind(canvas);

        var factor = Math.min(canvas.getWidth() / croppWidth, canvas.getHeight() / croppHeight);
        this.updateCanvasState(croppedTop, croppedLeft, canvas.getZoom());

        var factorX = canvas.getWidth() / croppWidth;
        var factorY = canvas.getHeight() / croppHeight;

        // Shifting the elements accordingly
        for (var i = 0; i < canvas.getObjects().length; i++) {
            if(canvas.getObjects()[i].type === 'image') {
                canvas.getObjects()[i].left = canvas.getObjects()[i].left * factorX - croppedLeft;
                canvas.getObjects()[i].top = canvas.getObjects()[i].top * factorY - croppedTop;
                canvas.getObjects()[i].setCoords();
            } else {
                canvas.getObjects()[i].left = canvas.getObjects()[i].left - croppedLeft;
                canvas.getObjects()[i].top = canvas.getObjects()[i].top - croppedTop;
                canvas.getObjects()[i].setCoords();
            }
        }

        canvas.setWidth(croppWidth * factor - 1);
        canvas.setHeight(croppHeight * factor - 1);
        canvas.setZoom(oldZoom);
        canvas.setZoom(factor);
        canvas.renderAll.bind(canvas);
    }

    addTextAnnotation(left, top) {
        var text = new fabric.IText('Your text', {
            left: left,
            top: top,
            fontFamily: 'arial',
            fontSize: defaultFontSize
        });
        this.fabricCanvas.add(text);
        this.fabricCanvas.setActiveObject(text);
        text.enterEditing();
        text.hiddenTextarea.focus();
        text.selectAll();
    }

    initFreehandDrawing() {
        var myThis = this;
        var currentFreehandDrawingColor = "#000000";
        var freehandControls = jQuery('.freehand-controls');
        freehandControls.hide();

        myThis.fabricCanvas.freeDrawingBrush.width = 6;

        freehandControls.find('a.freehand-color').css('color', currentFreehandDrawingColor);
        freehandControls.find('a.freehand-color').off().spectrum({
            color: defaultColor,
            containerClassName: myThis.colorPickerCSSClass,
            showPaletteOnly: true,
            togglePaletteOnly: true,
            togglePaletteMoreText: 'more',
            togglePaletteLessText: 'less',
            palette: [
                ["#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff"],
                ["#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f"],
                ["#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                ["#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                ["#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                ["#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79"],
                ["#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47"],
                ["#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130"]
            ],
            change: function (color) {
                var color = color.toHexString();
                jQuery(this).css('color', color);
                myThis.fabricCanvas.freeDrawingBrush.color = color;
                myThis.fabricCanvas.renderAll();
            }
        });

        jQuery('.screenshot-operations .freehand').on('click', function () {
            if (myThis.fabricCanvas.isDrawingMode) {
                myThis.disableFreehandDrawing();
            } else {
                myThis.enableFreehandDrawing();
            }
        });
    }

    enableFreehandDrawing() {
        this.cancelCropping();
        this.disableCurrentObjectInToolbar();
        var freehandControls = jQuery('.freehand-controls');
        this.fabricCanvas.isDrawingMode = true;
        jQuery('.screenshot-operations .freehand').css('border-bottom', '2px solid black');
        freehandControls.show();
        this.freehandActive = true;
        this.blockPanning = true;
    }

    disableFreehandDrawing() {
        var freehandControls = jQuery('.freehand-controls');
        jQuery('.screenshot-operations .freehand').css('border-bottom', 'none');
        this.fabricCanvas.isDrawingMode = false;
        freehandControls.hide();
        this.freehandActive = false;
        this.blockPanning = false;
    }

    disableCurrentObjectInToolbar() {
        if(this.currentObjectInToolbar) {
            this.currentObjectInToolbar.css('border-bottom', 'none');
            this.currentObjectInToolbar = null;
        }
    }

    initStickers() {
        var myThis = this;

        myThis.container.find('.sticker-source').on('click', function() {
            myThis.disableFreehandDrawing();
            myThis.cancelCropping();
            if(myThis.currentObjectInToolbar) {
                myThis.currentObjectInToolbar.css('border-bottom', 'none');
            }
            if(myThis.currentObjectInToolbar !== null && myThis.currentObjectInToolbar.attr('id') === jQuery(this).attr('id')) {
                myThis.currentObjectInToolbar = null;
            } else {
                myThis.currentObjectInToolbar = jQuery(this);
                jQuery(this).css('border-bottom', '2px solid black');
            }
        });
        myThis.screenshotPreviewElement.on('click', function(event) {
           if(myThis.currentObjectInToolbar !== null) {
                var sticker = myThis.currentObjectInToolbar;

               var p = {x: event.pageX - $(this).offset().left, y: event.pageY - $(this).offset().top};
               var invertedMatrix = fabric.util.invertTransform(myThis.fabricCanvas.viewportTransform);
               var transformedP = fabric.util.transformPoint(p, invertedMatrix);

               var offsetX = transformedP.x;
               var offsetY = transformedP.y;

               if (sticker.hasClass('text')) {
                   myThis.addTextAnnotation(offsetX, offsetY);
               } else if (sticker.hasClass('svg-sticker-source')) {
                   fabric.loadSVGFromURL(sticker.attr('src'), function (objects, options) {
                       var svgObject = fabric.util.groupSVGElements(objects, options);
                       svgObject.set('left', offsetX);
                       svgObject.set('top', offsetY);
                       svgObject.set('hasBorders', myThis.hasBordersAndControls);
                       svgObject.set('hasControls', myThis.hasBordersAndControls);
                       svgObject.scale(3);
                       myThis.fabricCanvas.add(svgObject).renderAll();
                       myThis.fabricCanvas.setActiveObject(svgObject);
                   });
               } else if (sticker.hasClass('object-source')) {
                   if (sticker.hasClass('arrow')) {
                       myThis.addArrowToCanvas(offsetX, offsetY);
                   } else if (sticker.hasClass('rect')) {
                       var rect = new fabric.Rect({
                           left: offsetX,
                           top: offsetY,
                           width: 50,
                           height: 50,
                           hasBorders: myThis.hasBordersAndControls,
                           hasControls: myThis.hasBordersAndControls,
                           type: 'fabricObject',
                           stroke: defaultColor,
                           strokeWidth: myThis.defaultStrokeWidth,
                           lockUniScaling: false,
                           fill: 'transparent'
                       });
                       myThis.fabricCanvas.add(rect).renderAll();
                       myThis.fabricCanvas.setActiveObject(rect);
                   } else if (sticker.hasClass('fillRect')) {
                       var rect = new fabric.Rect({
                           left: offsetX,
                           top: offsetY,
                           width: 50,
                           height: 50,
                           hasBorders: myThis.hasBordersAndControls,
                           hasControls: myThis.hasBordersAndControls,
                           type: 'fillRect',
                           stroke: defaultColor,
                           strokeWidth: myThis.defaultStrokeWidth,
                           lockUniScaling: false,
                           fill: defaultColor
                       });
                       myThis.fabricCanvas.add(rect).renderAll();
                       myThis.fabricCanvas.setActiveObject(rect);
                   } else if (sticker.hasClass('circle')) {
                       var circle = new fabric.Circle({
                           left: offsetX,
                           top: offsetY,
                           radius: 50,
                           hasBorders: myThis.hasBordersAndControls,
                           hasControls: myThis.hasBordersAndControls,
                           startAngle: 0,
                           type: 'fabricObject',
                           endAngle: 2 * Math.PI,
                           stroke: defaultColor,
                           strokeWidth: myThis.defaultStrokeWidth,
                           fill: 'transparent'
                       });
                       myThis.fabricCanvas.add(circle).renderAll();
                       myThis.fabricCanvas.setActiveObject(circle);
                   }
               }
               myThis.currentObjectInToolbar.css('border-bottom', 'none');
               myThis.currentObjectInToolbar = null;
           }
        });


        myThis.container.find('.sticker-source').draggable({
            cursor: "crosshair",
            revert: "invalid",
            helper: "clone",
            zIndex: 5000,
            drag: function (event:Event, ui) {
                myThis.disableFreehandDrawing();
                myThis.screenshotPreviewElement.css('border-style', 'dashed');
            },
            stop: function (event:DragEvent, ui) {
                myThis.screenshotPreviewElement.css('border-style', 'solid');
            }
        }).on('mouseover mouseenter', function () {
            myThis.screenshotPreviewElement.css('border-style', 'dashed');
        }).on('mouseleave', function () {
            myThis.screenshotPreviewElement.css('border-style', 'solid');
        });

        myThis.screenshotPreviewElement.droppable({
            drop: function (event:DragEvent, ui) {
                var sticker = $(ui.helper);

                var p = {x: event.pageX - $(this).offset().left, y: event.pageY - $(this).offset().top};
                var invertedMatrix = fabric.util.invertTransform(myThis.fabricCanvas.viewportTransform);
                var transformedP = fabric.util.transformPoint(p, invertedMatrix);

                var offsetX = transformedP.x;
                var offsetY = transformedP.y;

                if (sticker.hasClass('text')) {
                    myThis.addTextAnnotation(offsetX, offsetY);
                } else if (sticker.hasClass('svg-sticker-source')) {
                    fabric.loadSVGFromURL(sticker.attr('src'), function (objects, options) {
                        var svgObject = fabric.util.groupSVGElements(objects, options);
                        svgObject.set('left', offsetX);
                        svgObject.set('top', offsetY);
                        svgObject.set('hasBorders', myThis.hasBordersAndControls);
                        svgObject.set('hasControls', myThis.hasBordersAndControls);
                        svgObject.scale(3);
                        myThis.fabricCanvas.add(svgObject).renderAll();
                        myThis.fabricCanvas.setActiveObject(svgObject);
                    });
                } else if (sticker.hasClass('object-source')) {
                    if (sticker.hasClass('arrow')) {
                        myThis.addArrowToCanvas(offsetX, offsetY);
                    } else if (sticker.hasClass('rect')) {
                        var rect = new fabric.Rect({
                            left: offsetX,
                            top: offsetY,
                            width: 50,
                            height: 50,
                            hasBorders: myThis.hasBordersAndControls,
                            hasControls: myThis.hasBordersAndControls,
                            type: 'fabricObject',
                            stroke: defaultColor,
                            strokeWidth: myThis.defaultStrokeWidth,
                            lockUniScaling: false,
                            fill: 'transparent'
                        });
                        myThis.fabricCanvas.add(rect).renderAll();
                        myThis.fabricCanvas.setActiveObject(rect);
                    } else if (sticker.hasClass('fillRect')) {
                        var rect = new fabric.Rect({
                            left: offsetX,
                            top: offsetY,
                            width: 50,
                            height: 50,
                            hasBorders: myThis.hasBordersAndControls,
                            hasControls: myThis.hasBordersAndControls,
                            type: 'fillRect',
                            stroke: defaultColor,
                            strokeWidth: myThis.defaultStrokeWidth,
                            lockUniScaling: false,
                            fill: defaultColor
                        });
                        myThis.fabricCanvas.add(rect).renderAll();
                        myThis.fabricCanvas.setActiveObject(rect);
                    } else if (sticker.hasClass('circle')) {
                        var circle = new fabric.Circle({
                            left: offsetX,
                            top: offsetY,
                            radius: 50,
                            hasBorders: myThis.hasBordersAndControls,
                            hasControls: myThis.hasBordersAndControls,
                            startAngle: 0,
                            type: 'fabricObject',
                            endAngle: 2 * Math.PI,
                            stroke: defaultColor,
                            strokeWidth: myThis.defaultStrokeWidth,
                            fill: 'transparent'
                        });
                        myThis.fabricCanvas.add(circle).renderAll();
                        myThis.fabricCanvas.setActiveObject(circle);
                    }
                }
            }
        });
    }

    getScreenshotAsBinary() {
        if (this.screenshotCanvas) {
            var dataURL = this.screenshotCanvas.toDataURL("image/png");
            return DataHelper.dataURItoBlob(dataURL);
        } else {
            return null;
        }
    }

    addCaptureEventToButton() {
        var myThis = this;
        this.screenshotCaptureButton.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.makeScreenshot();
        });
    }

    hideElements() {
        if (this.elementsToHide != null) {
            for (var elementToHide of this.elementsToHide) {
                jQuery('' + elementToHide).hide();
            }
        }
    }

    showElements() {
        if (this.elementsToHide != null) {
            for (var elementToHide of this.elementsToHide) {
                jQuery('' + elementToHide).show();
            }
        }
    }

    getFeedback(): ScreenshotFeedback {
        return new ScreenshotFeedback(this.getPartName(), this.screenshotMechanism.id, this.getPartName(), 'png');
    };

    getPartName(): string {
        return "screenshot" + this.screenshotMechanism.id;
    }

    reset() {
        this.screenshotPreviewElement.hide();
        if (this.context) {
            this.context.clearRect(0, 0, this.context.width, this.context.height);
        }
        this.screenshotCanvas = null;
        this.canvasStates = [];
        this.container.find('.screenshot-operations').hide();

        this.disableAllScreenshotOperations();

        let screenshotCaptureButtonDefaultText = this.screenshotCaptureButton.data('default-text');
        this.screenshotCaptureButton.text(screenshotCaptureButtonDefaultText);
    }

    updateCanvasState(shiftTop:number, shiftLeft:number, zoomFactor:number) {
        var canvasState = new CanvasState(JSON.stringify(this.fabricCanvas), this.fabricCanvas.getWidth(), this.fabricCanvas.getHeight(), shiftTop, shiftLeft, zoomFactor);
        this.canvasStates.push(canvasState);
    }

    undoOperation() {
        if (this.canvasStates.length < 1) {
            return;
        }
        var myThis = this;
        var canvas = this.fabricCanvas;
        canvas.clear().renderAll();

        var canvasStateToRestore = this.canvasStates.pop();

        this.fabricCanvas.setWidth(canvasStateToRestore.width);
        this.fabricCanvas.setHeight(canvasStateToRestore.height);
        this.fabricCanvas.setZoom(canvasStateToRestore.zoomFactor);

        canvas.loadFromJSON(canvasStateToRestore.src, canvas.renderAll.bind(canvas), function (o, object) {
            // update page screenshot object
            if (object.type === 'image') {
                object.set('selectable', false);
                object.set('hoverCursor', 'default');
            } else if (object.type === cropperTypeObjectIdentifier) {
                object.remove();
            }
        });

        if (myThis.canvasStates.length < 1) {
            myThis.container.find('.screenshot-draw-undo').hide();
        }
    }

    initScreenshotOperations() {
        var myThis = this;

        this.container.find('.screenshot-crop').off().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.fabricCanvas.deactivateAll().renderAll();
            myThis.selectedObjectControls.hide();
            myThis.setCanvasObjectsMovement(true);
            myThis.disableFreehandDrawing();
            myThis.disableCurrentObjectInToolbar();
            myThis.initCrop();
        });

        this.container.find('.screenshot-draw-undo').off().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.undoOperation();
        });

        this.container.find('.screenshot-draw-remove').off().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.reset();
        });

        this.container.find('.screenshot-operations').show();
        this.container.find('.screenshot-operation.default-hidden').hide();
    }

    disableAllScreenshotOperations() {
        this.container.find('button.screenshot-operation').removeClass('active');
    }

    customizeControls() {
        var myThis = this;
        var colorLinkElement = jQuery('<a class="corner-color" href="#"><i class="material-icons">format_color_fill</i></a>');

        colorLinkElement.css('position', 'absolute');
        colorLinkElement.css('color', defaultColor);
        colorLinkElement.css('width', '16px');
        colorLinkElement.css('height', '16px');
        colorLinkElement.css('opacity', '0');

        var selectedObjectControls = jQuery('#screenshotMechanism' + myThis.screenshotMechanism.id + ' .selected-object-controls');

        fabric.Object.prototype.hide = function () {
            this.set({
                opacity: 0,
                selectable: false
            });
        };

        fabric.Object.prototype.show = function () {
            this.set({
                opacity: 1,
                selectable: true
            });
        };

        fabric.Canvas.prototype.customiseControls({
            mt: {
                action: function (e, target) {
                    var selectedObject = target;

                    if (target.get('type') === 'path-group') {
                        for (var path of target.paths) {
                            if (path.getFill() != "") {
                                var currentObjectColor = path.getFill();
                                break;
                            }
                        }
                    } else if (target.get('type') === 'path' || target.get('type') === 'fabricObject') {
                        var currentObjectColor = target.getStroke();
                    } else if (target.get('type') === 'fillRect') {
                        var currentObjectColor = target.getFill();
                    } else {
                        var currentObjectColor = target.getFill();
                    }

                    colorLinkElement.css('top', e.offsetY - 12 + 'px');
                    colorLinkElement.css('left', e.offsetX - 8 + 'px');
                    colorLinkElement.off().spectrum({
                        color: currentObjectColor,
                        containerClassName: myThis.colorPickerCSSClass,
                        showPaletteOnly: true,
                        togglePaletteOnly: true,
                        togglePaletteMoreText: 'more',
                        togglePaletteLessText: 'less',
                        palette: [
                            ["#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff"],
                            ["#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f"],
                            ["#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                            ["#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                            ["#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                            ["#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79"],
                            ["#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47"],
                            ["#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130"]
                        ],
                        change: function (color) {
                            var color = color.toHexString();
                            jQuery(this).css('color', color);

                            if (selectedObject.get('customType') === 'arrow') {
                                selectedObject.setFill(color);
                                selectedObject.setStroke(color);
                                if(selectedObject.line !== undefined) {
                                    selectedObject.line.setFill(color);
                                    selectedObject.line.setStroke(color);
                                }
                                if(selectedObject.arrow !== undefined) {
                                    selectedObject.arrow.setFill(color);
                                    selectedObject.arrow.setStroke(color);
                                }
                                if(selectedObject.circle !== undefined) {
                                    selectedObject.circle.setFill(color);
                                    selectedObject.circle.setStroke(color);
                                }
                            } else if (selectedObject.get('type') === 'path-group') {
                                for (var path of selectedObject.paths) {
                                    if (path.getFill() != "") {
                                        path.setFill(color);
                                    }
                                }
                            } else if (selectedObject.get('type') === 'path' || selectedObject.get('type') === 'fabricObject') {
                                selectedObject.setStroke(color);
                            } else if (selectedObject.get('type') === 'fillRect') {
                                selectedObject.setStroke(color);
                                selectedObject.setFill(color);
                            } else {
                                selectedObject.setFill(color);
                            }

                            selectedObjectControls.find('a.color').css('color', color);
                            myThis.fabricCanvas.renderAll();
                            jQuery(this).remove();
                        },
                        beforeShow: function (color) {
                            jQuery(this).spectrum("option", 'color', myThis.getObjectColor(target));
                        }
                    });
                    myThis.screenshotPreviewElement.append(colorLinkElement);
                    colorLinkElement.click();
                },
                cursor: 'pointer'
            },
            ml: {
                action: function (e, target) {
                    target.hide();
                    var fabricCanvas = myThis.fabricCanvas;

                    var activeObject = fabricCanvas.getActiveObject(),
                        activeGroup = fabricCanvas.getActiveGroup();

                    if(target.customType === 'arrow') {
                        fabricCanvas.deactivateAll().renderAll();
                        if (target.line !== undefined) {
                            target.line.remove();
                        }
                        if (target.arrow !== undefined) {
                            target.arrow.remove();
                        }
                        if (target.circle !== undefined) {
                            target.circle.remove();
                        }
                    } else if (activeGroup) {
                        var objectsInGroup = activeGroup.getObjects();
                        fabricCanvas.discardActiveGroup();
                        objectsInGroup.forEach(function (object) {
                            fabricCanvas.remove(object);
                        });
                    }
                    else if (activeObject) {
                        fabricCanvas.remove(activeObject);
                    }

                    fabricCanvas.renderAll.bind(fabricCanvas);
                },
                cursor: 'pointer'
            }
        });

        fabric.Object.prototype.customiseCornerIcons({
            settings: {
                borderColor: 'black',
                cornerSize: 24,
                cornerShape: 'rect',
                cornerPadding: 1
            },
            ml: {
                icon: myThis.distPath + 'img/ic_delete_black_24px_background.svg'
            },
            mt: {
                icon: myThis.distPath + 'img/ic_format_color_fill_black_24px_background.svg'
            }
        });
    }

    setDefaultStrokeWidth(strokeWidth:number) {
        this.defaultStrokeWidth = strokeWidth;
    }

    addArrowToCanvas(offsetX:number, offsetY:number) {
        var line,
            arrow,
            circle,
            myThis = this;

        line = new fabric.Line([offsetX, offsetY, offsetX + 50, offsetY + 50], {
            stroke: defaultColor,
            selectable: true,
            strokeWidth: 3,
            padding: 1,
            hasBorders: myThis.hasBordersAndControls,
            hasControls: myThis.hasBordersAndControls,
            originX: 'center',
            originY: 'center',
            lockScalingX: true,
            lockScalingY: true,
            lockRotation: true,
        });

        var centerX = (line.x1 + line.x2) / 2,
            centerY = (line.y1 + line.y2) / 2;
        var deltaX = line.left - centerX,
            deltaY = line.top - centerY;

        arrow = new fabric.Triangle({
            left: line.get('x1') + deltaX,
            top: line.get('y1') + deltaY,
            originX: 'center',
            originY: 'center',
            hasBorders: false,
            hasControls: false,
            lockScalingX: true,
            lockScalingY: true,
            lockRotation: true,
            padding: 0,
            pointType: 'arrow_start',
            angle: -45,
            width: 20,
            height: 20,
            fill: defaultColor
        });
        arrow.line = line;

        circle = new fabric.Circle({
            left: line.get('x2') + deltaX,
            top: line.get('y2') + deltaY,
            radius: 2,
            stroke: defaultColor,
            strokeWidth: 3,
            originX: 'center',
            originY: 'center',
            hasBorders: false,
            hasControls: false,
            lockScalingX: true,
            lockScalingY: true,
            lockRotation: true,
            padding: 0,
            pointType: 'arrow_end',
            fill: defaultColor
        });
        circle.line = line;

        line.customType = arrow.customType = circle.customType = 'arrow';
        line.circle = arrow.circle = circle;
        line.arrow = circle.arrow = arrow;

        myThis.fabricCanvas.add(line, arrow, circle);

        function moveEnd(obj) {
            var p = obj,
                x1, y1, x2, y2;

            if (obj.pointType === 'arrow_end') {
                obj.line.set('x2', obj.get('left'));
                obj.line.set('y2', obj.get('top'));
            } else {
                obj.line.set('x1', obj.get('left'));
                obj.line.set('y1', obj.get('top'));
            }

            obj.line._setWidthHeight();

            x1 = obj.line.get('x1');
            y1 = obj.line.get('y1');
            x2 = obj.line.get('x2');
            y2 = obj.line.get('y2');

            var angle = myThis.calcArrowAngle(x1, y1, x2, y2);

            if (obj.pointType === 'arrow_end') {
                obj.arrow.set('angle', angle - 90);
            } else {
                obj.set('angle', angle - 90);
            }

            obj.line.setCoords();
            myThis.fabricCanvas.renderAll();
        }

        function moveLine() {
            var oldCenterX = (line.x1 + line.x2) / 2,
                oldCenterY = (line.y1 + line.y2) / 2,
                deltaX = line.left - oldCenterX,
                deltaY = line.top - oldCenterY;

            line.arrow.set({
                'left': line.x1 + deltaX,
                'top': line.y1 + deltaY
            }).setCoords();

            line.circle.set({
                'left': line.x2 + deltaX,
                'top': line.y2 + deltaY
            }).setCoords();

            line.set({
                'x1': line.x1 + deltaX,
                'y1': line.y1 + deltaY,
                'x2': line.x2 + deltaX,
                'y2': line.y2 + deltaY
            });

            line.set({
                'left': (line.x1 + line.x2) / 2,
                'top': (line.y1 + line.y2) / 2
            });
        }

        arrow.on('moving', function () {
           moveEnd(arrow);
        });

        circle.on('moving', function () {
            moveEnd(circle);
        });

        line.on('moving', function () {
            moveLine();
        });
    }

    calcArrowAngle(x1, y1, x2, y2) {
        var angle = 0,
            x, y;

        x = (x2 - x1);
        y = (y2 - y1);

        if (x === 0) {
            angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
        } else if (y === 0) {
            angle = (x > 0) ? 0 : Math.PI;
        } else {
            angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);
        }

        return (angle * 180 / Math.PI);
    }

    setCanvasObjectsMovement(lock:boolean) {
        var objects = this.fabricCanvas.getObjects();

        for (var i = 0; i < objects.length; i++) {
            if(this.fabricCanvas.getObjects()[i].get('type') !== cropperTypeObjectIdentifier) {
                this.fabricCanvas.getObjects()[i].lockMovementX = lock;
                this.fabricCanvas.getObjects()[i].lockMovementX = lock;
            }
        }
    }

    getObjectColor(object:any) {
        if (object.get('type') === 'path-group') {
            for (var path of object.paths) {
                if (path.getFill() != "") {
                    var currentObjectColor = path.getFill();
                    break;
                }
            }
        } else if (object.get('type') === 'path' || object.get('type') === 'fabricObject') {
            var currentObjectColor = object.getStroke();
        } else if (object.get('type') === 'fillRect') {
            var currentObjectColor = object.getFill();
        } else {
            var currentObjectColor = object.getFill();
        }

        return currentObjectColor;
    }

    /**
     * Converts SVG objects (e.g. from highcharts lib) to a temporary canvas. This enables us to capture also SVG stuff
     * on the screenshot.
     */
    svgToCanvas() {
        var myThis = this;
        var svgElements = this.elementToCapture.find('svg:not(.jq-star-svg)');

        //replace all svgs with a temp canvas
        svgElements.each(function() {
            var canvas, xml;

            // canvg doesn't cope very well with em font sizes so find the calculated size in pixels and replace it in the element.
            jQuery.each(jQuery(this).find('[style*=em]'), function(index, el) {
                jQuery(this).css('font-size', myThis.getStyle(el, 'font-size'));
            });

            canvas = document.createElement("canvas");
            canvas.className = "screenShotTempCanvas";
            //convert SVG into a XML string
            xml = (new XMLSerializer()).serializeToString(this);

            // Removing the name space as IE throws an error
            xml = xml.replace(/xmlns=\"http:\/\/www\.w3\.org\/2000\/svg\"/, '');

            //draw the SVG onto a canvas
            canvg(canvas, xml);
            jQuery(canvas).insertAfter(this);
            //hide the SVG element
            jQuery(this).attr('class', 'temp-hide');
            jQuery(this).hide();
        });
    }

    showAllCanvasElements() {
        jQuery('.temp-hide').show();
    }

    getStyle(el, styleProp) {
        let camelize = function (str) {
            return str.replace(/\-(\w)/g, function(str, letter){
                return letter.toUpperCase();
            });
        };

        if (el.currentStyle) {
            return el.currentStyle[camelize(styleProp)];
        } else if (document.defaultView && document.defaultView.getComputedStyle) {
            return document.defaultView.getComputedStyle(el,null)
                .getPropertyValue(styleProp);
        } else {
            return el.style[camelize(styleProp)];
        }
    }
}














