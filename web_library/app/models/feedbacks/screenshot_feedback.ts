export class ScreenshotFeedback {
    name:string;
    mechanismId:number;
    part:string;
    fileExtension:string;

    constructor(name:string, mechanismId:number, part:string, fileExtension:string) {
        this.name = name;
        this.mechanismId = mechanismId;
        this.part = part;
        this.fileExtension = fileExtension;
    }
}

