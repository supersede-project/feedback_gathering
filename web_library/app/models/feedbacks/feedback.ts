import {Rating} from './rating';
import {ConfigurationService} from '../services/configuration_service';
import {textType} from '../../js/config';
import {PushConfiguration} from '../configurations/push_configuration';


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
     * @param configuration
     *  The configuration object used to configure the feedback mechanisms.
     * @returns
     *  If the validation was successful: true
     *  Otherwise: An object with error messages
     */
    validate(configuration: PushConfiguration): any {
        var textMechanism = configuration.getMechanismConfig(textType);
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

