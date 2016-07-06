define(["require", "exports", '../js/helper', '../js/lib/html2canvas.js'], function (require, exports, helper_1) {
    "use strict";
    var myThis;
    var ScreenshotView = (function () {
        function ScreenshotView(screenshotMechanism, screenshotPreviewElement, screenshotCaptureButton, elementToCapture, elementsToHide) {
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
        ScreenshotView.prototype.generateScreenshot = function () {
            this.hideElements();
            html2canvas(this.elementToCapture, {
                onrendered: function (canvas) {
                    myThis.showElements();
                    myThis.screenshotPreviewElement.empty().append(canvas);
                    myThis.screenshotPreviewElement.show();
                    var windowRatio = myThis.elementToCapture.width() / myThis.elementToCapture.height();
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
        };
        ScreenshotView.prototype.getScreenshotAsBinary = function () {
            var dataURL = this.screenshotCanvas.toDataURL("image/png");
            return helper_1.Helper.dataURItoBlob(dataURL);
        };
        ScreenshotView.prototype.addCaptureEventToButton = function () {
            var myThis = this;
            this.screenshotCaptureButton.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.generateScreenshot();
            });
        };
        ScreenshotView.prototype.hideElements = function () {
            for (var _i = 0, _a = this.elementsToHide; _i < _a.length; _i++) {
                var elementToHide = _a[_i];
                elementToHide.hide();
            }
        };
        ScreenshotView.prototype.showElements = function () {
            for (var _i = 0, _a = this.elementsToHide; _i < _a.length; _i++) {
                var elementToHide = _a[_i];
                elementToHide.show();
            }
        };
        ScreenshotView.prototype.initDrawing = function () {
            var context = this.screenshotCanvas.getContext('2d');
            this.isPainting = false;
            this.drawingMode = 'rect';
            this.context.strokeStyle = "#FF0000";
            $(this.screenshotCanvas).on('mousedown touchstart', function (event) {
                var parentOffset = $(this).parent().offset();
                myThis.startX = event.pageX - parentOffset.left;
                myThis.startY = event.pageY - parentOffset.top;
                if (myThis.drawingMode === 'freehand') {
                    context.beginPath();
                    context.moveTo(myThis.startX, myThis.startY);
                }
                myThis.isPainting = true;
            }).on('mousemove touchmove', function (event) {
                if (myThis.isPainting) {
                    context.clearRect(0, 0, myThis.canvasWidth, myThis.canvasHeight);
                    context.drawImage(myThis.canvasState, 0, 0, myThis.canvasState.width, myThis.canvasState.height, 0, 0, myThis.screenshotCanvas.width, myThis.screenshotCanvas.height);
                    var parentOffset = $(this).parent().offset();
                    var currentX = event.pageX - parentOffset.left;
                    var currentY = event.pageY - parentOffset.top;
                    var width = currentX - myThis.startX;
                    var height = currentY - myThis.startY;
                    if (myThis.drawingMode === 'rect') {
                        context.beginPath();
                        context.strokeRect(myThis.startX, myThis.startY, width, height);
                    }
                    else if (myThis.drawingMode === 'fillRect') {
                        context.beginPath();
                        context.fillRect(myThis.startX, myThis.startY, width, height);
                    }
                    else if (myThis.drawingMode === 'circle') {
                        context.beginPath();
                        var radius = height > width ? height : width;
                        context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI * 2);
                    }
                    else if (myThis.drawingMode === 'freehand') {
                        context.lineTo(currentX, currentY);
                    }
                    else if (myThis.drawingMode === 'arrow') {
                        context.beginPath();
                        myThis.draw_arrow(context, myThis.startX, myThis.startY, currentX, currentY);
                    }
                    context.stroke();
                }
            }).on('mouseup touchend', function (event) {
                myThis.isPainting = false;
                var parentOffset = $(this).parent().offset();
                var endX = event.pageX - parentOffset.left;
                var endY = event.pageY - parentOffset.top;
                var width = endX - myThis.startX;
                var height = endY - myThis.startY;
                if (myThis.drawingMode === 'rect') {
                    context.rect(myThis.startX, myThis.startY, width, height);
                }
                else if (myThis.drawingMode === 'fillRect') {
                    context.fillRect(myThis.startX, myThis.startY, width, height);
                }
                else if (myThis.drawingMode === 'circle') {
                    var radius = height > width ? height : width;
                    context.arc(myThis.startX, myThis.startY, radius, 0, Math.PI * 2);
                }
                else if (myThis.drawingMode === 'freehand') {
                    context.lineTo(endX, endY);
                }
                else if (myThis.drawingMode === 'arrow') {
                    myThis.draw_arrow(context, myThis.startX, myThis.startY, endX, endY);
                }
                context.stroke();
                context.closePath();
                myThis.updateCanvasState();
            }).on('mouseleave touchleave', function () {
                myThis.isPainting = false;
            });
            myThis.initScreenshotOperations();
        };
        ScreenshotView.prototype.draw_arrow = function (context, fromx, fromy, tox, toy) {
            var headLength = 10;
            var angle = Math.atan2(toy - fromy, tox - fromx);
            context.moveTo(fromx, fromy);
            context.lineTo(tox, toy);
            context.lineTo(tox - headLength * Math.cos(angle - Math.PI / 6), toy - headLength * Math.sin(angle - Math.PI / 6));
            context.moveTo(tox, toy);
            context.lineTo(tox - headLength * Math.cos(angle + Math.PI / 6), toy - headLength * Math.sin(angle + Math.PI / 6));
        };
        ScreenshotView.prototype.updateCanvasState = function () {
            this.canvasStates.push(this.canvasState.src);
            this.canvasState.src = this.screenshotCanvas.toDataURL("image/png");
        };
        ScreenshotView.prototype.undoOperation = function () {
            if (this.canvasStates.length < 1) {
                return;
            }
            this.canvasState.src = this.canvasStates.pop();
            this.context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
            this.context.drawImage(this.canvasState, 0, 0, this.canvasState.width, this.canvasState.height, 0, 0, this.screenshotCanvas.width, this.screenshotCanvas.height);
        };
        ScreenshotView.prototype.initScreenshotOperations = function () {
            $('#screenshotDrawRect').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'rect';
                myThis.context.strokeStyle = "#FF0000";
                myThis.context.fillStyle = "#FF0000";
            });
            $('#screenshotDrawFillRect').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'fillRect';
                myThis.context.strokeStyle = "#000000";
                myThis.context.fillStyle = "#000000";
            });
            $('#screenshotDrawCircle').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'circle';
                myThis.context.strokeStyle = "#FF0000";
                myThis.context.fillStyle = "#FF0000";
            });
            $('#screenshotDrawArrow').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'arrow';
                myThis.context.strokeStyle = "#FF0000";
                myThis.context.fillStyle = "#FF0000";
            });
            $('#screenshotDrawFreehand').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'freehand';
                myThis.context.strokeStyle = "#FF0000";
                myThis.context.fillStyle = "#FF0000";
            });
            $('#screenshotCrop').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.disableAllScreenshotOperations();
                $(this).addClass('active');
                myThis.drawingMode = 'crop';
                myThis.context.strokeStyle = "#FF0000";
                myThis.context.fillStyle = "#FF0000";
            });
            $('#screenshotDrawUndo').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                myThis.undoOperation();
            });
            $('.screenshot-operations').show();
        };
        ScreenshotView.prototype.disableAllScreenshotOperations = function () {
            $('button.screenshot-operation').removeClass('active');
        };
        return ScreenshotView;
    }());
    exports.ScreenshotView = ScreenshotView;
});
