define(["require", "exports", '../services/configuration_service', './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', '../services/backends/mock_backend', '../models/configurations/pull_configuration', '../models/feedbacks/feedback', '../models/feedbacks/rating', './helpers/page_navigation', './lib/jquery.star-rating-svg.js', './jquery.validate.js'], function (require, exports, configuration_service_1, config_1, pagination_container_1, screenshot_view_1, i18n_1, mock_backend_1, pull_configuration_1, feedback_1, rating_1, page_navigation_1) {
    "use strict";
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var pushConfigurationDialogId = "pushConfiguration";
        var pullDialog;
        var pullConfigurationDialogId = "pullConfiguration";
        var active = false;
        var pushConfiguration;
        var pullConfiguration;
        var dialogTemplate = require('../templates/feedback_dialog.handlebars');
        var pullDialogTemplate = require('../templates/feedback_dialog.handlebars');
        var mockData = require('json!../services/mocks/configurations_mock.json');
        var initMechanisms = function (configuration) {
            pushConfiguration = configuration;
            $('.server-response').removeClass('error').removeClass('success').text('');
            var context = configuration.getContextForView();
            var pageNavigation = new page_navigation_1.PageNavigation(configuration, $('#' + pushConfigurationDialogId));
            dialog = initTemplate(dialogTemplate, pushConfigurationDialogId, context, pushConfiguration, pageNavigation);
        };
        var initPullConfiguration = function (configuration) {
            pullConfiguration = configuration;
            $('.server-response').removeClass('error').removeClass('success').text('');
            var pageNavigation = new page_navigation_1.PageNavigation(pullConfiguration, $('#' + pullConfigurationDialogId));
            if (pullConfiguration.shouldGetTriggered()) {
                var context = pullConfiguration.getContextForView();
                pullDialog = initTemplate(pullDialogTemplate, pullConfigurationDialogId, context, pullConfiguration, pageNavigation);
                pullDialog.dialog('open');
            }
        };
        var initTemplate = function (template, dialogId, context, configuration, pageNavigation) {
            var html = template(context);
            $('body').append(html);
            new pagination_container_1.PaginationContainer($('#' + dialogId + '.feedback-container .pages-container'), pageNavigation);
            initRating("#" + dialogId + " .rating-input", configuration.getMechanismConfig(config_1.ratingType));
            var screenshotView = initScreenshot(configuration.getMechanismConfig(config_1.screenshotType), dialogId);
            var dialog = initDialog($('#' + dialogId), configuration.getMechanismConfig(config_1.textType));
            addEvents(dialogId, configuration);
            pageNavigation.screenshotView = screenshotView;
            return dialog;
        };
        var sendFeedback = function (formData, configuration) {
            var screenshotView = configuration.getMechanismConfig(config_1.screenshotType).screenshotView;
            var ratingMechanism = configuration.getMechanismConfig(config_1.ratingType);
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
            dialogObject.dialog('option', 'title', textMechanism.getParameter('title').value);
            return dialogObject;
        };
        var addEvents = function (containerId, configuration) {
            var container = $('#' + containerId);
            var textarea = container.find('textarea.text-type-text');
            var textMechanism = configuration.getMechanismConfig(config_1.textType);
            container.find('button.submit-feedback').unbind().on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.validate();
                if (!textarea.hasClass('invalid')) {
                    var formData = prepareFormData(container, configuration);
                    sendFeedback(formData, configuration);
                }
            });
            var maxLength = textMechanism.getParameter('maxLength').value;
            textarea.on('keyup focus', function () {
                container.find('span.text-type-max-length').text($(this).val().length + '/' + maxLength);
            });
            container.find('.text-type-text-clear').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.val('');
            });
        };
        var prepareFormData = function (container, configuration) {
            var formData = new FormData();
            var textMechanism = configuration.getMechanismConfig(config_1.textType);
            var ratingMechanism = configuration.getMechanismConfig(config_1.ratingType);
            var screenshotMechanism = configuration.getMechanismConfig(config_1.screenshotType);
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
        var toggleDialog = function () {
            if (!active) {
                dialog.dialog("open");
            }
            else {
                dialog.dialog("close");
            }
            active = !active;
        };
        $.fn.feedbackPlugin = function (options) {
            this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
            var currentOptions = this.options;
            var resources = {
                en: { translation: require('json!../locales/en/translation.json') },
                de: { translation: require('json!../locales/de/translation.json') }
            };
            i18n_1.I18nHelper.initializeI18n(resources, this.options);
            var configurationService = new configuration_service_1.ConfigurationService(new mock_backend_1.MockBackend(mockData));
            configurationService.retrieveConfiguration(function (configuration) {
                initMechanisms(configuration);
                if (configuration.pull_configurations.length > 0) {
                    var pullConfiguration = pull_configuration_1.PullConfiguration.initByData(configuration.pull_configurations[0]);
                    initPullConfiguration(pullConfiguration);
                }
            });
            this.css('background-color', currentOptions.backgroundColor);
            this.css('color', currentOptions.color);
            this.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                toggleDialog();
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