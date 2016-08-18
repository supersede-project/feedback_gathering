import {ConfigurationInterface} from '../../models/configurations/configuration_interface';
import i18n = require('i18next');
import './../jquery.validate';
import {mechanismTypes} from '../config';


export class PageNavigation {
    configuration:ConfigurationInterface;
    container:JQuery;

    constructor(configuration:ConfigurationInterface, container:JQuery) {
        this.configuration = configuration;
        this.container = container;
    }

    /**
     *
     * @param currentPage
     * @param nextPage
     * @returns {boolean}
     *  indicates whether the navigation forward should happen (true) or not (false)
     */
    pageForwardCallback(currentPage, nextPage) {
        var textMechanisms = this.configuration.getMechanismConfig(mechanismTypes.textType);
        var ratingMechanisms = this.configuration.getMechanismConfig(mechanismTypes.ratingType);
        var screenshotMechanisms = this.configuration.getMechanismConfig(mechanismTypes.screenshotType);
        var categoryMechanisms = this.configuration.getMechanismConfig(mechanismTypes.categoryType);

        currentPage.find('.validate').each(function () {
            $(this).validate();
        });
        if (currentPage.find('.invalid').length > 0 && currentPage.find('.validate[data-mandatory-validate-on-skip="1"]').length > 0) {
            return false;
        }

        if (nextPage) {
            nextPage.find('.text-review').empty();
            nextPage.find('.rating-review').empty();
            nextPage.find('.screenshot-review').empty();

            for (var textMechanism of textMechanisms) {
                if (textMechanism != null && nextPage.find('.text-review').length > 0 && textMechanism.active) {
                    var sectionSelector = "textMechanism" + textMechanism.id;
                    var textarea = currentPage.find('section#' + sectionSelector + ' textarea.text-type-text');
                    nextPage.find('.text-review').append('<p>' + textarea.val() + '</p>');
                }
            }

            for(var ratingMechanism of ratingMechanisms) {
                if (ratingMechanism != null && nextPage.find('.rating-review').length > 0 && ratingMechanism.active) {
                    nextPage.find('.rating-review').append(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
                }
            }

            for(var screenshotMechanism of screenshotMechanisms) {
                if (screenshotMechanism != null && nextPage.find('.screenshot-review').length > 0 && screenshotMechanism.active &&
                    screenshotMechanism.screenshotView !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== null) {
                    var img = $('<img src="' + screenshotMechanism.screenshotView.screenshotCanvas.toDataURL() + '" />');
                    img.css('max-width', '20%');
                    nextPage.find('.screenshot-review').append(img);
                }
            }

            for (var categoryMechanism of categoryMechanisms) {
                if (categoryMechanism !== null && nextPage.find('.category-review').length > 0 && categoryMechanism.active) {
                    var inputSelector = 'section#categoryMechanism' + categoryMechanism.id + '.category-type input';
                    currentPage.find(inputSelector).each(function () {
                        var input = $(this);
                        var selector = 'input#review' + input.attr('id');
                        var correspondingReviewInput = nextPage.find(selector);
                        correspondingReviewInput.prop("checked", input.is(':checked'));
                    });
                }
            }
        }
        return true;
    }

}