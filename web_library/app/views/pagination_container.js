define(["require", "exports"], function (require, exports) {
    "use strict";
    var PaginationContainer = (function () {
        function PaginationContainer(container) {
            this.container = container;
            this.pages = this.container.find('.feedback-page');
            this.showFirstPage();
            this.addNavigationEvents();
            this.activePage = 1;
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
                paginationContainerThis.navigateForward();
            });
            this.container.find('.feedback-dialog-backward').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                paginationContainerThis.navigateBackward();
            });
        };
        PaginationContainer.prototype.navigateForward = function () {
            var feedbackPage = this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]');
            if (this.activePage < this.pages.length) {
                this.activePage++;
            }
            feedbackPage.hide();
            var nextPage = this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]');
            nextPage.show();
            if (nextPage.find('#textReview').length > 0) {
                nextPage.find('#textReview').text(jQuery('textarea#textTypeText').val());
            }
        };
        PaginationContainer.prototype.navigateBackward = function () {
            var feedbackPage = this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]');
            if (this.activePage > 1) {
                this.activePage--;
            }
            feedbackPage.hide();
            this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]').show();
        };
        return PaginationContainer;
    }());
    exports.PaginationContainer = PaginationContainer;
});
