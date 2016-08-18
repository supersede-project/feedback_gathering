import {HttpBackend} from './http_backend';


describe('Http Backend', () => {
    let configurationHttpBackend:HttpBackend;
    let feebackHttpBackend:HttpBackend;

    beforeEach(() => {
        configurationHttpBackend = new HttpBackend('feedback_orchestrator/example/configuration');
        feebackHttpBackend = new HttpBackend('feedbacks');
    });

    it('should return the correct url for the given path', () => {
        var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration';
        expect(configurationHttpBackend.getUrl()).toEqual(expectedPath);
    });

    it('should list all the mock data', () => {
        configurationHttpBackend.list(function(responseData) {
            expect(responseData.length).toBe(4);
        });
    });
});