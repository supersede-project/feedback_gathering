import {Feedback} from './feedback';
import {PushConfiguration} from '../configurations/push_configuration';
import {ConfigurationFactory} from '../configurations/configuration_factory';


describe('Feedback', () => {

    it('should validate itself according to the given configuration data', () => {
        var configurationData = {
            "id": 1,
            "type": "PUSH",
            "general_configurations": null,
            "mechanisms": [
                {
                    "id": 1,
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
            ]
        };

        var configuration = ConfigurationFactory.createByData(configurationData);
        var feedback = new Feedback('Feedback', 'application', null, 'This is my feedback!', 1.0, []);
        expect(feedback.validate(configuration)).toBeTruthy();
    });
});

