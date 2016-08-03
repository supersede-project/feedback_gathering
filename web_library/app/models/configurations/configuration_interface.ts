import {Mechanism} from '../mechanisms/mechanism';


export interface ConfigurationInterface {
    id:number;
    mechanisms:Mechanism[];
    dialogId?:string;

    getMechanismConfig(mechanismTypeConstant:string): any;
}
