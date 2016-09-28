export class AttachmentFeedback {
    part:string;
    name:string;
    extension:string;
    mechanismId:number;

    constructor(part:string, name:string, extension:string, mechanismId:number) {
        this.part = part;
        this.name = name;
        this.extension = extension;
        this.mechanismId = mechanismId;
    }
}