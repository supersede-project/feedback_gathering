import Handlebars = require('handlebars');
import './lib/rating/jquery.star-rating-svg.js';
import './jquery.validate';
import './jquery.validate_category';
import './jquery.fileupload';
import {
    apiEndpointRepository, feedbackPath, applicationName, defaultSuccessMessage,
    feedbackObjectTitle, dialogOptions, mechanismTypes, applicationId
} from './config';
import {PaginationContainer} from '../views/pagination_container';
import {ScreenshotView} from '../views/screenshot/screenshot_view';
import {I18nHelper} from './helpers/i18n';
import i18n = require('i18next');
import {MockBackend} from '../services/backends/mock_backend';
import {RatingMechanism} from '../models/mechanisms/rating_mechanism';
import {PullConfiguration} from '../models/configurations/pull_configuration';
import {Feedback} from '../models/feedbacks/feedback';
import {PageNavigation} from './helpers/page_navigation';
import {ConfigurationInterface} from '../models/configurations/configuration_interface';
import {Application} from '../models/applications/application';
import {ApplicationService} from '../services/application_service';
import * as t from '../templates/t';
import * as compare from '../templates/compare';
var dialogTemplate = require('../templates/feedback_dialog.handlebars');
var pullDialogTemplate = require('../templates/feedback_dialog.handlebars');
var intermediateDialogTemplate = require('../templates/intermediate_dialog.handlebars');
var notificationTemplate = require('../templates/notification.handlebars');
import {GeneralConfiguration} from '../models/configurations/general_configuration';
import {RatingFeedback} from '../models/feedbacks/rating_feedback';
import {ScreenshotFeedback} from '../models/feedbacks/screenshot_feedback';
import {AttachmentFeedback} from '../models/feedbacks/attachment_feedback';
import {AudioFeedback} from '../models/feedbacks/audio_feedback';
import {ContextInformation} from '../models/feedbacks/context_information';
import {AudioView} from '../views/audio/audio_view';
import {FeedbackApp} from './feedback_app';
import {RatingView} from '../views/rating/rating_view';
import {AttachmentView} from '../views/attachment/attachment_view';
import {TextView} from '../views/text/text_view';
import {DialogView} from '../views/dialog/dialog_view';
import {CategoryView} from '../views/category/category_view';
var mockData = require('json!../services/mocks/dev/applications_mock.json');


export declare var feedbackApp: FeedbackApp;

export var feedbackPluginModule = function ($, window, document) {
    var dialog;
    var pushConfigurationDialogId = "pushConfiguration";
    var pullDialog;
    var pullConfigurationDialogId = "pullConfiguration";
    var active = false;
    var colorPickerCSSClass;
    var defaultStrokeWidth;
    var audioView;

    // TODO refactoring: I don't know how yet
    // TODO check which part of the configuration is really needed by the other modules --> use DI to pass this configuration to other modules as well as to allow to pass testing configuration that way
    /**
     * @param applicationObject
     *  The current application object that configures the library.
     */
    var initApplication = function (applicationObject:Application) {
        initPushMechanisms(application.getPushConfiguration(), application.generalConfiguration);

    };

    // TODO refactoring: move to dialog view
    /**
     * @param configuration
     *  PushConfiguration data retrieved from the feedback orchestrator
     *
     * Initializes the mechanism objects with the configuration data. It then constructs the context variable for the
     * template and invokes the feedbackDialog template with the configuration data.
     * Furthermore, the pagination inside the dialog is initialized, the rating component is configured, the dialog
     * is configured and displayed and some events are added to the UI.
     * All events on the HTML have to be added after the template is appended to the body (if not using live binding).
     */
    var initPushMechanisms = function (configuration, generalConfiguration:GeneralConfiguration) {
        var context = prepareTemplateContext(configuration.getContextForView());

        var pageNavigation = new PageNavigation(configuration, $('#' + pushConfigurationDialogId));
        dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
    };

    // TODO refactoring: I don't know how yet
    var showPullDialog = function (configuration:PullConfiguration, generalConfiguration:GeneralConfiguration) {
        configuration.wasTriggered();
        var pageNavigation = new PageNavigation(configuration, $('#' + pullConfigurationDialogId));

        var context = prepareTemplateContext(configuration.getContextForView());

        pullDialog = initTemplate(pullDialogTemplate, pullConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
        var delay = 0;
        if (configuration.generalConfiguration.getParameterValue('delay')) {
            delay = configuration.generalConfiguration.getParameterValue('delay');
        }
        if (configuration.generalConfiguration.getParameterValue('intermediateDialog')) {
            var intermediateDialog = initIntermediateDialogTemplate(intermediateDialogTemplate, 'intermediateDialog', configuration, pullDialog, generalConfiguration);
            if (intermediateDialog !== null) {
                setTimeout(function () {
                    if (!active) {
                        intermediateDialog.dialog('open');
                    }
                }, delay * 1000);
            }
        } else {
            setTimeout(function () {
                if (!active) {
                    openDialog(pullDialog, configuration);
                }
            }, delay * 1000);
        }
    };

    // TODO refactoring: I don't know how yet
    var initTemplate = function (template, dialogId, context, configuration, pageNavigation,
                                 generalConfiguration:GeneralConfiguration):HTMLElement {
        var html = template(context);
        $('body').append(html);

        // after template is loaded
        new PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
        pageNavigation.screenshotViews = [];

        for (var textMechanism of configuration.getMechanismConfig(mechanismTypes.textType)) {
            new TextView(textMechanism, dialogId);
        }

        for (var ratingMechanism of configuration.getMechanismConfig(mechanismTypes.ratingType)) {
            new RatingView(ratingMechanism, dialogId);
        }

        for (var screenshotMechanism of configuration.getMechanismConfig(mechanismTypes.screenshotType)) {
            var screenshotView = initScreenshot(screenshotMechanism, dialogId);
            pageNavigation.screenshotViews.push(screenshotView);
        }

        var audioMechanism = configuration.getMechanismConfig(mechanismTypes.audioType).filter(mechanism => mechanism.active === true)[0];
        if (audioMechanism) {
            var audioContainer = $("#" + dialogId + " #audioMechanism" + audioMechanism.id);
            new AudioView(audioMechanism, audioContainer, distPath);
        }

        for (var attachmentMechanism of configuration.getMechanismConfig(mechanismTypes.attachmentType)) {
            new AttachmentView(attachmentMechanism, dialogId);
        }

        var title = "Feedback";
        if (generalConfiguration.getParameterValue('dialogTitle') !== null && generalConfiguration.getParameterValue('dialogTitle') !== "") {
            title = generalConfiguration.getParameterValue('dialogTitle');
        }

        var modal = false;
        if (generalConfiguration.getParameterValue('dialogModal') !== null && generalConfiguration.getParameterValue('dialogModal') !== "") {
            modal = generalConfiguration.getParameterValue('dialogModal');
        }

        var dialog = initDialog($('#' + dialogId), title, modal, dialogId);
        addEvents(dialogId, configuration, generalConfiguration);
        return dialog;
    };

    // TODO refactoring: I don't know how yet
    var initIntermediateDialogTemplate = function (template, dialogId, configuration, pullDialog,
                                                   generalConfiguration:GeneralConfiguration):HTMLElement {
        var html = template({});
        $('body').append(html);

        var dialog = initDialog($('#' + dialogId), generalConfiguration.getParameterValue('dialogTitle'), true, dialogId);
        $('#feedbackYes').on('click', function () {
            dialog.dialog('close');
            openDialog(pullDialog, configuration);
        });
        $('#feedbackNo').on('click', function () {
            dialog.dialog('close');
        });
        $('#feedbackLater').on('click', function () {
            dialog.dialog('close');
        });
        return dialog;
    };

    // TODO adjust comment
    // TODO refactoring: move to FeedbackService
    /**
     * This method takes the data from the text mechanism and the rating mechanism and composes a feedback object with
     * the help of this data.
     * Then an AJAX request is done to send the submitted feedback to the feedback repository. A success or failure
     * message is shown after the request is done.
     */
    var sendFeedback = function (formData:FormData, configuration:ConfigurationInterface, generalConfiguration:GeneralConfiguration) {
        $.ajax({
            url: apiEndpointRepository + 'feedback_repository/' + language + '/applications/' + applicationId + '/feedbacks/',
            type: 'POST',
            data: formData,
            dataType: 'json',
            processData: false,
            contentType: false,
            success: function (data) {
                resetPlugin(configuration);
                if (generalConfiguration.getParameterValue('closeDialogOnSuccess')) {
                    dialog.dialog('close');
                    pageNotification(defaultSuccessMessage);
                } else {
                    $('.server-response').addClass('success').text(defaultSuccessMessage);
                }
            },
            error: function (data) {
                $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(data));
            }
        });
    };

    // TODO refactoring: move to own view
    var pageNotification = function (message:string) {
        var html = notificationTemplate({message: message});
        $('html').append(html);
        setTimeout(function () {
            $(".feedback-notification").remove();
        }, 3000);
    };

    // TODO refactoring: I don't know how yet
    var initScreenshot = function (screenshotMechanism, containerId):ScreenshotView {
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
            elementsToHide = ['.ui-widget-overlay.ui-front', dialogSelector];
        // TODO attention: circular dependency
        var screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton,
            elementToCapture, container, distPath, elementsToHide, screenshotMechanism.getParameterValue('manipulationOnObject'));
        screenshotView.colorPickerCSSClass = colorPickerCSSClass;
        screenshotView.setDefaultStrokeWidth(defaultStrokeWidth);

        screenshotMechanism.setScreenshotView(screenshotView);
        return screenshotView;
    };


    // TODO refactoring: see inside function
    /**
     * @param containerId
     *  The ID of the surrounding element that contains the feedback mechanisms
     * @param configuration
     *  Configuration used to set the events
     *
     * Adds the following events:
     * - Send event for the feedback form
     * - Character count event for the text mechanism
     */
    var addEvents = function (containerId, configuration:ConfigurationInterface, generalConfiguration:GeneralConfiguration) {
        var container = $('#' + containerId);
        var textareas = container.find('textarea.text-type-text');
        var textMechanisms = configuration.getMechanismConfig(mechanismTypes.textType);

        // TODO refactoring: move to feedbackDialog
        container.find('button.submit-feedback').unbind().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            // validate anyway before sending
            if (textMechanisms.length > 0) {
                textareas.each(function () {
                    $(this).validate();
                });

                var invalidTextareas = container.find('textarea.text-type-text.invalid');
                if (invalidTextareas.length == 0) {
                    prepareFormData(container, configuration, function (formData) {
                        sendFeedback(formData, configuration, generalConfiguration);
                    });
                }
            } else {
                prepareFormData(container, configuration, function (formData) {
                    sendFeedback(formData, configuration, generalConfiguration);
                });
            }
        });
    };

    // TODO refactoring: the mechanism views should return their feedback data
    /**
     * Creates the multipart form data containing the data of the active mechanisms.
     */
    var prepareFormData = function (dialogView:DialogView, configuration:ConfigurationInterface, callback?:any) {
        var formData = new FormData();
        var audioMechanisms = configuration.getMechanismConfig(mechanismTypes.audioType);
        var hasAudioMechanism = audioMechanisms.filter(audioMechanism => audioMechanism.active === true).length > 0;

        dialogView.resetMessageView();

        var feedbackObject = new Feedback(feedbackObjectTitle, feedbackApp.options.userId, feedbackApp.options.language, applicationId, configuration.id, [], [], [], [], null, [], []);
        feedbackObject.contextInformation = ContextInformation.create();

        for(var mechanismView of dialogView.mechanismViews) {
            if(mechanismView instanceof TextView) {
                feedbackObject.textFeedbacks.push(mechanismView.getFeedback());
            } else if (mechanismView instanceof RatingView) {
                feedbackObject.ratingFeedbacks.push(mechanismView.getFeedback());
            } else if (mechanismView instanceof AttachmentView) {
                feedbackObject.attachmentFeedbacks.push(mechanismView.getFeedbacks());
                for(let i = 0; i < mechanismView.getFiles(); i++) {
                    let file = mechanismView.getFiles()[i];
                    formData.append(mechanismView.getPartName(), file, file.name);
                }
            } else if (mechanismView instanceof ScreenshotView) {
                let screenshotBinary = mechanismView.getScreenshotAsBinary();
                if(screenshotBinary !== null) {
                    feedbackObject.screenshotFeedbacks.push(mechanismView.getFeedback());
                    formData.append(mechanismView.getPartName(), mechanismView.getScreenshotAsBinary());
                }
            } else if (mechanismView instanceof CategoryView) {
                feedbackObject.categoryFeedbacks.push(mechanismView.getCategoryFeedbacks());
            }
        }

        // TODO assumes only one audio mechanism --> support multiple
        for (var audioMechanism of audioMechanisms.filter(mechanism => mechanism.active === true)) {
            let partName = "audio" + audioMechanism.id;
            var audioElement = jQuery('section#audioMechanism' + audioMechanism.id + ' audio')[0];
            if (!audioElement || Fr.voice.recorder === null) {
                formData.append('json', JSON.stringify(feedbackObject));
                callback(formData);
            }

            try {
                var duration = Math.ceil(audioElement.duration === undefined || audioElement.duration === 'NaN' ? 0 : audioElement.duration);
                if (duration === 0) {
                    hasAudioMechanism = false;
                    break;
                }
                var audioFeedback = new AudioFeedback(partName, duration, "wav", audioMechanism.id);
                audioView.getBlob(function (blob) {
                    var date = new Date();
                    formData.append(partName, blob, "recording" + audioMechanism.id + "_" + date.getTime());
                    feedbackObject.audioFeedbacks.push(audioFeedback);
                    formData.append('json', JSON.stringify(feedbackObject));
                    callback(formData);
                });
            } catch (e) {
                formData.append('json', JSON.stringify(feedbackObject));
                callback(formData);
                console.log((<Error>e).message);
            }
        }

        if (!hasAudioMechanism) {
            formData.append('json', JSON.stringify(feedbackObject));
            callback(formData);
        }
    };

    // TODO refactoring: move to FeedbackDialog
    var openDialog = function (dialog, configuration) {
        for (var screenshotMechanism of configuration.getMechanismConfig(mechanismTypes.screenshotType)) {
            if (screenshotMechanism !== null && screenshotMechanism !== undefined && screenshotMechanism.screenshotView !== null) {
                screenshotMechanism.screenshotView.checkAutoTake();
            }
        }
        dialog.dialog('open');
    };

    // TODO refactoring: move to FeedbackDialog?
    var prepareTemplateContext = function (context) {
        var contextWithApplicationContext = $.extend({}, applicationContext, context);
        var pluginContext = {
            'distPath': distPath
        };
        return $.extend({}, pluginContext, contextWithApplicationContext);
    };

    /**
     * @param options
     *  Client side configuration of the feedback library
     * @returns {jQuery}
     *
     * The feedbackPlugin() function can get applied to a HTML element. This element is then configured via the passed
     * options and the default options. If a click event on this element happens the configuration is fetched from the
     * server and the feedback mechanism is invoked.
     */
    $.fn.feedbackPlugin = function (options) {
        I18nHelper.initializeI18n(options);
        var language = I18nHelper.getLanguage(options);
        var options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
        feedbackApp = new FeedbackApp(new ApplicationService(language), applicationId, options, this);

        return this;
    };

    $.fn.feedbackPlugin.defaults = {
        'color': '#fff',
        'lang': 'de',
        'backgroundColor': '#7c9009',
        'fallbackLang': 'en',
        'distPath': 'dist/',
        'userId': '',
        'dialogCSSClass': 'feedback-dialog',
        'colorPickerCSSClass': 'color-picker',
        'defaultStrokeWidth': 4
    };
};

(function ($, window, document) {
    feedbackPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config({
    "shim": {
        "feedbackPlugin": ["jquery"]
    }
});