define(["require", "exports", '../js/helper', '../js/lib/html2canvas.js'], function (require, exports, helper_1) {
    "use strict";
    var ScreenshotView = (function () {
        function ScreenshotView(screenshotMechanism, screenshotPreviewElement, screenshotCaptureButton, elementToCapture, elementsToHide) {
            this.screenshotMechanism = screenshotMechanism;
            this.screenshotPreviewElement = screenshotPreviewElement;
            this.screenshotCaptureButton = screenshotCaptureButton;
            this.elementToCapture = elementToCapture;
            this.elementsToHide = elementsToHide;
            this.addCaptureEventToButton();
        }
        ScreenshotView.prototype.generateScreenshot = function () {
            $('.ui-widget-overlay.ui-front').hide();
            $('.ui-dialog').hide();
            var screenshotViewObject = this;
            html2canvas(this.elementToCapture, {
                onrendered: function (canvas) {
                    $('.ui-widget-overlay.ui-front').show();
                    $('.ui-dialog').show();
                    screenshotViewObject.screenshotPreviewElement.empty().append(canvas);
                    screenshotViewObject.screenshotPreviewElement.show();
                    var windowRatio = screenshotViewObject.elementToCapture.width() / screenshotViewObject.elementToCapture.height();
                    var data = canvas.toDataURL();
                    var context = canvas.getContext("2d");
                    $(canvas).prop('width', screenshotViewObject.screenshotPreviewElement.width());
                    $(canvas).prop('height', screenshotViewObject.screenshotPreviewElement.width() / windowRatio);
                    var img = new Image();
                    img.onload = function () {
                        context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                    };
                    img.src = data;
                    screenshotViewObject.screenshotCanvas = canvas;
                }
            });
        };
        ScreenshotView.prototype.getScreenshotAsBinary = function () {
            var dataURL = this.screenshotCanvas.toDataURL("image/png");
            return helper_1.Helper.dataURItoBlob(dataURL);
        };
        ScreenshotView.prototype.addCaptureEventToButton = function () {
            var screenshotViewObject = this;
            this.screenshotCaptureButton.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                screenshotViewObject.generateScreenshot();
            });
        };
        return ScreenshotView;
    }());
    exports.ScreenshotView = ScreenshotView;
});
