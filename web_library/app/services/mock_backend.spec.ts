import {MockBackend} from './mock_backend';
import {readJSON} from './mocks/mocks_loader';


describe('Mock Backend', () => {
    let configurationMockBackend:MockBackend;
    let feedbackMockBackend:MockBackend;

    beforeEach(() => {
        var configurationMockData = readJSON('app/services/mocks/configurations_mock.json');
        configurationMockBackend = new MockBackend(configurationMockData);

        var feedbackMockData = readJSON('app/services/mocks/feedbacks_mock.json');
        feedbackMockBackend = new MockBackend(feedbackMockData);
    });

    it('should list all the mock data', () => {
        var data = configurationMockBackend.list();
        expect(data.length).toBe(2);
    });

    it('should retrieve a single object in the mock data', () => {
        var expectedObject = {
            "id": 2,
            "general_configurations": [
                {
                    "id": 1,
                    "key": "review",
                    "value": true
                }
            ],
            "pull_configurations": [],
            "mechanisms": []
        };

        expect(configurationMockBackend.retrieve(2)).toEqual(expectedObject);
    });

    it('should create a new object in the mock data', () => {
        var data = feedbackMockBackend.list();
        expect(data.length).toBe(1);

        var newFeedback = {
            "title": "Feedback 2",
            "configVersion": null,
            "text": "This is the feedback text",
            "user_identification": "u8102390",
            "language": "DE",
            "context_information_id": null
        };

        feedbackMockBackend.create(newFeedback);

        var data = feedbackMockBackend.list();
        expect(data.length).toBe(2);
    });

    it('should update an object in the mock data', () => {
        var attributes = {
            "title": "New title for this feedback object"
        };

        feedbackMockBackend.update(1, attributes);

        var updatedFeedback = feedbackMockBackend.retrieve(1);
        expect(updatedFeedback.title).toEqual('New title for this feedback object');
    });

    it('should delete an object in the mock data', () => {
        var data = feedbackMockBackend.list();
        expect(data.length).toBe(1);

        feedbackMockBackend.destroy(1);

        var data = feedbackMockBackend.list();
        expect(data.length).toBe(0);
    });
});