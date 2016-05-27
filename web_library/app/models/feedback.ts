import {Rating} from './ratings';


export class Feedback {
    title:string;
    application:string;
    user:string;
    text:string;
    configVersion:number;
    ratings:Rating[];

    constructor(title?:string, application?:string, user?:string, text?:string, configVersion?:number, ratings?:Rating[]) {
        this.title = title;
        this.application = application;
        this.user = user;
        this.text = text;
        this.configVersion = configVersion;
        this.ratings = ratings;
    }
}

