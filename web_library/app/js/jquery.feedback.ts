import './lib/jquery.star-rating-svg.js';
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
import {ScreenshotView} from '../views/screenshot/screenshot_view';
import {Mechanism} from '../models/mechanism';
import {I18nHelper} from './helpers/i18n';
import i18n = require('i18next');
import {MockBackend} from '../services/backends/mock_backend';


export var feedbackPluginModule = function ($, window, document) {
    var dialog;
    var active = false;
    var textMechanism:Mechanism;
    var ratingMechanism:RatingMechanism;
    var screenshotMechanism:Mechanism;
    var screenshotView:ScreenshotView;
    var template = require('../templates/feedback_dialog.handlebars');
    var mockData = require('json!../services/mocks/configurations_mock.json');

    /**
     * @param configuration
     *  Configuration data retrieved from the feedback orchestrator
     *
     * Initializes the mechanism objects with the configuration data. It then constructs the context variable for the
     * template and invokes the feedbackDialog template with the configuration data.
     * Furthermore, the pagination inside the dialog is initialized, the rating component is configured, the dialog
     * is configured and displayed and some events are added to the UI.
     * All events on the HTML have to be added after the template is appended to the body (if not using live binding).
     */
    var initMechanisms = function (configuration) {
        textMechanism = configuration.getMechanismConfig(textType);
        ratingMechanism = configuration.getMechanismConfig(ratingType);
        screenshotMechanism = configuration.getMechanismConfig(screenshotType);
        $('#serverResponse').removeClass().text('');

        var context = configuration.getContextForView();
        initTemplate(context, screenshotMechanism, textMechanism, ratingMechanism);
    };

    var initTemplate = function (context, screenshotMechanism, textMechanism, ratingMechanism) {
        var html = template(context);
        $('body').append(html);

        // after template is loaded
        new PaginationContainer($('#feedbackContainer .pages-container'), pageForwardCallback);
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
                screenshotView.reset();
                initRating(".rating-input", ratingMechanism);
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
        // reset to default rating
        $('' + selector + ' .jq-star:nth-child(' + ratingMechanism.initialRating + ')').click();
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

    /**
     *
     * @param currentPage
     * @param nextPage
     * @returns {boolean}
     *  indicates whether the navigation forward should happen (true) or not (false)
     */
    var pageForwardCallback = function(currentPage, nextPage) {
        currentPage.find('.validate').each(function() {
            $(this).validate();
        });
        if(currentPage.find('.invalid').length > 0 && currentPage.find('.validate[data-mandatory-validate-on-skip="1"]').length > 0) {
            return false;
        }
        if(nextPage.find('#textReview').length > 0 && textMechanism.active) {
            nextPage.find('#textReview').text($('textarea#textTypeText').val());
        }
        if(nextPage.find('#ratingReview').length > 0 && ratingMechanism.active) {
            nextPage.find('#ratingReview').text(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
        }
        if(nextPage.find('#screenshotReview').length > 0 && screenshotMechanism.active && screenshotView.screenshotCanvas != null) {
            var img = $('<img src="' + screenshotView.screenshotCanvas.toDataURL() + '" />');
            img.css('max-width', '20%');
            $('#screenshotReview').empty().append(img);
        }
        return true;
    };

    /**
     * Creates the multipart form data containing the data of the active mechanisms.
     *
     * @returns {FormData}
     */
    var prepareFormData = function ():FormData {
        var formData = new FormData();

        $('#serverResponse').removeClass();
        var feedbackObject = new Feedback(feedbackObjectTitle, applicationName, "uid12345", null, 1.0, null);

        if(textMechanism.active) {
            feedbackObject.text = $('textarea#textTypeText').val();
        }
        if(ratingMechanism.active) {
            var ratingTitle = $('.rating-text').text().trim();
            var rating = new Rating(ratingTitle, ratingMechanism.currentRatingValue);
            feedbackObject.ratings = [];
            feedbackObject.ratings.push(rating);
        }
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
            var configurationService = new ConfigurationService(new MockBackend(mockData));
            configurationService.retrieveConfiguration(function(configuration) {
               initMechanisms(configuration);
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
        var resources = { en: {translation: require('json!../locales/en/translation.json')}, de: {translation: require('json!../locales/de/translation.json') }};

        I18nHelper.initializeI18n(resources, this.options);

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
    feedbackPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config({
    "shim": {
        "feedbackPlugin": ["jquery"]
    }
});