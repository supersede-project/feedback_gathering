import i18n = require('i18next');
import {MechanismView} from '../mechanism_view';
import {mechanismTypes, applicationId, apiEndpointRepository} from '../../js/config';
import {DialogView} from './dialog_view';
import {Configuration} from '../../models/configurations/configuration';
import {TextView} from '../text/text_view';
import {RatingView} from '../rating/rating_view';
import {AudioView} from '../audio/audio_view';
import {AttachmentView} from '../attachment/attachment_view';
import {RatingMechanism} from '../../models/mechanisms/rating_mechanism';
import {AttachmentMechanism} from '../../models/mechanisms/attachment_mechanism';
import {ScreenshotView} from '../screenshot/screenshot_view';
import {PageNavigation} from '../../js/helpers/page_navigation';
import {PaginationContainer} from '../pagination_container';
import {ConfigurationInterface} from '../../models/configurations/configuration_interface';
import {Feedback} from '../../models/feedbacks/feedback';
import {FeedbackSettings} from '../../models/feedbacks/feedback_settings'
import {CategoryView} from '../category/category_view';
import {AudioFeedback} from '../../models/feedbacks/audio_feedback';
import {ContextInformation} from '../../models/feedbacks/context_information';
import {FeedbackService} from '../../services/feedback_service';
import {PageNotification} from '../page_notification';
import {GeneralConfiguration} from '../../models/configurations/general_configuration';
import {InfoView} from '../info/info_view';
import {InfoMechanism} from '../../models/mechanisms/info_mechanism';
import {CategoryMechanism} from '../../models/mechanisms/category_mechanism';
import {QuestionDialogView} from './question_dialog_view';
import {CategoryFeedback} from '../../models/feedbacks/category_feedback';



/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class FeedbackDialogView extends DialogView {
    mechanismViews:MechanismView[];
    pageNavigation:PageNavigation;
    paginationContainer:PaginationContainer;
    audioView:AudioView;

    constructor(public dialogId:string, public template:any, public configuration:Configuration, public context:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        super(dialogId, template, context, openCallback, closeCallback);
        this.dialogContext = $.extend({}, this.dialogContext, this.configuration.getContext());
        this.initMechanismViews();
        this.configurePageNavigation();
    }

    initDialog() {
        let myThis = this,
            dialogContainer = jQuery('#' + this.dialogId);
        super.initDialog();
        this.dialogElement.dialog('option', 'position', {
            my: this.dialogContext.dialogPositionMy,
            at: this.dialogContext.dialogPositionAt,
            of: this.dialogContext.dialogPositionOf
        });

        dialogContainer.find('.discard-feedback').on('click', function () {
            myThis.discardFeedback();
        });
    }

    initMechanismViews() {
        this.mechanismViews = [];

        for (let textMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.textType)) {
            this.mechanismViews.push(new TextView(textMechanism, this.dialogId));
        }

        for (let ratingMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.ratingType)) {
            this.mechanismViews.push(new RatingView(<RatingMechanism>ratingMechanism, this.dialogId));
        }

        for (let categoryMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.categoryType)) {
            this.mechanismViews.push(new CategoryView(<CategoryMechanism>categoryMechanism));
        }

        for (let screenshotMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.screenshotType)) {
            let screenshotView = this.initScreenshot(screenshotMechanism, this.dialogId);
            this.mechanismViews.push(screenshotView);
        }

        let audioMechanism = this.configuration.getActiveMechanismConfig(mechanismTypes.audioType)[0];
        if (audioMechanism) {
            let audioContainer = $("#" + this.dialogId + " #audioMechanism" + audioMechanism.id);
            this.audioView = new AudioView(audioMechanism, audioContainer, this.dialogContext.distPath);
            this.mechanismViews.push(this.audioView);
        }

        for (let attachmentMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.attachmentType)) {
            this.mechanismViews.push(new AttachmentView(<AttachmentMechanism>attachmentMechanism, this.dialogId, this.dialogContext.distPath));
        }

        for (let infoMechanism of this.configuration.getActiveMechanismConfig(mechanismTypes.infoType)) {
            this.mechanismViews.push(new InfoView(<InfoMechanism>infoMechanism, this.dialogId));
        }

        this.addEvents(this.dialogId, this.configuration);
    }

    configurePageNavigation() {
        let myThis = this;
        this.pageNavigation = new PageNavigation(this.configuration, jQuery('#' + this.dialogId));
        this.paginationContainer = new PaginationContainer(jQuery('#' + this.dialogId + '.feedback-container .pages-container'), this.pageNavigation, (changedPageNumber) => {
            myThis.changeDialogTitle(changedPageNumber);
        });
    }

    changeDialogTitle(pageNumber:number) {
        if(this.context.localesOverride && this.context.localesOverride.dialog && this.context.localesOverride.dialog.dialog && this.context.localesOverride.dialog.dialog.titles) {
            if(this.context.localesOverride.dialog.dialog.titles[pageNumber]) {
                this.dialogElement.dialog('option', 'title', this.context.localesOverride.dialog.dialog.titles[pageNumber]);
            } else {
                this.dialogElement.dialog('option', 'title', this.dialogContext.dialogTitle);
            }
        }
    }

    addEvents(containerId, configuration:ConfigurationInterface) {
        let myThis = this;
        let generalConfiguration = configuration.generalConfiguration;
        var container = $('#' + containerId);
        var textareas = container.find('textarea.text-type-text');
        var textMechanisms = configuration.getMechanismConfig(mechanismTypes.textType);
        var feedbackDialogView = this;

        var feedbackService = new FeedbackService(this.context.apiEndpointRepository, this.dialogContext.lang);

        container.find('button.submit-feedback').unbind().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            var submitButton= $(this);
            submitButton.prop('disabled', true);
            submitButton.text(submitButton.text() + '...');


            if(!myThis.ratingMechanismsAreValid(container)) {
                submitButton.prop('disabled', false);
                submitButton.text(submitButton.text().replace(/...$/,''));
                return;
            }

            if(!myThis.categoryMechanismsAreValid(container)) {
                submitButton.prop('disabled', false);
                submitButton.text(submitButton.text().replace(/...$/,''));
                return;
            }

            // TODO adjust
            // validate anyway before sending
            if (textMechanisms.length > 0) {
                textareas.each(function () {
                    $(this).validate();
                });


                var invalidTextareas = container.find('textarea.text-type-text.invalid');
                if (invalidTextareas.length == 0) {
                    feedbackDialogView.prepareFormData(configuration, function (formData) {
                        feedbackDialogView.sendFeedback(feedbackService, formData, generalConfiguration);
                    });
                } else {
                    submitButton.prop('disabled', false);
                    submitButton.text(submitButton.text().replace(/...$/,''));
                }
            } else {
                feedbackDialogView.prepareFormData(configuration, function (formData) {
                    feedbackDialogView.sendFeedback(feedbackService, formData, generalConfiguration);
                });
            }
        });
    };

    ratingMechanismsAreValid(container:any):boolean {
        let valid = true;
        container.find('.review-page-mechanisms .rating-type.mandatory .rating-input').each(function() {
            if(parseInt(jQuery(this).starRating('getRating')) === 0) {
                valid = false;
                let errorMessage = jQuery(this).data('mandatory-message');
                jQuery(this).append('<span class="feedback-form-error">' + errorMessage + '</span>');
            }
        });

        return valid;
    }

    categoryMechanismsAreValid(container:any):boolean {
        container.find('.review-page-mechanisms .category-type.mandatory').validateCategory();

        if(container.find('.review-page-mechanisms .category-type.mandatory.invalid').length > 0) {
            return false;
        } else {
            return true;
        }
    }

    sendFeedback(feedbackService:FeedbackService, formData:any, generalConfiguration:GeneralConfiguration) {
        var feedbackDialogView = this;
        var url = this.context.apiEndpointRepository + 'feedback_repository/' + this.context.lang + '/applications/' + this.context.applicationId + '/feedbacks/';
        var urlAuthenticate = this.context.apiEndpointRepository + 'feedback_repository/authenticate';
        var urlSettings = this.context.apiEndpointRepository + 'feedback_repository/' + this.context.lang + '/applications/' + this.context.applicationId + '/feedbacks/feedbacksettings';
        feedbackService.sendFeedback(url, formData, function(data) {
            // console.log("FormData to be sent: " + JSON.stringify(data));
            var feedbackJson = JSON.parse(JSON.stringify(data));
            console.log("urlForUser: " + feedbackJson.userIdentification);
            var urlForUser = url + 'user_identification/'+feedbackJson.userIdentification;
            console.log(urlForUser);
            if(generalConfiguration && generalConfiguration.getParameterValue('successDialog')) {
                console.log("=== feedback successfully sent ===");
                // feedbackDialogView.discardFeedback();
                // feedbackDialogView.paginationContainer.showFirstPage();
                // let dialogTemplate = require('../../templates/info_dialog.handlebars');
                // let successMessage = generalConfiguration.getParameterValue('successMessage') || i18n.t('general.success_message');
                // let successDialogView = new QuestionDialogView('infoDialog', dialogTemplate, {'message': <string>successMessage});
                // successDialogView.setTitle(<string>i18n.t('general.success_dialog_title'));
                // successDialogView.setModal(true);
                // successDialogView.addAnswerOption('#infoDialogOkay', function() {
                //     successDialogView.close();

                feedbackDialogView.discardFeedback();

                // Überprüfung ob ein Feedback existiert für user 99999999
                //  ==> Falls ja, check ob globalSetting = true ==> falls ja, zeige die nachfolgenden Dialoge nicht
                //                                       = false ==> lade die settings in die templates
                //  ==> Falls nein, zeige Dialog felder so auf

                var authenticationToken = "";

                var previousFeedbacksExist:boolean = false;
                var reloadPreviousSettings:boolean = true;


                let dialogTemplate = require('../../templates/info_dialog.handlebars');

                console.log("test console");
                console.log("reloadprevious settings: "  + reloadPreviousSettings);

                var reloadReload:boolean = true;
                console.log("before executing ajax request");

                var ajaxRequest2;
                var ajaxRequest3;

                var statusUpdates = null;
                var statusUpdatesContactChannel = null;
                var feedbackQuery = null;
                var feedbackQueryChannel = null;
                var setSettingsGlobal= null;

                var ajaxRequest = $.ajax({
                    url: urlAuthenticate,
                    type: 'POST',
                    data: JSON.stringify({
                        name: 'admin',
                        password: 'password'
                    }),
                    contentType: 'application/json',
                    async: false,
                    success: function (data) {
                        authenticationToken = JSON.parse(JSON.stringify(data)).token;
                        console.log("token: " + authenticationToken);
                        ajaxRequest2 = feedbackService.getFeedbacks(urlForUser,authenticationToken, function (data) {
                            console.log("success on getting feedbacks for user");
                            var feedbackArrayLength = JSON.parse(JSON.stringify(data)).length;
                            var previousFeedbacks = JSON.parse(JSON.stringify(data));
                            console.log("json array length: " + feedbackArrayLength);
                            console.log("json string length: " + JSON.stringify(data).length);

                            if(feedbackArrayLength > 1){
                                previousFeedbacksExist = true;
                            }

                            if(previousFeedbacksExist){
                                var lastFeedbackId = previousFeedbacks[previousFeedbacks.length - 2].id;
                                console.log("lastFeedbackId: " + lastFeedbackId);
                                var urlSettingsRetrieve = urlSettings + "/feedback/"+lastFeedbackId;
                                console.log("urlSettingsRetrieve: " + urlSettingsRetrieve);
                                ajaxRequest3 = feedbackService.getFeedbackSettings(urlSettingsRetrieve,authenticationToken,function (data) {
                                    console.log("data settings: " + JSON.stringify(data));
                                    statusUpdates = JSON.parse(JSON.stringify(data)).statusUpdates;
                                    statusUpdatesContactChannel = JSON.parse(JSON.stringify(data)).statusUpdatesContactChannel;
                                    feedbackQuery = JSON.parse(JSON.stringify(data)).feedbackQuery;
                                    feedbackQueryChannel = JSON.parse(JSON.stringify(data)).feedbackQueryChannel;
                                    setSettingsGlobal= JSON.parse(JSON.stringify(data)).globalFeedbackSetting;

                                    if(setSettingsGlobal){
                                        reloadReload = false;
                                    } else reloadReload = true;
                                    console.log("reload settings: " + reloadReload);
                                }, function (error) {
                                    $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(error));
                                    console.log("Could not retrieve settings for " + lastFeedbackId);
                                })
                            } else {
                                reloadReload=true;
                            }

                        }, function (error) {
                            $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(error));
                            console.log("Could not retrieve feedbacks for 99999999");
                        });
                    },
                    error: function (data) {
                        $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(data));
                    }
                });

                console.log("after executing ajax");

                console.log("initialize dialogs");

                if(reloadReload === true){
                    console.log("inside reloadReload");

                    // let f2fUpdateTemplate;
                    // let f2fInquiryTemplate;
                    //
                    // let updateDialogView;
                    // let inquiryDialogView;
                    // let summaryDialogView;
                    //
                    // if(statusUpdates != null && statusUpdatesContactChannel != null && feedbackQuery != null
                    //     && feedbackQueryChannel!= null && setSettingsGlobal != null){
                    //     f2fUpdateTemplate = require('../../templates/f2f_dialog_updateform_v2_prefilled.handlebars');
                    //     f2fInquiryTemplate = require('../../templates/f2f_dialog_inquiryform_v2_prefilled.handlebars');
                    //
                    //     updateDialogView = new QuestionDialogView('UpdateForm_v2', f2fUpdateTemplate,
                    //         {
                    //             'statusUpdates':statusUpdates,
                    //             'statusUpdatesContactChannel':statusUpdatesContactChannel,
                    //         });
                    //     inquiryDialogView = new QuestionDialogView('InquiryForm_v2', f2fInquiryTemplate,
                    //         {
                    //             'feedbackQuery':feedbackQuery,
                    //             'feedbackQueryChannel':feedbackQueryChannel
                    //         });
                    // } else {
                    //     f2fUpdateTemplate = require('../../templates/f2f_dialog_updateform_v2.handlebars');
                    //     f2fInquiryTemplate = require('../../templates/f2f_dialog_inquiryform_v2.handlebars');
                    //
                    //     updateDialogView = new QuestionDialogView('UpdateForm_v2', f2fUpdateTemplate);
                    //     inquiryDialogView = new QuestionDialogView('InquiryForm_v2', f2fInquiryTemplate);
                    // }

                    let f2fUpdateTemplate = require('../../templates/f2f_dialog_updateform_v2.handlebars');
                    let f2fInquiryTemplate = require('../../templates/f2f_dialog_inquiryform_v2.handlebars');

                    let updateDialogView = new QuestionDialogView('UpdateForm_v2', f2fUpdateTemplate);
                    let inquiryDialogView = new QuestionDialogView('InquiryForm_v2', f2fInquiryTemplate);

                    let f2fSummaryTemplate = require('../../templates/f2f_dialog_summary.handlebars');

                    let summaryAllowUpdate:string = "";
                    let summaryChannelUpdate:string = "No information provided";
                    let summaryAllowInquiry:string = "";
                    let summaryChannelInquiry:string = "No information provided";
                    let summarySetGlobal:boolean = false;
                    // let messageChannelDescription = <string>i18n.t('general.f2f_dialog_channelDescription');
                    // let messageHint = <string>i18n.t('general.f2f_dialog_hint');
                    // let updateDialogView = new QuestionDialogView('UpdateForm_v2', f2fUpdateTemplate,
                    //     {'dialogTitle': <string>titleMessageUpdate,
                    //         'messageAllowDescription': <string>messageAllowDescription,
                    //         'messageHint': <string>messageHint,
                    //         'messageChannelDescription': <string>messageChannelDescription});
                    // let updateDialogView = new QuestionDialogView('UpdateForm_v2', f2fUpdateTemplate,
                    //     {
                    //         'previousFeedbacksExist':previousFeedbacksExist
                    //     });
                    updateDialogView.setTitle(<string>i18n.t('general.success_dialog_title_f2f'));
                    updateDialogView.setModal(true);

                    // let inquiryDialogView = new QuestionDialogView('InquiryForm', f2fInquiryTemplate,
                    //     {'dialogTitle': <string>titleMessageInquiry,
                    //         'messageAllowDescription': <string>messageInquiryDescription,
                    //         'messageHint': <string>messageHint,
                    //         'messageChannelDescription': <string>messageChannelDescription});
                    inquiryDialogView = new QuestionDialogView('InquiryForm_v2', f2fInquiryTemplate);
                    inquiryDialogView.setTitle(<string>i18n.t('general.success_dialog_title_f2f'));
                    inquiryDialogView.setModal(true);

                    $("input[name='allowUpdate']").change(function(){
                        if(jQuery('#UpdateForm_v2').find('input[name="allowUpdate"]:checked').val() == "No"){
                            $("#hideDivUpdate").hide();
                            $("#updateFormEmail").prop('checked',false);
                            $("#updateFormCentral").prop('checked',false);
                        }
                        if(jQuery('#UpdateForm_v2').find('input[name="allowUpdate"]:checked').val() == "Yes"){
                            $("#hideDivUpdate").show();
                        }
                    });

                    $("input[name='allowInquiry']").change(function(){
                        if(jQuery('#InquiryForm_v2').find('input[name="allowInquiry"]:checked').val() == "No"){
                            $("#hideDivInquiry").hide();
                            $("#inquiryFormEmail").prop('checked',false);
                            $("#inquiryFormCentral").prop('checked',false);
                        }
                        if(jQuery('#InquiryForm_v2').find('input[name="allowInquiry"]:checked').val() == "Yes"){
                            $("#hideDivInquiry").show();
                        }
                    });

                    updateDialogView.addAnswerOption('#f2fDialogContinue', function() {
                        summaryAllowUpdate = jQuery('#UpdateForm_v2').find('input[name="allowUpdate"]:checked').val();
                        let channelUpdate = jQuery('#UpdateForm_v2').find('input[name="contactChannelUpdate"]:checked').val();
                        if(typeof channelUpdate !== 'undefined'){
                            summaryChannelUpdate = channelUpdate;
                        }
                        updateDialogView.close();
                        console.log("allowContactReply:" + summaryAllowUpdate);
                        console.log("contactChannel:" + channelUpdate);
                        inquiryDialogView.open();
                    });

                    // inquiryDialogView.addAnswerOption('#f2fDialogBackToUpdate', function () {
                    //     inquiryDialogView.close();
                    //     updateDialogView.open();
                    // });

                    inquiryDialogView.addAnswerOption('#f2fDialogFinish', function() {
                        summaryAllowInquiry = jQuery('#InquiryForm_v2').find('input[name="allowInquiry"]:checked').val();
                        let channelInquiry = jQuery('#InquiryForm_v2').find('input[name="contactChannelInquiry"]:checked').val();
                        if(typeof channelInquiry !== 'undefined'){
                            summaryChannelInquiry = channelInquiry;
                        }
                        inquiryDialogView.close();
                        let summaryDialogView = new QuestionDialogView('SummaryForm', f2fSummaryTemplate,
                            {'allowUpdate': summaryAllowUpdate,
                                'allowInquiry': summaryAllowInquiry,
                                'contactChannelUpdate': summaryChannelUpdate,
                                'contactChannelInquiry': summaryChannelInquiry});
                        summaryDialogView.setTitle(<string>i18n.t('general.success_dialog_title_f2f'));
                        summaryDialogView.setModal(true);

                        // summaryDialogView.addAnswerOption('#f2fDialogBackToInquiry',function () {
                        //     summaryDialogView.close();
                        //     inquiryDialogView.open();
                        // });

                        summaryDialogView.addAnswerOption('#f2fDialogSave', function() {
                            let setGlobal:string= jQuery('#SummaryForm').find('input[name="saveSettings"]:checked').val();
                            if (setGlobal.toLowerCase() == "yes"){
                                summarySetGlobal = true;
                            }
                            summaryDialogView.close();

                            let finalSettings: JSON = {};
                            if(summaryAllowUpdate.toLowerCase() == "yes"){
                                finalSettings.statusUpdates = true;
                                finalSettings.statusUpdatesContactChannel = summaryChannelUpdate;
                            } else {
                                finalSettings.statusUpdates = false;
                                finalSettings.statusUpdatesContactChannel = "";
                            }

                            if(summaryAllowInquiry.toLowerCase() == "yes"){
                                finalSettings.feedbackQuery = true;
                                finalSettings.feedbackQueryChannel = summaryChannelInquiry;
                            } else {
                                finalSettings.feedbackQuery = false;
                                finalSettings.feedbackQueryChannel = "";
                            }


                            console.log("FEEDBACK ID: " + feedbackJson.id);
                            console.log("User ID: " + feedbackJson.userIdentification);


                            finalSettings.globalFeedbackSetting = summarySetGlobal;
                            finalSettings.feedback_id = feedbackJson.id;
                            // finalSettings.feedback = JSON.stringify(data);
                            console.log("Feedback data: " + JSON.stringify(data));

                            feedbackService.sendFeedbackSettings(urlSettings, finalSettings, function(data) {
                                console.log("it worked");
                                console.log(JSON.stringify(data))
                            }, function(error) {
                                console.log("sendFeedbackSettings: Error");
                                console.log('Failure: ' + JSON.stringify(error));
                            });
                        });
                        summaryDialogView.open();
                    });

                    if(statusUpdates != null && statusUpdatesContactChannel != null && feedbackQuery != null
                        && feedbackQueryChannel!= null && setSettingsGlobal != null){
                        if(statusUpdates === true){
                            $('input[name="allowUpdate"]').val(["Yes"]);
                            $('input[name="contactChannelUpdate"]').val([statusUpdatesContactChannel]);
                        } else {
                            $('input[name="allowUpdate"]').val(["No"]);
                            $("#hideDivUpdate").hide();
                            $("#updateFormEmail").prop('checked',false);
                            $("#updateFormCentral").prop('checked',false);
                        }

                        if(feedbackQuery === true){
                            $('input[name="allowInquiry"]').val(["Yes"]);
                            $('input[name="contactChannelInquiry"]').val([feedbackQueryChannel]);
                        } else {
                            $('input[name="allowInquiry"]').val(["No"]);
                            $("#hideDivInquiry").hide();
                            $("#inquiryFormEmail").prop('checked',false);
                            $("#inquiryFormCentral").prop('checked',false);
                        }
                    }
                    console.log("opening updateDialog");
                    updateDialogView.open();
                }

            } else if (generalConfiguration && generalConfiguration.getParameterValue('closeDialogOnSuccess')) {
                feedbackDialogView.discardFeedback();
                feedbackDialogView.paginationContainer.showFirstPage();
                PageNotification.show(<string>i18n.t('general.success_message'));
            } else {
                feedbackDialogView.resetDialog();
                $('.server-response').addClass('success').text(i18n.t('general.success_message'));
            }
            $('button.submit-feedback').prop('disabled', false);
            $('button.submit-feedback').text($('button.submit-feedback').text().replace(/...$/,''));
        }, function(error) {
            $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(error));
            $('button.submit-feedback').prop('disabled', false);
            $('button.submit-feedback').text($('button.submit-feedback').text().replace(/...$/,''));
        });
    }

    /**
     * Creates the multipart form data containing the data of the active mechanisms.
     */
    prepareFormData(configuration:ConfigurationInterface, callback?:any) {
        // TODO refactoring: the mechanism views should return their feedback data
        var dialogView = this;
        var formData = new FormData();
        var feedbackVisibility = jQuery('#checkPublic').is(":checked");
        var audioMechanisms = configuration.getMechanismConfig(mechanismTypes.audioType);
        var hasAudioMechanism = audioMechanisms.filter(audioMechanism => audioMechanism.active === true).length > 0;

        dialogView.resetMessageView();

        var feedbackObject = new Feedback('Feedback', this.dialogContext.userId, this.dialogContext.language, this.context.applicationId, configuration.id, [], [], [], [], null, [], [],false,feedbackVisibility);
        feedbackObject.contextInformation = ContextInformation.create(this.context.metaData);

        for (var mechanismView of dialogView.mechanismViews) {
            if (mechanismView instanceof TextView) {
                feedbackObject.textFeedbacks.push(mechanismView.getFeedback());
            } else if (mechanismView instanceof RatingView) {
                feedbackObject.ratingFeedbacks.push(mechanismView.getFeedback());
            } else if (mechanismView instanceof AttachmentView) {
                feedbackObject.attachmentFeedbacks = mechanismView.getFeedbacks();
                for (let i = 0; i < mechanismView.getFiles().length; i++) {
                    let file = mechanismView.getFiles()[i];
                    formData.append(mechanismView.getPartName(i), file, file.name);
                }
            } else if (mechanismView instanceof ScreenshotView) {
                let screenshotBinary = mechanismView.getScreenshotAsBinary();
                if (screenshotBinary !== null) {
                    feedbackObject.screenshotFeedbacks.push(mechanismView.getFeedback());
                    formData.append(mechanismView.getPartName(), mechanismView.getScreenshotAsBinary(), 'weblib_screenshot_' + this.context.userId + '.png');
                }
            } else if (mechanismView instanceof CategoryView) {
                for(let categoryFeedback of mechanismView.getCategoryFeedbacks()) {
                    feedbackObject.categoryFeedbacks.push(categoryFeedback);
                }
            }
        }

        // TODO assumes only one audio mechanism --> support multiple
        for (var audioMechanism of audioMechanisms.filter(mechanism => mechanism.active === true)) {
            let partName = "audio" + audioMechanism.id;
            var audioElement = jQuery('section#audioMechanism' + audioMechanism.id + ' audio')[0];
            if (!audioElement || Fr.voice.recorder === null) {
                formData.append('json', new Blob([JSON.stringify(feedbackObject)], { type: 'application/json' }));
                callback(formData);
            }

            try {
                var duration = Math.ceil(audioElement.duration === undefined || audioElement.duration === 'NaN' ? 0 : audioElement.duration);
                if (duration === 0) {
                    hasAudioMechanism = false;
                    break;
                }
                var audioFeedback = new AudioFeedback(partName, duration, "wav", audioMechanism.id);
                this.audioView.getBlob(function (blob) {
                    var date = new Date();
                    formData.append(partName, blob, "recording" + audioMechanism.id + "_" + date.getTime());
                    feedbackObject.audioFeedbacks.push(audioFeedback);
                    formData.append('json', new Blob([JSON.stringify(feedbackObject)], { type: 'application/json' }));
                    callback(formData);
                });
            } catch (e) {
                formData.append('json', new Blob([JSON.stringify(feedbackObject)], { type: 'application/json' }));
                callback(formData);
            }
        }

        if (!hasAudioMechanism) {
            formData.append('json', new Blob([JSON.stringify(feedbackObject)], { type: 'application/json' }));
            callback(formData);
        }
    };

    initScreenshot(screenshotMechanism, containerId):ScreenshotView {
        if (screenshotMechanism == null) {
            return;
        }

        var elementToCaptureSelector = 'body';
        if (screenshotMechanism.getParameterValue('elementToCapture') !== null && screenshotMechanism.getParameterValue('elementToCapture') !== "") {
            elementToCaptureSelector = screenshotMechanism.getParameterValue('elementToCapture');
        }

        var container = $('#' + containerId);
        var dialogSelector = '[aria-describedby="' + containerId + '"]';

        var screenshotPreview = container.find('.screenshot-preview'),
            screenshotCaptureButton = container.find('button.take-screenshot'),
            elementToCapture = $('' + elementToCaptureSelector),
            elementsToHide = ['.ui-widget-overlay', dialogSelector, '.ui-dialog.feedback-dialog', '.' + this.context.dialogCSSClass];
        // TODO attention: circular dependency
        var screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton,
            elementToCapture, container, this.dialogContext.distPath, elementsToHide, screenshotMechanism.getParameterValue('manipulationOnObject'));
        screenshotView.colorPickerCSSClass = this.dialogContext.colorPickerCSSClass;
        screenshotView.setDefaultStrokeWidth(this.dialogContext.defaultStrokeWidth);

        screenshotMechanism.setScreenshotView(screenshotView);
        return screenshotView;
    };

    resetDialog() {
        super.resetDialog();
        if (this.mechanismViews) {
            for (var mechanismView of this.mechanismViews) {
                mechanismView.reset();
            }
        }
    }

    discardFeedback() {
        this.resetDialog();
        this.close();
    }
}