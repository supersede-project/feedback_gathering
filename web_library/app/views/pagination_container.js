define(["require", "exports"], function (require, exports) {
    "use strict";
    var PaginationContainer = (function () {
        function PaginationContainer(elementSelector) {
            this.elementSelector = elementSelector;
            this.container = jQuery('' + elementSelector);
            this.pages = this.container.find('.feedback-page');
            this.showFirstPage();
            this.addNavigationEvents();
        }
        PaginationContainer.prototype.showFirstPage = function () {
            this.pages.hide();
            this.container.find('.feedback-page[data-feedback-page="1"]').show();
        };
        PaginationContainer.prototype.addNavigationEvents = function () {
            var paginationContainerThis = this;
            this.container.find('.feedback-dialog-forward').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                paginationContainerThis.navigateForward($(this));
            });
            this.container.find('.feedback-dialog-backward').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                paginationContainerThis.navigateBackward($(this));
            });
        };
        PaginationContainer.prototype.navigateForward = function (forwardButton) {
            var feedbackPage = forwardButton.closest('.feedback-page');
            var pageNumber = feedbackPage.data('feedback-page');
            var nextPageNumber = pageNumber + 1;
            feedbackPage.hide();
            var nextPage = this.container.find('.feedback-page[data-feedback-page="' + nextPageNumber + '"]');
            nextPage.show();
            if (nextPage.find('#textReview').length > 0) {
                nextPage.find('#textReview').text($('textarea#textTypeText').val());
            }
        };
        PaginationContainer.prototype.navigateBackward = function (backwardButton) {
            var feedbackPage = backwardButton.closest('.feedback-page');
            var pageNumber = feedbackPage.data('feedback-page');
            var nextPage = pageNumber - 1;
            feedbackPage.hide();
            this.container.find('.feedback-page[data-feedback-page="' + nextPage + '"]').show();
        };
        return PaginationContainer;
    }());
    exports.PaginationContainer = PaginationContainer;
});
