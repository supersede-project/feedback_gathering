export class PaginationContainer {
    elementSelector:any;
    container:JQuery;
    pages:JQuery;

    constructor(elementSelector) {
        this.elementSelector = elementSelector;
        this.container = jQuery('' + elementSelector);
        this.pages = this.container.find('.feedback-page');
        this.showFirstPage();
        this.addNavigationEvents();
    }

    showFirstPage() {
        this.pages.hide();
        this.container.find('.feedback-page[data-feedback-page="1"]').show();
    }

    addNavigationEvents() {
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
    }

    navigateForward(forwardButton:JQuery) {
        var feedbackPage = forwardButton.closest('.feedback-page');
        var pageNumber = feedbackPage.data('feedback-page');
        var nextPageNumber = pageNumber + 1;

        feedbackPage.hide();
        var nextPage = this.container.find('.feedback-page[data-feedback-page="' + nextPageNumber + '"]');
        nextPage.show();

        if (nextPage.find('#textReview').length > 0) {
            nextPage.find('#textReview').text($('textarea#textTypeText').val());
        }
    }

    navigateBackward(backwardButton:JQuery) {
        var feedbackPage = backwardButton.closest('.feedback-page');
        var pageNumber = feedbackPage.data('feedback-page');
        var nextPage = pageNumber - 1;

        feedbackPage.hide();
        this.container.find('.feedback-page[data-feedback-page="' + nextPage + '"]').show();
    }
}
