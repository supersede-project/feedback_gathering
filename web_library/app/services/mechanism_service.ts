import {Mechanism} from '../models/mechanism';


export class MechanismService {
    private data:any;

    constructor(data: any) {
        this.data = data;
    }

    getMechanismConfig(mechanismTypeConstant:string): Mechanism {
        var filteredArray = this.data.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return Mechanism.initByData(filteredArray[0]);
        } else {
            return null;
        }
    }
}
