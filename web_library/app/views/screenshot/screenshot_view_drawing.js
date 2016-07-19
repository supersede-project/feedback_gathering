define(["require", "exports"], function (require, exports) {
    "use strict";
    var ScreenshotViewDrawing = (function () {
        function ScreenshotViewDrawing() {
        }
        ScreenshotViewDrawing.prototype.getRectangleTopLeftCorner = function (startX, startY, endX, endY) {
            return [Math.min(startX, endX), Math.min(startY, endY)];
        };
        ScreenshotViewDrawing.prototype.getRectangleWidthAndHeight = function (startX, startY, endX, endY) {
            return [Math.abs(startX - endX), Math.abs(startY - endY)];
        };
        ScreenshotViewDrawing.prototype.getNewDimensionsAfterCrop = function (startX, startY, endX, endY, canvasWidth, canvasHeight) {
            var croppedImageDimensions = this.getRectangleWidthAndHeight(startX, startY, endX, endY);
            var croppedImageWidth = croppedImageDimensions[0];
            var croppedImageHeight = croppedImageDimensions[1];
            var ratioToScaleTheCroppedImage = Math.min(canvasWidth / croppedImageWidth, canvasHeight / croppedImageHeight);
            var scaledUpCroppedImageWidth = ratioToScaleTheCroppedImage * croppedImageWidth;
            var scaledUpCroppedImageHeight = ratioToScaleTheCroppedImage * croppedImageHeight;
            return [scaledUpCroppedImageWidth, scaledUpCroppedImageHeight];
        };
        return ScreenshotViewDrawing;
    }());
    exports.ScreenshotViewDrawing = ScreenshotViewDrawing;
});
//# sourceMappingURL=screenshot_view_drawing.js.map