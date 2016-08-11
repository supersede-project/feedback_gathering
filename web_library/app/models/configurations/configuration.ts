import {ConfigurationInterface} from './configuration_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {GeneralConfiguration} from './general_configuration';
import {mechanismTypes, configurationTypes} from '../../js/config';
import {PushConfiguration} from './push_configuration';
import {PullConfiguration} from './pull_configuration';
import {CategoryMechanism} from '../mechanisms/category_mechanism';


export abstract class Configuration implements ConfigurationInterface {
    id:number;
    mechanisms:Mechanism[];
    type:string;
    generalConfiguration:GeneralConfiguration;
    dialogId:string;

    constructor(id:number, mechanisms:Mechanism[], type:string, generalConfiguration:GeneralConfiguration) {
        this.id = id;
        this.mechanisms = mechanisms;
        this.type = type;
        this.generalConfiguration = generalConfiguration;
    }

    /**
     * @param mechanismTypeConstant
     *  The ID of the mechanism to get config of
     * @returns Mechanism
     *  The mechanism object with all its configuration data if the mechanism was found in the data, otherwise null.
     */
    getMechanismConfig(mechanismTypeConstant:string): Mechanism {
        var filteredArray = this.mechanisms.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return filteredArray[0];
        } else {
            return null;
        }
    }

    getMechanismsSorted(): Mechanism[] {
        return this.mechanisms.sort(function(a,b) {
            return (a.order > b.order) ? 1 : ((b.order > a.order) ? -1 : 0);}
        );
    }

    /**
     * @returns any
     *  Context object that contains all the data to configure the feedback mechanism in the view.
     */
    getContextForView() {
        var context = {
            dialogId: this.dialogId,
            mechanisms: []
        };

        for(var mechanism of this.getMechanismsSorted()) {
            var mechanismContext = jQuery.extend({}, mechanism, mechanism.getContext());
            context.mechanisms.push(mechanismContext);
        }
        return context;
    }


}