import './lib/jquery.star-rating-svg.min.js';
import './jquery.validate.js';
import {Feedback} from '../models/feedback';
import {Rating} from '../models/rating';
import {ConfigurationService} from '../services/configuration_service';
import {
    apiEndpoint, feedbackPath, configPath, applicationName, defaultSuccessMessage,
    feedbackObjectTitle, dialogOptions, textType, ratingType, screenshotType
} from './config';
import {PaginationContainer} from '../views/pagination_container';
import {RatingMechanism} from '../models/rating_mechanism';
import {ScreenshotView} from '../views/screenshot_view';
import {Mechanism} from '../models/mechanism';


export var feedbackPluginModule = function ($, window, document) {
    var dialog;
    var active = false;
    var ratingMechanism:RatingMechanism;
    var screenshotMechanism:Mechanism;
    var screenshotView:ScreenshotView;
    var template = require('../templates/feedback_dialog.handlebars');

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
    var initMechanisms = function (data) {
        var configurationService = new ConfigurationService(data);
        var textMechanism = configurationService.getMechanismConfig(textType);
        ratingMechanism = configurationService.getMechanismConfig(ratingType);
        screenshotMechanism = configurationService.getMechanismConfig(screenshotType);
        $('#serverResponse').removeClass().text('');

        var context = configurationService.getContextForView();
        initTemplate(context, screenshotMechanism, textMechanism, ratingMechanism);
    };

    var initTemplate = function (context, screenshotMechanism, textMechanism, ratingMechanism) {
        var html = template(context);
        $('body').append(html);

        // after template is loaded
        new PaginationContainer($('#feedbackContainer .pages-container'));
        initRating(".rating-input", ratingMechanism);
        initScreenshot(screenshotMechanism);
        initDialog($('#feedbackContainer'), textMechanism);
        addEvents(textMechanism);
    };

    /**
     * This method takes the data from the text mechanism and the rating mechanism and composes a feedback object with
     * the help of this data.
     * Then an AJAX request is done to send the submitted feedback to the feedback repository. A success or failure
     * message is shown after the request is done.
     */
    var sendFeedback = function (formData:FormData) {
        $.ajax({
            url: apiEndpoint + feedbackPath,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                $('#serverResponse').addClass('success').text(defaultSuccessMessage);
                $('textarea#textTypeText').val('');
                // TODO reset screenshot and rating
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
     *
     * Applies the jQuery star rating plugin on a specified element with the configuration from the rating mechanism.
     */
    var initRating = function (selector, ratingMechanism:RatingMechanism) {
        var options = ratingMechanism.getRatingElementOptions();
        $('' + selector).starRating(options);
    };

    var initScreenshot = function (screenshotMechanism):void {
        var screenshotPreview = $('#screenshotPreview'),
            screenshotCaptureButton = $('button#takeScreenshot'),
            elementToCapture = $('#page-wrapper_1'),
            elementsToHide = [$('.ui-widget-overlay.ui-front'), $('.ui-dialog')];
        screenshotView = new ScreenshotView(screenshotMechanism, screenshotPreview, screenshotCaptureButton,
            elementToCapture, elementsToHide)
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
        $('button#submitFeedback').unbind().on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            // validate anyway before sending
            textarea.validate();
            if (!textarea.hasClass('invalid')) {
                var formData = prepareFormData();
                sendFeedback(formData);
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
    };

    var prepareFormData = function ():FormData {
        var formData = new FormData();

        // TODO check which mechanism are active
        var text = $('textarea#textTypeText').val();
        $('#serverResponse').removeClass();
        var ratingTitle = $('.rating-text').text().trim();

        var feedbackObject = new Feedback(feedbackObjectTitle, applicationName, "uid12345", text, 1.0,
            [new Rating(ratingTitle, ratingMechanism.currentRatingValue)]);

        if (screenshotMechanism.active && screenshotView.getScreenshotAsBinary() !== null) {
            formData.append('file', screenshotView.getScreenshotAsBinary());
        }

        formData.append('json', JSON.stringify(feedbackObject));
        return formData;
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
        'backgroundColor': '#b3cd40'
    };

};

(function ($, window, document) {
    feedbackPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config({
    "shim": {
        "feedbackPlugin": ["jquery"]
    }
});