package monitoring.controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import configurationHandler.Handler;;

@Path("configuration")
public class Configuration {
	
	private Handler handler = new Handler("UserEventsConfProfResults");
	
	@POST
	public String addConfiguration(String jsonConf) {
		return handler.addConfiguration(jsonConf);
	}

}

