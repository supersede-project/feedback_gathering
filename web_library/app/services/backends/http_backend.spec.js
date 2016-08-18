define(["require", "exports", './http_backend'], function (require, exports, http_backend_1) {
    "use strict";
    describe('Http Backend', function () {
        var configurationHttpBackend;
        var feebackHttpBackend;
        beforeEach(function () {
            configurationHttpBackend = new http_backend_1.HttpBackend('feedback_orchestrator/example/configuration');
            feebackHttpBackend = new http_backend_1.HttpBackend('feedbacks');
        });
        it('should return the correct url for the given path', function () {
            var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration';
            expect(configurationHttpBackend.getUrl()).toEqual(expectedPath);
        });
        it('should list all the mock data', function () {
            configurationHttpBackend.list(function (responseData) {
                expect(responseData.length).toBe(4);
            });
        });
    });
});
//# sourceMappingURL=http_backend.spec.js.map