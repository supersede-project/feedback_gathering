import {ParameterInterface} from '../parameters/parameter_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';
import {Parameterizable} from '../mixins/parameterizable';
import {applyMixins} from '../../js/helpers/mixin_helper';


/**
 * Configuration object used for the pull feedback case. That means when there is a reminder, popup etc. that is
 * displayed to the user in order to get feedback from him.
 * Note that this class is extended by the Parameterizable mixin to provide methods on a parameter array field.
 */
export class PullConfiguration extends Configuration implements Parameterizable {
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

    getParameter: (key:string) => ParameterInterface;
    getParameterValue: (key:string) => any;
}

applyMixins(PullConfiguration, [Parameterizable]);