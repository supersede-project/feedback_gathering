import {ScreenshotViewDrawing} from './screenshot_view_drawing';
import {DataHelper} from '../../js/helpers/data_helper';
import '../../js/lib/html2canvas.js';
import {Mechanism} from '../../models/mechanisms/mechanism';
import 'fabric';

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
const red:string = "#FF0000";
const defaultColor:string = black;


export class ScreenshotView {
    screenshotMechanism:Mechanism;
    screenshotPreviewElement:JQuery;
    screenshotCaptureButton:JQuery;
    elementToCapture:JQuery;
    elementsToHide:[JQuery];
    screenshotCanvas:any;
    context:any;
    startX:number;
    startY:number;
    drawingMode:string;
    canvasState:HTMLImageElement;
    canvasStates:any;
    isPainting:boolean;
    canvasWidth:number;
    canvasHeight:number;
    screenshotViewDrawing:ScreenshotViewDrawing;
    canvasOriginalWidth:number;
    canvasOriginalHeight:number;
    container:JQuery;
    distPath:string;
    fabricCanvas;


    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, container:JQuery, distPath:string, elementsToHide?:any) {
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
            onrendered: function (canvas) {
                myThis.showElements();
                myThis.screenshotPreviewElement.empty().append(canvas);
                myThis.screenshotPreviewElement.show();
                var canvasId = 'screenshotCanvas';
                jQuery('.screenshot-preview canvas').attr('id', canvasId);

                var windowRatio = myThis.elementToCapture.width() / myThis.elementToCapture.height();

                // save the canvas content as imageURL
                var data = canvas.toDataURL("image/png");
                myThis.context = canvas.getContext("2d");
                myThis.canvasOriginalWidth = canvas.width;
                myThis.canvasOriginalHeight = canvas.height;

                myThis.canvasWidth = myThis.screenshotPreviewElement.width();
                myThis.canvasHeight = myThis.screenshotPreviewElement.width() / windowRatio;

                jQuery(canvas).prop('width', myThis.canvasWidth);
                jQuery(canvas).prop('height', myThis.canvasHeight);

                var img = new Image();
                img.crossOrigin = "Anonymous";
                myThis.canvasState = img;
                myThis.screenshotCanvas = canvas;
                img.src = data;

                let screenshotCaptureButtonActiveText = myThis.screenshotCaptureButton.data('active-text');
                myThis.screenshotCaptureButton.text(screenshotCaptureButtonActiveText);
                myThis.initDrawing();

                myThis.initFabric(canvasId, img, canvas);
                myThis.initSVGStickers();
            }
        });
    }

    initFabric(canvasId, img, canvas) {
        var myThis = this;
        this.fabricCanvas = new fabric.Canvas(canvasId);
        var oldCanvas = new fabric.Image(img, { width: canvas.width, height: canvas.height });

        oldCanvas.set('selectable', false);
        oldCanvas.set('hoverCursor', 'default');
        this.fabricCanvas.add(oldCanvas);

        fabric.Object.prototype.set({
            transparentCorners: true,
            borderColor: '#000000',
            cornerColor: '#545454',
            cornerSize: 9,
            padding: 3
        });

        var selectedObjectControls = jQuery('#screenshotMechanism' + myThis.screenshotMechanism.id + ' .selectedObjectControls');
        selectedObjectControls.hide();

        myThis.fabricCanvas.on('object:selected', function(e) {
            var selectedObject = e.target;

            if(selectedObject.get('type') !== 'path-group') {
                var currentObjectColor = selectedObject.getFill();
            } else {
                for(var path of selectedObject.paths) {
                    if(path.getFill() != "") {
                        var currentObjectColor = path.getFill();
                        break;
                    }
                }
            }

            selectedObjectControls.show();
            selectedObjectControls.find('.delete').off().on('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                if(selectedObject) {
                    selectedObject.remove();
                }
            });

            selectedObjectControls.find('a.color').css('color', currentObjectColor);
            selectedObjectControls.find('a.color').spectrum({
                color: defaultColor,
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

                    if(selectedObject.get('type') !== 'path-group') {
                        selectedObject.setFill(color);
                    } else {
                        for(var path of selectedObject.paths) {
                            if(path.getFill() != "") {
                                path.setFill(color);
                            }
                        }
                    }
                    myThis.fabricCanvas.renderAll();
                }
            });
        });

        myThis.fabricCanvas.on('selection:cleared', function() {
            var selectedObjectControls = jQuery('#screenshotMechanism' + myThis.screenshotMechanism.id + ' .selectedObjectControls');
            selectedObjectControls.hide();
            selectedObjectControls.find('.delete').off();
            selectedObjectControls.find('.color').off();
        });

    }

    addTextAnnotation(left, top) {
        var text = new fabric.IText('Your text', { left: left, top: top, fontFamily: 'arial black' });
        this.fabricCanvas.add(text);
    }

    initSVGStickers() {
        var myThis = this;
        myThis.container.find('.svg-sticker-source').draggable({
            cursor: "crosshair",
            revert: "invalid",
            helper: "clone",
            zIndex: 5000,
            drag: function(event:Event, ui) {
                myThis.screenshotPreviewElement.css('border-style', 'dashed');
            },
            stop: function(event:DragEvent, ui) {
                myThis.screenshotPreviewElement.css('border-style', 'solid');
            }
        }).on('mouseover mouseenter', function() {
            myThis.screenshotPreviewElement.css('border-style', 'dashed');
        }).on('mouseleave', function() {
            myThis.screenshotPreviewElement.css('border-style', 'solid');
        });

        myThis.screenshotPreviewElement.droppable({
            drop: function (event:DragEvent, ui) {
                var sticker = $(ui.helper);

                var offsetY = event.pageY - $(this).offset().top;
                var offsetX = event.pageX - $(this).offset().left;

                offsetY -= 12;
                offsetX -= 12;

                if(sticker.hasClass('text')) {
                    myThis.addTextAnnotation(offsetX, offsetY);
                } else {
                    fabric.loadSVGFromURL(sticker.attr('src'), function(objects, options) {
                        var obj = fabric.util.groupSVGElements(objects, options);
                        obj.set('left', offsetX);
                        obj.set('top', offsetY);
                        myThis.fabricCanvas.add(obj).renderAll();
                    });
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
                elementToHide.hide();
            }
        }
    }

    showElements() {
        if (this.elementsToHide != null) {
            for (var elementToHide of this.elementsToHide) {
                elementToHide.show();
            }
        }
    }

    initDrawing() {
        var context = this.screenshotCanvas.getContext('2d');
        this.isPainting = false;
        this.initRectAsDefaultDrawing();
        this.drawingMode = rectDrawingMode;
        this.context.strokeStyle = red;
        var myThis = this;

        jQuery(this.screenshotCanvas).on('mousedown touchstart', function (event) {
            var parentOffset = jQuery(this).parent().offset();
            myThis.startX = event.pageX - parentOffset.left;
            myThis.startY = event.pageY - parentOffset.top;

            if (myThis.drawingMode === freehandDrawingMode) {
                context.beginPath();
                context.moveTo(myThis.startX, myThis.startY);
            }
            myThis.isPainting = true;
        }).on('mousemove touchmove', function (event) {
            if (myThis.isPainting) {
                // reset canvas to the saved state to be able to provide a preview
                context.clearRect(0, 0, myThis.canvasWidth, myThis.canvasHeight);
                context.drawImage(myThis.canvasState, 0, 0, myThis.canvasState.width,
                    myThis.canvasState.height, 0, 0, myThis.screenshotCanvas.width,
                    myThis.screenshotCanvas.height);

                var parentOffset = jQuery(this).parent().offset();
                var currentX = event.pageX - parentOffset.left;
                var currentY = event.pageY - parentOffset.top;
                var width = currentX - myThis.startX;
                var height = currentY - myThis.startY;

                if (myThis.drawingMode === rectDrawingMode) {
                    // TODO extract all the drawing to various drawers that implement drawstart and drawend and implement the same interface
                    context.beginPath();
                    context.strokeRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === fillRectDrawingMode) {
                    context.beginPath();
                    context.fillRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === circleDrawingMode) {
                    context.beginPath();
                    var radius = height > width ? height : width;
                    context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI * 2);
                } else if (myThis.drawingMode === freehandDrawingMode) {
                    context.lineTo(currentX, currentY);
                } else if (myThis.drawingMode === arrowDrawingMode) {
                    context.beginPath();
                    myThis.draw_arrow(context, myThis.startX, myThis.startY, currentX, currentY);
                } else if (myThis.drawingMode === croppingMode) {
                    context.beginPath();
                    context.strokeRect(myThis.startX, myThis.startY, width, height);
                }
                context.stroke();
            }
        }).on('mouseup touchend', function (event) {
            myThis.isPainting = false;

            var parentOffset = jQuery(this).parent().offset();
            var endX = event.pageX - parentOffset.left;
            var endY = event.pageY - parentOffset.top;
            var width = endX - myThis.startX;
            var height = endY - myThis.startY;

            if (myThis.drawingMode === rectDrawingMode) {
                context.strokeRect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === fillRectDrawingMode) {
                context.fillRect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === circleDrawingMode) {
                var radius = height > width ? height : width;
                context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI * 2);
            } else if (myThis.drawingMode === freehandDrawingMode) {
                context.lineTo(endX, endY);
            } else if (myThis.drawingMode === arrowDrawingMode) {
                myThis.draw_arrow(context, myThis.startX, myThis.startY, endX, endY);
            } else if (myThis.drawingMode === croppingMode) {
                var topLeftCorner = myThis.screenshotViewDrawing.getRectangleTopLeftCorner(myThis.startX, myThis.startY, endX, endY);
                var widthAndHeight = myThis.screenshotViewDrawing.getRectangleWidthAndHeight(myThis.startX, myThis.startY, endX, endY);
                var newDimensions = myThis.screenshotViewDrawing.getNewDimensionsAfterCrop(myThis.startX, myThis.startY, endX, endY, myThis.screenshotCanvas.width, myThis.screenshotCanvas.height);

                width = widthAndHeight[0];
                height = widthAndHeight[1];
                var newWidth = newDimensions[0];
                var newHeight = newDimensions[1];
                var topLeftX = topLeftCorner[0];
                var topLeftY = topLeftCorner[1];


                /* if original image is the current canvas image we have to adjust some values, since the original
                 width and height differ from the width and height set after an annotation and redrawing and the
                 crop would be to small and not at the right position  */
                if (myThis.canvasStates === null || myThis.canvasStates.length === 0) {
                    width = myThis.canvasOriginalWidth / myThis.canvasWidth * width;
                    height = myThis.canvasOriginalHeight / myThis.canvasHeight * height;

                    topLeftX = myThis.canvasOriginalWidth / myThis.canvasWidth * topLeftX;
                    topLeftY = myThis.canvasOriginalHeight / myThis.canvasHeight * topLeftY;
                }

                context.clearRect(0, 0, myThis.canvasWidth, myThis.canvasHeight);
                context.drawImage(myThis.canvasState, topLeftX, topLeftY, width,
                    height, 0, 0, newWidth, newHeight);

                myThis.updateCanvasState();
            }
            context.stroke();
            context.closePath();
        }).on('mouseleave touchleave', function () {
            //myThis.isPainting = false;
        });

        myThis.initScreenshotOperations();
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

        this.initRectAsDefaultDrawing();
    }

    initRectAsDefaultDrawing() {
        var myThis = this;
        setTimeout(function () {
            myThis.container.find('.screenshot-draw-rect').addClass('active', 500);
            myThis.drawingMode = rectDrawingMode;
        }, 1500);
    }

    draw_arrow(context, fromx, fromy, tox, toy) {
        if (Math.sqrt(Math.pow(fromx - tox, 2) + Math.pow(fromy - toy, 2)) < 20) {
            return;
        }
        var headLength = 10;
        var angle = Math.atan2(toy - fromy, tox - fromx);
        context.moveTo(fromx, fromy);
        context.lineTo(tox, toy);
        context.lineTo(tox - headLength * Math.cos(angle - Math.PI / 6), toy - headLength * Math.sin(angle - Math.PI / 6));
        context.moveTo(tox, toy);
        context.lineTo(tox - headLength * Math.cos(angle + Math.PI / 6), toy - headLength * Math.sin(angle + Math.PI / 6));
    }

    updateCanvasState() {
        this.canvasStates.push(this.canvasState.src);
        this.canvasState.src = this.screenshotCanvas.toDataURL("image/png");
    }

    undoOperation() {
        if (this.canvasStates.length < 1) {
            return;
        }

        this.canvasState.src = this.canvasStates.pop();

        this.context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
        this.context.drawImage(this.canvasState, 0, 0, this.canvasState.width,
            this.canvasState.height, 0, 0, this.screenshotCanvas.width,
            this.screenshotCanvas.height);
    }

    initScreenshotOperations() {
        var myThis = this;
        this.container.find('.screenshot-draw-rect').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = rectDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0, 0]);
        });

        this.container.find('.screenshot-draw-fill-rect').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = fillRectDrawingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([0, 0]);
        });

        this.container.find('.screenshot-draw-circle').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = circleDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0, 0]);
        });

        this.container.find('.screenshot-draw-arrow').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = arrowDrawingMode;
            myThis.context.stokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0, 0]);
        });

        this.container.find('.screenshot-draw-freehand').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = freehandDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0, 0]);
        });

        this.container.find('.screenshot-crop').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = croppingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([3, 8]);
        });

        this.container.find('.screenshot-text').off('click').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            myThis.drawingMode = textMode;

            var imgSrc = jQuery(this).find('img').attr('src');
            var img = jQuery('<img src="' + imgSrc + '" class="sticker" />');
            var stickerContainer = jQuery('<div class="sticker-container text">' +
                '<a class="edit"><img src="' + myThis.distPath + 'img/ic_mode_edit_black_shadow_24px.png" /></a>' +
                '<a class="remove"><img src="' + myThis.distPath + 'img/ic_remove_circle_red_shadow_24px.png" /></a>' +
                '<article class="text-container">' +
                '<textarea placeholder="Your text"></textarea>' +
                '</article>' +
                '</div>');

            stickerContainer.css('width', '60px');
            stickerContainer.css('height', 'auto');
            stickerContainer.append(img);
            img.css('width', '100%');
            img.css('height', '100%');

            var containmentSelector = '#' + myThis.container.attr('id') + ' .screenshot-preview';

            stickerContainer.resizable({
                containment: containmentSelector
            });
            stickerContainer.draggable({
                cursor: "crosshair",
                containment: containmentSelector,
            });

            stickerContainer.find('a.remove').on('click', function () {
                jQuery(this).closest('.sticker-container').remove();
            });
            stickerContainer.find('a.edit').on('click', function () {
                var textContainer = stickerContainer.find('article.text-container');
                if (textContainer.is(":visible")) {
                    textContainer.hide();
                } else {
                    textContainer.show();
                }
            });

            myThis.screenshotPreviewElement.append(stickerContainer);
        });

        this.container.find('.screenshot-text-2').off('click').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            myThis.drawingMode = textMode2;

            var stickerContainer = jQuery('<div class="sticker-container text-2">' +
                '<textarea placeholder="Your text"></textarea>' +
                '<a class="remove"><img src="' + myThis.distPath + 'img/ic_remove_circle_red_shadow_24px.png" /></a>' +
                '<a class="color" style="background-image: url(\'' + myThis.distPath + 'img/screenshot_button_background.png\');"><i class="material-icons">format_color_text</i></a>' +
                '</div>');

            var textarea = stickerContainer.find('textarea');
            var colorLink = stickerContainer.find('a.color');
            var defaultColor = "#000";
            textarea.css('color', defaultColor);

            stickerContainer.css('width', 'auto');
            stickerContainer.css('height', 'auto');

            stickerContainer.find('a.color').spectrum({
                color: defaultColor,
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
                    textarea.css('color', color);
                    colorLink.css('color', color);
                }
            });

            var containmentSelector = '#' + myThis.container.attr('id') + ' .screenshot-preview';

            stickerContainer.resizable({
                containment: containmentSelector,
                resize: function () {
                    textArea.css('height', (stickerContainer.height() - 40) + 'px');
                    textArea.css('width', (stickerContainer.width() - 40) + 'px');
                }
            });
            stickerContainer.draggable({
                cursor: "crosshair",
                containment: containmentSelector,
            });
            stickerContainer.find('a.remove').on('click', function () {
                jQuery(this).closest('.sticker-container').remove();
            });

            myThis.screenshotPreviewElement.append(stickerContainer);

            var textArea = stickerContainer.find('textarea:first');
            textArea.on('keydown', function () {
                setTimeout(function () {
                    textArea.css('height', textArea[0].scrollHeight + 'px');
                    stickerContainer.css('height', textArea[0].scrollHeight + 40 + 'px');
                }, 1);
            });
            textArea.focus();
        });

        this.container.find('.screenshot-draw-undo').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.undoOperation();
        });

        this.container.find('.screenshot-draw-remove').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.reset();
        });

        this.container.find('.screenshot-operation.sticking').off('click').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            myThis.drawingMode = stickingMode;

            var imgSrc = jQuery(this).find('img').attr('src');
            var img = jQuery('<img src="' + imgSrc + '" class="sticker" />');
            var stickerContainer = jQuery('<div class="sticker-container"><a class="remove"><img src="' + myThis.distPath + 'img/ic_remove_circle_red_shadow_24px.png" /></a></div>');

            stickerContainer.css('width', '60px');
            stickerContainer.css('height', 'auto');
            stickerContainer.append(img);
            img.css('width', '100%');
            img.css('height', '100%');

            var containmentSelector = '#' + myThis.container.attr('id') + ' .screenshot-preview';

            stickerContainer.resizable({
                containment: containmentSelector
            });
            stickerContainer.draggable({
                cursor: "crosshair",
                containment: containmentSelector,
            });

            stickerContainer.find('a.remove').on('click', function () {
                jQuery(this).closest('.sticker-container').remove();
            });

            myThis.screenshotPreviewElement.append(stickerContainer);
        });

        this.container.find('.screenshot-operations').show();
    }

    getContentDiagonal(element) {
        var contentWidth = element.width();
        var contentHeight = element.height();
        return contentWidth * contentWidth + contentHeight * contentHeight;
    }

    disableAllScreenshotOperations() {
        this.container.find('button.screenshot-operation').removeClass('active');
    }
}














