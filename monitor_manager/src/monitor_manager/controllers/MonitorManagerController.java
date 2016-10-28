package monitor_manager.controllers;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Singleton
public class MonitorManagerController {
	
	private String monitorHost = "localhost:8080/";

	@POST
	@Path("addConfiguration")
	public void addConfiguration(String json) {
		
	}
	
	@POST
	@Path("updateConfiguration")
	public void updateConfiguration(String json) {
		
	}
	
	@POST
	@Path("deleteConfiguration")
	public void deleteConfiguration(String json) {
		
	}
	
}
