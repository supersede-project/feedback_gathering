import {Mechanism} from '../models/mechanism';
import '../js/lib/html2canvas.js';
import {Helper} from '../js/helper';

var myThis;


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

    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, elementsToHide:any) {
        myThis = this;
        this.screenshotMechanism = screenshotMechanism;
        this.screenshotPreviewElement = screenshotPreviewElement;
        this.screenshotCaptureButton = screenshotCaptureButton;
        this.elementToCapture = elementToCapture;
        this.elementsToHide = elementsToHide;
        this.canvasState = null;
        this.canvasStates = [];
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
                var data = canvas.toDataURL();
                myThis.context = canvas.getContext("2d");
                myThis.canvasWidth = myThis.screenshotPreviewElement.width();
                myThis.canvasHeight = myThis.screenshotPreviewElement.width() / windowRatio;

                $(canvas).prop('width', myThis.canvasWidth);
                $(canvas).prop('height', myThis.canvasHeight);

                var img = new Image();
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
        var dataURL = this.screenshotCanvas.toDataURL("image/png");
        return Helper.dataURItoBlob(dataURL);
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
        for(var elementToHide of this.elementsToHide) {
            elementToHide.hide();
        }
    }

    showElements() {
        for(var elementToHide of this.elementsToHide) {
            elementToHide.show();
        }
    }

    initDrawing() {
        var context = this.screenshotCanvas.getContext('2d');
        this.isPainting = false;
        this.drawingMode = 'rect';
        this.context.strokeStyle="#FF0000";

        $(this.screenshotCanvas).on('mousedown touchstart', function(event) {
            var parentOffset = $(this).parent().offset();
            myThis.startX = event.pageX - parentOffset.left;
            myThis.startY = event.pageY - parentOffset.top;

            if(myThis.drawingMode === 'freehand') {
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

                var parentOffset = $(this).parent().offset();
                var currentX = event.pageX - parentOffset.left;
                var currentY = event.pageY - parentOffset.top;

                var width = currentX - myThis.startX;
                var height = currentY - myThis.startY;

                if(myThis.drawingMode === 'rect') {
                    context.beginPath();
                    context.strokeRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === 'fillRect') {
                    context.beginPath();
                    context.fillRect(myThis.startX, myThis.startY, width, height);
                } else if (myThis.drawingMode === 'circle') {
                    context.beginPath();
                    var radius = height > width ? height : width;
                    context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI*2);
                } else if (myThis.drawingMode === 'freehand') {
                    context.lineTo(currentX, currentY);
                    context.stroke();
                }
                context.stroke();
            }
        }).on('mouseup touchend', function(event) {
            myThis.isPainting = false;

            var parentOffset = $(this).parent().offset();
            var endX = event.pageX - parentOffset.left;
            var endY = event.pageY - parentOffset.top;

            var width = endX - myThis.startX;
            var height = endY - myThis.startY;

            if(myThis.drawingMode === 'rect') {
                context.rect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === 'fillRect') {
                context.fillRect(myThis.startX, myThis.startY, width, height);
            } else if (myThis.drawingMode === 'circle') {
                var radius = height > width ? height : width;
                context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI*2);
            } else if (myThis.drawingMode === 'freehand') {
                context.lineTo(endX, endY);
            }
            context.stroke();
            context.closePath();

            myThis.updateCanvasState();
        }).on('mouseleave touchleave', function () {
            myThis.isPainting = false;
        });

        myThis.initScreenshotOperations();
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
        $('#screenshotDrawRect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'rect';
            myThis.context.strokeStyle = "#FF0000";
            myThis.context.fillStyle = "#FF0000";
        });

        $('#screenshotDrawFillRect').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'fillRect';
            myThis.context.strokeStyle = "#000000";
            myThis.context.fillStyle = "#000000";
        });

        $('#screenshotDrawCircle').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'circle';
            myThis.context.strokeStyle = "#FF0000";
            myThis.context.fillStyle = "#FF0000";
        });

        $('#screenshotDrawArrow').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'arrow';
            myThis.context.strokeStyle = "#FF0000";
            myThis.context.fillStyle = "#FF0000";
        });

        $('#screenshotDrawFreehand').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'freehand';
            myThis.context.strokeStyle = "#FF0000";
            myThis.context.fillStyle = "#FF0000";
        });

        $('#screenshotCrop').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();

            myThis.disableAllScreenshotOperations();
            $(this).addClass('active');
            myThis.drawingMode = 'crop';
            myThis.context.strokeStyle = "#FF0000";
            myThis.context.fillStyle = "#FF0000";
        });

        $('#screenshotDrawUndo').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.undoOperation();
        });

        $('.screenshot-operations').show();
    }

    disableAllScreenshotOperations() {
        $('button.screenshot-operation').removeClass('active');
    }
}



















