import {Feedback} from '../models/feedback';
import {Rating} from '../models/ratings';
import {MechanismService} from '../services/mechanism_service';
import {
    apiEndpoint, feedbackPath, configPath, applicationName, defaultSuccessMessage,
    feedbackObjectTitle
} from './config';
import './jquery.star-rating-svg.min.js';
import {textType, ratingType} from '../models/mechanism';
import {PaginationContainer} from '../views/pagination_container';
let feedbackDialog = require('../templates/feedback_dialog.handlebars');


(function ($, window, document) {
    var dialog;
    var currentRatingValue;
    var active = false;

    function initMechanisms(data) {
        var mechanismService = new MechanismService(data);
        var textMechanism = mechanismService.getMechanismConfig(textType);
        var ratingMechanism = mechanismService.getMechanismConfig(ratingType);
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
        var paginationContainer = new PaginationContainer($('#feedbackContainer .pages-container'));
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

    var sendFeedback = function() {
        var text = $('textarea#textTypeText').val();
        $('#serverResponse').removeClass();

        var ratingTitle = $('.rating-text').text().trim();
        var feedbackObject = new Feedback(feedbackObjectTitle, applicationName, "uid12345", text, 1.0,
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
        var currentOptions = this.options;

        this.css('background-color', currentOptions.backgroundColor);
        this.css('color', currentOptions.color);

        // feedback mechanism gets invoked
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