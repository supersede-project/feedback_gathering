import {ContextInformation} from './context_information';


describe('Context information', () => {

    fit('should contain all relevant information when created', () => {
        var contextInformation = ContextInformation.create();
        console.log(contextInformation);
        expect(contextInformation.devicePixelRatio).not.toBeNull();
    });
});

