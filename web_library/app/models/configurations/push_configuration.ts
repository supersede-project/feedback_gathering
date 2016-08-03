import {PullConfiguration} from './pull_configuration';
import {ParameterInterface} from '../parameters/parameter_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';
import {MechanismFactory} from '../mechanisms/mechanism_factory';


/**
 * Configuration object for the case when the user actively invokes the feedback mechanisms in order to give feedback.
 */
export class PushConfiguration extends Configuration {
    general_configurations:ParameterInterface[];
    pull_configurations:PullConfiguration[];

    constructor(id:number, mechanisms:Mechanism[], general_configurations:ParameterInterface[], pull_configurations:PullConfiguration[]) {
        super(id, mechanisms);
        this.dialogId = 'pushConfiguration';
        this.general_configurations = general_configurations;
        this.pull_configurations = pull_configurations;
    }

    static initByData(data:any) {
        var mechanisms = [];
        for(var mechanism of data.mechanisms) {
            mechanisms.push(MechanismFactory.createByData(mechanism));
        }
        return new PushConfiguration(data.id, mechanisms, data.general_configurations, data.pull_configurations);
    }
}

