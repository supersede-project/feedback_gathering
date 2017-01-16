import {ApplicationService} from '../services/application_service';
import {Application} from '../models/applications/application';
import {shuffle} from './helpers/array_shuffle';
import {DialogView} from '../views/dialog/dialog_view';
import {PageNavigation} from './helpers/page_navigation';
import {Configuration} from '../models/configurations/configuration';
import {PaginationContainer} from '../views/pagination_container';


export class FeedbackApp {
    applicationService:ApplicationService;
    application:Application;
    applicationId:number;
    options:any;
    feedbackButton:JQuery;
    dialogView:DialogView;

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
        this.dialogView = new DialogView('pushConfiguration', dialogTemplate, context);
        this.configurePageNavigation(application.getPushConfiguration(), dialogId);
    }

    configurePageNavigation(configuration:Configuration, dialogId:string) {
        let pageNavigation = new PageNavigation(configuration, $('#' + dialogId));
        new PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
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