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
        var audioMechanisms = this.configuration.getMechanismConfig(mechanismTypes.audioType);
        var attachmentMechanisms = this.configuration.getMechanismConfig(mechanismTypes.attachmentType);

        currentPage.find('.validate').each(function () {
            $(this).validate();
        });
        currentPage.find('.validate-category').each(function () {
            $(this).validateCategory();
        });

        if (currentPage.find('.invalid').length > 0) {
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
                    img.css('width', '40%');
                    img.addClass('base');
                    var screenshotReviewElement = nextPage.find('.screenshot-review');
                    screenshotReviewElement.append(img);

                    var screenshotTextReview = $('<p class="screenshot-text-review"></p>');
                    screenshotReviewElement.append(screenshotTextReview);

                    // TODO get this 965 from the actual html
                    var ratio = 965*0.4 / screenshotMechanism.screenshotView.screenshotPreviewElement.width();

                }
                jQuery('section#screenshotMechanism' + screenshotMechanism.id + ' .sticker-container').each(function() {
                    var x = $(this).position().left;
                    var y = $(this).position().top;
                    var width = $(this).width();
                    var height = $(this).height();

                    var reviewClone = $(this).clone();
                    reviewClone.removeClass('ui-resizable');
                    reviewClone.removeClass('ui-draggable');
                    reviewClone.removeClass('ui-draggable-handle');
                    reviewClone.css('left', x * ratio + "px");
                    reviewClone.css('top', y * ratio + "px");
                    reviewClone.css('width', width * ratio + "px");
                    reviewClone.css('height', height * ratio + "px");
                    reviewClone.css('padding', "0");
                    reviewClone.addClass('sticker-container-review');
                    reviewClone.find('a').remove();

                    // adjust sizes of text
                    if($(this).hasClass('text-2')) {
                        var textarea = $(this).find('textarea');
                        var text = textarea.val();
                        var oldFontSize = textarea.css('font-size');
                        var newFontSize = ratio * parseInt(oldFontSize);
                        reviewClone.find('textarea').val(text).css('font-size', newFontSize + 'px').prop("disabled", true);
                    }

                    // text review on hover
                    if($(this).hasClass('text') || $(this).hasClass('text-2')) {
                        var text = $(this).find('textarea').val();
                        reviewClone.on('mouseover mouseenter', function() {
                            $('.screenshot-text-review').text(text).css('display', 'inline');
                        }).on('mouseleave', function() {
                            $('.screenshot-text-review').text("").css('display', 'none');
                        });
                    }

                    screenshotReviewElement.append(reviewClone);
                });
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

            for (var audioMechanism of audioMechanisms) {
                if (audioMechanism !== null && nextPage.find('.audio-review').length > 0 && audioMechanism.active) {
                    var audio =  currentPage.find('#audioMechanism' + audioMechanism.id + ' audio:first');
                    if(audio[0].duration && audio[0].duration > 0) {
                        var audioCopy = audio.clone();
                        audioCopy.css('display', 'block');
                        nextPage.find('.audio-review').empty().append(audioCopy);
                    } else {
                        // to adjust if multiple audio mechanisms should get supported
                        nextPage.find('.audio-review').empty();
                    }
                }
            }

            for (var attachmentMechanism of attachmentMechanisms) {
                if (attachmentMechanism !== null && nextPage.find('.attachment-review').length > 0 && attachmentMechanism.active) {
                    var table =  currentPage.find('#attachmentMechanism' + attachmentMechanism.id + ' table.current-files:first');
                    var tableCopy = table.clone();
                    nextPage.find('.attachment-review').empty().append(tableCopy);
                }
            }
        }
        return true;
    }
}