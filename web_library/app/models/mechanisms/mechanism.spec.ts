import {Mechanism} from './mechanism';
import {TextMechanism} from './text_mechanism';


describe('Mechanism', () => {

    it('should return the corresponding parameter object', () => {
        var parameters = [{key: 'maxLength', value: 100}, {key: 'title', value: 'Feedback'}, {key: 'hint', value: 'Enter your feedback'}];
        var mechanism = new TextMechanism(1, 'TEXT_TYPE', true, 1, true, parameters);
        expect(mechanism.getParameter('maxLength').value).toEqual(100);
        expect(mechanism.getParameter('title').value).toEqual('Feedback');
        expect(mechanism.getParameter('hint').value).toEqual('Enter your feedback');
    });

    it('should return the corresponding parameter value or null', () => {
        var parameters = [{key: 'maxLength', value: 100}, {key: 'title', value: 'Feedback'}, {key: 'hint', value: 'Enter your feedback'}];
        var mechanism = new TextMechanism(2, 'TEXT_TYPE', true, 1, true, parameters);

        expect(mechanism.getParameterValue('maxLength')).toEqual(100);
        expect(mechanism.getParameterValue('title')).toEqual('Feedback');
        expect(mechanism.getParameterValue('hint')).toEqual('Enter your feedback');
        expect(mechanism.getParameterValue('notExistingParameter')).toBeNull();
    })
});

