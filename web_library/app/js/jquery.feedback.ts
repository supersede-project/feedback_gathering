import './lib/jquery.star-rating-svg.js';
import './jquery.validate';
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
import {shuffle} from './helpers/array_shuffle';
import * as dialogTemplate from '../templates/feedback_dialog.handlebars';
import * as pullDialogTemplate from '../templates/feedback_dialog.handlebars';
import * as intermediateDialogTemplate from '../templates/intermediate_dialog.handlebars';
import {GeneralConfiguration} from '../models/configurations/general_configuration';
import {TextFeedback} from '../models/feedbacks/text_feedback';
import {RatingFeedback} from '../models/feedbacks/rating_feedback';
import {ScreenshotFeedback} from '../models/feedbacks/screenshot_feedback';
var mockData = require('json!../services/mocks/applications_mock.json');


export var feedbackPluginModule = function ($, window, document) {
    var dialog;
    var pushConfigurationDialogId = "pushConfiguration";
    var pullDialog;
    var pullConfigurationDialogId = "pullConfiguration";
    var active = false;
    var application:Application;

    /**
     * @param applicationObject
     *  The current application object that configures the library.
     */
    var initApplication = function(applicationObject:Application) {
        application = applicationObject;
        resetMessageView();
        initPushMechanisms(application.getPushConfiguration(), application.generalConfiguration);

        var alreadyTriggeredOne = false;

        for(var pullConfiguration of shuffle(application.getPullConfigurations())) {
            alreadyTriggeredOne = initPullConfiguration(pullConfiguration, application.generalConfiguration, alreadyTriggeredOne);
        }
    };

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
        var context = configuration.getContextForView();

        var pageNavigation = new PageNavigation(configuration, $('#' + pushConfigurationDialogId));
        dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
    };

    /**
     * Initializes the pull mechanisms and triggers the feedback mechanisms if necessary.
     *
     * @param configuration
     * @param alreadyTriggeredOne
     *  Boolean that indicated whether a pull configuration has already been triggered.
     *
     * @returns boolean
     *  Whether the pull configuration was triggered or not.
     */
    var initPullConfiguration = function(configuration:PullConfiguration, generalConfiguration:GeneralConfiguration,
                                         alreadyTriggeredOne:boolean = false): boolean {
        if(!alreadyTriggeredOne && configuration.shouldGetTriggered()) {
            var pageNavigation = new PageNavigation(configuration, $('#' + pullConfigurationDialogId));
            var context = configuration.getContextForView();
            pullDialog = initTemplate(pullDialogTemplate, pullConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
            if(configuration.generalConfiguration.getParameterValue('intermediateDialog')) {
                var intermediateDialog = initIntermediateDialogTemplate(intermediateDialogTemplate, 'intermediateDialog', configuration, pullDialog, generalConfiguration);
                if(intermediateDialog !== null) {
                    intermediateDialog.dialog('open');
                }
            } else {
                openDialog(pullDialog, configuration);
            }
            return true;
        }
        return false;
    };

    var initTemplate = function (template, dialogId, context, configuration, pageNavigation,
                                 generalConfiguration:GeneralConfiguration): HTMLElement {
        var html = template(context);
        $('body').append(html);

        // after template is loaded
        new PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
        pageNavigation.screenshotViews = [];

        for(var ratingMechanism of configuration.getMechanismConfig(mechanismTypes.ratingType)) {
            initRating("#" + dialogId + " #ratingMechanism" + ratingMechanism.id + " .rating-input", ratingMechanism);
        }

        for(var screenshotMechanism of configuration.getMechanismConfig(mechanismTypes.screenshotType)) {
            var screenshotView = initScreenshot(screenshotMechanism, dialogId);
            pageNavigation.screenshotViews.push(screenshotView);
        }

        var title = "Feedback";
        if(generalConfiguration.getParameterValue('dialogTitle') !== null && generalConfiguration.getParameterValue('dialogTitle') !== "") {
            title = generalConfiguration.getParameterValue('dialogTitle');
        }

        var dialog = initDialog($('#'+ dialogId), title);
        addEvents(dialogId, configuration);
        return dialog;
    };

    var initIntermediateDialogTemplate = function(template, dialogId, configuration, pullDialog,
                                                  generalConfiguration:GeneralConfiguration): HTMLElement {
        var html = template({});
        $('body').append(html);

        var dialog = initDialog($('#'+ dialogId), generalConfiguration.getParameterValue('dialogTitle'));
        $('#feedbackYes').on('click', function() {
            dialog.dialog('close');
            openDialog(pullDialog, configuration);
        });
        $('#feedbackNo').on('click', function() {
            dialog.dialog('close');
        });
        $('#feedbackLater').on('click', function() {
            dialog.dialog('close');
        });
        return dialog;
    };

    /**
     * This method takes the data from the text mechanism and the rating mechanism and composes a feedback object with
     * the help of this data.
     * Then an AJAX request is done to send the submitted feedback to the feedback repository. A success or failure
     * message is shown after the request is done.
     */
    var sendFeedback = function (formData:FormData, configuration:ConfigurationInterface) {
        $.ajax({
            url: apiEndpointRepository + feedbackPath,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                resetPlugin(configuration);
            },
            error: function (data) {
                $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(data));
            }
        });
    };

    var resetPlugin = function(configuration) {
        $('.server-response').addClass('success').text(defaultSuccessMessage);
        $('textarea.text-type-text').val('');
        for(var screenshotMechanism of configuration.getMechanismConfig(mechanismTypes.screenshotType)) {
            screenshotMechanism.screenshotView.reset();
        }
        for(var ratingMechanism of configuration.getMechanismConfig(mechanismTypes.ratingType)) {
            initRating("#" + configuration.dialogId + " #ratingMechanism" + ratingMechanism.id + " .rating-input", ratingMechanism);
        }
    };

    /**
     * @param selector
     *  The jQuery selector that matches the element the star rating should be applied on
     * @param ratingMechanism
     *  The rating mechanism object that contains the configuration
     *
     * Applies the jQuery star rating plugin on a specified element with the configuration from the rating mechanism.
     */
    var initRating = function (selector, ratingMechanism:RatingMechanism) {
        if(ratingMechanism !== null && ratingMechanism.active) {
            var options = ratingMechanism.getRatingElementOptions();
            $('' + selector).starRating(options);
            // reset to default rating
            $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
        }
    };

    var initScreenshot = function (screenshotMechanism, containerId): ScreenshotView {
        if(screenshotMechanism == null) {
            return;
        }
        var container = $('#' + containerId);
        var dialogSelector = $('[aria-describedby="' + containerId + '"]');

        var screenshotPreview = container.find('.screenshot-preview'),
            screenshotCaptureButton = container.find('button.take-screenshot'),
            elementToCapture = $('#page-wrapper_1'),
            elementsToHide = [$('.ui-widget-overlay.ui-front'), dialogSelector];
        // TODO attention: circular dependency
        var screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton,
            elementToCapture, container, elementsToHide);

        screenshotMechanism.setScreenshotView(screenshotView);
        return screenshotView;
    };

    /**
     * @param dialogContainer
     *  Element that contains the dialog content
     * @param title
     *  The title of the dialog
     *
     * Initializes the dialog on a given element and opens it.
     */
    var initDialog = function (dialogContainer, title) {
        var dialogObject = dialogContainer.dialog(
            $.extend({}, dialogOptions, {
                close: function () {
                    dialogObject.dialog("close");
                    active = false;
                }
            })
        );
        dialogObject.dialog('option', 'title', title);
        return dialogObject;
    };

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
    var addEvents = function (containerId, configuration:ConfigurationInterface) {
        var container = $('#' + containerId);
        var textareas = container.find('textarea.text-type-text');
        var textMechanisms = configuration.getMechanismConfig(mechanismTypes.textType);

        container.find('button.submit-feedback').unbind().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            // validate anyway before sending
            if(textMechanisms.length > 0) {
                textareas.each(function() {
                    $(this).validate();
                });

                var invalidTextareas = container.find('textarea.text-type-text.invalid');
                if(invalidTextareas.length == 0) {
                    var formData = prepareFormData(container, configuration);
                    sendFeedback(formData, configuration);
                }
            } else {
                var formData = prepareFormData(container, configuration);
                sendFeedback(formData, configuration);
            }
        });

        // character length
        for(var textMechanism of textMechanisms) {
            var sectionSelector = "textMechanism" + textMechanism.id;
            var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
            var maxLength = textMechanism.getParameterValue('maxLength');

            textarea.on('keyup focus', function () {
                container.find('section#' + sectionSelector + ' span.text-type-max-length').text($(this).val().length + '/' + maxLength);
            });

            // text clear button
            container.find('section#' + sectionSelector + ' .text-type-text-clear').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.val('');
            });
        }
    };

    /**
     * Creates the multipart form data containing the data of the active mechanisms.
     *
     * @returns {FormData}
     */
    var prepareFormData = function (container:JQuery, configuration:ConfigurationInterface):FormData {
        var formData = new FormData();

        var textMechanisms = configuration.getMechanismConfig(mechanismTypes.textType);
        var ratingMechanisms = configuration.getMechanismConfig(mechanismTypes.ratingType);
        var screenshotMechanisms = configuration.getMechanismConfig(mechanismTypes.screenshotType);

        container.find('.server-response').removeClass('error').removeClass('success');
        var feedbackObject = new Feedback(feedbackObjectTitle, "uid12345", "DE", applicationId, configuration.id, [], [], []);

        for(var textMechanism of textMechanisms) {
            if(textMechanism.active) {
                var sectionSelector = "textMechanism" + textMechanism.id;
                var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
                var textFeedback = new TextFeedback(textarea.val(), textMechanism.id);
                feedbackObject.textFeedbacks.push(textFeedback);
            }
        }

        for(var ratingMechanism of ratingMechanisms) {
            if (ratingMechanism.active) {
                var rating = new RatingFeedback(ratingMechanism.currentRatingValue, ratingMechanism.id);
                feedbackObject.ratingFeedbacks.push(rating);
            }
        }

        for(var screenshotMechanism of screenshotMechanisms) {
            if(screenshotMechanism.active) {
                var partName = "screenshot" + screenshotMechanism.id;
                var screenshotFeedback = new ScreenshotFeedback(partName, screenshotMechanism.id, partName);
                feedbackObject.screenshotFeedbacks.push(screenshotFeedback);
                formData.append(partName, screenshotMechanism.screenshotView.getScreenshotAsBinary());
            }
        }

        formData.append('json', JSON.stringify(feedbackObject));

        return formData;
    };

    /**
     * The configuration data is fetched from the API if the feedback mechanism is not currently active. In the other
     * case the feedback mechanism dialog is closed. The active variable is toggled on each invocation.
     */
    var toggleDialog = function (pushConfiguration) {
        if (!active) {
            openDialog(dialog, pushConfiguration);
        } else {
            dialog.dialog("close");
        }
        active = !active;
    };

    var openDialog = function(dialog, configuration) {
        for(var screenshotMechanism of configuration.getMechanismConfig(mechanismTypes.screenshotType)) {
            if(screenshotMechanism !== null && screenshotMechanism !== undefined && screenshotMechanism.screenshotView !== null) {
                screenshotMechanism.screenshotView.checkAutoTake();
            }
        }
        dialog.dialog('open');
    };

    var resetMessageView = function () {
        $('.server-response').removeClass('error').removeClass('success').text('');
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
        this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
        var currentOptions = this.options;
        var resources = {
            en: {translation: require('json!../locales/en/translation.json')},
            de: {translation: require('json!../locales/de/translation.json')}
        };

        I18nHelper.initializeI18n(resources, this.options);

        // loadDataHere to trigger pull if necessary
        var applicationService = new ApplicationService();
        applicationService.retrieveApplication(applicationId, function(application) {
            initApplication(application);
        });

        this.css('background-color', currentOptions.backgroundColor);
        this.css('color', currentOptions.color);
        this.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            toggleDialog(application.getPushConfiguration());
        });

        return this;
    };

    $.fn.feedbackPlugin.defaults = {
        'color': '#fff',
        'lang': 'en',
        'backgroundColor': '#b3cd40'
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