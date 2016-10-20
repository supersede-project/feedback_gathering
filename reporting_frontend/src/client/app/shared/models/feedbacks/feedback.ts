import {TextFeedback} from './text_feedback';
import {RatingFeedback} from './rating_feedback';
import {ScreenshotFeedback} from './screenshot_feedback';
import {CategoryFeedback} from './category_feedback';
import {PushConfiguration} from '../configurations/push_configuration';
import {mechanismTypes} from '../mechanisms/mechanism_types';
import {ConfigurationInterface} from '../configurations/configuration_interface';
import {FeedbackStatus} from './feedback_status';


const validationMessages = {
  textMechanism: {
      noText: 'Please input a text'
  }
};


export class Feedback {
    id:number;
    title:string;
    userIdentification:string;
    language:string;
    applicationId:number;
    configurationId:number;
    configuration:ConfigurationInterface;
    ratingFeedbacks:RatingFeedback[];
    textFeedbacks:TextFeedback[];
    screenshotFeedbacks:ScreenshotFeedback[];
    categoryFeedbacks:CategoryFeedback[];
    createdAt:Date;
    personalFeedbackStatus:FeedbackStatus;
    read:boolean;

    constructor(title:string, userIdentification:string, language:string, applicationId:number, configurationId:number,
                ratingFeedbacks?:RatingFeedback[], textFeedbacks?:TextFeedback[],
                screenshotFeedbacks?:ScreenshotFeedback[], categoryFeedbacks?:CategoryFeedback[]) {
        this.title = title;
        this.userIdentification = userIdentification;
        this.language = language;
        this.applicationId = applicationId;
        this.configurationId = configurationId;
        this.ratingFeedbacks = ratingFeedbacks;
        this.textFeedbacks = textFeedbacks;
        this.screenshotFeedbacks = screenshotFeedbacks;
        this.categoryFeedbacks = categoryFeedbacks;
    }

    /**
     * @param configuration
     *  The configuration object used to configure the feedback mechanisms.
     * @returns
     *  If the validation was successful: true
     *  Otherwise: An object with error messages
     */
    validate(configuration: PushConfiguration): any {
        var textMechanisms = configuration.getMechanismConfig(mechanismTypes.textType);
        var errors = {textMechanisms: [], ratingMechanisms: [], general: []};

        this.validateTextMechanism(textMechanisms, errors);

        if(errors.textMechanisms.length === 0 && errors.ratingMechanisms.length === 0 && errors.general.length === 0) {
            return true;
        } else {
            return errors;
        }
    }

    private validateTextMechanism(textMechanisms, errors) {
        for(var textMechanism of textMechanisms) {
            if(textMechanism) {
                if(textMechanism.text === null || textMechanism.text === '') {
                    errors.textMechanism.push(validationMessages.textMechanism.noText);
                }
            }
        }
    }
}

