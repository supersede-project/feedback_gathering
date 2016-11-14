import {AttachmentMechanism} from '../../models/mechanisms/attachment_mechanism';
import {feedbackApp} from '../../js/jquery.feedback';


export class AttachmentView {

    constructor(private attachmentMechanism:AttachmentMechanism, private dialogId:string) {
        if (attachmentMechanism.active) {
            var dropArea = jQuery('' + this.getSelector()).find('.drop-area');
            dropArea.fileUpload(feedbackApp.options.distPath);
        }
    }

    getSelector():string {
        return "#" + this.dialogId + " #attachmentMechanism" + this.attachmentMechanism.id;
    }
}