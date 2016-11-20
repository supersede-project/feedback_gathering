import {AttachmentMechanism} from '../mechanisms/attachment_mechanism';


export class AttachmentFeedback {
  part:string;
  name:string;
  fileExtension:string;
  path:string;
  mechanismId:number;
  mechanism:AttachmentMechanism;
  downloadLink:string;

  constructor(part:string, name:string, fileExtension:string, mechanismId:number, mechanism:AttachmentMechanism) {
    this.part = part;
    this.name = name;
    this.fileExtension = fileExtension;
    this.mechanismId = mechanismId;
    this.mechanism = mechanism;
  }
}

