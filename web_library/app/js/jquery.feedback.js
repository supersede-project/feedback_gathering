define(["require", "exports", '../models/feedback', '../models/ratings', '../services/mechanism_service', './config', '../models/mechanism', '../views/pagination_container', './jquery.star-rating-svg.min.js'], function (require, exports, feedback_1, ratings_1, mechanism_service_1, config_1, mechanism_1, pagination_container_1) {
    "use strict";
    var feedbackDialog = require('../templates/feedback_dialog.handlebars');
    (function ($, window, document) {
        var dialog;
        var currentRatingValue;
        var active = false;
        function initMechanisms(data) {
            var mechanismService = new mechanism_service_1.MechanismService(data);
            var textMechanism = mechanismService.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = mechanismService.getMechanismConfig(mechanism_1.ratingType);
            $('#serverResponse').removeClass().text('');
            currentRatingValue = ratingMechanism.getParameter('defaultRating').value;
            $(".rating-input").starRating({
                starSize: 25,
                totalStars: ratingMechanism.getParameter('maxRating').value,
                initialRating: currentRatingValue,
                useFullStars: true,
                disableAfterRate: false,
                callback: function (currentRating, $el) {
                    currentRatingValue = currentRating;
                }
            });
            var context = {
                textMechanism: {
                    hint: textMechanism.getParameter('hint').value,
                    currentLength: 0,
                    maxLength: textMechanism.getParameter('maxLength').value
                },
                ratingMechanism: {
                    title: ratingMechanism.getParameter('title').value
                }
            };
            var html = feedbackDialog(context);
            $('body').append(html);
            var dialogContainer = $('#feedbackContainer');
            dialog = dialogContainer.dialog({
                autoOpen: false,
                height: 'auto',
                width: 'auto',
                minWidth: 500,
                modal: true,
                title: 'Feedback',
                buttons: {},
                close: function () {
                    dialog.dialog("close");
                    active = false;
                }
            });
            var paginationContainer = new pagination_container_1.PaginationContainer($('#feedbackContainer .pages-container'));
            handleMechanismVisibility(textMechanism, '.feedback-mechanism#textType');
            handleMechanismVisibility(ratingMechanism, '.feedback-mechanism#ratingType');
            var textarea = $('textarea#textTypeText');
            $('#feedbackContainer').dialog('option', 'title', textMechanism.getParameter('title').value);
            dialog.dialog("open");
            $('button#submitFeedback').on('click', function (event) {
                event.preventDefault();
                sendFeedback();
            });
            var maxLength = textMechanism.getParameter('maxLength').value;
            textarea.on('keyup focus', function () {
                $('span#textTypeMaxLength').text($(this).val().length + '/' + maxLength);
            });
        }
        var sendFeedback = function () {
            var text = $('textarea#textTypeText').val();
            $('#serverResponse').removeClass();
            var ratingTitle = $('.rating-text').text().trim();
            var feedbackObject = new feedback_1.Feedback(config_1.feedbackObjectTitle, config_1.applicationName, "uid12345", text, 1.0, [new ratings_1.Rating(ratingTitle, currentRatingValue)]);
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
        var handleMechanismVisibility = function (mechanism, selector) {
            if (mechanism.active) {
                $('' + selector).show();
            }
            else {
                $('' + selector).hide();
            }
        };
        $.fn.feedbackPlugin = function (options) {
            this.options = $.extend({}, $.fn.feedbackPlugin.defaults, options);
            var currentOptions = this.options;
            this.css('background-color', currentOptions.backgroundColor);
            this.css('color', currentOptions.color);
            this.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                var url = config_1.apiEndpoint + config_1.configPath;
                if (!active) {
                    $.get(url, null, function (data) {
                        initMechanisms(data);
                    });
                }
                else {
                    dialog.dialog("close");
                }
                active = !active;
            });
            return this;
        };
        $.fn.feedbackPlugin.defaults = {
            'color': '#fff',
            'backgroundColor': '#b3cd40',
        };
    })(jQuery, window, document);
    requirejs.config({
        "shim": {
            "feedbackPlugin": ["jquery"]
        }
    });
});
