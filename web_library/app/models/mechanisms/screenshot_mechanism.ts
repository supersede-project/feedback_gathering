import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {ScreenshotView} from '../../views/screenshot/screenshot_view';


export class ScreenshotMechanism extends Mechanism {
    // TODO move this to a view class later on where the views and the configuration are put together
    screenshotView:ScreenshotView;

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[], screenshotView?:ScreenshotView) {
        super(id, type, active, order, canBeActivated, parameters);
        this.screenshotView = screenshotView;
    }

    setScreenshotView(screenshotView:ScreenshotView) {
        this.screenshotView = screenshotView;
    }

    getContext():any {
        return {
            autoTake: this.getParameterValue('autoTake'),
            annotationTitle: this.getParameterValue('annotationTitle'),
            annotationExplanation: this.getParameterValue('annotationExplanation'),
            cropTitle: this.getParameterValue('cropTitle'),
            cropExplanation: this.getParameterValue('cropExplanation'),
            sentimentSatisfiedMouseover: this.getParameterValue('sentimentSatisfiedMouseover'),
            sentimentDissatisfiedMouseover: this.getParameterValue('sentimentDissatisfiedMouseover'),
            arrowMouseover: this.getParameterValue('arrowMouseover'),
            circleMouseover: this.getParameterValue('circleMouseover'),
            rectMouseover: this.getParameterValue('rectMouseover'),
            filledRectMouseover: this.getParameterValue('filledRectMouseover'),
            thumbUpMouseover: this.getParameterValue('thumbUpMouseover'),
            thumbDownMouseover: this.getParameterValue('thumbDownMouseover'),
            textMouseover: this.getParameterValue('textMouseover'),
            freehandMouseover: this.getParameterValue('freehandMouseover'),
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px',
            screenshotTakeButtonTitle: this.getParameterValue('screenshotTakeButtonTitle'),
            screenshotTakeNewButtonTitle: this.getParameterValue('screenshotTakeNewButtonTitle'),
            screenshotRemoveButtonTitle: this.getParameterValue('screenshotRemoveButtonTitle'),
            textTitle: this.getParameterValue('textTitle'),
            textExplanation: this.getParameterValue('textExplanation'),
            zoomTitle: this.getParameterValue('zoomTitle'),
            annotationsTitle: this.getParameterValue('annotationsTitle'),
            selectionTitle: this.getParameterValue('selectionTitle'),
            colorTitle: this.getParameterValue('colorTitle'),
            cropTitle: this.getParameterValue('cropTitle')
            zoomExplanation: this.getParameterValue('zoomExplanation'),
        }
    }
}
