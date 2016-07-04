import {Parameter} from './parameter';
import {RatingMechanism} from './rating_mechanism';
import {ratingType} from '../js/config';
import {Mechanism} from './mechanism';




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
            return new RatingMechanism(data.type, data.active, data.order, data.canBeActivated, data.parameters);
        } else {
            return new Mechanism(data.type, data.active, data.order, data.canBeActivated, data.parameters);
        }
    }
}
