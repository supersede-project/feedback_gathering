define(["require", "exports", './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', '../services/backends/mock_backend', '../models/feedbacks/feedback', '../models/feedbacks/rating', './helpers/page_navigation', '../services/application_service', './helpers/array_shuffle', './lib/jquery.star-rating-svg.js', './jquery.validate.js'], function (require, exports, config_1, pagination_container_1, screenshot_view_1, i18n_1, mock_backend_1, feedback_1, rating_1, page_navigation_1, application_service_1, array_shuffle_1) {
    "use strict";
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var pushConfigurationDialogId = "pushConfiguration";
        var pullDialog;
        var pullConfigurationDialogId = "pullConfiguration";
        var active = false;
        var application;
        var dialogTemplate = require('../templates/feedback_dialog.handlebars');
        var pullDialogTemplate = require('../templates/feedback_dialog.handlebars');
        var mockData = require('json!../services/mocks/applications_mock.json');
        var initApplication = function (applicationObject) {
            application = applicationObject;
            resetMessageView();
            initPushMechanisms(application.getPushConfiguration());
            var alreadyTriggeredOne = false;
            for (var _i = 0, _a = array_shuffle_1.shuffle(application.getPullConfigurations()); _i < _a.length; _i++) {
                var pullConfiguration = _a[_i];
                alreadyTriggeredOne = initPullConfiguration(pullConfiguration, alreadyTriggeredOne);
            }
        };
        var initPushMechanisms = function (configuration) {
            var context = configuration.getContextForView();
            var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pushConfigurationDialogId));
            dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, configuration, pageNavigation);
        };
        var initPullConfiguration = function (configuration, alreadyTriggeredOne) {
            if (alreadyTriggeredOne === void 0) { alreadyTriggeredOne = false; }
            if (!alreadyTriggeredOne && configuration.shouldGetTriggered()) {
                var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pullConfigurationDialogId));
                var context = configuration.getContextForView();
                pullDialog = initTemplate(pullDialogTemplate, pullConfigurationDialogId, context, configuration, pageNavigation);
                openDialog(pullDialog, configuration);
                return true;
            }
            return false;
        };
        var initTemplate = function (template, dialogId, context, configuration, pageNavigation) {
            var html = template(context);
            $('body').append(html);
            new pagination_container_1.PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
            initRating("#" + dialogId + " .rating-input", configuration.getMechanismConfig(config_1.mechanismTypes.ratingType));
            var screenshotView = initScreenshot(configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType), dialogId);
            var dialog = initDialog($('#' + dialogId), configuration.getMechanismConfig(config_1.mechanismTypes.textType));
            addEvents(dialogId, configuration);
            pageNavigation.screenshotView = screenshotView;
            return dialog;
        };
        var sendFeedback = function (formData, configuration) {
            var screenshotView = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType).screenshotView;
            var ratingMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            $.ajax({
                url: config_1.apiEndpoint + config_1.feedbackPath,
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    $('.server-response').addClass('success').text(config_1.defaultSuccessMessage);
                    $('textarea.text-type-text').val('');
                    screenshotView.reset();
                    initRating(".rating-input", ratingMechanism);
                },
                error: function (data) {
                    $('.server-response').addClass('error').text('Failure: ' + JSON.stringify(data));
                }
            });
        };
        var initRating = function (selector, ratingMechanism) {
            var options = ratingMechanism.getRatingElementOptions();
            $('' + selector).starRating(options);
            $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
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
        var initDialog = function (dialogContainer, textMechanism) {
            var dialogObject = dialogContainer.dialog($.extend({}, config_1.dialogOptions, {
                close: function () {
                    dialogObject.dialog("close");
                    active = false;
                }
            }));
            if (textMechanism) {
                dialogObject.dialog('option', 'title', textMechanism.getParameter('title').value);
            }
            else {
                dialogObject.dialog('option', 'title', 'Feedback');
            }
            return dialogObject;
        };
        var addEvents = function (containerId, configuration) {
            var container = $('#' + containerId);
            var textarea = container.find('textarea.text-type-text');
            var textMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            container.find('button.submit-feedback').unbind().on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                if (textMechanism) {
                    textarea.validate();
                    if (!textarea.hasClass('invalid')) {
                        var formData = prepareFormData(container, configuration);
                        sendFeedback(formData, configuration);
                    }
                }
                else {
                    var formData = prepareFormData(container, configuration);
                    sendFeedback(formData, configuration);
                }
            });
            if (textMechanism) {
                var maxLength = textMechanism.getParameter('maxLength').value;
                textarea.on('keyup focus', function () {
                    container.find('span.text-type-max-length').text($(this).val().length + '/' + maxLength);
                });
                container.find('.text-type-text-clear').on('click', function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    textarea.val('');
                });
            }
        };
        var prepareFormData = function (container, configuration) {
            var formData = new FormData();
            var textMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            container.find('.server-response').removeClass('error').removeClass('success');
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, config_1.applicationName, "uid12345", null, 1.0, null);
            if (textMechanism.active) {
                feedbackObject.text = container.find('textarea.text-type-text').val();
            }
            if (ratingMechanism.active) {
                var ratingTitle = container.find('.rating-text').text().trim();
                var rating = new rating_1.Rating(ratingTitle, ratingMechanism.currentRatingValue);
                feedbackObject.ratings = [];
                feedbackObject.ratings.push(rating);
            }
            if (screenshotMechanism.active && screenshotMechanism.screenshotView.getScreenshotAsBinary() !== null) {
                formData.append('file', screenshotMechanism.screenshotView.getScreenshotAsBinary());
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
            var screenshotMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            if (screenshotMechanism !== null && screenshotMechanism !== undefined && screenshotMechanism.screenshotView !== null) {
                screenshotMechanism.screenshotView.checkAutoTake();
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
            applicationService.retrieveApplication(1, function (application) {
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