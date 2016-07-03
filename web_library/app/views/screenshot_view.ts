import {Mechanism} from '../models/mechanism';
import '../js/lib/html2canvas.js';
import {Helper} from '../js/helper';


export class ScreenshotView {
    screenshotMechanism:Mechanism;
    screenshotPreviewElement:JQuery;
    screenshotCaptureButton:JQuery;
    elementToCapture:JQuery;
    elementsToHide:[JQuery];
    screenshotCanvas:any;

    constructor(screenshotMechanism:Mechanism, screenshotPreviewElement:JQuery, screenshotCaptureButton:JQuery,
                elementToCapture:JQuery, elementsToHide:any) {
        this.screenshotMechanism = screenshotMechanism;
        this.screenshotPreviewElement = screenshotPreviewElement;
        this.screenshotCaptureButton = screenshotCaptureButton;
        this.elementToCapture = elementToCapture;
        this.elementsToHide = elementsToHide;
        this.addCaptureEventToButton();
    }

    generateScreenshot() {
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

                // save the canvas content as imageURL
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
    }

    getScreenshotAsBinary() {
        var dataURL = this.screenshotCanvas.toDataURL("image/png");
        return Helper.dataURItoBlob(dataURL);
    }

    addCaptureEventToButton() {
        var screenshotViewObject = this;
        this.screenshotCaptureButton.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            screenshotViewObject.generateScreenshot();
        });
    }
}
