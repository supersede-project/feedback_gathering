define(["require", "exports", './mechanism_factory', './rating_mechanism'], function (require, exports, mechanism_factory_1, rating_mechanism_1) {
    "use strict";
    describe('Mechanism Factory', function () {
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
        it('should initialize a rating mechanism object with data passed', function () {
            var data = {
                "type": "RATING_TYPE",
                "active": true,
                "order": 4,
                "canBeActivated": false,
                "parameters": [
                    {
                        "key": "title",
                        "value": "Rate your user experience on this page"
                    },
                    {
                        "key": "ratingIcon",
                        "value": "star"
                    },
                    {
                        "key": "maxRating",
                        "value": 5
                    },
                    {
                        "key": "defaultRating",
                        "value": 3,
                        "defaultValue": 0,
                        "editableByUser": false
                    }
                ]
            };
            var mechanism = mechanism_factory_1.MechanismFactory.createByData(data);
            expect(mechanism).toEqual(jasmine.any(rating_mechanism_1.RatingMechanism));
            expect(mechanism.type).toEqual('RATING_TYPE');
            expect(mechanism.active).toEqual(true);
            expect(mechanism.order).toEqual(4);
            expect(mechanism.canBeActivated).toEqual(false);
            expect(mechanism.parameters.length).toEqual(4);
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
    });
});
//# sourceMappingURL=mechanism_factory.spec.js.map