import {Feedback} from '../models/feedback';
import {Rating} from '../models/ratings';
import {MechanismService} from '../services/mechanism_service';
import {apiEndpoint, feedbackPath, configPath} from './config';
import './jquery.star-rating-svg.min.js';


(function ($, window, document) {
    var dialog;

    function initMechanisms(data) {
        var mechanismService = new MechanismService(data);
        var textMechanism = mechanismService.getMechanismConfig('TEXT_TYPE');
        if(textMechanism.active) {
            $('.feedback-mechanism#textType').show();
        } else {
            $('.feedback-mechanism#textType').hide();
        }
        
        var ratingMechanism = mechanismService.getMechanismConfig('RATING_TYPE');
        if(ratingMechanism.active) {
            $('.feedback-mechanism#ratingType').show();
        } else {
            $('.feedback-mechanism#ratingType').hide();
        }

        var textarea = $('textarea#textTypeText');

        $('span#textTypeMaxLength').text(textarea.val.length + '/' + textMechanism.getParameter('maxLength').value);
        $('#serverResponse').removeClass().text('');
        $('#textTypeHint').text(textMechanism.getParameter('hint').value);

        var currentRatingValue = ratingMechanism.getParameter('defaultRating').value;
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

            var text = $('textarea#textTypeText').val();
            $('#serverResponse').removeClass();

            var ratingTitle = $('.rating-text').text().trim();
            var feedbackObject = new Feedback("Feedback", "energiesparkonto.de", "uid12345", text, 1.0,
                [new Rating(ratingTitle, currentRatingValue)]);

            $.ajax({
                url: apiEndpoint + feedbackPath,
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
        var currentOptions = this.options,
            active = false,
            dialogContainer = $('#feedbackContainer');

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

            var url = apiEndpoint + configPath;

            if (!active) {
                $.get(url, null, function (data) {
                    initMechanisms(data);
                });
            } else {
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

requirejs.config( {
    "shim": {
        "feedbackPlugin"  : ["jquery"]
    }
} );