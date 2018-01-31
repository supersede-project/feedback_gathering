import {RatingMechanism} from './rating_mechanism';
import {Mechanism} from './mechanism';
import {mechanismTypes} from '../../js/config';
import {ScreenshotMechanism} from './screenshot_mechanism';
import {Parameter} from '../parameters/parameter';
import {CategoryMechanism} from './category_mechanism';
import {TextMechanism} from './text_mechanism';
import {AudioMechanism} from './audio_mechanism';
import {AttachmentMechanism} from './attachment_mechanism';
import {InfoMechanism} from './info_mechanism';


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
        } else if (data.type === mechanismTypes.categoryType) {
            return new CategoryMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else if (data.type === mechanismTypes.textType) {
            return new TextMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else if (data.type === mechanismTypes.audioType) {
            return new AudioMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else if (data.type === mechanismTypes.attachmentType) {
            return new AttachmentMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else if (data.type === mechanismTypes.infoType) {
            return new InfoMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
        } else {
            console.warn('This mechanism type is not supported by the mechanism factory: ' + data.type);
            return null;
        }
    }
}
