import {Feedback} from '../models/feedback';
import {Rating} from '../models/rating';
import {ConfigurationService} from '../services/configuration_service';
import {apiEndpoint, feedbackPath, configPath, applicationName, defaultSuccessMessage,
    feedbackObjectTitle, dialogOptions} from './config';
import {textType, ratingType} from '../models/mechanism';
import {PaginationContainer} from '../views/pagination_container';
import './lib/jquery.star-rating-svg.min.js';
import './lib/html2canvas.js';
import './jquery.validate.js';


export var feedbackPluginModule = function ($, window, document) {
    var dialog;
    var currentRatingValue;
    var active = false;
    var feedbackDialog = require('../templates/feedback_dialog.handlebars');
    
    /**
     * @param data
     *  Configuration data retrieved from the feedback orchestrator
     *
     * Initializes the mechanism objects with the configuration data. It then constructs the context variable for the
     * template and invokes the feedbackDialog template with the configuration data.
     * Furthermore, the pagination inside the dialog is initialized, the rating component is configured, the dialog
     * is configured and displayed and some events are added to the UI.
     * All events on the HTML have to be added after the template is appended to the body (if not using live binding).
     */
    function initMechanisms(data) {
        var mechanismService = new ConfigurationService(data);
        var textMechanism = mechanismService.getMechanismConfig(textType);
        var ratingMechanism = mechanismService.getMechanismConfig(ratingType);
        currentRatingValue = ratingMechanism.getParameter('defaultRating').value;
        $('#serverResponse').removeClass().text('');

        var context = mechanismService.getContextForView();
        var html = feedbackDialog(context);
        $('body').append(html);

        var dialogContainer = $('#feedbackContainer');

        new PaginationContainer($('#feedbackContainer .pages-container'));
        initRating(".rating-input", ratingMechanism, currentRatingValue);
        initDialog(dialogContainer, textMechanism);
        addEvents(textMechanism);
    }

    /**
     * This method takes the data from the text mechanism and the rating mechanism and composes a feedback object with
     * the help of this data.
     * Then an AJAX request is done to send the submitted feedback to the feedback repository. A success or failure
     * message is shown after the request is done.
     */
    var sendFeedback = function () {
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

    /**
     * @param selector
     *  The jQuery selector that matches the element the star rating should be applied on
     * @param ratingMechanism
     *  The rating mechanism object that contains the configuration
     * @param currentRatingValue
     *  The value that sets the initial rating of the rating mechanism
     *
     * Applies the jQuery star rating plugin on a specified element with the configuration from the rating mechanism.
     */
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

    /**
     * @param dialogContainer
     *  Element that contains the dialog content
     * @param textMechanism
     *  The text mechanism object that contains the configuration
     *
     * Initializes the dialog on a given element and opens it.
     */
    var initDialog = function (dialogContainer, textMechanism) {
        dialog = dialogContainer.dialog(
            $.extend({}, dialogOptions, {
                close: function () {
                    dialog.dialog("close");
                    active = false;
                }
            })
        );
        dialog.dialog('option', 'title', textMechanism.getParameter('title').value);
        dialog.dialog("open");
    };

    /**
     * @param textMechanism
     *  Configuration data for the text mechanism
     *
     * Adds the following events:
     * - Send event for the feedback form
     * - Character count event for the text mechanism
     */
    var addEvents = function (textMechanism) {
        var textarea = $('textarea#textTypeText');

        // send
        $('button#submitFeedback').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            // validate anyway before sending
            textarea.validate();
            if (!textarea.hasClass('invalid')) {
                sendFeedback();
            }
        });

        // character length
        var maxLength = textMechanism.getParameter('maxLength').value;
        textarea.on('keyup focus', function () {
            $('span#textTypeMaxLength').text($(this).val().length + '/' + maxLength);
        });

        // text clear button
        $('#textTypeTextClear').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            textarea.val('');
        });

        // take screenshot
        $('button#takeScreenshot').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            html2canvas(document.body, {
                onrendered: function(canvas) {
                    document.body.appendChild(canvas);
                }
            });
        });
    };

    /**
     * The configuration data is fetched from the API if the feedback mechanism is not currently active. In the other
     * case the feedback mechanism dialog is closed. The active variable is toggled on each invocation.
     */
    var retrieveConfigurationDataOrClose = function () {
        if (!active) {
            var url = apiEndpoint + configPath;
            $.get(url, null, function (data) {
                initMechanisms(data);
            });
        } else {
            dialog.dialog("close");
        }
        active = !active;
    };

    /**
     * @param options
     *  Client side configuration of the feedback library
     * @returns {jQuery}
     *
     * The feedbackPlugin() function can get applied to a HTML element. This element is then configured via the passed
     * options and the default options. If a click event on this element happens the configuration is fetched from the
     * server and the feedback mechanism is invoked.
     */
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

(function($, window, document) {
  feedbackPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config( {
    "shim": {
        "feedbackPlugin"  : ["jquery"]
    }
} );