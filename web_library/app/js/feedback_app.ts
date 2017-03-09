import {ApplicationService} from '../services/application_service';
import {Application} from '../models/applications/application';
import {shuffle} from './helpers/array_shuffle';
import {FeedbackDialogView} from '../views/dialog/feedback_dialog_view';


export class FeedbackApp {
    applicationService:ApplicationService;
    application:Application;
    applicationId:number;
    options:any;
    feedbackButton:JQuery;
    dialogView:FeedbackDialogView;

    constructor(applicationService:ApplicationService, applicationId:number, options:{}, feedbackButton:JQuery) {
        this.applicationService = applicationService;
        this.applicationId = applicationId;
        this.options = options;
        this.feedbackButton = feedbackButton;
    }

    loadApplicationConfiguration() {
        this.applicationService.retrieveApplication(this.applicationId, loadedApplication => {
            this.application = loadedApplication;
            if (!loadedApplication.state) {
                this.feedbackButton.hide();
            }
            this.checkPullConfigurations(loadedApplication);
            this.configureDialog(loadedApplication);
            this.configureFeedbackButton();
            this.feedbackButton.show();
        }, errorData => {
            console.warn('SERVER ERROR ' + errorData.status + ' ' + errorData.statusText + ': ' + errorData.responseText);
            this.feedbackButton.hide();
        });
    };

    configureDialog(application:Application) {
        let dialogId = 'pushConfiguration';
        var dialogTemplate = require('../templates/feedback_dialog.handlebars');
        var context = application.getContextForView();
        context = $.extend({}, context, this.options);
        context = $.extend({}, context, application.getPushConfiguration().getContext());
        this.dialogView = new FeedbackDialogView(dialogId, dialogTemplate, application.getPushConfiguration(), context);
    }

    configureFeedbackButton() {
        var myThis = this;
        this.feedbackButton.css('background-color', this.options.backgroundColor);
        this.feedbackButton.css('color', this.options.color);
        this.feedbackButton.attr('title', this.application.generalConfiguration.getParameterValue('quickInfo'));
        this.feedbackButton.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            myThis.dialogView.toggleDialog();
        });
    }

    checkPullConfigurations(application:Application) {
        var alreadyTriggeredOne = false;
        for (var pullConfiguration of shuffle(application.getPullConfigurations())) {
            alreadyTriggeredOne = pullConfiguration.checkTrigger(alreadyTriggeredOne);
        }
    }
}