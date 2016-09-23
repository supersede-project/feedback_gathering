import {HttpBackend} from './http_backend';


describe('Http Backend', () => {
    let configurationHttpBackend:HttpBackend;
    let feebackHttpBackend:HttpBackend;

    beforeEach(() => {
        configurationHttpBackend = new HttpBackend('feedback_orchestrator/{lang}/example/configuration', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/', 'en');
        feebackHttpBackend = new HttpBackend('{lang}/feedbacks', 'http://ec2-54-175-37-30.compute-1.amazonaws.com/', 'de');
    });

    it('should return the correct url for the given path', () => {
        var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/en/example/configuration';
        expect(configurationHttpBackend.getUrl()).toEqual(expectedPath);

        var expectedFeedbackPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/de/feedbacks';
        expect(feebackHttpBackend.getUrl()).toEqual(expectedFeedbackPath);
    });
});