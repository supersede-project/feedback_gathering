describe('JQuery Feedback Plugin', function() {

    beforeEach(function() {
        browser.ignoreSynchronization = true;
        browser.get('/');
    });

    it('should open a dialog on link click', function() {
        let feedbackEntryLink = browser.element(by.css('a#feedbackEntryPoint'));
        feedbackEntryLink.click();

        browser.sleep(2000);

        expect(feedbackEntryLink.isPresent()).toEqual(true);
        expect(feedbackEntryLink).toBeDefined();

        let feedbackDialog = browser.element(by.css('section#feedbackContainer'));

        expect(feedbackDialog.isPresent()).toEqual(true);
    });

    xit('should validate text input', function() {
        let feedbackEntryLink = browser.element(by.css('a#feedbackEntryPoint'));
        let dialogForward = browser.element(by.css('.feedback-dialog-forward'));
        let textTypeTextarea = browser.element(by.css('textarea#textTypeText'));

        feedbackEntryLink.click();

        browser.sleep(2000);

        textTypeTextarea.clear();   
        dialogForward.click();

        console.log(feedbackPage2);

        expect(feedbackPage2.isPresent()).toEqual(false);

        /*browser.sleep(2000);

        textTypeTextarea.sendKeys('Hey! This is my feedback!');
        dialogForward.click();

        feedbackReviewContainer = browser.element(by.css('#feedbackReview'));
        expect(feedbackReviewContainer.isPresent()).toEqual(true);

        browser.sleep(1000);*/
    });

});
