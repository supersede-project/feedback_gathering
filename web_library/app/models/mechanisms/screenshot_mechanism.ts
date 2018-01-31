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

    getMaxResolutionWidthAndHeight():number[] {
        if(this.getParameterValue('maxResolution') === null || this.getParameterValue('maxResolution') === undefined) {
            return null;
        }

        let split = this.getParameterValue('maxResolution').split('x');
        if(split.length === 2) {
            return split;
        }
        return null;
    }

    getContext():any {
        var labelStyle = this.getCssStyle([
            new ParameterValuePropertyPair('labelColor', 'color'),
            new ParameterValuePropertyPair('labelFontSize', 'font-size')
        ]);

        return {
            annotationTitle: this.getParameterValue('annotationTitle'),
            annotationsTitle: this.getParameterValue('annotationsTitle'),
            annotationExplanation: this.getParameterValue('annotationExplanation'),
            arrowMouseover: this.getParameterValue('arrowMouseover'),
            autoTake: this.getParameterValue('autoTake'),
            bitdepth: this.getParameterValue('bitdepth'),
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px',
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            circleMouseover: this.getParameterValue('circleMouseover'),
            colorTitle: this.getParameterValue('colorTitle'),
            cropExplanation: this.getParameterValue('cropExplanation'),
            cropTitle: this.getParameterValue('cropTitle'),
            filledRectMouseover: this.getParameterValue('filledRectMouseover'),
            freehandMouseover: this.getParameterValue('freehandMouseover'),
            labelStyle: labelStyle,
            maxResolution: this.getParameterValue('maxResolution'),
            rectMouseover: this.getParameterValue('rectMouseover'),
            screenshotTakeButtonTitle: this.getParameterValue('screenshotTakeButtonTitle'),
            screenshotTakeNewButtonTitle: this.getParameterValue('screenshotTakeNewButtonTitle'),
            screenshotUrl: this.getParameterValue('screenshotUrl'),
            selectionTitle: this.getParameterValue('selectionTitle'),
            sentimentSatisfiedMouseover: this.getParameterValue('sentimentSatisfiedMouseover'),
            sentimentDissatisfiedMouseover: this.getParameterValue('sentimentDissatisfiedMouseover'),
            textExplanation: this.getParameterValue('textExplanation'),
            textMouseover: this.getParameterValue('textMouseover'),
            textTitle: this.getParameterValue('textTitle'),
            title: this.getParameterValue('title'),
            thumbDownMouseover: this.getParameterValue('thumbDownMouseover'),
            thumbUpMouseover: this.getParameterValue('thumbUpMouseover'),
            zoomTitle: this.getParameterValue('zoomTitle'),
            zoomExplanation: this.getParameterValue('zoomExplanation'),
        }
    }
}
