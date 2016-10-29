import {PaginationContainer} from './pagination_container';


describe('Pagination Container', () => {
    let element:any;
    var elementWithValidationErrorOnFirstPage:any;

    beforeEach(() => {
        element = $j('<section class="pages-container">' +
            '<article data-feedback-page="1" class="feedback-page">' +
            '<section class="feedback-mechanism" id="textType">' +
            '<p id="textTypeHint" class="explanation">Please input text</p>' +
            '<textarea class="no-validate" id="textTypeText" data-mandatory="1" ' +
            'data-mandatory-default-text="This field can\'t be blank" ' +
            'data-mandatory-manual-text="Please fill in this field" ></textarea>' +
            '<p class="text-count"><span id="textTypeMaxLength"></span></p>' +
            '</section>' +
            '<section class="feedback-mechanism" id="ratingType">' +
            '<p class="rating-text">Please rate your experience on this page:</p>' +
            '<p class="rating-input"></p>' +
            '</section>' +
            '<button class="feedback-dialog-forward navigation-button">Review feedback</button>' +
            '<div class="clearfix"></div>' +
            '</article>' +
            '<article data-feedback-page="2" class="feedback-page">' +
            '<div class="clearfix"></div>' +
            '<div id="feedbackReview"><h2>Your feedback:</h2>' +
            '<p id="textReview"></p><p id="ratingReview"></p>' +
            '</div>' +
            '<div id="feedbackPrivacy">' +
            '<h2>Who will receive your feedback?</h2>' +
            '<fieldset><p>' +
            '<input type="radio" id="developers" name="privacy" value="developers">' +
            '<label for="developers"> Only the Energiesparkonto-Team</label></p><p>' +
            '<input type="radio" id="public" name="privacy" value="public">' +
            '<label for="public"> The feedback is going to be published in the forum. Signed in users are able to read the feedback there.</label>' +
            '</p>' +
            '</fieldset>' +
            '</div>' +
            '<section class="dialog-navigation">' +
            '<button class="feedback-dialog-backward navigation-button">back</button>' +
            '<button type="button" class="navigation-button" id="submitFeedback">Submit feedback</button>' +
            '<div class="clearfix"></div>' +
            '<span id="serverResponse"></span>' +
            '</section>' +
            '</article>' +
            '</section>');

        elementWithValidationErrorOnFirstPage = $j('<section class="pages-container">' +
            '<article data-feedback-page="1" class="feedback-page">' +
            '<section class="feedback-mechanism" id="textType">' +
            '<p id="textTypeHint" class="explanation">Please input text</p>' +
            '<textarea class="no-validate" id="textTypeText" data-mandatory="1" ' +
            'data-mandatory-default-text="This field can\'t be blank" ' +
            'data-mandatory-manual-text="Please fill in this field" ></textarea>' +
            '<p class="text-count"><span id="textTypeMaxLength"></span></p>' +
            '</section>' +
            '<section class="feedback-mechanism" id="ratingType">' +
            '<p class="rating-text">Please rate your experience on this page:</p>' +
            '<p class="rating-input"></p>' +
            '</section>' +
            '<button class="feedback-dialog-forward navigation-button">Review feedback</button>' +
            '<div class="clearfix"></div>' +
            '</article>' +
            '<article data-feedback-page="2" class="feedback-page">' +
            '<div class="clearfix"></div>' +
            '<div id="feedbackReview"><h2>Your feedback:</h2>' +
            '<p id="textReview"></p><p id="ratingReview"></p>' +
            '</div>' +
            '<div id="feedbackPrivacy">' +
            '<h2>Who will receive your feedback?</h2>' +
            '<fieldset><p>' +
            '<input type="radio" id="developers" name="privacy" value="developers">' +
            '<label for="developers"> Only the Energiesparkonto-Team</label></p><p>' +
            '<input type="radio" id="public" name="privacy" value="public">' +
            '<label for="public"> The feedback is going to be published in the forum. Signed in users are able to read the feedback there.</label>' +
            '</p>' +
            '</fieldset>' +
            '</div>' +
            '<section class="dialog-navigation">' +
            '<button class="feedback-dialog-backward navigation-button">back</button>' +
            '<button type="button" class="navigation-button" id="submitFeedback">Submit feedback</button>' +
            '<div class="clearfix"></div>' +
            '<span id="serverResponse"></span>' +
            '</section>' +
            '</article>' +
            '</section>');
    });

    it('should have 2 pages', () => {
        var paginationContainer = new PaginationContainer(element);
        expect(paginationContainer.pages.length).toBe(2);
    });

    it('should only show the first page on init', () => {
        var paginationContainer = new PaginationContainer(element);

        var firstPage = paginationContainer.container.find('.feedback-page[data-feedback-page="1"]');
        var secondPage = paginationContainer.container.find('.feedback-page[data-feedback-page="2"]');
        expect(firstPage.css('display')).toEqual('block');
        expect(secondPage.css('display')).toEqual('none');
    });

    it('should navigate forward and backward', () => {
        var paginationContainer = new PaginationContainer(element);

        var firstPage = paginationContainer.container.find('.feedback-page[data-feedback-page="1"]');
        var secondPage = paginationContainer.container.find('.feedback-page[data-feedback-page="2"]');
        expect(firstPage.css('display')).toEqual('block');
        expect(secondPage.css('display')).toEqual('none');

        paginationContainer.navigateForward();

        expect(firstPage.css('display')).toEqual('none');
        expect(secondPage.css('display')).toEqual('block');

        paginationContainer.navigateBackward();

        expect(firstPage.css('display')).toEqual('block');
        expect(secondPage.css('display')).toEqual('none');
    });

    it('should navigate forward and backward via buttons', () => {
        var paginationContainer = new PaginationContainer(element);

        var firstPage = paginationContainer.container.find('.feedback-page[data-feedback-page="1"]');
        var secondPage = paginationContainer.container.find('.feedback-page[data-feedback-page="2"]');
        expect(firstPage.css('display')).toEqual('block');
        expect(secondPage.css('display')).toEqual('none');

        paginationContainer.container.find('.feedback-dialog-forward').click();

        expect(firstPage.css('display')).toEqual('none');
        expect(secondPage.css('display')).toEqual('block');

        paginationContainer.container.find('.feedback-dialog-backward').click();

        expect(firstPage.css('display')).toEqual('block');
        expect(secondPage.css('display')).toEqual('none');
    });
});

