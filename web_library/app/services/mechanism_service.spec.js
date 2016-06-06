define(["require", "exports", './mechanism_service'], function (require, exports, mechanism_service_1) {
    "use strict";
    describe('Mechanism Service', function () {
        var mechanismService;
        beforeEach(function () {
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
                },
                {
                    "type": "AUDIO_TYPE",
                    "active": false,
                    "order": 2,
                    "canBeActivated": false,
                    "parameters": [
                        {
                            "key": "maxTime",
                            "value": 30,
                            "defaultValue": 30
                        }
                    ]
                },
                {
                    "type": "SCREENSHOT_TYPE",
                    "active": false,
                    "order": 3,
                    "canBeActivated": false,
                    "parameters": [
                        {
                            "key": "title",
                            "value": "Title for screenshot feedback"
                        },
                        {
                            "key": "defaultPicture",
                            "value": "lastScreenshot",
                            "defaultValue": "noImage",
                            "editableByUser": true
                        }
                    ]
                },
                {
                    "type": "RATING_TYPE",
                    "active": true,
                    "order": 4,
                    "canBeActivated": true,
                    "parameters": [
                        {
                            "key": "title",
                            "value": "Rate your user experience"
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
                            "value": 2,
                            "defaultValue": 0,
                            "editableByUser": false
                        }
                    ]
                }
            ];
            mechanismService = new mechanism_service_1.MechanismService(data);
        });
        it('should return the corresponding mechanisms', function () {
            var textMechanism = mechanismService.getMechanismConfig('TEXT_TYPE');
            expect(textMechanism).toBeDefined();
            expect(textMechanism.type).toEqual('TEXT_TYPE');
            expect(textMechanism.active).toEqual(true);
            expect(textMechanism.order).toEqual(1);
            expect(textMechanism.canBeActivated).toEqual(false);
            expect(textMechanism.parameters.length).toEqual(3);
        });
    });
});
