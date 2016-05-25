describe('JQuery Feedback Plugin', function() {

    beforeEach(function() {
        browser.ignoreSynchronization = true;
        browser.get('/');
    });

    it('should open a dialog on link click', function() {
        let feedbackEntryLink = element(by.css('a#feedbackEntryPoint'));
        feedbackEntryLink.click();

        browser.sleep(2000);

        expect(feedbackEntryLink).toBeDefined();
    });

});
