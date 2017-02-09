package ch.uzh.ifi.feedback.repository.integration;


import ch.uzh.ifi.feedback.library.rest.integration.MdmIntegrator;
import eu.supersede.integration.api.mdm.types.KafkaTopic;

public class MdmIntegratorRepository {
    private MdmIntegrator mdmIntegrator;

    public MdmIntegratorRepository() {
        this.mdmIntegrator = new MdmIntegrator();
    }

    public KafkaTopic getTopic(String eventName, String schemaVersion, String json) {
        try {
            return mdmIntegrator.getTopic(eventName, schemaVersion, json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
