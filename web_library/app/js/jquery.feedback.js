define(["require", "exports", '../models/feedback', '../models/ratings', '../services/mechanism_service', './config', '../models/mechanism', '../views/pagination_container', './jquery.star-rating-svg.min.js'], function (require, exports, feedback_1, ratings_1, mechanism_service_1, config_1, mechanism_1, pagination_container_1) {
    "use strict";
    (function ($, window, document) {
        var dialog;
        var currentRatingValue;
        function initMechanisms(data) {
            var mechanismService = new mechanism_service_1.MechanismService(data);
            var textMechanism = mechanismService.getMechanismConfig(mechanism_1.textType);
            handleMechanismVisibility(textMechanism, '.feedback-mechanism#textType');
            var ratingMechanism = mechanismService.getMechanismConfig(mechanism_1.ratingType);
            handleMechanismVisibility(ratingMechanism, '.feedback-mechanism#ratingType');
            var textarea = $('textarea#textTypeText');
            $('span#textTypeMaxLength').text(textarea.val.length + '/' + textMechanism.getParameter('maxLength').value);
            $('#serverResponse').removeClass().text('');
            $('#textTypeHint').text(textMechanism.getParameter('hint').value);
            currentRatingValue = ratingMechanism.getParameter('defaultRating').value;
            var ratingTitle = ratingMechanism.getParameter('title').value;
            var numberOfStars = ratingMechanism.getParameter('maxRating').value;
            $('#ratingType > p.rating-text').text(ratingTitle);
            $(".rating-input").starRating({
                starSize: 25,
                totalStars: numberOfStars,
                initialRating: currentRatingValue,
                useFullStars: true,
                disableAfterRate: false,
                callback: function (currentRating, $el) {
                    currentRatingValue = currentRating;
                }
            });
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
            var currentOptions = this.options, active = false, dialogContainer = $('#feedbackContainer');
            this.css('background-color', currentOptions.backgroundColor);
            this.css('color', currentOptions.color);
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
            var paginationContainer = new pagination_container_1.PaginationContainer('#feedbackContainer .pages-container');
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
