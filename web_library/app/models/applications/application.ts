import {GeneralConfiguration} from '../configurations/general_configuration';
import {ConfigurationInterface} from '../configurations/configuration_interface';
import {PullConfiguration} from '../configurations/pull_configuration';
import {PushConfiguration} from '../configurations/push_configuration';
import {ConfigurationFactory} from '../configurations/configuration_factory';
import {configurationTypes} from '../../js/config';


export class Application {
    id:number;
    name:string;
    state:number;
    generalConfiguration:GeneralConfiguration;
    configurations:ConfigurationInterface[];

    constructor(id:number, name:string, state:number, generalConfiguration:GeneralConfiguration, configurations:ConfigurationInterface[]) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
    }

    static initByData(data:any) {
        var generalConfiguration = GeneralConfiguration.initByData(data.generalConfiguration);
        var configurations = [];
        for(var configuration of data.configurations) {
            configurations.push(ConfigurationFactory.createByData(configuration));
        }
        return new Application(data.id, data.name, data.state, generalConfiguration, configurations);
    }

    getPushConfiguration(): PushConfiguration {
        return <PushConfiguration>this.configurations.filter(configuration => configuration.type === configurationTypes.push)[0];
    }

    getPullConfigurations(): PullConfiguration[] {
        return <PullConfiguration[]>this.configurations.filter(configuration => configuration.type === configurationTypes.pull);
    }
}