import i18n = require('i18next');
import {MechanismView} from '../mechanism_view';
import {PageNavigation} from '../../js/helpers/page_navigation';
import {FeedbackDialogView} from './feedback_dialog_view';
import {PullConfiguration} from '../../models/configurations/pull_configuration';
import {QuestionDialogView} from './question_dialog_view';
import { mechanismTypes } from '../../js/config';


/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class PullFeedbackDialogView extends FeedbackDialogView {

    constructor(public dialogId:string, public template:any, public configuration:PullConfiguration, public context:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        super(dialogId, template, configuration, context, openCallback, closeCallback);
        this.checkMechanisms();
    }

    open() {
        this.configuration.wasTriggered();
        let pullFeedbackDialogView = this;

        var delay = 0;
        if (this.configuration.generalConfiguration.getParameterValue('delay')) {
            delay = this.configuration.generalConfiguration.getParameterValue('delay');
        }
        if (this.configuration.generalConfiguration.getParameterValue('intermediateDialog')) {
            let intermediateDialogTemplate = require('../templates/intermediate_dialog.handlebars');
            let intermediateDialog = new QuestionDialogView('intermediateDialog', intermediateDialogTemplate, {});
            intermediateDialog.addAnswerOption('#feedbackYes', function() {
                intermediateDialog.close();
                setTimeout(function () {
                    pullFeedbackDialogView.dialogElement.dialog('open');
                }, delay * 1000);
            });
            intermediateDialog.addAnswerOption('#feedbackNo', function() {
                intermediateDialog.close();
            });
            intermediateDialog.addAnswerOption('#feedbackLater', function() {
                intermediateDialog.close();
            });
        } else {
            setTimeout(function () {
                pullFeedbackDialogView.dialogElement.dialog('open');
            }, delay * 1000);
        }
    }

    checkMechanisms() {
        if(!this.configuration) {
            return;
        }
        let mechanisms = this.configuration.mechanisms;
        if(mechanisms.length === 1 && mechanisms[0].type === mechanismTypes.infoType) {
            let container = jQuery('#'+ this.dialogId);
            container.find('button.submit-feedback').hide();
        }
    }
}