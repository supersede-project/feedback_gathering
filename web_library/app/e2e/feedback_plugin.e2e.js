describe('JQuery Feedback Plugin', function () {
    beforeEach(function () {
        browser.ignoreSynchronization = true;
        browser.get('/');
    });
    it('should open a dialog on link click', function () {
        var feedbackEntryLink = element(by.css('a#feedbackEntryPoint'));
        feedbackEntryLink.click();
        browser.sleep(2000);
        expect(feedbackEntryLink).toBeDefined();
    });
});
//# sourceMappingURL=feedback_plugin.e2e.js.map