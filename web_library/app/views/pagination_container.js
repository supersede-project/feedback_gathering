define(["require", "exports"], function (require, exports) {
    "use strict";
    var PaginationContainer = (function () {
        function PaginationContainer(container, pageNavigation) {
            this.container = container;
            this.pageNavigation = pageNavigation;
            this.pages = this.container.find('.feedback-page');
            this.showFirstPage();
            this.activePage = 1;
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
            var nextPage = this.container.find('.feedback-page[data-feedback-page="' + (this.activePage + 1) + '"]');
            if (this.pageNavigation != null && !this.pageNavigation.pageForwardCallback(feedbackPage, nextPage)) {
                return;
            }
            if (this.activePage < this.pages.length) {
                this.activePage++;
            }
            feedbackPage.hide();
            nextPage.show();
            jQuery('html, body').animate({
                scrollTop: nextPage.offset().top - 100
            }, 0);
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
//# sourceMappingURL=pagination_container.js.map