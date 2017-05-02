package ch.uzh.ifi.feedback.repository;


import eu.supersede.integration.api.mdm.proxies.MetadataManagementProxy;
import eu.supersede.integration.api.mdm.types.KafkaTopic;
import eu.supersede.integration.api.mdm.types.Release;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.List;


// TODO ATTENTION: cannot handle empty arrays in json []
public class MdmIntegratorRepositoryTest {
    String testDataJson = "{\"title\":\"Feedback title at the current time of 1481121292636 milliseconds\",\"id\":\"144\",\"applicationId\":\"9\",\"userIdentification\":\"e39c01a9050e7fa\",\"createdAt\":\"2016-12-07 01:31:30.155\",\"configurationId\":\"20\",\"language\":\"en\",\"textFeedbacks\":[{\"id\":\"137\",\"text\":\"Demo pre-test\",\"mechanismId\":76}],\"ratingFeedbacks\":[{\"id\":\"81\",\"rating\":\"5\",\"mechanismId\":\"79\"}],\"screenshotFeedbacks\":[{\"mechanismId\":\"78\",\"textAnnotations\":[{\"id\":\"9\",\"referenceNumber\":\"1\",\"text\":\"Demo pre-test text annotation\"}],\"id\":\"20\",\"path\":\"screenshots/384861_1481121090136.png\",\"size\":\"384861\",\"name\":\"2annotatedImageWithStickers\",\"fileExtension\":\"png\"}],\"attachmentFeedbacks\":null,\"audioFeedbacks\":[{\"duration\":\"5760\",\"mechanismId\":\"77\",\"id\":\"13\",\"path\":\"audios/12060_1481121090145.m4a\",\"size\":\"12060\",\"name\":\"77audioFile\",\"fileExtension\":\"m4a\"}],\"categoryFeedbacks\":[{\"id\":\"86\",\"parameterId\":\"601\"},{\"id\":\"87\",\"parameterId\":\"603\",\"text\":\"General Feedback\"},{\"id\":\"88\",\"parameterId\":\"602\"}]}";

    @Test
    @Ignore
    /**
     * Corresponding curl:
     *
     * curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 2f1123ba-418a-f614-7659-6b59fd6d49d8" -d '{"event":"FeedbackGatheringRepository", "schemaVersion": "Sample_Version_1", "jsonInstances": "{\"title\":\"Feedback title at the current time of 1481121292636 milliseconds\",\"id\":\"144\",\"applicationId\":\"9\",\"userIdentification\":\"e39c01a9050e7fa\",\"createdAt\":\"2016-12-07 01:31:30.155\",\"configurationId\":\"20\",\"language\":\"en\",\"textFeedbacks\":[{\"id\":\"137\",\"text\":\"Demo pre-test\",\"mechanismId\":76}],\"ratingFeedbacks\":[{\"id\":\"81\",\"rating\":\"5\",\"mechanismId\":\"79\"}],\"screenshotFeedbacks\":[{\"mechanismId\":\"78\",\"textAnnotations\":[{\"id\":\"9\",\"referenceNumber\":\"1\",\"text\":\"Demo pre-test text annotation\"}],\"id\":\"20\",\"path\":\"screenshots/384861_1481121090136.png\",\"size\":\"384861\",\"name\":\"2annotatedImageWithStickers\",\"fileExtension\":\"png\"}],\"attachmentFeedbacks\":[],\"audioFeedbacks\":[{\"duration\":\"5760\",\"mechanismId\":\"77\",\"id\":\"13\",\"path\":\"audios/12060_1481121090145.m4a\",\"size\":\"12060\",\"name\":\"77audioFile\",\"fileExtension\":\"m4a\"}],\"categoryFeedbacks\":[{\"id\":\"86\",\"parameterId\":\"601\"},{\"id\":\"87\",\"parameterId\":\"603\",\"text\":\"General Feedback\"},{\"id\":\"88\",\"parameterId\":\"602\"}]}}"}' "http://supersede.es.atos.net:3001/release"
     */
    public void testRegisterRelease() throws Exception {
        MetadataManagementProxy proxy = new MetadataManagementProxy<Object, Object>();

        Release release = new Release();
        release.setEvent("FeedbackGatheringRepository");
        release.setSchemaVersion("v1");
        //String jsonInstances = "{\\\"SocialNetworksMonitoredData\\\":{\\\"idOutput\\\":\\\"12345\\\",\\\"confId\\\":\\\"67890\\\",\\\"searchTimeStamp\\\":\\\"2016-07-19 17:23:00.000\\\",\\\"numDataItems\\\":1,\\\"DataItems\\\":[{\\\"idItem\\\":\\\"6253282\\\",\\\"timeStamp\\\":\\\"2016-05-25 20:03\\\",\\\"message\\\":\\\"Game on. Big ten network in 10 mins. Hoop for water. Flint we got ya back\\\",\\\"author\\\":\\\"@SnoopDogg\\\",\\\"link\\\":\\\"https:\\/\\/twitter.com\\/SnoopDogg\\/status\\/734894106967703552\\\"}]}}";
        //String jsonInstances = "{\"SocialNetworksMonitoredData\":{\"idOutput\":\"12345\",\"confId\":\"67890\",\"searchTimeStamp\":\"2016-07-19 17:23:00.000\",\"numDataItems\":1,\"DataItems\":[{\"idItem\":\"6253282\",\"timeStamp\":\"2016-05-25 20:03\",\"message\":\"Game on. Big ten network in 10 mins. Hoop for water. Flint we got ya back\",\"author\":\"@SnoopDogg\",\"link\":\"https://twitter.com/SnoopDogg/status/734894106967703552\"}]}}";

        release.setJsonInstances(testDataJson);
        KafkaTopic result = proxy.registerRelease(release);


        System.out.println(result);
        System.out.println(result.getTopic());

        Assert.notNull(result);
    }

    @Test
    @Ignore
    public void testGelAllReleases() throws Exception {
        MetadataManagementProxy<?, ?> proxy = new MetadataManagementProxy<Object, Object>();

        List<Release> result = proxy.getAllReleases();
        Assert.notNull(result);
        Assert.notEmpty(result);
    }
}
