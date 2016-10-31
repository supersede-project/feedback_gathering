import {ContextInformation} from './context_information';


describe('Context information', () => {

    it('should contain all relevant information when created', () => {
        var contextInformation = ContextInformation.create();
        expect(contextInformation.devicePixelRatio).not.toBeNull();
    });
});

