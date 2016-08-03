import {ParameterInterface} from '../parameters/parameter_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';
import {Parameterizable} from '../mixins/parameterizable';
import {applyMixins} from '../../js/helpers/mixin_helper';
import {MechanismFactory} from '../mechanisms/mechanism_factory';


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
        var mechanisms = [];
        for(var mechanism of data.mechanisms) {
            mechanisms.push(MechanismFactory.createByData(mechanism));
        }
        return new PullConfiguration(data.id, mechanisms, data.active, data.parameters);
    }

    getParameter: (key:string) => ParameterInterface;
    getParameterValue: (key:string) => any;

    /**
     * Decides whether the mechanisms associated with this configuration should get activated or not.
     *
     * @returns {boolean} true if the mechanismes should get triggered.
     */
    shouldGetTriggered(): boolean {
        return this.getParameterValue('askOnAppStartup') || Math.random() <= this.getParameterValue('likelihood');
    }
}

applyMixins(PullConfiguration, [Parameterizable]);