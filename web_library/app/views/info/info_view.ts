import {MechanismView} from '../mechanism_view';
import {InfoMechanism} from '../../models/mechanisms/info_mechanism';


export class InfoView implements MechanismView {

    constructor(private infoMechanism:InfoMechanism, private dialogId:string) {
        this.reset();
    }

    getSelector():string {
        return "#" + this.dialogId + " #infoMechanism" + this.infoMechanism.id + " .info-input";
    }

    reset() {
        jQuery('' + this.getSelector() + ' p').empty()

    }

    getFeedback():any {
        return null;
    }
}