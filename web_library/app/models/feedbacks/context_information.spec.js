define(["require", "exports", './context_information'], function (require, exports, context_information_1) {
    "use strict";
    describe('Context information', function () {
        it('should contain all relevant information when created', function () {
            var contextInformation = context_information_1.ContextInformation.create();
            expect(contextInformation.devicePixelRatio).not.toBeNull();
        });
    });
});
//# sourceMappingURL=context_information.spec.js.map