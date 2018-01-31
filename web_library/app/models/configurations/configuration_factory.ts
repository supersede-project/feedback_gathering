import {configurationTypes} from '../../js/config';
import {PushConfiguration} from './push_configuration';
import {MechanismFactory} from '../mechanisms/mechanism_factory';
import {GeneralConfiguration} from './general_configuration';
import {PullConfiguration} from './pull_configuration';
import {Mechanism} from '../mechanisms/mechanism';
import {ConfigurationInterface} from './configuration_interface';
import {ElementSpecificPushConfiguration} from './element_specific_push_configuration';


export class ConfigurationFactory {
    /**
     * Factory to create corresponding configuration type.
     *
     * @param data
     * @returns ConfigurationInterface
     *  The mechanism object parsed from json.
     */
    static createByData(data:any): PushConfiguration | PullConfiguration {
        if(!data.hasOwnProperty('type')) {
            return null;
        }
        var generalConfiguration:GeneralConfiguration = GeneralConfiguration.initByData(data.generalConfiguration);
        var mechanisms = [];
        for(var mechanism of data.mechanisms) {
            var mechanismObject:Mechanism = MechanismFactory.createByData(mechanism);
            if(mechanismObject !== null) {
                mechanisms.push(mechanismObject);
            }
        }

        if(data.type === configurationTypes.push) {
            return new PushConfiguration(data.id, mechanisms, generalConfiguration);
        } else if (data.type === configurationTypes.pull) {
            return new PullConfiguration(data.id, mechanisms, generalConfiguration);
        } else if (data.type === configurationTypes.elementSpecificPush) {
            return new ElementSpecificPushConfiguration(data.id, mechanisms, generalConfiguration);
        } else {
            return null;
        }
    }
}
