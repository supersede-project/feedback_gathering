define(["require", "exports", './mechanism', './parameter', './mechanism_factory'], function (require, exports, mechanism_1, parameter_1, mechanism_factory_1) {
    "use strict";
    describe('Mechanism', function () {
        it('should initialize a mechanism object with data passed', function () {
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
            var mechanism = mechanism_factory_1.MechanismFactory.createByData(data);
            expect(mechanism.type).toEqual('TEXT_TYPE');
            expect(mechanism.active).toEqual(true);
            expect(mechanism.order).toEqual(1);
            expect(mechanism.canBeActivated).toEqual(false);
            expect(mechanism.parameters.length).toEqual(3);
        });
        it('should not initialize a mechanism if its type is not given', function () {
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
            var mechanism = mechanism_factory_1.MechanismFactory.createByData(data);
            expect(mechanism).toBeNull();
        });
        it('should return the corresponding parameter object', function () {
            var parameters = [new parameter_1.Parameter('maxLength', 100), new parameter_1.Parameter('title', 'Feedback'), new parameter_1.Parameter('hint', 'Enter your feedback')];
            var mechanism = new mechanism_1.Mechanism('TEXT_TYPE', true, 1, true, parameters);
            expect(mechanism.getParameter('maxLength').value).toEqual(100);
            expect(mechanism.getParameter('title').value).toEqual('Feedback');
            expect(mechanism.getParameter('hint').value).toEqual('Enter your feedback');
        });
        it('should return the corresponding parameter value or null', function () {
            var parameters = [new parameter_1.Parameter('maxLength', 100), new parameter_1.Parameter('title', 'Feedback'), new parameter_1.Parameter('hint', 'Enter your feedback')];
            var mechanism = new mechanism_1.Mechanism('TEXT_TYPE', true, 1, true, parameters);
            expect(mechanism.getParameterValue('maxLength')).toEqual(100);
            expect(mechanism.getParameterValue('title')).toEqual('Feedback');
            expect(mechanism.getParameterValue('hint')).toEqual('Enter your feedback');
            expect(mechanism.getParameterValue('notExistingParameter')).toBeNull();
        });
    });
});
//# sourceMappingURL=mechanism.spec.js.map