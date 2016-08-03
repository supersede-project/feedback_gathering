import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {ScreenshotView} from '../../views/screenshot/screenshot_view';


export class ScreenshotMechanism extends Mechanism {
    // TODO move this to a view class later on where the views and the configuration are put together
    screenshotView:ScreenshotView;

    constructor(id: number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[], screenshotView?:ScreenshotView) {
        super(id, type, active, order, canBeActivated, parameters);
        this.screenshotView = screenshotView;
    }

    setScreenshotView(screenshotView:ScreenshotView) {
        this.screenshotView = screenshotView;
    }
}
