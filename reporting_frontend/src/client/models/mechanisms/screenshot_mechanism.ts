import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {ScreenshotView} from '../../views/screenshot/screenshot_view';


export class ScreenshotMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }
}
