define(["require", "exports", '../services/configuration_service', './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', 'i18next', '../services/backends/mock_backend', '../models/configurations/pull_configuration', '../models/feedbacks/feedback', '../models/feedbacks/rating', './lib/jquery.star-rating-svg.js', './jquery.validate.js'], function (require, exports, configuration_service_1, config_1, pagination_container_1, screenshot_view_1, i18n_1, i18n, mock_backend_1, pull_configuration_1, feedback_1, rating_1) {
    "use strict";
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var pullDialog;
        var active = false;
        var textMechanism;
        var ratingMechanism;
        var screenshotMechanism;
        var screenshotView;
        var dialogTemplate = require('../templates/feedback_dialog.handlebars');
        var pullDialogTemplate = require('../templates/feedback_dialog.handlebars');
        var mockData = require('json!../services/mocks/configurations_mock.json');
        var initMechanisms = function (configuration) {
            textMechanism = configuration.getMechanismConfig(config_1.textType);
            ratingMechanism = configuration.getMechanismConfig(config_1.ratingType);
            screenshotMechanism = configuration.getMechanismConfig(config_1.screenshotType);
            $('#serverResponse').removeClass().text('');
            var context = configuration.getContextForView();
            initTemplate(dialogTemplate, dialog, "pushConfiguration", context, screenshotMechanism, textMechanism, ratingMechanism);
        };
        var initPullConfiguration = function (configuration) {
            var pullConfiguration = pull_configuration_1.PullConfiguration.initByData(configuration.pull_configurations[0]);
            textMechanism = pullConfiguration.getMechanismConfig(config_1.textType);
            ratingMechanism = pullConfiguration.getMechanismConfig(config_1.ratingType);
            screenshotMechanism = pullConfiguration.getMechanismConfig(config_1.screenshotType);
            $('#serverResponse').removeClass().text('');
            var context = pullConfiguration.getContextForView();
            initTemplate(pullDialogTemplate, pullDialog, "pullConfiguration", context, screenshotMechanism, textMechanism, ratingMechanism);
            pullDialog.dialog('open');
        };
        var initTemplate = function (template, dialogObject, dialogId, context, screenshotMechanism, textMechanism, ratingMechanism) {
            var html = template(context);
            $('body').append(html);
            new pagination_container_1.PaginationContainer($('#feedbackContainer .pages-container'), pageForwardCallback);
            initRating(".rating-input", ratingMechanism);
            initScreenshot(screenshotMechanism);
            initDialog(dialogObject, $('#' + dialogId), textMechanism);
            addEvents(textMechanism);
        };
        var sendFeedback = function (formData) {
            $.ajax({
                url: config_1.apiEndpoint + config_1.feedbackPath,
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    $('#serverResponse').addClass('success').text(config_1.defaultSuccessMessage);
                    $('textarea#textTypeText').val('');
                    screenshotView.reset();
                    initRating(".rating-input", ratingMechanism);
                },
                error: function (data) {
                    $('#serverResponse').addClass('error').text('Failure: ' + JSON.stringify(data));
                }
            });
        };
        var initRating = function (selector, ratingMechanism) {
            var options = ratingMechanism.getRatingElementOptions();
            $('' + selector).starRating(options);
            $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
        };
        var initScreenshot = function (screenshotMechanism) {
            var screenshotPreview = $('#screenshotPreview'), screenshotCaptureButton = $('button#takeScreenshot'), elementToCapture = $('#page-wrapper_1'), elementsToHide = [$('.ui-widget-overlay.ui-front'), $('.ui-dialog')];
            screenshotView = new screenshot_view_1.ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton, elementToCapture, elementsToHide);
        };
        var initDialog = function (dialogObject, dialogContainer, textMechanism) {
            dialogObject = dialogContainer.dialog($.extend({}, config_1.dialogOptions, {
                close: function () {
                    dialogObject.dialog("close");
                    active = false;
                }
            }));
            dialogObject.dialog('option', 'title', textMechanism.getParameter('title').value);
        };
        var addEvents = function (textMechanism) {
            var textarea = $('textarea#textTypeText');
            $('button#submitFeedback').unbind().on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.validate();
                if (!textarea.hasClass('invalid')) {
                    var formData = prepareFormData();
                    sendFeedback(formData);
                }
            });
            var maxLength = textMechanism.getParameter('maxLength').value;
            textarea.on('keyup focus', function () {
                $('span#textTypeMaxLength').text($(this).val().length + '/' + maxLength);
            });
            $('#textTypeTextClear').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.val('');
            });
        };
        var pageForwardCallback = function (currentPage, nextPage) {
            currentPage.find('.validate').each(function () {
                $(this).validate();
            });
            if (currentPage.find('.invalid').length > 0 && currentPage.find('.validate[data-mandatory-validate-on-skip="1"]').length > 0) {
                return false;
            }
            if (nextPage.find('#textReview').length > 0 && textMechanism.active) {
                nextPage.find('#textReview').text($('textarea#textTypeText').val());
            }
            if (nextPage.find('#ratingReview').length > 0 && ratingMechanism.active) {
                nextPage.find('#ratingReview').text(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
            }
            if (nextPage.find('#screenshotReview').length > 0 && screenshotMechanism.active && screenshotView.screenshotCanvas != null) {
                var img = $('<img src="' + screenshotView.screenshotCanvas.toDataURL() + '" />');
                img.css('max-width', '20%');
                $('#screenshotReview').empty().append(img);
            }
            return true;
        };
        var prepareFormData = function () {
            var formData = new FormData();
            $('#serverResponse').removeClass();
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, config_1.applicationName, "uid12345", null, 1.0, null);
            if (textMechanism.active) {
                feedbackObject.text = $('textarea#textTypeText').val();
            }
            if (ratingMechanism.active) {
                var ratingTitle = $('.rating-text').text().trim();
                var rating = new rating_1.Rating(ratingTitle, ratingMechanism.currentRatingValue);
                feedbackObject.ratings = [];
                feedbackObject.ratings.push(rating);
            }
            if (screenshotMechanism.active && screenshotView.getScreenshotAsBinary() !== null) {
                formData.append('file', screenshotView.getScreenshotAsBinary());
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
                initPullConfiguration(configuration);
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