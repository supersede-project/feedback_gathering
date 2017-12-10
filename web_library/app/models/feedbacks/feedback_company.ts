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


export class FeedbackCompany {
    feedbackId:number;
    userId:number;
    chatText:string;
    initiatedByUser:boolean;

    constructor(feedbackId: number, userId: number, chatText: string, initiatedByUser: boolean) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.chatText = chatText;
        this.initiatedByUser = initiatedByUser;
    }
}

