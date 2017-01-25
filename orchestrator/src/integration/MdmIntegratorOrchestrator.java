package integration;


import ch.uzh.ifi.feedback.library.rest.integration.MdmIntegrator;
import eu.supersede.integration.api.mdm.types.KafkaTopic;

public class MdmIntegratorOrchestrator {
    private MdmIntegrator mdmIntegrator;

    public MdmIntegratorOrchestrator() {
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
