/**
 * Some drawing and helper functions.
 *
 * Hint: Top left corner in canvas has coordinates 0,0.
 */
export class ScreenshotViewDrawing {

    getRectangleTopLeftCorner(startX, startY, endX, endY) {
        return [Math.min(startX, endX), Math.min(startY, endY)]
    }

    getRectangleWidthAndHeight(startX, startY, endX, endY) {
        return [Math.abs(startX - endX), Math.abs(startY - endY)]
    }

    getNewDimensionsAfterCrop(startX, startY, endX, endY, canvasWidth, canvasHeight) {
        var croppedImageDimensions = this.getRectangleWidthAndHeight(startX, startY, endX, endY);
        var croppedImageWidth = croppedImageDimensions[0];
        var croppedImageHeight = croppedImageDimensions[1];

        var ratioToScaleTheCroppedImage = Math.min(canvasWidth/croppedImageWidth, canvasHeight/croppedImageHeight);
        var scaledUpCroppedImageWidth = ratioToScaleTheCroppedImage * croppedImageWidth;
        var scaledUpCroppedImageHeight = ratioToScaleTheCroppedImage * croppedImageHeight;

        return [scaledUpCroppedImageWidth, scaledUpCroppedImageHeight];
    }
}



















