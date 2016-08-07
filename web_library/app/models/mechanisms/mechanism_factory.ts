import {RatingMechanism} from './rating_mechanism';
import {Mechanism} from './mechanism';
import {mechanismTypes} from '../../js/config';
import {ScreenshotMechanism} from './screenshot_mechanism';
import {Parameter} from '../parameters/parameter';


export class MechanismFactory {
    /**
     * Factory to create corresponding mechanism type.
     *
     * @param data
     * @returns Mechanism
     *  The mechanism object parsed from json.
     */
    static createByData(data:any): Mechanism {
        if(!data.hasOwnProperty('type')) {
            return null;
        }
        var parameters = [];
        for(var parameter of data.parameters) {
            parameters.push(Parameter.initByData(parameter));
        }

        if(data.type === mechanismTypes.ratingType) {
            return new RatingMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else if (data.type === mechanismTypes.screenshotType) {
            return new ScreenshotMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else {
            return new Mechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        }
    }
}
