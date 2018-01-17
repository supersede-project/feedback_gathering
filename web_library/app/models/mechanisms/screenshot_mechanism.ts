import {Mechanism} from './mechanism';
import {ScreenshotView} from '../../views/screenshot/screenshot_view';
import {Parameter} from '../parameters/parameter';
import { ParameterValuePropertyPair } from '../parameters/parameter_value_property_pair';


export class ScreenshotMechanism extends Mechanism {
    screenshotView:ScreenshotView;

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[], screenshotView?:ScreenshotView) {
        super(id, type, active, order, canBeActivated, parameters);
        this.screenshotView = screenshotView;
    }

    setScreenshotView(screenshotView:ScreenshotView) {
        this.screenshotView = screenshotView;
    }

    getContext():any {
        var labelStyle = this.getCssStyle([
            new ParameterValuePropertyPair('labelColor', 'color'),
            new ParameterValuePropertyPair('labelFontSize', 'font-size')
        ]);

        return {
            autoTake: this.getParameterValue('autoTake'),
            annotationTitle: this.getParameterValue('annotationTitle'),
            annotationExplanation: this.getParameterValue('annotationExplanation'),
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
            labelStyle: labelStyle,
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px',
            screenshotTakeButtonTitle: this.getParameterValue('screenshotTakeButtonTitle'),
            screenshotTakeNewButtonTitle: this.getParameterValue('screenshotTakeNewButtonTitle'),
            screenshotUrl: this.getParameterValue('screenshotUrl'),
            textTitle: this.getParameterValue('textTitle'),
            textExplanation: this.getParameterValue('textExplanation'),
            title: this.getParameterValue('title'),
            zoomTitle: this.getParameterValue('zoomTitle'),
            annotationsTitle: this.getParameterValue('annotationsTitle'),
            selectionTitle: this.getParameterValue('selectionTitle'),
            colorTitle: this.getParameterValue('colorTitle'),
            cropTitle: this.getParameterValue('cropTitle'),
            zoomExplanation: this.getParameterValue('zoomExplanation'),
        }
    }
}
