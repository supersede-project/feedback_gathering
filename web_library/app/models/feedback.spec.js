define(["require", "exports", '../services/configuration_service', './feedback'], function (require, exports, configuration_service_1, feedback_1) {
    "use strict";
    describe('Feedback', function () {
        it('should validate itself according to the given configuration data', function () {
            var data = [
                {
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
                }
            ];
            var configurationService = new configuration_service_1.ConfigurationService(data);
            var feedback = new feedback_1.Feedback('Feedback', 'application', null, 'This is my feedback!', 1.0, []);
            expect(feedback.validate(configurationService)).toBeTruthy();
        });
        it('should return error messages if the validation was not successful', function () {
            var data = [
                {
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
                }
            ];
            var configurationService = new configuration_service_1.ConfigurationService(data);
            var feedback = new feedback_1.Feedback('Feedback', 'application', null, '', 1.0, []);
            var errors = feedback.validate(configurationService);
            expect(errors.textMechanism.length).toBe(1);
            expect(errors.ratingMechanism.length).toBe(0);
            expect(errors.general.length).toBe(0);
            expect(errors.textMechanism[0]).toEqual('Please input a text');
        });
    });
});
