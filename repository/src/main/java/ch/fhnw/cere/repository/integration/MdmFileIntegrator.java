package ch.fhnw.cere.repository.integration;


import eu.supersede.integration.api.mdm.proxies.MetadataManagementProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Forwards files to WP2.
 */
public class MdmFileIntegrator {

    @Value("${mdm.file_endpoint}")
    private String mdmFileEndpoint;

    private MetadataManagementProxy metadataManagementProxy;

    public MdmFileIntegrator() {
        metadataManagementProxy = new MetadataManagementProxy();
    }

    @Async
    public void sendFile(File file) throws Exception {
        Path path = Paths.get(file.getAbsolutePath());
        metadataManagementProxy.sendFile(path);
    }
}
