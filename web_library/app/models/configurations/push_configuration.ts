import {PullConfiguration} from './pull_configuration';
import {ParameterInterface} from '../parameters/parameter_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';


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
        return new PushConfiguration(data.id, data.mechanisms, data.general_configurations, data.pull_configurations);
    }
}

