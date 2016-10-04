export class ScreenshotFeedback {
    name:string;
    mechanismId:number;
    part:string;

    constructor(name:string, mechanismId:number, part:string) {
        this.name = name;
        this.mechanismId = mechanismId;
        this.part = part;
    }
}

