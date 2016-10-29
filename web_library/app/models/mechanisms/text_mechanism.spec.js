define(["require", "exports", './text_mechanism'], function (require, exports, text_mechanism_1) {
    "use strict";
    describe('Text Mechanism', function () {
        var textMechanism;
        var $ = $j;
        beforeAll(function () {
            var html = '<section id="textMechanism1" class="feedback-mechanism horizontal text-type">' +
                '<p class="text-type-label explanation col col-left" style="">Tell us about your problem</p>' +
                '<article class="col col-right">' +
                '<textarea class="validate text-type-text" ' +
                'data-mandatory="1" ' +
                'data-mandatory-default-text="This field can\'t be blank" ' +
                'data-mandatory-manual-text="Please put something into this field" ' +
                'placeholder="Enter your feedback" ' +
                'data-validation-max-length="100" ' +
                'style="">Hey this is the feedback text</textarea>' +
                '<p class="textarea-bottom">' +
                '<button class="text-type-text-clear"' +
                'title="Clear the text input">x</button>' +
                '<span class="text-type-max-length">0 / 100</span></p>' +
                '<div class="clearfix"></div>' +
                '</article>' +
                '<div class="clearfix"></div>' +
                '</section>';
            $('body').append(html);
        });
        beforeEach(function () {
            var parameters = [
                { key: 'maxLength', value: 100 },
                { key: 'title', value: 'Feedback' },
                { key: 'hint', value: 'Enter your feedback' },
                { key: 'label', value: 'Tell us about your problem' },
                { key: 'maxLengthVisible', value: 1 },
                { key: 'clearInput', value: 1 },
                { key: 'mandatory', value: 1 },
                { key: 'mandatoryReminder', value: 'Please put something into this field' },
            ];
            textMechanism = new text_mechanism_1.TextMechanism(1, 'TEXT_TYPE', true, 1, true, parameters);
        });
        it('should return the parameter value', function () {
            var maxLength = textMechanism.getParameterValue('maxLength');
            expect(maxLength).toEqual(100);
        });
        it('should return the text feedback object', function () {
            var textFeedback = textMechanism.getTextFeedback();
            expect(textFeedback.text).toEqual("Hey this is the feedback text");
            expect(textFeedback.mechanismId).toEqual(1);
        });
    });
});
//# sourceMappingURL=text_mechanism.spec.js.map