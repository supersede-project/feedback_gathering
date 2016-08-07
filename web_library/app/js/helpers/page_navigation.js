define(["require", "exports", 'i18next', '../config', './../jquery.validate.js'], function (require, exports, i18n, config_1) {
    "use strict";
    var PageNavigation = (function () {
        function PageNavigation(configuration, container) {
            this.configuration = configuration;
            this.container = container;
        }
        PageNavigation.prototype.pageForwardCallback = function (currentPage, nextPage) {
            var textMechanism = this.configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanism = this.configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanism = this.configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            var categoryMechanism = this.configuration.getMechanismConfig(config_1.mechanismTypes.categoryType);
            currentPage.find('.validate').each(function () {
                $(this).validate();
            });
            if (currentPage.find('.invalid').length > 0 && currentPage.find('.validate[data-mandatory-validate-on-skip="1"]').length > 0) {
                return false;
            }
            if (nextPage) {
                if (textMechanism != null && nextPage.find('.text-review').length > 0 && textMechanism.active) {
                    nextPage.find('.text-review').text(currentPage.find('textarea.text-type-text').val());
                }
                if (ratingMechanism != null && nextPage.find('.rating-review').length > 0 && ratingMechanism.active) {
                    nextPage.find('.rating-review').text(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
                }
                if (screenshotMechanism != null && nextPage.find('.screenshot-review').length > 0 && screenshotMechanism.active && this.screenshotView !== undefined && this.screenshotView.screenshotCanvas !== undefined) {
                    var img = $('<img src="' + this.screenshotView.screenshotCanvas.toDataURL() + '" />');
                    img.css('max-width', '20%');
                    nextPage.find('.screenshot-review').empty().append(img);
                }
                if (categoryMechanism !== null && nextPage.find('.category-review').length > 0 && categoryMechanism.active) {
                    currentPage.find('.category-type input').each(function () {
                        var input = $(this);
                        var selector = 'input#review' + input.attr('id');
                        var correspondingReviewInput = nextPage.find(selector);
                        correspondingReviewInput.prop("checked", input.is(':checked'));
                    });
                }
            }
            return true;
        };
        return PageNavigation;
    }());
    exports.PageNavigation = PageNavigation;
});
//# sourceMappingURL=page_navigation.js.map