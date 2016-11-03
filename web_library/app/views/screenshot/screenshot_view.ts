import {ScreenshotViewDrawing} from './screenshot_view_drawing';
import {DataHelper} from '../../js/helpers/data_helper';
import '../../js/lib/screenshot/html2canvas.js';
import {Mechanism} from '../../models/mechanisms/mechanism';
import {CanvasState} from './canvas_state';

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


export class ScreenshotView {
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
            this.generateScreenshot();
        }
    }

    generateScreenshot() {
        this.hideElements();
        var myThis = this;

        html2canvas(this.elementToCapture, {
            useCORS: true,
            onrendered: function (canvas) {
                myThis.showElements();
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
                myThis.canvasHeight = (myThis.screenshotPreviewElement.width() / windowRatio) -2;

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
            }
        });
    }

    initFabric(img, canvas) {
        var myThis = this;
        this.fabricCanvas = new fabric.Canvas(canvasId);
        this.determineCanvasScaleForRetinaDisplay();

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
            myThis.fabricCanvas.setZoom(myThis.fabricCanvas.getZoom() * 1.1);
        });
        this.container.find('img.zoom-out').on('click', function() {
            myThis.fabricCanvas.setZoom(myThis.fabricCanvas.getZoom() / 1.1);
        });

        this.fabricCanvas.on('mouse:up', function (e) {
            myThis.panning = false;
        });

        this.fabricCanvas.on('mouse:down', function (e) {
            myThis.panning = true;
        });

        this.fabricCanvas.on('mouse:move', function (e) {
            if (!myThis.croppingIsActive && !myThis.freehandActive && !myThis.blockPanning && myThis.panning && e && e.e) {
                var delta = new fabric.Point(e.e.movementX, e.e.movementY);
                myThis.fabricCanvas.relativePan(delta);
            }
        });
    }

    determineCanvasScaleForRetinaDisplay() {
        if (window.devicePixelRatio !== 1) {
            var screenshotPreviewCanvas = jQuery('.screenshot-preview canvas');
            var height = screenshotPreviewCanvas.height();
            var width = screenshotPreviewCanvas.width();
            var canvas = this.fabricCanvas.getElement();

            // TODO get this working!!!
            canvas.setAttribute('width', window.devicePixelRatio * width);
            canvas.setAttribute('height', window.devicePixelRatio * height);
            canvas.setAttribute('style', 'width="' + width + 'px"; height="' + height + 'px";');

            canvas.getContext('2d').scale(window.devicePixelRatio, window.devicePixelRatio);
        }
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

        var croppingRect = new fabric.Rect({
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

        croppingRect.visible = false;
        this.fabricCanvas.add(croppingRect);

        this.fabricCanvas.on("mouse:down", function (event) {
            if (!myThis.croppingIsActive) {
                return;
            }
            croppingRect.left = event.e.pageX - pos[0];
            croppingRect.top = event.e.pageY - pos[1];
            croppingRect.visible = true;
            mousex = event.e.pageX;
            mousey = event.e.pageY;
            crop = true;
            myThis.fabricCanvas.bringToFront(croppingRect);
        });

        this.fabricCanvas.on("mouse:move", function (event) {
            if (crop && myThis.croppingIsActive) {
                if (event.e.pageX - mousex > 0) {
                    croppingRect.width = event.e.pageX - mousex;
                }

                if (event.e.pageY - mousey > 0) {
                    croppingRect.height = event.e.pageY - mousey;
                }
            }
            myThis.fabricCanvas.renderAll();
        });

        this.fabricCanvas.on("mouse:up", function (event) {
            crop = false;
        });

        this.container.find('.screenshot-crop-cancel').show().on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            myThis.croppingIsActive = false;
            jQuery(this).hide();
            jQuery('.screenshot-crop-confirm').hide();
            croppingRect.remove();
            myThis.setCanvasObjectsMovement(false);
        });

        this.container.find('.screenshot-crop-confirm').show().off().on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            myThis.cropTheCanvas(croppingRect);
            myThis.croppingIsActive = false;
            jQuery(this).hide();
            jQuery('.screenshot-crop-cancel').hide();
            myThis.setCanvasObjectsMovement(false);
        });
    }

    cropTheCanvas(croppingRect) {
        this.container.find('.screenshot-draw-undo').show();

        var canvas = this.fabricCanvas;
        var objectsToMove = canvas.getObjects();

        // Cropping canvas according to cropper rectangle
        var croppedTop = croppingRect.top + 1;
        var croppedLeft = croppingRect.left + 1;
        var croppWidth = croppingRect.width - 2;
        var croppHeight = croppingRect.height - 2;
        croppingRect.remove();
        canvas.renderAll.bind(canvas);

        var factor = Math.min(canvas.getWidth() / croppWidth, canvas.getHeight() / croppHeight);
        this.updateCanvasState(croppedTop, croppedLeft, canvas.getZoom());

        // Shifting the elements accordingly
        for (var i = 0; i < objectsToMove.length; i++) {
            canvas.getObjects()[i].left = canvas.getObjects()[i].left - croppedLeft;
            canvas.getObjects()[i].top = canvas.getObjects()[i].top - croppedTop;
            canvas.getObjects()[i].setCoords();
        }

        canvas.setZoom(factor);
        canvas.setWidth(croppWidth * factor - 1);
        canvas.setHeight(croppHeight * factor - 1);

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
        var freehandControls = jQuery('.freehand-controls');
        this.fabricCanvas.isDrawingMode = true;
        jQuery('.screenshot-operations .freehand').css('border-bottom', '1px solid black');
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

    initStickers() {
        var myThis = this;
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

                if(myThis.fabricCanvas.getZoom() === 1) {
                    var offsetY = event.pageY - $(this).offset().top;
                    var offsetX = event.pageX - $(this).offset().left;
                } else {
                    var offsetY = 20;
                    var offsetX = 20;
                }

                offsetY -= 12;
                offsetX -= 12;

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
            myThis.generateScreenshot();
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
}














