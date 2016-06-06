define(["require", "exports", '../models/feedback', '../models/ratings', '../services/mechanism_service', './config'], function (require, exports, feedback_1, ratings_1, mechanism_service_1, config_1) {
    "use strict";
    (function ($, window, document) {
        var dialog;
        function initMechanisms(data) {
            var mechanismService = new mechanism_service_1.MechanismService(data);
            var textMechanism = mechanismService.getMechanismConfig('TEXT_TYPE');
            var textarea = $('textarea#textTypeText');
            $('span#textTypeMaxLength').text(textarea.val.length + '/' + textMechanism.getParameter('maxLength').value);
            $('#serverResponse').removeClass().text('');
            $('#textTypeHint').text(textMechanism.getParameter('hint').value);
            var currentRatingValue = 0;
            $(".rating-input").starRating({
                starSize: 25,
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
                var text = $('textarea#textTypeText').val();
                $('#serverResponse').removeClass();
                var ratingTitle = $('.rating-text').text().trim();
                var feedbackObject = new feedback_1.Feedback("Feedback", "energiesparkonto.de", "uid12345", text, 1.0, [new ratings_1.Rating(ratingTitle, currentRatingValue)]);
                $.ajax({
                    url: config_1.apiEndpoint + config_1.feedbackPath,
                    type: 'POST',
                    data: JSON.stringify(feedbackObject),
                    success: function (data) {
                        $('#serverResponse').addClass('success').text('Your feedback was successfully sent');
                        $('textarea#textTypeText').val('');
                    },
                    error: function (data) {
                        $('#serverResponse').addClass('error').text('Failure: ' + JSON.stringify(data));
                    }
                });
            });
            var maxLength = textMechanism.getParameter('maxLength').value;
            textarea.on('keyup focus', function () {
                $('span#textTypeMaxLength').text($(this).val().length + '/' + maxLength);
            });
        }
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
            dialogContainer.find('.feedback-page').hide();
            dialogContainer.find('.feedback-page[data-feedback-page="1"]').show();
            dialogContainer.find('.feedback-dialog-forward').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                var feedbackPage = $(this).closest('.feedback-page');
                var pageNumber = feedbackPage.data('feedback-page');
                var nextPageNumber = pageNumber + 1;
                feedbackPage.hide();
                var nextPage = $('.feedback-page[data-feedback-page="' + nextPageNumber + '"]');
                nextPage.show();
                if (nextPage.find('#textReview').length > 0) {
                    nextPage.find('#textReview').text($('textarea#textTypeText').val());
                }
            });
            dialogContainer.find('.feedback-dialog-backward').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                var feedbackPage = $(this).closest('.feedback-page');
                var pageNumber = feedbackPage.data('feedback-page');
                var nextPage = pageNumber - 1;
                feedbackPage.hide();
                $('.feedback-page[data-feedback-page="' + nextPage + '"]').show();
            });
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
