import {RatingMechanism} from './rating_mechanism';
import {Mechanism} from './mechanism';
import {ratingType, screenshotType} from '../../js/config';
import {ScreenshotMechanism} from './screenshot_mechanism';


export class MechanismFactory {
    /**
     * Factory to create corresponding mechanism type.
     *
     * @param data
     * @returns Mechanism
     *  The mechanism object parsed from json.
     */
    static createByData(data:any): any {
        if(!data.hasOwnProperty('type') || !data.hasOwnProperty('active')) {
            return null;
        }
        if(data.type === ratingType) {
            return new RatingMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, data.parameters);
        } else if (data.type === screenshotType) {
            return new ScreenshotMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, data.parameters);
        } else {
            return new Mechanism(data.id, data.type, data.active, data.order, data.canBeActivated, data.parameters);
        }
    }
}
