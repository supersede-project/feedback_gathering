import {ConfigurationInterface} from './configuration_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {GeneralConfiguration} from './general_configuration';


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
     * @returns Mechanisms
     *  The matched mechanisms
     */
    getMechanismConfig(mechanismTypeConstant:string): Mechanism[] {
        var filteredArray = this.mechanisms.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return filteredArray;
        } else {
            return [];
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
    getContext():any {
        let context = {
            dialogId: this.dialogId,
            mechanisms: []
        };

        for(let mechanism of this.getMechanismsSorted()) {
            let mechanismContext = jQuery.extend({}, mechanism.defaultContext(), mechanism.getContext());
            context.mechanisms.push(mechanismContext);
        }

        return context;
    }


}