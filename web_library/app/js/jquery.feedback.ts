import {Feedback} from '../models/feedback';
import {Rating} from '../models/ratings';
import {MechanismService} from '../services/mechanism_service';
import {apiEndpoint, feedbackPath, configPath, feedbackTitle, applicationName, defaultSuccessMessage} from './config';
import './jquery.star-rating-svg.min.js';
import {textType, ratingType} from '../models/mechanism';


(function ($, window, document) {
    var dialog;
    var currentRatingValue;

    function initMechanisms(data) {
        var mechanismService = new MechanismService(data);
        var textMechanism = mechanismService.getMechanismConfig(textType);
        handleMechanismVisibility(textMechanism, '.feedback-mechanism#textType');
        
        var ratingMechanism = mechanismService.getMechanismConfig(ratingType);
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

    var sendFeedback = function() {
        var text = $('textarea#textTypeText').val();
        $('#serverResponse').removeClass();

        var ratingTitle = $('.rating-text').text().trim();
        var feedbackObject = new Feedback(feedbackTitle, applicationName, "uid12345", text, 1.0,
            [new Rating(ratingTitle, currentRatingValue)]);

        $.ajax({
            url: apiEndpoint + feedbackPath,
            type: 'POST',
            data: JSON.stringify(feedbackObject),
            success: function (data) {
                $('#serverResponse').addClass('success').text(defaultSuccessMessage);
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
        } else {
            $('' + selector).hide();
        }
    };

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