package ch.uzh.ifi.feedback.repository;


import ch.uzh.ifi.feedback.repository.integration.DataProviderIntegratorRepository;
import org.json.JSONObject;
import org.junit.Test;


public class DataProviderIntegratorRepositoryTest {
    String topic = "c9f1b657-d88c-41aa-be00-d7b563bc22fe";
    String jsonData = "{\"title\":\"Feedback title at the current time of 1481121292636 milliseconds\",\"id\":\"144\",\"applicationId\":\"9\",\"userIdentification\":\"e39c01a9050e7fa\",\"createdAt\":\"2016-12-07 01:31:30.155\",\"configurationId\":\"20\",\"language\":\"en\",\"textFeedbacks\":[{\"id\":\"137\",\"text\":\"Demo pre-test\",\"mechanismId\":76}],\"ratingFeedbacks\":[{\"id\":\"81\",\"rating\":\"5\",\"mechanismId\":\"79\"}],\"screenshotFeedbacks\":[{\"mechanismId\":\"78\",\"textAnnotations\":[{\"id\":\"9\",\"referenceNumber\":\"1\",\"text\":\"Demo pre-test text annotation\"}],\"id\":\"20\",\"path\":\"screenshots/384861_1481121090136.png\",\"size\":\"384861\",\"name\":\"2annotatedImageWithStickers\",\"fileExtension\":\"png\"}],\"attachmentFeedbacks\":null,\"audioFeedbacks\":[{\"duration\":\"5760\",\"mechanismId\":\"77\",\"id\":\"13\",\"path\":\"audios/12060_1481121090145.m4a\",\"size\":\"12060\",\"name\":\"77audioFile\",\"fileExtension\":\"m4a\"}],\"categoryFeedbacks\":[{\"id\":\"86\",\"parameterId\":\"601\"},{\"id\":\"87\",\"parameterId\":\"603\",\"text\":\"General Feedback\"},{\"id\":\"88\",\"parameterId\":\"602\"}]}";

    @Test
    public void testIngestJsonData() throws Exception {
        DataProviderIntegratorRepository dataProviderIntegratorRepository = new DataProviderIntegratorRepository();
        JSONObject jsonObject = new JSONObject(jsonData);

        dataProviderIntegratorRepository.ingestJsonData(jsonObject, topic);
    }
}
