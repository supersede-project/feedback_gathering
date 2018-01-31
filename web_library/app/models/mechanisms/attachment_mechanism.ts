import {Mechanism} from './mechanism';
import {Parameter} from '../parameters/parameter';


export class AttachmentMechanism extends Mechanism {
    dropArea:any;

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        return {
            title: this.getParameterValue('title'),
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            maximumTotalFileSize: this.getParameterValue('maximumTotalFileSize') || 0,
            maximumTotalFileSizeDisplay: this.formatBytes(this.getParameterValue('maximumTotalFileSize') || 0),
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px'
        }
    }

    formatBytes(bytes, decimals?):string {
        if (0 == bytes) return "0 Bytes";
        let c = 1024, d = decimals || 2, e = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
            f = Math.floor(Math.log(bytes) / Math.log(c));
        return parseFloat((bytes / Math.pow(c, f)).toFixed(d)) + " " + e[f]
    };
}
