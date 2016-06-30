define(["require", "exports", '../models/feedback', '../models/rating', '../services/configuration_service', './config', '../models/mechanism', '../views/pagination_container', './lib/jquery.star-rating-svg.min.js', './lib/html2canvas.js', './jquery.validate.js'], function (require, exports, feedback_1, rating_1, configuration_service_1, config_1, mechanism_1, pagination_container_1) {
    "use strict";
    exports.feedbackPluginModule = function ($, window, document) {
        var dialog;
        var currentRatingValue;
        var active = false;
        var feedbackDialog = require('../templates/feedback_dialog.handlebars');
        function initMechanisms(data) {
            var mechanismService = new configuration_service_1.ConfigurationService(data);
            var textMechanism = mechanismService.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = mechanismService.getMechanismConfig(mechanism_1.ratingType);
            currentRatingValue = ratingMechanism.getParameter('defaultRating').value;
            $('#serverResponse').removeClass().text('');
            var context = mechanismService.getContextForView();
            var html = feedbackDialog(context);
            $('body').append(html);
            var dialogContainer = $('#feedbackContainer');
            new pagination_container_1.PaginationContainer($('#feedbackContainer .pages-container'));
            initRating(".rating-input", ratingMechanism, currentRatingValue);
            initDialog(dialogContainer, textMechanism);
            addEvents(textMechanism);
        }
        var sendFeedback = function () {
            var text = $('textarea#textTypeText').val();
            $('#serverResponse').removeClass();
            var ratingTitle = $('.rating-text').text().trim();
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, config_1.applicationName, "uid12345", text, 1.0, [new rating_1.Rating(ratingTitle, currentRatingValue)]);
            $.ajax({
                url: config_1.apiEndpoint + config_1.feedbackPath,
                type: 'POST',
                data: JSON.stringify(feedbackObject),
                success: function (data) {
                    $('#serverResponse').addClass('success').text(config_1.defaultSuccessMessage);
                    $('textarea#textTypeText').val('');
                },
                error: function (data) {
                    $('#serverResponse').addClass('error').text('Failure: ' + JSON.stringify(data));
                }
            });
        };
        var initRating = function (selector, ratingMechanism, currentRatingValue) {
            $('' + selector).starRating({
                starSize: 25,
                totalStars: ratingMechanism.getParameter('maxRating').value,
                initialRating: currentRatingValue,
                useFullStars: true,
                disableAfterRate: false,
                callback: function (currentRating, $el) {
                    currentRatingValue = currentRating;
                }
            });
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
            $('button#submitFeedback').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                textarea.validate();
                if (!textarea.hasClass('invalid')) {
                    sendFeedback();
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
            $('button#takeScreenshot').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                html2canvas(document.body, {
                    onrendered: function (canvas) {
                        document.body.appendChild(canvas);
                    }
                });
            });
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
            'backgroundColor': '#b3cd40',
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
