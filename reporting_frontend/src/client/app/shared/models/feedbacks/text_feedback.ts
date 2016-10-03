import {TextMechanism} from '../mechanisms/text_mechanism';


export class TextFeedback {
    text:string;
    mechanismId:number;
    mechanism:TextMechanism;

    constructor(text:string, mechanismId:number) {
        this.text = text;
        this.mechanismId = mechanismId;
    }
}

