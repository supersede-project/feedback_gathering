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


export class FeedbackSettings {
    feedbackQuery:string;
    feedbackQueryChannel:string;
    globalFeedbackSetting:boolean;
    statusUpdates:string;
    statusUpdatesContactChannel:string;

    constructor(feedbackQuery: string, feedbackQueryChannel: string, globalFeedbackSetting: boolean, statusUpdates: string, statusUpdatesContactChannel: string) {
        this.feedbackQuery = feedbackQuery;
        this.feedbackQueryChannel = feedbackQueryChannel;
        this.globalFeedbackSetting = globalFeedbackSetting;
        this.statusUpdates = statusUpdates;
        this.statusUpdatesContactChannel = statusUpdatesContactChannel;
    }
}

