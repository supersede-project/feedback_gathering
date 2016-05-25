import {MechanismService} from './mechanism_service';


describe('Mechanism Service', () => {
    let mechanismService:MechanismService;

    beforeEach(() => {
        mechanismService = new MechanismService();
    });

    it('should return mechanisms', () => {
        var mechanisms = mechanismService.findAll();
        expect(mechanisms).toBeDefined();
    });
});

