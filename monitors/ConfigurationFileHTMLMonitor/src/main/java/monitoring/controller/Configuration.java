package monitoring.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import configurationHandler.Handler;;

@Path("configuration")
public class Configuration {
	
	private Handler handler = new Handler("UserEventsConfProfResults");
	
	/*
	@GET
	@Produces("text/html")
	public String HelloWorld()
	{
		return "Hello World!";
	}
	*/

	@POST
	public String addConfiguration(String jsonConf) {
		return handler.addConfiguration(jsonConf);
	}

}

