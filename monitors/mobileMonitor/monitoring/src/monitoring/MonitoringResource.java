package monitoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;


@Path("/monitor")
public class MonitoringResource {
	
	private static List<String> androidTargets = new ArrayList<String>();
	private static int idConfig = 0;
	
	@Path("/config")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject newOrder(JSONObject mobile){
		JSONObject resp = new JSONObject();
		try {
			sendMessage(mobile);
			resp.put("status", "succes");
			resp.put("idConf", idConfig);
			idConfig++;
			resp.put("errorMessage", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resp.put("status","failure");
			resp.put("idConf", "");
			resp.put("errorMessage",e.getMessage());
			e.printStackTrace();
		}
		
		return resp;
	}
	
	@Path("/registerid")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject registerID(JSONObject id){
		
		JSONObject resp = new JSONObject();
		String idaux = id.get("nameValuePairs").toString();
		if((idaux.length()>6)){
			idaux=idaux.substring(7,idaux.length()-1);
			if(!androidTargets.contains(idaux)){
				androidTargets.add(idaux);
			}
			resp.put("status", "registered");
		}
		else{
			 resp.put("status", "No correct format");
		}
		System.out.println(resp);
		return resp;
	}
	
	private void sendMessage(JSONObject jsonObject) throws IOException{
		
		final String GCM_API_KEY = "AIzaSyAjRw9ejuVzZuOMdMLGhu0mJzFx49XJg7c"; //MonitoringApp Browser API Key    
	    final String MESSAGE_KEY = "message";
		Sender sender = new Sender(GCM_API_KEY);

        Message message = new Message.Builder().timeToLive(30)
                .delayWhileIdle(true).addData(MESSAGE_KEY, jsonObject.toJSONString()).build();
        
        MulticastResult result = sender.send(message, androidTargets, 1);
        sender.send(message, androidTargets, 1);
	}	
	public MonitoringResource(){
	}
	

}

