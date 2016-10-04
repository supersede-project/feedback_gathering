import {TextMechanism} from './text_mechanism';


describe('Text Mechanism', () => {
    let textMechanism:TextMechanism;

    beforeEach(() => {
        var parameters = [{key: 'maxLength', value: 100}, {key: 'title', value: 'Feedback'}, {key: 'hint', value: 'Enter your feedback'}];
        textMechanism = new TextMechanism(1, 'TEXT_TYPE', true, 1, true, parameters);
    });

    it('should return the parameter value', () => {
        var maxLength = textMechanism.getParameterValue('maxLength');
        expect(maxLength).toEqual(100);
    });
});

