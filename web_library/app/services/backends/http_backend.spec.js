define(["require", "exports", './http_backend'], function (require, exports, http_backend_1) {
    "use strict";
    describe('Http Backend', function () {
        var configurationHttpBackend;
        var feebackHttpBackend;
        beforeEach(function () {
            configurationHttpBackend = new http_backend_1.HttpBackend('feedback_orchestrator/example/configuration', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/');
            feebackHttpBackend = new http_backend_1.HttpBackend('feedbacks', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/');
        });
        it('should return the correct url for the given path', function () {
            var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration';
            expect(configurationHttpBackend.getUrl()).toEqual(expectedPath);
        });
    });
});
//# sourceMappingURL=http_backend.spec.js.map