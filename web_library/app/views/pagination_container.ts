import {PageNavigation} from '../js/helpers/page_navigation';


/**
 * Class that simply provides a pagination mechanism by showing/hiding some elements(pages)
 */
export class PaginationContainer {
    container:JQuery;
    pages:JQuery;
    activePage:number;
    pageNavigation:PageNavigation;

    /**
     *
     * @param container
     *  The element that contains the pages and buttons to move forward or backward
     * @param pageNavigation
     *  Object that has a method which is called on forward navigation
     */
    constructor(container, pageNavigation?) {
        this.container = container;
        this.pageNavigation = pageNavigation;
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
        if(this.pageNavigation != null && !this.pageNavigation.pageForwardCallback(feedbackPage, nextPage)) {
            return;
        }

        if(this.activePage < this.pages.length) {
            this.activePage++;
        }
        feedbackPage.hide();
        nextPage.show();

        // show top of dialog
        jQuery('html, body').animate({
            scrollTop: nextPage.offset().top - 100
        }, 0);
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
