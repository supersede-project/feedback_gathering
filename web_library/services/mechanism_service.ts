import {Mechanism} from '../models/mechanism';


export class MechanismService {

    findAll(): Mechanism[] {
        var mechanisms: Array<Mechanism> = [
            new Mechanism("TEXT_MECHANISM", []),
            new Mechanism("SCREENSHOT_MECHANISM", [])
        ];
        return mechanisms;
    }
}