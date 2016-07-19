define(["require", "exports", '../models/feedback', '../models/rating', '../services/configuration_service', './config', '../views/pagination_container', '../views/screenshot/screenshot_view', './helpers/i18n', './lib/jquery.star-rating-svg.js', './jquery.validate.js'], function (require, exports, feedback_1, rating_1, configuration_service_1, config_1, pagination_container_1, screenshot_view_1, i18n_1) {
    "use strict";
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var active = false;
        var textMechanism;
        var ratingMechanism;
        var screenshotMechanism;
        var screenshotView;
        var template = require('../templates/feedback_dialog.handlebars');
        var initMechanisms = function (data) {
            var configurationService = new configuration_service_1.ConfigurationService(data);
            textMechanism = configurationService.getMechanismConfig(config_1.textType);
            ratingMechanism = configurationService.getMechanismConfig(config_1.ratingType);
            screenshotMechanism = configurationService.getMechanismConfig(config_1.screenshotType);
            $('#serverResponse').removeClass().text('');
            var context = configurationService.getContextForView();
            initTemplate(context, screenshotMechanism, textMechanism, ratingMechanism);
        };
        var initTemplate = function (context, screenshotMechanism, textMechanism, ratingMechanism) {
            var html = template(context);
            $('body').append(html);
            new pagination_container_1.PaginationContainer($('#feedbackContainer .pages-container'), pageForwardCallback);
            initRating(".rating-input", ratingMechanism);
            initScreenshot(screenshotMechanism);
            initDialog($('#feedbackContainer'), textMechanism);
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
        var initDialog = function (dialogContainer, textMechanism) {
            dialog = dialogContainer.dialog($.extend({}, config_1.dialogOptions, {
                close: function () {
                    dialog.dialog("close");
                    active = false;
                }
            }));
            dialog.dialog('option', 'title', textMechanism.getParameter('title').value);
            dialog.dialog("open");
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
                nextPage.find('#ratingReview').text(ratingMechanism.getParameterValue('title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
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
        var retrieveConfigurationDataOrClose = function () {
            if (!active) {
                var url = config_1.apiEndpoint + config_1.configPath;
                $.get(url, null, function (data) {
                    initMechanisms(data);
                });
            }
            else {
                dialog.dialog("close");
            }
            active = !active;
        };
        $.fn.feedbackPlugin = function (options) {
            this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
            var currentOptions = this.options;
            var resources = { en: { translation: require('json!../locales/en/translation.json') }, de: { translation: require('json!../locales/de/translation.json') } };
            i18n_1.I18nHelper.initializeI18n(resources, this.options);
            this.css('background-color', currentOptions.backgroundColor);
            this.css('color', currentOptions.color);
            this.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                retrieveConfigurationDataOrClose();
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