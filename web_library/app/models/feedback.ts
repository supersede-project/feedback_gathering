import {Rating} from './rating';
import {MechanismService} from '../services/mechanism_service';
import {textType, ratingType} from './mechanism';


const validationMessages = {
  textMechanism: {
      noText: 'Please input a text'
  }
};


export class Feedback {
    title:string;
    application:string;
    user:string;
    text:string;
    configVersion:number;
    ratings:Rating[];

    constructor(title?:string, application?:string, user?:string, text?:string, configVersion?:number, ratings?:Rating[]) {
        this.title = title;
        this.application = application;
        this.user = user;
        this.text = text;
        this.configVersion = configVersion;
        this.ratings = ratings;
    }

    /**
     * @param mechanismService
     *  The mechanismService object used to configure the feedback mechanisms.
     * @returns
     *  If the validation was successful: true
     *  Otherwise: An object with error messages
     */
    validate(mechanismService: MechanismService): any {
        var textMechanism = mechanismService.getMechanismConfig(textType);
        var ratingMechanism = mechanismService.getMechanismConfig(ratingType);
        var errors = {textMechanism: [], ratingMechanism: [], general: []};

        this.validateTextMechanism(textMechanism, errors);

        if(errors.textMechanism.length === 0 && errors.ratingMechanism.length === 0 && errors.general.length === 0) {
            return true;
        } else {
            return errors;
        }
    }

    private validateTextMechanism(textMechanism, errors) {
        if(textMechanism) {
            if(this.text === null || this.text === '') {
                errors.textMechanism.push(validationMessages.textMechanism.noText);
            }
        }
    }
}
