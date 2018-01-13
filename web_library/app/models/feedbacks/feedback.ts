import {mechanismTypes} from '../../js/config';
import {PushConfiguration} from '../configurations/push_configuration';
import {TextFeedback} from './text_feedback';
import {RatingFeedback} from './rating_feedback';
import {ScreenshotFeedback} from './screenshot_feedback';
import {CategoryFeedback} from './category_feedback';
import {ContextInformation} from './context_information';
import {AttachmentMechanism} from '../mechanisms/attachment_mechanism';
import {AttachmentFeedback} from './attachment_feedback';
import {AudioFeedback} from './audio_feedback';


const validationMessages = {
  textMechanism: {
      noText: 'Please input a text'
  }
};


export class Feedback {
    title:string;
    userIdentification:string;
    language:string;
    applicationId:number;
    configurationId:number;
    ratingFeedbacks:RatingFeedback[];
    textFeedbacks:TextFeedback[];
    screenshotFeedbacks:ScreenshotFeedback[];
    categoryFeedbacks:CategoryFeedback[];
    contextInformation:ContextInformation;
    attachmentFeedbacks:AttachmentFeedback[];
    audioFeedbacks:AudioFeedback[];
    published:boolean;
    visibility:boolean;


    constructor(title: string, userIdentification: string, language: string, applicationId: number, configurationId: number, ratingFeedbacks: RatingFeedback[], textFeedbacks: TextFeedback[], screenshotFeedbacks: ScreenshotFeedback[], categoryFeedbacks: CategoryFeedback[], contextInformation: ContextInformation, attachmentFeedbacks: AttachmentFeedback[], audioFeedbacks: AudioFeedback[], published: boolean, visibility: boolean) {
        this.title = title;
        this.userIdentification = userIdentification;
        this.language = language;
        this.applicationId = applicationId;
        this.configurationId = configurationId;
        this.ratingFeedbacks = ratingFeedbacks;
        this.textFeedbacks = textFeedbacks;
        this.screenshotFeedbacks = screenshotFeedbacks;
        this.categoryFeedbacks = categoryFeedbacks;
        this.contextInformation = contextInformation;
        this.attachmentFeedbacks = attachmentFeedbacks;
        this.audioFeedbacks = audioFeedbacks;
        this.published = published;
        this.visibility = visibility;
    }

    constructor(title:string, userIdentification:string, language:string, applicationId:number, configurationId:number,
                ratingFeedbacks:RatingFeedback[], textFeedbacks:TextFeedback[], screenshotFeedbacks:ScreenshotFeedback[],
                categoryFeedbacks:CategoryFeedback[], contextInformation:ContextInformation,
                attachmentFeedbacks:AttachmentFeedback[], audioFeedbacks:AudioFeedback[]) {
        this.title = title;
        this.userIdentification = userIdentification;
        this.language = language;
        this.applicationId = applicationId;
        this.configurationId = configurationId;
        this.ratingFeedbacks = ratingFeedbacks;
        this.textFeedbacks = textFeedbacks;
        this.screenshotFeedbacks = screenshotFeedbacks;
        this.categoryFeedbacks = categoryFeedbacks;
        this.contextInformation = contextInformation;
        this.attachmentFeedbacks = attachmentFeedbacks;
        this.audioFeedbacks = audioFeedbacks;
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

