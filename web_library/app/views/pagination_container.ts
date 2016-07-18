/**
 * Class that simply provides a pagination mechanism by showing/hiding some elements(pages)
 */
export class PaginationContainer {
    container:JQuery;
    pages:JQuery;
    activePage:number;
    pageForwardCallback:any;

    /**
     *
     * @param container
     *  The element that contains the pages and buttons to move forward or backward
     * @param pageForwardCallback
     *  Function that is executed when the navigation goes forward
     */
    constructor(container, pageForwardCallback?) {
        this.container = container;
        this.pageForwardCallback = pageForwardCallback;
        this.pages = this.container.find('.feedback-page');
        this.showFirstPage();
        this.activePage = 1;
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
            paginationContainerThis.navigateForward();
        });
        this.container.find('.feedback-dialog-backward').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            paginationContainerThis.navigateBackward();
        });
    }

    /**
     * Shows the next page if any. Additionally, it copies the data from previous pages to the review section.
     */
    navigateForward() {
        var feedbackPage = this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]');
        var nextPage = this.container.find('.feedback-page[data-feedback-page="' + (this.activePage + 1) + '"]');
        if(this.pageForwardCallback != null && !this.pageForwardCallback(feedbackPage, nextPage)) {
            return;
        }

        if(this.activePage < this.pages.length) {
            this.activePage++;
        }
        feedbackPage.hide();
        nextPage.show();
    }

    navigateBackward() {
        var feedbackPage = this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]');
        if(this.activePage > 1) {
            this.activePage--;
        }
        feedbackPage.hide();
        this.container.find('.feedback-page[data-feedback-page="' + this.activePage + '"]').show();
    }
}
