define(["require", "exports", './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', '../models/feedbacks/feedback', './helpers/page_navigation', '../services/application_service', './helpers/array_shuffle', '../templates/feedback_dialog.handlebars', '../templates/feedback_dialog.handlebars', '../templates/intermediate_dialog.handlebars', '../models/feedbacks/rating_feedback', '../models/feedbacks/screenshot_feedback', '../models/feedbacks/attachment_feedback', '../models/feedbacks/audio_feedback', './lib/jquery.star-rating-svg.js', './jquery.validate', './jquery.fileupload', './lib/html2canvas.js'], function (require, exports, config_1, pagination_container_1, screenshot_view_1, i18n_1, feedback_1, page_navigation_1, application_service_1, array_shuffle_1, dialogTemplate, pullDialogTemplate, intermediateDialogTemplate, rating_feedback_1, screenshot_feedback_1, attachment_feedback_1, audio_feedback_1) {
    "use strict";
    var mockData = require('json!../services/mocks/dev/applications_mock.json');
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var pushConfigurationDialogId = "pushConfiguration";
        var pullDialog;
        var pullConfigurationDialogId = "pullConfiguration";
        var active = false;
        var application;
        var feedbackButton;
        var applicationContext;
        var distPath;
        var userId;
        var language;
        var dropArea;
        var dialogCSSClass;
        var colorPickerCSSClass;
        var initApplication = function (applicationObject) {
            application = applicationObject;
            applicationContext = application.getContextForView();
            resetMessageView();
            initPushMechanisms(application.getPushConfiguration(), application.generalConfiguration);
            feedbackButton.attr('title', application.generalConfiguration.getParameterValue('quickInfo'));
            var alreadyTriggeredOne = false;
            for (var _i = 0, _a = array_shuffle_1.shuffle(application.getPullConfigurations()); _i < _a.length; _i++) {
                var pullConfiguration = _a[_i];
                alreadyTriggeredOne = initPullConfiguration(pullConfiguration, application.generalConfiguration, alreadyTriggeredOne);
            }
        };
        var initPushMechanisms = function (configuration, generalConfiguration) {
            var context = prepareTemplateContext(configuration.getContextForView());
            var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pushConfigurationDialogId));
            dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, configuration, pageNavigation, generalConfiguration);
        };
        var initPullConfiguration = function (configuration, generalConfiguration, alreadyTriggeredOne) {
            if (alreadyTriggeredOne === void 0) { alreadyTriggeredOne = false; }
            if (!alreadyTriggeredOne && configuration.shouldGetTriggered()) {
                configuration.wasTriggered();
                var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pullConfigurationDialogId));
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
            for (var _d = 0, _e = configuration.getMechanismConfig(config_1.mechanismTypes.audioType); _d < _e.length; _d++) {
                var audioMechanism = _e[_d];
                var recordButton = $("#" + dialogId + " #audioMechanism" + audioMechanism.id + " .record-audio");
            }
            for (var _f = 0, _g = configuration.getMechanismConfig(config_1.mechanismTypes.attachmentType); _f < _g.length; _f++) {
                var attachmentMechanism = _g[_f];
                if (attachmentMechanism.active) {
                    var sectionSelector = "#attachmentMechanism" + attachmentMechanism.id;
                    dropArea = $('' + sectionSelector).find('.drop-area');
                    dropArea.fileUpload(distPath);
                }
            }
            var title = "Feedback";
            if (generalConfiguration.getParameterValue('dialogTitle') !== null && generalConfiguration.getParameterValue('dialogTitle') !== "") {
                title = generalConfiguration.getParameterValue('dialogTitle');
            }
            var modal = false;
            if (generalConfiguration.getParameterValue('dialogModal') !== null && generalConfiguration.getParameterValue('dialogModal') !== "") {
                modal = generalConfiguration.getParameterValue('dialogModal');
            }
            var dialog = initDialog($('#' + dialogId), title, modal);
            addEvents(dialogId, configuration);
            return dialog;
        };
        var initIntermediateDialogTemplate = function (template, dialogId, configuration, pullDialog, generalConfiguration) {
            var html = template({});
            $('body').append(html);
            var dialog = initDialog($('#' + dialogId), generalConfiguration.getParameterValue('dialogTitle'), true);
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
                url: config_1.apiEndpointRepository + 'feedback_repository/' + language + '/feedbacks/',
                type: 'POST',
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                success: function (data) {
                    $('.server-response').addClass('success').text(config_1.defaultSuccessMessage);
                    console.log('response');
                    console.log(data);
                    resetPlugin(configuration);
                },
                error: function (data) {
                    $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(data));
                }
            });
        };
        var resetPlugin = function (configuration) {
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
                if (ratingMechanism.initialRating) {
                    $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
                }
            }
        };
        var initScreenshot = function (screenshotMechanism, containerId) {
            if (screenshotMechanism == null) {
                return;
            }
            var elementToCaptureSelector = 'body';
            if (screenshotMechanism.getParameterValue('elementToCapture') !== null && screenshotMechanism.getParameterValue('elementToCapture') !== "") {
                elementToCaptureSelector = screenshotMechanism.getParameterValue('elementToCapture');
            }
            var container = $('#' + containerId);
            var dialogSelector = '[aria-describedby="' + containerId + '"]';
            var screenshotPreview = container.find('.screenshot-preview'), screenshotCaptureButton = container.find('button.take-screenshot'), elementToCapture = $('' + elementToCaptureSelector), elementsToHide = ['.ui-widget-overlay.ui-front', dialogSelector];
            var screenshotView = new screenshot_view_1.ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton, elementToCapture, container, distPath, elementsToHide);
            screenshotView.colorPickerCSSClass = colorPickerCSSClass;
            screenshotMechanism.setScreenshotView(screenshotView);
            return screenshotView;
        };
        var initDialog = function (dialogContainer, title, modal) {
            var dialogObject = dialogContainer.dialog($.extend({}, config_1.dialogOptions, {
                close: function () {
                    dialogObject.dialog("close");
                    active = false;
                },
                create: function (event, ui) {
                    var widget = $(this).dialog("widget");
                    $(".ui-dialog-titlebar-close span", widget)
                        .removeClass("ui-icon-closethick")
                        .addClass("ui-icon-minusthick");
                }
            }));
            dialogObject.dialog('option', 'title', title);
            dialogObject.dialog('option', 'modal', modal);
            dialogObject.dialog('option', 'dialogClass', dialogCSSClass);
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
                        prepareFormData(container, configuration, function (formData) {
                            sendFeedback(formData, configuration);
                        });
                    }
                }
                else {
                    prepareFormData(container, configuration, function (formData) {
                        sendFeedback(formData, configuration);
                    });
                }
            });
            for (var _i = 0, textMechanisms_1 = textMechanisms; _i < textMechanisms_1.length; _i++) {
                var textMechanism = textMechanisms_1[_i];
                var sectionSelector = "textMechanism" + textMechanism.id;
                var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
                var maxLength = textMechanism.getParameterValue('maxLength');
                textarea.on('keyup focus paste', function () {
                    container.find('section#' + sectionSelector + ' span.text-type-max-length').text($(this).val().length + '/' + maxLength);
                });
                container.find('section#' + sectionSelector + ' .text-type-text-clear').on('click', function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    textarea.val('');
                });
            }
            container.find('.discard-feedback').on('click', function () {
                if (configuration.dialogId === 'pushConfiguration') {
                    dialog.dialog("close");
                }
                else if (configuration.dialogId === 'pullConfiguration') {
                    pullDialog.dialog("close");
                }
                resetPlugin(configuration);
            });
            for (var _a = 0, categoryMechanisms_1 = categoryMechanisms; _a < categoryMechanisms_1.length; _a++) {
                var categoryMechanism = categoryMechanisms_1[_a];
                categoryMechanism.coordinateOwnInputAndRadioBoxes();
            }
        };
        var prepareFormData = function (container, configuration, callback) {
            var formData = new FormData();
            var textMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            var categoryMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.categoryType);
            var attachmentMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.attachmentType);
            var audioMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.audioType);
            container.find('.server-response').removeClass('error').removeClass('success');
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, userId, language, config_1.applicationId, configuration.id, [], [], [], [], null, [], []);
            for (var _i = 0, textMechanisms_2 = textMechanisms; _i < textMechanisms_2.length; _i++) {
                var textMechanism = textMechanisms_2[_i];
                if (textMechanism.active) {
                    feedbackObject.textFeedbacks.push(textMechanism.getTextFeedback());
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
                    if (screenshotMechanism.screenshotView.getScreenshotAsBinary() === null) {
                        continue;
                    }
                    var partName = "screenshot" + screenshotMechanism.id;
                    var screenshotFeedback = new screenshot_feedback_1.ScreenshotFeedback(partName, screenshotMechanism.id, partName, 'png');
                    feedbackObject.screenshotFeedbacks.push(screenshotFeedback);
                    formData.append(partName, screenshotMechanism.screenshotView.getScreenshotAsBinary());
                }
            }
            for (var _c = 0, categoryMechanisms_2 = categoryMechanisms; _c < categoryMechanisms_2.length; _c++) {
                var categoryMechanism = categoryMechanisms_2[_c];
                if (categoryMechanism.active) {
                    var categoryFeedbacks = categoryMechanism.getCategoryFeedbacks();
                    for (var _d = 0, categoryFeedbacks_1 = categoryFeedbacks; _d < categoryFeedbacks_1.length; _d++) {
                        var categoryFeedback = categoryFeedbacks_1[_d];
                        feedbackObject.categoryFeedbacks.push(categoryFeedback);
                    }
                }
            }
            for (var _e = 0, attachmentMechanisms_1 = attachmentMechanisms; _e < attachmentMechanisms_1.length; _e++) {
                var attachmentMechanism = attachmentMechanisms_1[_e];
                if (attachmentMechanism.active) {
                    var sectionSelector = "attachmentMechanism" + attachmentMechanism.id;
                    var input = container.find('section#' + sectionSelector + ' input[type=file]');
                    var files = dropArea.currentFiles;
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        var partName_1 = 'attachment' + i;
                        var attachmentFeedback = new attachment_feedback_1.AttachmentFeedback(partName_1, file.name, file.extension, attachmentMechanism.id);
                        formData.append(partName_1, file, file.name);
                        feedbackObject.attachmentFeedbacks.push(attachmentFeedback);
                    }
                }
            }
            var _loop_1 = function() {
                if (audioMechanism.active) {
                    var partName_2 = "audio" + audioMechanism.id;
                    audioElement = jQuery('section#audioMechanism' + audioMechanism.id + ' audio')[0];
                    if (!audioElement || Fr.voice.recorder === null) {
                        return "continue";
                    }
                    try {
                        audioFeedback = new audio_feedback_1.AudioFeedback(partName_2, Math.round(audioElement.duration), "wav", audioMechanism.id);
                        console.log('export is called');
                        Fr.voice.export(function (blob) {
                            console.log('blob is not called');
                            formData.append(partName_2, blob);
                            feedbackObject.audioFeedbacks.push(audioFeedback);
                        });
                    }
                    catch (e) {
                        console.log(e.message);
                    }
                }
            };
            var audioElement, audioFeedback;
            for (var _f = 0, audioMechanisms_1 = audioMechanisms; _f < audioMechanisms_1.length; _f++) {
                var audioMechanism = audioMechanisms_1[_f];
                var state_1 = _loop_1();
                if (state_1 === "continue") continue;
            }
            formData.append('json', JSON.stringify(feedbackObject));
            callback(formData);
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
        var prepareTemplateContext = function (context) {
            var contextWithApplicationContext = $.extend({}, applicationContext, context);
            var pluginContext = {
                'distPath': distPath
            };
            return $.extend({}, pluginContext, contextWithApplicationContext);
        };
        var resetMessageView = function () {
            $('.server-response').removeClass('error').removeClass('success').text('');
        };
        $.fn.feedbackPlugin = function (options) {
            feedbackButton = this;
            this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
            var currentOptions = this.options;
            distPath = currentOptions.distPath;
            userId = currentOptions.userId;
            dialogCSSClass = currentOptions.dialogCSSClass;
            colorPickerCSSClass = currentOptions.colorPickerCSSClass;
            language = i18n_1.I18nHelper.getLanguage(this.options);
            i18n_1.I18nHelper.initializeI18n(this.options);
            var applicationService = new application_service_1.ApplicationService(language);
            applicationService.retrieveApplication(config_1.applicationId, function (application) {
                if (application.state === null || application.state === 0) {
                    feedbackButton.hide();
                    return feedbackButton;
                }
                initApplication(application);
                feedbackButton.show();
            }, function (errorData) {
                console.warn('SERVER ERROR ' + errorData.status + ' ' + errorData.statusText + ': ' + errorData.responseText);
                feedbackButton.hide();
                return feedbackButton;
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
            'lang': 'de',
            'backgroundColor': '#7c9009',
            'fallbackLang': 'en',
            'distPath': 'dist/',
            'userId': '',
            'dialogCSSClass': 'feedback-dialog',
            'colorPickerCSSClass': 'color-picker'
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