package ch.uzh.ifi.feedback.library.rest.integration;

import eu.supersede.integration.api.mdm.proxies.MetadataManagementProxy;
import eu.supersede.integration.api.mdm.types.KafkaTopic;
import eu.supersede.integration.api.mdm.types.Release;

public class MdmIntegrator {
    private MetadataManagementProxy<?, ?> proxy;

    public MdmIntegrator() {
        proxy = new MetadataManagementProxy<Object, Object>();
    }

    public KafkaTopic getTopic(String eventName, String schemaVersion, String json) throws Exception {
        Release release = new Release();
        release.setEvent(eventName);
        release.setSchemaVersion(schemaVersion);

        release.setJsonInstances(json);
        return proxy.registerRelease(release );
    }
}
