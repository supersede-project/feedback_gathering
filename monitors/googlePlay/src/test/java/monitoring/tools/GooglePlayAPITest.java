package monitoring.tools;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

public class GooglePlayAPITest {

	Client client = ClientBuilder.newBuilder()
            .register(JacksonJaxbJsonProvider.class)
            .build();
	
	WebTarget target = client
            .target("http://localhost:8080/googlePlay/service/configuration");
		
}
