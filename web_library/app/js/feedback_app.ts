import {ApplicationService} from '../services/application_service';
import {Application} from '../models/applications/application';
import {shuffle} from './helpers/array_shuffle';


export class FeedbackApp {
    applicationService:ApplicationService;
    application:Application;
    applicationId:number;
    options:any;
    feedbackButton:JQuery;

    constructor(applicationService:ApplicationService, applicationId:number, options:{}, feedbackButton:JQuery) {
        this.applicationService = applicationService;
        this.applicationId = applicationId;
        this.options = options;
        this.feedbackButton = feedbackButton;
    }

    loadApplicationConfiguration() {
        this.applicationService.retrieveApplication(this.applicationId, loadedApplication => {
            if (!loadedApplication.state) {
                this.feedbackButton.hide();
            }
            this.checkPullConfigurations();
            this.configureFeedbackButton();
            this.feedbackButton.show();
        }, errorData => {
            console.warn('SERVER ERROR ' + errorData.status + ' ' + errorData.statusText + ': ' + errorData.responseText);
            this.feedbackButton.hide();
        });
    };

    configureFeedbackButton() {
        this.feedbackButton.css('background-color', this.options.backgroundColor);
        this.feedbackButton.css('color', this.options.color);
        this.feedbackButton.attr('title', this.application.generalConfiguration.getParameterValue('quickInfo'));
        this.feedbackButton.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            // TODO toggleDialog(application.getPushConfiguration());
        });
    }

    checkPullConfigurations() {
        var alreadyTriggeredOne = false;
        for (var pullConfiguration of shuffle(this.application.getPullConfigurations())) {
            alreadyTriggeredOne = pullConfiguration.checkTrigger(alreadyTriggeredOne);
        }
    }
}