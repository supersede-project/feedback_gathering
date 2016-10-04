define(["require", "exports", './text_mechanism'], function (require, exports, text_mechanism_1) {
    "use strict";
    describe('Text Mechanism', function () {
        var textMechanism;
        beforeEach(function () {
            var parameters = [{ key: 'maxLength', value: 100 }, { key: 'title', value: 'Feedback' }, { key: 'hint', value: 'Enter your feedback' }];
            textMechanism = new text_mechanism_1.TextMechanism(1, 'TEXT_TYPE', true, 1, true, parameters);
        });
        it('should return the parameter value', function () {
            var maxLength = textMechanism.getParameterValue('maxLength');
            expect(maxLength).toEqual(100);
        });
    });
});
//# sourceMappingURL=text_mechanism.spec.js.map