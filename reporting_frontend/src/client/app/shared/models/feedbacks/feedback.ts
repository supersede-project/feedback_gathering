import {TextFeedback} from './text_feedback';
import {RatingFeedback} from './rating_feedback';
import {ScreenshotFeedback} from './screenshot_feedback';
import {CategoryFeedback} from './category_feedback';
import {PushConfiguration} from '../configurations/push_configuration';
import {mechanismTypes} from '../mechanisms/mechanism_types';
import {ConfigurationInterface} from '../configurations/configuration_interface';
import {FeedbackStatus} from './feedback_status';
import {AttachmentFeedback} from './attachment_feedback';
import {AudioFeedback} from './audio_feedback';


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
  audioFeedbacks:AudioFeedback[];
  attachmentFeedbacks:AttachmentFeedback[];
  createdAt:Date;
  personalFeedbackStatus:FeedbackStatus;
  read:boolean;

  constructor(title:string, userIdentification:string, language:string, applicationId:number, configurationId:number,
              ratingFeedbacks?:RatingFeedback[], textFeedbacks?:TextFeedback[],
              screenshotFeedbacks?:ScreenshotFeedback[], categoryFeedbacks?:CategoryFeedback[],
              audioFeedbacks?:AudioFeedback[], attachmentFeedbacks?:AttachmentFeedback[]) {
    this.title = title;
    this.userIdentification = userIdentification;
    this.language = language;
    this.applicationId = applicationId;
    this.configurationId = configurationId;
    this.ratingFeedbacks = ratingFeedbacks;
    this.textFeedbacks = textFeedbacks;
    this.screenshotFeedbacks = screenshotFeedbacks;
    this.categoryFeedbacks = categoryFeedbacks;
    this.audioFeedbacks = audioFeedbacks;
    this.attachmentFeedbacks = attachmentFeedbacks;
  }
}

