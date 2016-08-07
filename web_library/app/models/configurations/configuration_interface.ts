import {Mechanism} from '../mechanisms/mechanism';
import {GeneralConfiguration} from './general_configuration';


export interface ConfigurationInterface {
    id:number;
    mechanisms:Mechanism[];
    type:string;
    generalConfiguration:GeneralConfiguration;
    dialogId?:string;

    getMechanismConfig(mechanismTypeConstant:string): any;
}
