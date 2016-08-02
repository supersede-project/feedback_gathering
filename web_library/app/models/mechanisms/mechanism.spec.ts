import {Mechanism} from './mechanism';
import {MechanismFactory} from './mechanism_factory';


describe('Mechanism', () => {

    it('should initialize a mechanism object with data passed', () => {
        var data = {
            "type": "TEXT_TYPE",
            "active": true,
            "order": 1,
            "canBeActivated": false,
            "parameters": [
                {
                    "key": "maxLength",
                    "value": 100
                },
                {
                    "key": "title",
                    "value": "Feedback"
                },
                {
                    "key": "hint",
                    "value": "Enter your feedback"
                }
            ]
        };
        var mechanism = MechanismFactory.createByData(data);
        expect(mechanism.type).toEqual('TEXT_TYPE');
        expect(mechanism.active).toEqual(true);
        expect(mechanism.order).toEqual(1);
        expect(mechanism.canBeActivated).toEqual(false);
        expect(mechanism.parameters.length).toEqual(3);
    });

    it('should not initialize a mechanism if its type is not given', () => {
        var data = {
            "active": true,
            "order": 1,
            "canBeActivated": false,
            "parameters": [
                {
                    "key": "maxLength",
                    "value": 100
                },
                {
                    "key": "title",
                    "value": "Feedback"
                },
                {
                    "key": "hint",
                    "value": "Enter your feedback"
                }
            ]
        };
        var mechanism = MechanismFactory.createByData(data);
        expect(mechanism).toBeNull();
    });

    it('should return the corresponding parameter object', () => {
        var parameters = [{key: 'maxLength', value: 100}, {key: 'title', value: 'Feedback'}, {key: 'hint', value: 'Enter your feedback'}];
        var mechanism = new Mechanism(1, 'TEXT_TYPE', true, 1, true, parameters);

        expect(mechanism.getParameter('maxLength').value).toEqual(100);
        expect(mechanism.getParameter('title').value).toEqual('Feedback');
        expect(mechanism.getParameter('hint').value).toEqual('Enter your feedback');
    });

    it('should return the corresponding parameter value or null', () => {
        var parameters = [{key: 'maxLength', value: 100}, {key: 'title', value: 'Feedback'}, {key: 'hint', value: 'Enter your feedback'}];
        var mechanism = new Mechanism(2, 'TEXT_TYPE', true, 1, true, parameters);

        expect(mechanism.getParameterValue('maxLength')).toEqual(100);
        expect(mechanism.getParameterValue('title')).toEqual('Feedback');
        expect(mechanism.getParameterValue('hint')).toEqual('Enter your feedback');
        expect(mechanism.getParameterValue('notExistingParameter')).toBeNull();
    })
});

