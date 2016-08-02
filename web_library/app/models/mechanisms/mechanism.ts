import {ParameterInterface} from '../parameters/parameter_interface';
import {Parameterizable} from '../mixins/parameterizable';
import {applyMixins} from '../../js/helpers/mixin_helper';


/**
 * Base class for the mechanisms. Note that this class is extended by the Parameterizable mixin to provide methods on a
 * parameter array field.
 */
export class Mechanism implements Parameterizable {
    id:number;
    type:string;
    active:boolean;
    order: number;
    canBeActivated:boolean;
    parameters: ParameterInterface[];

    constructor(id: number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        this.id = id;
        this.type = type;
        this.active = active;
        this.order = order;
        this.canBeActivated = canBeActivated;
        this.parameters = parameters;
    }

    getParameter: (key:string) => ParameterInterface;
    getParameterValue: (key:string) => any;
}

applyMixins(Mechanism, [Parameterizable]);