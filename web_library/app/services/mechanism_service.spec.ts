import {expect, it, beforeEach, describe} from "jasmine";
import {MechanismService} from './mechanism_service';


describe("Mechanism Service", () => {
    let mechanismService:MechanismService;

    beforeEach(() => {
        mechanismService = new MechanismService();
    });

    it("should return mechanism", () => {
        var mechanisms = mechanismService.findAll();
        expect(mechanisms).toBeDefined();
    });
});


