import {HttpBackend} from './http_backend';


describe('Http Backend', () => {
    let httpBackend:HttpBackend;

    beforeEach(() => {
        httpBackend = new HttpBackend('configurations');
    });

    it('should return the correct url for the given path', () => {
        var expectedPath = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configurations';
        expect(httpBackend.getUrl()).toEqual(expectedPath);
    });
});