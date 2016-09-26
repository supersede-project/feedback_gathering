import {ParameterInterface} from '../parameters/parameter_interface';
import {Parameterizable} from '../mixins/parameterizable';
import {Parameter} from '../parameters/parameter';
import {applyMixins} from '../mixins/mixin_helper';


export class GeneralConfiguration implements Parameterizable {
    id:number;
    name:string;
    parameters:ParameterInterface[];

    constructor(id:number, name:string, parameters:ParameterInterface[]) {
        this.id = id;
        this.name = name;
        this.parameters = parameters;
    }

    static initByData(data:any): GeneralConfiguration {
        if(!data) {
            return null;
        }
        var parameters = [];
        for(var parameter of data.parameters) {
            parameters.push(Parameter.initByData(parameter));
        }
        return new GeneralConfiguration(data.id, data.name, parameters);
    }

    getParameter: (key:string) => ParameterInterface;
    getParameterValue: (key:string) => any;
}

applyMixins(GeneralConfiguration, [Parameterizable]);
