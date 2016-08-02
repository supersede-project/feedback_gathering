import {ParameterInterface} from '../parameters/parameter_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';


/**
 * Configuration object used for the pull feedback case. That means when there is a reminder, popup etc. that is
 * displayed to the user in order to get feedback from him.
 */
export class PullConfiguration extends Configuration {
    active:boolean;
    parameters:ParameterInterface[];

    constructor(id:number, mechanisms:Mechanism[], active:boolean, parameters:ParameterInterface[]) {
        super(id, mechanisms);
        this.dialogId = 'pullConfiguration';
        this.active = active;
        this.parameters = parameters;
    }

    static initByData(data:any) {
        return new PullConfiguration(data.id, data.mechanisms, data.active, data.parameters);
    }
}