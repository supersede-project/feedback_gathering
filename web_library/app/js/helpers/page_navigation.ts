import {ConfigurationInterface} from '../../models/configurations/configuration_interface';
import i18n = require('i18next');
import './../jquery.validate';
import {mechanismTypes} from '../config';
import { ClusteringService } from '../../services/clustering_service';
import { readJSON } from '../../services/mocks/mocks_loader';


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
        let validFeedback = true;

        var textMechanisms = this.configuration.getMechanismConfig(mechanismTypes.textType);
        var ratingMechanisms = this.configuration.getMechanismConfig(mechanismTypes.ratingType);
        var screenshotMechanisms = this.configuration.getMechanismConfig(mechanismTypes.screenshotType);
        var categoryMechanisms = this.configuration.getMechanismConfig(mechanismTypes.categoryType);
        var audioMechanisms = this.configuration.getMechanismConfig(mechanismTypes.audioType);
        var attachmentMechanisms = this.configuration.getMechanismConfig(mechanismTypes.attachmentType);

        currentPage.find('.validate').each(function () {
            jQuery(this).validate();
        });
        currentPage.find('.validate-category').each(function () {
            jQuery(this).validateCategory();
        });

        if (currentPage.find('.invalid').length > 0) {
            validFeedback = false;
        }

        if(!this.validWithNoMechanismMandatory(currentPage)) {
            validFeedback = false;
        }

        if (nextPage) {
            nextPage.find('.text-review').empty();
            nextPage.find('.rating-review').empty();
            nextPage.find('.screenshot-review').empty();

            for (let textMechanism of textMechanisms) {
                if (textMechanism != null && nextPage.find('.text-review').length > 0 && textMechanism.active && textMechanism.getParameterValue('page') != "review") {
                    let sectionSelector = "textMechanism" + textMechanism.id;
                    let textarea = currentPage.find('section#' + sectionSelector + ' textarea.text-type-text');
                    nextPage.find('.text-review').append('<p>' + textarea.val() + '</p>');
                }
            }

            for(let ratingMechanism of ratingMechanisms) {
                if (ratingMechanism != null && nextPage.find('.rating-review').length > 0 && ratingMechanism.active && ratingMechanism.getParameterValue('page') != "review") {
                    nextPage.find('.rating-review').append(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
                }
            }

            for(let screenshotMechanism of screenshotMechanisms) {
                if (screenshotMechanism != null && nextPage.find('.screenshot-review').length > 0 && screenshotMechanism.active &&
                    screenshotMechanism.screenshotView !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== null
                    && screenshotMechanism.getParameterValue('page') != "review") {

                    var img = jQuery('<img src="' + screenshotMechanism.screenshotView.screenshotCanvas.toDataURL({
                        format: "image/jpeg",
                        quality: 1.0,
                        multiplier: 2.0
                    }) + '" />');
                    img.css('width', '40%');
                    img.addClass('base');
                    var screenshotReviewElement = nextPage.find('.screenshot-review');
                    screenshotReviewElement.append(img);

                    var screenshotTextReview = jQuery('<p class="screenshot-text-review"></p>');
                    screenshotReviewElement.append(screenshotTextReview);

                    // TODO get this 965 from the actual html
                    var ratio = 965*0.4 / screenshotMechanism.screenshotView.screenshotPreviewElement.width();

                }
                jQuery('section#screenshotMechanism' + screenshotMechanism.id + ' .sticker-container').each(function() {
                    var x = jQuery(this).position().left;
                    var y = jQuery(this).position().top;
                    var width = jQuery(this).width();
                    var height = jQuery(this).height();

                    var reviewClone = jQuery(this).clone();
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
                    if(jQuery(this).hasClass('text-2')) {
                        var textarea = jQuery(this).find('textarea');
                        var text = textarea.val();
                        var oldFontSize = textarea.css('font-size');
                        var newFontSize = ratio * parseInt(oldFontSize);
                        reviewClone.find('textarea').val(text).css('font-size', newFontSize + 'px').prop("disabled", true);
                    }

                    // text review on hover
                    if(jQuery(this).hasClass('text') || jQuery(this).hasClass('text-2')) {
                        var text = jQuery(this).find('textarea').val();
                        reviewClone.on('mouseover mouseenter', function() {
                            jQuery('.screenshot-text-review').text(text).css('display', 'inline');
                        }).on('mouseleave', function() {
                            jQuery('.screenshot-text-review').text("").css('display', 'none');
                        });
                    }

                    screenshotReviewElement.append(reviewClone);
                });
            }

            for (let categoryMechanism of categoryMechanisms) {
                if (categoryMechanism !== null && nextPage.find('.category-review').length > 0 && categoryMechanism.active && categoryMechanism.getParameterValue('page') != "review") {
                    var inputSelector = 'section#categoryMechanism' + categoryMechanism.id + '.category-type input';
                    currentPage.find(inputSelector).each(function () {
                        var input = jQuery(this);
                        var selector = 'input#review' + input.attr('id');
                        var correspondingReviewInput = nextPage.find(selector);
                        correspondingReviewInput.prop("checked", input.is(':checked'));
                    });
                }
            }

            for (let audioMechanism of audioMechanisms) {
                if (audioMechanism !== null && nextPage.find('.audio-review').length > 0 && audioMechanism.active && audioMechanism.getParameterValue('page') != "review") {
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

            for (let attachmentMechanism of attachmentMechanisms) {
                if (attachmentMechanism !== null && nextPage.find('.attachment-review').length > 0 && attachmentMechanism.active && attachmentMechanism.getParameterValue('page') != "review") {
                    var table =  currentPage.find('#attachmentMechanism' + attachmentMechanism.id + ' table.current-files:first');
                    var tableCopy = table.clone();
                    nextPage.find('.attachment-review').empty().append(tableCopy);
                }
            }
        }

        // TODO define parameters in the Orchestrator to control the feature, including tenant and number of similar feedback shown
        let feedbackClusteringEnabled = false;

        if(validFeedback && feedbackClusteringEnabled) {
            this.showClusteredFeedback();
        }


        return validFeedback;
    }

    showClusteredFeedback():void {
        let instantClusteringContainer = this.container.find('.instant-clustering-container');
        let textMechanisms = this.configuration.getMechanismConfig(mechanismTypes.textType);
        let feedbackText = '';

        for (let textMechanism of textMechanisms) {
            if (textMechanism != null && textMechanism.active && textMechanism.getParameterValue('page') != "review") {
                let sectionSelector = "textMechanism" + textMechanism.id;
                let textarea = this.container.find('section#' + sectionSelector + ' textarea.text-type-text');
                feedbackText += textarea.val() + ' ';
            }
        }

        let clusteringEndpointUrl = 'http://supersede.es.atos.net:3001/cluster/feedback';
        let clusteringService:ClusteringService = new ClusteringService();
        clusteringService.retrieveRelatedFeedback(clusteringEndpointUrl, feedbackText, 'atos', 5,
            (data) => {
                instantClusteringContainer.empty().html(data);
            },
            (data) => {
                console.warn('instant clustering request failed');
                console.warn(data);
            }
        );
/*
        let similarFeedbacks = require('json!../../services/mocks/dev/feedback_clustering.json');
        similarFeedbacks = similarFeedbacks.map((similarFeedback, index) => {
           return {
               'feedback': similarFeedback.feedback,
               'number': index + 1
           }
        });
        let similarFeedbacksTemplate = require('../../templates/partials/clustering_list_view.handlebars');
        let responseDataHtml = similarFeedbacksTemplate({similarFeedbacks: similarFeedbacks});
        instantClusteringContainer.empty().html(responseDataHtml);
*/
    }

    /**
     * If no mechanism is mandatory, at least one mechansim should be used by the user to have a meaningful feedback.
     *
     * @param currentPage
     * @returns {boolean}
     */
    validWithNoMechanismMandatory(currentPage):boolean {
        if(currentPage.find('.feedback-mechanism.mandatory').length === 0 && !this.atLeastOneMechanismWasUsed(currentPage)) {
            currentPage.parent().find('.at-least-one-mechanism').remove();
            let errorMessage = i18n.t('general.validation_at_least_one_mechanism');
            currentPage.after('<p class="feedback-form-error at-least-one-mechanism">' + errorMessage + '</p>');
            return false;
        } else {
            currentPage.parent().find('.at-least-one-mechanism').remove();
        }
        return true;
    }

    atLeastOneMechanismWasUsed(currentPage):boolean {
        let mechanismWasUsed = false;

        currentPage.find('.feedback-mechanism.text-type').each(function() {
            let textarea = jQuery(this).find('textarea.text-type-text');
            if(textarea.val().length > 0) {
                mechanismWasUsed = true;
            }
        });
        // only meaningful category mechanism should alone be valid for a feedback
        currentPage.find('.feedback-mechanism.category-type.valid-on-its-own').each(function() {
            if(jQuery(this).find('input:checked').length > 0 || jQuery(this).find('.own-category').val().length > 0) {
                mechanismWasUsed = true;
            }
        });
        currentPage.find('.feedback-mechanism.rating-type.valid-on-its-own').each(function() {
            if(parseInt(jQuery(this).find('.rating-input').starRating('getRating')) !== 0) {
                mechanismWasUsed = true;
            }
        });
        if(currentPage.find('.feedback-mechanism.screenshot-type.valid-on-its-own.dirty').length > 0) {
            mechanismWasUsed = true;
        }
        if(currentPage.find('.feedback-mechanism.audio-type.valid-on-its-own.dirty').length > 0) {
            mechanismWasUsed = true;
        }

        return mechanismWasUsed;
    }
}