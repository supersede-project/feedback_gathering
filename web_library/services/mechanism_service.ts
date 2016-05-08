import {Mechanism} from '../models/mechanism';


export class MechanismService {

    findAll(): Mechanism[] {
        var mechanisms: Array<Mechanism> = [
            new Mechanism("TEXT_MECHANISM", true),
            new Mechanism("SCREENSHOT_MECHANISM", true)
        ];
        return mechanisms;
    }
}