define(["require", "exports", './http_backend'], function (require, exports, http_backend_1) {
    "use strict";
    describe('Http Backend', function () {
        var httpBackend;
        beforeEach(function () {
            httpBackend = new http_backend_1.HttpBackend('configurations');
        });
        it('should return the correct url for the given path', function () {
            var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configurations';
            expect(httpBackend.getUrl()).toEqual(expectedPath);
        });
    });
});
//# sourceMappingURL=http_backend.spec.js.map