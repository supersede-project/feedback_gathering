import {ContextInformation} from './context_information';


describe('Context information', () => {

    it('should contain all relevant information when created', () => {
        let context = {metaData: {"someData": 1}};
        var contextInformation = ContextInformation.create(context);
        expect(contextInformation.devicePixelRatio).not.toBeNull();
    });
});

