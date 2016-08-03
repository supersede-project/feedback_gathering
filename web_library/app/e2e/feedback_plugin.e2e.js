describe('JQuery Feedback Plugin', function () {
    beforeEach(function () {
        browser.ignoreSynchronization = true;
        browser.get('/');
    });
    it('should open a dialog on link click', function () {
        var feedbackEntryLink = browser.element(by.css('a#feedbackEntryPoint'));
        feedbackEntryLink.click();
        browser.sleep(2000);
        expect(feedbackEntryLink.isPresent()).toEqual(true);
        expect(feedbackEntryLink).toBeDefined();
        var feedbackDialog = browser.element(by.css('section#feedbackContainer'));
        expect(feedbackDialog.isPresent()).toEqual(true);
    });
    xit('should validate text input', function () {
        var feedbackEntryLink = browser.element(by.css('a#feedbackEntryPoint'));
        var dialogForward = browser.element(by.css('.feedback-dialog-forward'));
        var textTypeTextarea = browser.element(by.css('textarea.textTypeText'));
        feedbackEntryLink.click();
        browser.sleep(2000);
        textTypeTextarea.clear();
        dialogForward.click();
        console.log(feedbackPage2);
        expect(feedbackPage2.isPresent()).toEqual(false);
    });
});
//# sourceMappingURL=feedback_plugin.e2e.js.map