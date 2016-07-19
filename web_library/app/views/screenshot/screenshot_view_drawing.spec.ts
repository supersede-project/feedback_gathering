import {ScreenshotViewDrawing} from './screenshot_view_drawing';


describe('Screenshot View Drawing', () => {
    let screenshotViewDrawing:ScreenshotViewDrawing;

    beforeEach(() => {
        screenshotViewDrawing = new ScreenshotViewDrawing();
    });

    it('should return the top left corner of a rectangle', () => {
        // start to draw in top left corner
        var startX = 5, startY = 5, endX = 15, endY = 10;
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[0]).toEqual(5);
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in top right corner
        var startX = 15, startY = 5, endX = 5, endY = 10;
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[0]).toEqual(5);
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in bottom left corner
        var startX = 5, startY = 10, endX = 15, endY = 5;
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[0]).toEqual(5);
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in bottom right corner
        var startX = 15, startY = 10, endX = 5, endY = 5;
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[0]).toEqual(5);
        expect(screenshotViewDrawing.getRectangleTopLeftCorner(startX, startY, endX, endY)[1]).toEqual(5);
    });

    it('should return the width and height of a rectangle', () => {
        // start to draw in top left corner
        var startX = 5, startY = 5, endX = 15, endY = 10;
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[0]).toEqual(10);
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in top right corner
        var startX = 15, startY = 5, endX = 5, endY = 10;
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[0]).toEqual(10);
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in bottom left corner
        var startX = 5, startY = 10, endX = 15, endY = 5;
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[0]).toEqual(10);
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[1]).toEqual(5);

        // start to draw in bottom right corner
        var startX = 15, startY = 10, endX = 5, endY = 5;
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[0]).toEqual(10);
        expect(screenshotViewDrawing.getRectangleWidthAndHeight(startX, startY, endX, endY)[1]).toEqual(5);
    });

    it('should return the new dimensions for a cropped image such that it uses the canvas space ideally', () => {
        var startX = 5, startY = 5, endX = 15, endY = 10, canvasWidth = 20, canvasHeight = 20;
        var newDimensions = screenshotViewDrawing.getNewDimensionsAfterCrop(startX, startY, endX, endY, canvasWidth, canvasHeight);
        expect(newDimensions[0]).toEqual(20);
        expect(newDimensions[1]).toEqual(10);

        var startX = 10, startY = 15, endX = 5, endY = 5, canvasWidth = 20, canvasHeight = 20;
        var newDimensions = screenshotViewDrawing.getNewDimensionsAfterCrop(startX, startY, endX, endY, canvasWidth, canvasHeight);
        expect(newDimensions[0]).toEqual(10);
        expect(newDimensions[1]).toEqual(20);
    });

});

