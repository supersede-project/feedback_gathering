import {ScreenshotViewDrawing} from './screenshot_view_drawing';
import {DataHelper} from '../../js/helpers/data_helper';
import '../../js/lib/html2canvas.js';
import {Mechanism} from '../../models/mechanisms/mechanism';


const freehandDrawingMode:string = 'freehandDrawingMode';
const rectDrawingMode:string = 'rectDrawingMode';
const fillRectDrawingMode:string = 'fillRectDrawingMode';
const circleDrawingMode:string = 'circleDrawingMode';
const arrowDrawingMode:string = 'arrowDrawingMode';
const croppingMode:string = 'croppingMode';
const stickingMode:string = 'stickingMode';
const textMode:string = 'textMode';
const black:string = "#000000";
const red:string = "#FF0000";


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

    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, container:JQuery, elementsToHide?:any) {
        this.screenshotMechanism = screenshotMechanism;
        this.screenshotPreviewElement = screenshotPreviewElement;
        this.screenshotCaptureButton = screenshotCaptureButton;
        this.elementToCapture = elementToCapture;
        this.container = container;
        this.elementsToHide = elementsToHide;
        this.canvasState = null;
        this.canvasStates = [];
        this.screenshotViewDrawing = new ScreenshotViewDrawing();
        this.addCaptureEventToButton();
    }

    checkAutoTake() {
        if(this.screenshotMechanism.getParameterValue('autoTake')) {
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
                myThis.canvasState = img;
                myThis.screenshotCanvas = canvas;
                img.src = data;
                img.onload = function () {
                    myThis.context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                };

                myThis.initDrawing();
            }
        });
    }

    getScreenshotAsBinary() {
        if(this.screenshotCanvas) {
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
        if(this.elementsToHide != null) {
            for(var elementToHide of this.elementsToHide) {
                elementToHide.hide();
            }
        }
    }

    showElements() {
        if(this.elementsToHide != null) {
            for(var elementToHide of this.elementsToHide) {
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

        jQuery(this.screenshotCanvas).on('mousedown touchstart', function(event) {
            var parentOffset = jQuery(this).parent().offset();
            myThis.startX = event.pageX - parentOffset.left;
            myThis.startY = event.pageY - parentOffset.top;

            if(myThis.drawingMode === freehandDrawingMode) {
                context.beginPath();
                context.moveTo(myThis.startX, myThis.startY);
            }
            myThis.isPainting = true;
        }).on('mousemove touchmove', function(event) {
            if(myThis.isPainting) {
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

                if(myThis.drawingMode === rectDrawingMode) {
                    // TODO extract all the drawing to various drawers that implement drawstart and drawend and implement the same interface
                    context.beginPath();
                    context.strokeRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === fillRectDrawingMode) {
                    context.beginPath();
                    context.fillRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === circleDrawingMode) {
                    context.beginPath();
                    var radius = height > width ? height : width;
                    context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI*2);
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
        }).on('mouseup touchend', function(event) {
            myThis.isPainting = false;

            var parentOffset = jQuery(this).parent().offset();
            var endX = event.pageX - parentOffset.left;
            var endY = event.pageY - parentOffset.top;
            var width = endX - myThis.startX;
            var height = endY - myThis.startY;

            if(myThis.drawingMode === rectDrawingMode) {
                context.strokeRect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === fillRectDrawingMode) {
                context.fillRect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === circleDrawingMode) {
                var radius = height > width ? height : width;
                context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI*2);
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
            }
            context.stroke();
            context.closePath();

            myThis.updateCanvasState();
        }).on('mouseleave touchleave', function () {
            //myThis.isPainting = false;
        });

        myThis.initScreenshotOperations();
    }

    reset() {
        this.screenshotPreviewElement.hide();
        if(this.context) {
            this.context.clearRect(0, 0, this.context.width, this.context.height);
        }
        this.screenshotCanvas = null;
        this.canvasStates = [];
        this.container.find('.screenshot-operations').hide();

        this.disableAllScreenshotOperations();

        this.initRectAsDefaultDrawing();
    }

    initRectAsDefaultDrawing() {
        var myThis = this;
        setTimeout(function() {
            myThis.container.find('.screenshot-draw-rect').addClass('active', 500);
            myThis.drawingMode = rectDrawingMode;
        }, 1500);
    }

    draw_arrow(context, fromx, fromy, tox, toy){
        if(Math.sqrt(Math.pow(fromx - tox, 2) + Math.pow(fromy - toy, 2)) < 20) {
            return;
        }
        var headLength = 10;
        var angle = Math.atan2(toy-fromy,tox-fromx);
        context.moveTo(fromx, fromy);
        context.lineTo(tox, toy);
        context.lineTo(tox-headLength*Math.cos(angle-Math.PI/6),toy-headLength*Math.sin(angle-Math.PI/6));
        context.moveTo(tox, toy);
        context.lineTo(tox-headLength*Math.cos(angle+Math.PI/6),toy-headLength*Math.sin(angle+Math.PI/6));
    }

    updateCanvasState() {
        this.canvasStates.push(this.canvasState.src);
        this.canvasState.src = this.screenshotCanvas.toDataURL("image/png");
    }

    undoOperation() {
        if(this.canvasStates.length < 1) {
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
        this.container.find('.screenshot-draw-rect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = rectDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        this.container.find('.screenshot-draw-fill-rect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = fillRectDrawingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([0,0]);
        });

        this.container.find('.screenshot-draw-circle').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = circleDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        this.container.find('.screenshot-draw-arrow').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = arrowDrawingMode;
            myThis.context.stokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        this.container.find('.screenshot-draw-freehand').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = freehandDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        this.container.find('.screenshot-crop').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = croppingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([3, 8]);
        });

        this.container.find('.screenshot-text').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            myThis.drawingMode = textMode;

            var imgSrc = jQuery(this).find('img').attr('src');
            var img = jQuery('<img src="' + imgSrc + '" class="sticker" />');
            var stickerContainer = jQuery('<div class="sticker-container">' +
                '<a class="edit"><img src="dist/img/ic_mode_edit_black_shadow_24px.png"</a>' +
                '<a class="remove"><img src="dist/img/ic_remove_circle_red_shadow_24px.png"</a>' +
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

            stickerContainer.find('a.remove').on('click', function() {
                jQuery(this).closest('.sticker-container').remove();
            });

            myThis.screenshotPreviewElement.append(stickerContainer);
        });

        this.container.find('.screenshot-draw-undo').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.undoOperation();
        });

        this.container.find('.screenshot-draw-remove').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.reset();
        });

        this.container.find('.screenshot-operation.sticking').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            myThis.drawingMode = stickingMode;

            var imgSrc = jQuery(this).find('img').attr('src');
            var img = jQuery('<img src="' + imgSrc + '" class="sticker" />');
            var stickerContainer = jQuery('<div class="sticker-container"><a class="remove"><img src="dist/img/ic_remove_circle_red_shadow_24px.png"</a></div>');

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

            stickerContainer.find('a.remove').on('click', function() {
               jQuery(this).closest('.sticker-container').remove();
            });

            myThis.screenshotPreviewElement.append(stickerContainer);
        });

        this.container.find('.screenshot-operations').show();
    }

    disableAllScreenshotOperations() {
        this.container.find('button.screenshot-operation').removeClass('active');
    }
}



















