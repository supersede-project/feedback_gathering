import {Mechanism} from '../../models/mechanism';
import {ScreenshotViewDrawing} from './screenshot_view_drawing';
import {DataHelper} from '../../js/helpers/data_helper';
import '../../js/lib/html2canvas.js';


var myThis;
const freehandDrawingMode:string = 'freehandDrawingMode';
const rectDrawingMode:string = 'rectDrawingMode';
const fillRectDrawingMode:string = 'fillRectDrawingMode';
const circleDrawingMode:string = 'circleDrawingMode';
const arrowDrawingMode:string = 'arrowDrawingMode';
const croppingMode:string = 'croppingMode';
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

    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, elementsToHide?:any) {
        myThis = this;
        this.screenshotMechanism = screenshotMechanism;
        this.screenshotPreviewElement = screenshotPreviewElement;
        this.screenshotCaptureButton = screenshotCaptureButton;
        this.elementToCapture = elementToCapture;
        this.elementsToHide = elementsToHide;
        this.canvasState = null;
        this.canvasStates = [];
        this.screenshotViewDrawing = new ScreenshotViewDrawing();
        this.addCaptureEventToButton();
    }

    generateScreenshot() {
        this.hideElements();

        html2canvas(this.elementToCapture, {
            onrendered: function (canvas) {
                myThis.showElements();
                myThis.screenshotPreviewElement.empty().append(canvas);
                myThis.screenshotPreviewElement.show();

                var windowRatio = myThis.elementToCapture.width() / myThis.elementToCapture.height();

                // save the canvas content as imageURL
                var data = canvas.toDataURL("image/png");
                myThis.context = canvas.getContext("2d");
                myThis.canvasWidth = myThis.screenshotPreviewElement.width();
                myThis.canvasHeight = myThis.screenshotPreviewElement.width() / windowRatio;

                jQuery(canvas).prop('width', myThis.canvasWidth);
                jQuery(canvas).prop('height', myThis.canvasHeight);

                var img = new Image();
                img.src = data;
                img.onload = function () {
                    myThis.context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                };
                img.src = data;
                myThis.canvasState = img;
                myThis.screenshotCanvas = canvas;

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
        this.drawingMode = rectDrawingMode;
        this.context.strokeStyle = red;

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

                context.clearRect(0, 0, myThis.canvasWidth, myThis.canvasHeight);
                context.drawImage(myThis.canvasState, topLeftX, topLeftY, width,
                    height, 0, 0, newWidth, newHeight);
            }
            context.stroke();
            context.closePath();

            myThis.updateCanvasState();
        }).on('mouseleave touchleave', function () {
            myThis.isPainting = false;
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
        jQuery('.screenshot-operations').hide();

        this.disableAllScreenshotOperations();
        jQuery('#screenshotDrawRect').addClass('active');
        this.drawingMode = rectDrawingMode;
    }

    draw_arrow(context, fromx, fromy, tox, toy){
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
        jQuery('#screenshotDrawRect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = rectDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        jQuery('#screenshotDrawFillRect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = fillRectDrawingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([0,0]);
        });

        jQuery('#screenshotDrawCircle').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = circleDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        jQuery('#screenshotDrawArrow').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = arrowDrawingMode;
            myThis.context.stokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        jQuery('#screenshotDrawFreehand').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = freehandDrawingMode;
            myThis.context.strokeStyle = red;
            myThis.context.fillStyle = red;
            myThis.context.setLineDash([0,0]);
        });

        jQuery('#screenshotCrop').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            jQuery(this).addClass('active');
            myThis.drawingMode = croppingMode;
            myThis.context.strokeStyle = black;
            myThis.context.fillStyle = black;
            myThis.context.setLineDash([3, 8]);
        });

        jQuery('#screenshotDrawUndo').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.undoOperation();
        });

        jQuery('#screenshotDrawRemove').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.reset();
        });

        jQuery('.screenshot-operations').show();
    }

    disableAllScreenshotOperations() {
        jQuery('button.screenshot-operation').removeClass('active');
    }
}



















