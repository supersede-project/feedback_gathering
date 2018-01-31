import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';
import {GeneralConfiguration} from './general_configuration';
import {configurationTypes} from '../../js/config';


/**
 * Configuration object for the case when the user actively invokes the feedback mechanisms in order to give feedback.
 */
export class ElementSpecificPushConfiguration extends Configuration {

    constructor(id:number, mechanisms:Mechanism[], generalConfiguration:GeneralConfiguration) {
        super(id, mechanisms, configurationTypes.elementSpecificPush, generalConfiguration);
        this.dialogId = 'elementSpecificPushConfiguration';
    }

    /**
     * Returns the context for templates without the contexts of the mechanisms.
     */
    getContext():any {
        return super.getContext();
    }
}

