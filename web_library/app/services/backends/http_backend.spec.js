define(["require", "exports", './http_backend'], function (require, exports, http_backend_1) {
    "use strict";
    describe('Http Backend', function () {
        var configurationHttpBackend;
        var feebackHttpBackend;
        beforeEach(function () {
            configurationHttpBackend = new http_backend_1.HttpBackend('feedback_orchestrator/{lang}/example/configuration', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/', 'en');
            feebackHttpBackend = new http_backend_1.HttpBackend('{lang}/feedbacks', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/', 'de');
        });
        it('should return the correct url for the given path', function () {
            var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/en/example/configuration';
            expect(configurationHttpBackend.getUrl()).toEqual(expectedPath);
            var expectedFeedbackPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/de/feedbacks';
            expect(feebackHttpBackend.getUrl()).toEqual(expectedFeedbackPath);
        });
    });
});
//# sourceMappingURL=http_backend.spec.js.map