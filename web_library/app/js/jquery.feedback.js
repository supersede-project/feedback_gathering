define(["require", "exports", './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', '../services/backends/mock_backend', '../models/feedbacks/feedback', './helpers/page_navigation', '../services/application_service', './helpers/array_shuffle', '../templates/feedback_dialog.handlebars', '../templates/feedback_dialog.handlebars', '../templates/intermediate_dialog.handlebars', '../models/feedbacks/text_feedback', '../models/feedbacks/rating_feedback', '../models/feedbacks/screenshot_feedback', './lib/jquery.star-rating-svg.js', './jquery.validate'], function (require, exports, config_1, pagination_container_1, screenshot_view_1, i18n_1, mock_backend_1, feedback_1, page_navigation_1, application_service_1, array_shuffle_1, dialogTemplate, pullDialogTemplate, intermediateDialogTemplate, text_feedback_1, rating_feedback_1, screenshot_feedback_1) {
    "use strict";
    var mockData = require('json!../services/mocks/applications_mock.json');
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var pushConfigurationDialogId = "pushConfiguration";
        var pullDialog;
        var pullConfigurationDialogId = "pullConfiguration";
        var active = false;
        var application;
        var initApplication = function (applicationObject) {
            application = applicationObject;
            resetMessageView();
            initPushMechanisms(application.getPushConfiguration(), application.generalConfiguration);
            var alreadyTriggeredOne = false;
            for (var _i = 0, _a = array_shuffle_1.shuffle(application.getPullConfigurations()); _i < _a.length; _i++) {
                var pullConfiguration = _a[_i];
                alreadyTriggeredOne = initPullConfiguration(pullConfiguration, application.generalConfiguration, alreadyTriggeredOne);
            }
        };
        var initPushMechanisms = function (configuration, generalConfiguration) {
            var context = configuration.getContextForView();
            var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pushConfigurationDialogId));
            dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
        };
        var initPullConfiguration = function (configuration, generalConfiguration, alreadyTriggeredOne) {
            if (alreadyTriggeredOne === void 0) { alreadyTriggeredOne = false; }
            if (!alreadyTriggeredOne && configuration.shouldGetTriggered()) {
                configuration.wasTriggered();
                var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pullConfigurationDialogId));
                var context = configuration.getContextForView();
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
                }
                else {
                    setTimeout(function () {
                        if (!active) {
                            openDialog(pullDialog, configuration);
                        }
                    }, delay * 1000);
                }
                return true;
            }
            return false;
        };
        var initTemplate = function (template, dialogId, context, configuration, pageNavigation, generalConfiguration) {
            var html = template(context);
            $('body').append(html);
            new pagination_container_1.PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
            pageNavigation.screenshotViews = [];
            for (var _i = 0, _a = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType); _i < _a.length; _i++) {
                var ratingMechanism = _a[_i];
                initRating("#" + dialogId + " #ratingMechanism" + ratingMechanism.id + " .rating-input", ratingMechanism);
            }
            for (var _b = 0, _c = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType); _b < _c.length; _b++) {
                var screenshotMechanism = _c[_b];
                var screenshotView = initScreenshot(screenshotMechanism, dialogId);
                pageNavigation.screenshotViews.push(screenshotView);
            }
            var title = "Feedback";
            if (generalConfiguration.getParameterValue('dialogTitle') !== null && generalConfiguration.getParameterValue('dialogTitle') !== "") {
                title = generalConfiguration.getParameterValue('dialogTitle');
            }
            var dialog = initDialog($('#' + dialogId), title);
            addEvents(dialogId, configuration);
            return dialog;
        };
        var initIntermediateDialogTemplate = function (template, dialogId, configuration, pullDialog, generalConfiguration) {
            var html = template({});
            $('body').append(html);
            var dialog = initDialog($('#' + dialogId), generalConfiguration.getParameterValue('dialogTitle'));
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
        var sendFeedback = function (formData, configuration) {
            $.ajax({
                url: config_1.apiEndpointRepository + config_1.feedbackPath,
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
        var resetPlugin = function (configuration) {
            $('.server-response').addClass('success').text(config_1.defaultSuccessMessage);
            $('textarea.text-type-text').val('');
            for (var _i = 0, _a = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType); _i < _a.length; _i++) {
                var screenshotMechanism = _a[_i];
                screenshotMechanism.screenshotView.reset();
            }
            for (var _b = 0, _c = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType); _b < _c.length; _b++) {
                var ratingMechanism = _c[_b];
                initRating("#" + configuration.dialogId + " #ratingMechanism" + ratingMechanism.id + " .rating-input", ratingMechanism);
            }
        };
        var initRating = function (selector, ratingMechanism) {
            if (ratingMechanism !== null && ratingMechanism.active) {
                var options = ratingMechanism.getRatingElementOptions();
                $('' + selector).starRating(options);
                $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
            }
        };
        var initScreenshot = function (screenshotMechanism, containerId) {
            if (screenshotMechanism == null) {
                return;
            }
            var container = $('#' + containerId);
            var dialogSelector = $('[aria-describedby="' + containerId + '"]');
            var screenshotPreview = container.find('.screenshot-preview'), screenshotCaptureButton = container.find('button.take-screenshot'), elementToCapture = $('#page-wrapper_1'), elementsToHide = [$('.ui-widget-overlay.ui-front'), dialogSelector];
            var screenshotView = new screenshot_view_1.ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton, elementToCapture, container, elementsToHide);
            screenshotMechanism.setScreenshotView(screenshotView);
            return screenshotView;
        };
        var initDialog = function (dialogContainer, title) {
            var dialogObject = dialogContainer.dialog($.extend({}, config_1.dialogOptions, {
                close: function () {
                    dialogObject.dialog("close");
                    active = false;
                }
            }));
            dialogObject.dialog('option', 'title', title);
            return dialogObject;
        };
        var addEvents = function (containerId, configuration) {
            var container = $('#' + containerId);
            var textareas = container.find('textarea.text-type-text');
            var textMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var categoryMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.categoryType);
            container.find('button.submit-feedback').unbind().on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                if (textMechanisms.length > 0) {
                    textareas.each(function () {
                        $(this).validate();
                    });
                    var invalidTextareas = container.find('textarea.text-type-text.invalid');
                    if (invalidTextareas.length == 0) {
                        var formData = prepareFormData(container, configuration);
                        sendFeedback(formData, configuration);
                    }
                }
                else {
                    var formData = prepareFormData(container, configuration);
                    sendFeedback(formData, configuration);
                }
            });
            for (var _i = 0, textMechanisms_1 = textMechanisms; _i < textMechanisms_1.length; _i++) {
                var textMechanism = textMechanisms_1[_i];
                var sectionSelector = "textMechanism" + textMechanism.id;
                var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
                var maxLength = textMechanism.getParameterValue('maxLength');
                textarea.on('keyup focus', function () {
                    container.find('section#' + sectionSelector + ' span.text-type-max-length').text($(this).val().length + '/' + maxLength);
                });
                container.find('section#' + sectionSelector + ' .text-type-text-clear').on('click', function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    textarea.val('');
                });
            }
            for (var _a = 0, categoryMechanisms_1 = categoryMechanisms; _a < categoryMechanisms_1.length; _a++) {
                var categoryMechanism = categoryMechanisms_1[_a];
                categoryMechanism.coordinateOwnInputAndRadioBoxes();
            }
        };
        var prepareFormData = function (container, configuration) {
            var formData = new FormData();
            var textMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            var categoryMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.categoryType);
            container.find('.server-response').removeClass('error').removeClass('success');
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, "uid12345", "DE", config_1.applicationId, configuration.id, [], [], [], []);
            for (var _i = 0, textMechanisms_2 = textMechanisms; _i < textMechanisms_2.length; _i++) {
                var textMechanism = textMechanisms_2[_i];
                if (textMechanism.active) {
                    var sectionSelector = "textMechanism" + textMechanism.id;
                    var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
                    var textFeedback = new text_feedback_1.TextFeedback(textarea.val(), textMechanism.id);
                    feedbackObject.textFeedbacks.push(textFeedback);
                }
            }
            for (var _a = 0, ratingMechanisms_1 = ratingMechanisms; _a < ratingMechanisms_1.length; _a++) {
                var ratingMechanism = ratingMechanisms_1[_a];
                if (ratingMechanism.active) {
                    var rating = new rating_feedback_1.RatingFeedback(ratingMechanism.currentRatingValue, ratingMechanism.id);
                    feedbackObject.ratingFeedbacks.push(rating);
                }
            }
            for (var _b = 0, screenshotMechanisms_1 = screenshotMechanisms; _b < screenshotMechanisms_1.length; _b++) {
                var screenshotMechanism = screenshotMechanisms_1[_b];
                if (screenshotMechanism.active) {
                    var partName = "screenshot" + screenshotMechanism.id;
                    var screenshotFeedback = new screenshot_feedback_1.ScreenshotFeedback(partName, screenshotMechanism.id, partName);
                    feedbackObject.screenshotFeedbacks.push(screenshotFeedback);
                    formData.append(partName, screenshotMechanism.screenshotView.getScreenshotAsBinary());
                }
            }
            for (var _c = 0, categoryMechanisms_2 = categoryMechanisms; _c < categoryMechanisms_2.length; _c++) {
                var categoryMechanism = categoryMechanisms_2[_c];
                if (categoryMechanism.active) {
                    var categoryFeedback = categoryMechanism.getCategoryFeedback();
                    feedbackObject.categoryFeedbacks.push(categoryFeedback);
                }
            }
            formData.append('json', JSON.stringify(feedbackObject));
            return formData;
        };
        var toggleDialog = function (pushConfiguration) {
            if (!active) {
                openDialog(dialog, pushConfiguration);
            }
            else {
                dialog.dialog("close");
            }
            active = !active;
        };
        var openDialog = function (dialog, configuration) {
            for (var _i = 0, _a = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType); _i < _a.length; _i++) {
                var screenshotMechanism = _a[_i];
                if (screenshotMechanism !== null && screenshotMechanism !== undefined && screenshotMechanism.screenshotView !== null) {
                    screenshotMechanism.screenshotView.checkAutoTake();
                }
            }
            dialog.dialog('open');
        };
        var resetMessageView = function () {
            $('.server-response').removeClass('error').removeClass('success').text('');
        };
        $.fn.feedbackPlugin = function (options) {
            this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
            var currentOptions = this.options;
            var resources = {
                en: { translation: require('json!../locales/en/translation.json') },
                de: { translation: require('json!../locales/de/translation.json') }
            };
            i18n_1.I18nHelper.initializeI18n(resources, this.options);
            var applicationService = new application_service_1.ApplicationService(new mock_backend_1.MockBackend(mockData));
            applicationService.retrieveApplication(config_1.applicationId, function (application) {
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
        exports.feedbackPluginModule($, window, document);
    })(jQuery, window, document);
    requirejs.config({
        "shim": {
            "feedbackPlugin": ["jquery"]
        }
    });
});
//# sourceMappingURL=jquery.feedback.js.map