export class AudioFeedback {
    name:string;
    part:string;
    duration:number;
    fileExtension:string;
    mechanismId:number;

    constructor(part:string, duration:number, fileExtension:string, mechanismId:number) {
        this.part = part;
        this.duration = duration;
        this.fileExtension = fileExtension;
        this.mechanismId = mechanismId;
        this.name = part;
    }
}