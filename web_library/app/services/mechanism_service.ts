import {Mechanism, textType, ratingType} from '../models/mechanism';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class MechanismService {
    private data:any;

    /**
     * @param data
     *  The json data coming from the orchestrator core
     */
    constructor(data: any) {
        this.data = data;
    }

    /**
     * @param mechanismTypeConstant
     *  The ID of the mechanism to get config of
     * @returns Mechanism
     *  The mechanism object with all its configuration data if the mechanism was found in the data, otherwise null.
     */
    getMechanismConfig(mechanismTypeConstant:string): Mechanism {
        var filteredArray = this.data.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return Mechanism.initByData(filteredArray[0]);
        } else {
            return null;
        }
    }

    /**
     * @returns any
     *  Context object that contains all the data to configure the feedback mechanism in the view.
     */
    getContextForView() {
        var context = {textMechanism: null, ratingMechanism: null};
        var textMechanism = this.getMechanismConfig(textType);
        var ratingMechanism = this.getMechanismConfig(ratingType);

        if(textMechanism) {
            context.textMechanism = {
                active: textMechanism.active,
                hint: textMechanism.getParameter('hint').value,
                currentLength: 0,
                maxLength: textMechanism.getParameter('maxLength').value
            }
        }
        if(ratingMechanism) {
            context.ratingMechanism =  {
                active: ratingMechanism.active,
                title: ratingMechanism.getParameter('title').value
            }
        }
        return context;
    }
}
