import {AudioMechanism} from '../mechanisms/audio_mechanism';


export class AudioFeedback {
  name:string;
  part:string;
  duration:number;
  fileExtension:string;
  mechanismId:number;
  mechanism:AudioMechanism;

  constructor(part:string, duration:number, fileExtension:string, mechanismId:number) {
    this.part = part;
    this.duration = duration;
    this.fileExtension = fileExtension;
    this.mechanismId = mechanismId;
    this.name = part;
  }
}
