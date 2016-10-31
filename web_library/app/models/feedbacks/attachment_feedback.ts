export class AttachmentFeedback {
    part:string;
    name:string;
    fileExtension:string;
    mechanismId:number;

    constructor(part:string, name:string, fileExtension:string, mechanismId:number) {
        this.part = part;
        this.name = name;
        this.fileExtension = this.getFileExtensionFromContentType(fileExtension);
        this.mechanismId = mechanismId;
    }

    getFileExtensionFromContentType(fileContentType:string):string {
        if(!fileContentType) {
            return "";
        } else if(fileContentType === 'application/pdf') {
            return "pdf";
        } else if(fileContentType === 'application/msword') {
            return "doc";
        } else if(fileContentType === 'image/jpeg') {
            return "jpg";
        } else if(fileContentType === 'image/png') {
            return "png";
        } else if(fileContentType === 'text/plain') {
            return "txt";
        } else {
            if(fileContentType.split('/').length > 1) {
                return fileContentType.split('/')[1];
            } else {
                return "";
            }
        }
    }
}