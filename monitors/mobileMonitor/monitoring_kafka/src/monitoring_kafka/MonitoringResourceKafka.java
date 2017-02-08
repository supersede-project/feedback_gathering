package monitoring_kafka;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import scala.Console;

@Path("/monitor")
public class MonitoringResourceKafka {
	private static int idConfig = 0;
	
	@Path("/kafka")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject newKafkaMessage(JSONObject mobile){
		JSONObject resp = new JSONObject();
		sendKafkaMessage(mobile);
		idConfig++;
		resp.put("status", "success");
		return resp;
	}
	
	private void sendKafkaMessage(JSONObject data){
		//From the JSON coming from the Android, we have to parse the variables we need.
		String j = data.get("nameValuePairs").toString();
		ArrayList<String> aSnd = new ArrayList<String>();
		String metrics = "";
		String[] jA = j.split(", ");
		jA[2] += ", "+jA[3];
		for (int i = 0; i < jA.length; i++){
			if(jA[i].contains("=") && i<5){
				aSnd.add(getJSONStringValue(jA[i]));
			}
			else if(jA[i].contains("="))
				metrics += getJSONStringValue(jA[i]+", ");
		}
		aSnd.add(metrics);
		
		try {
			Runtime.getRuntime().exec(new String[]{"java", "-jar", "C:\\Users\\Rubén\\Documents\\GitHub\\monitoring_kafka\\KafkaProducer.jar", aSnd.get(0), aSnd.get(1), aSnd.get(2), aSnd.get(3), aSnd.get(4)});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private String getJSONStringValue (String s){
		String[] fSplit = s.split("=");
		switch(fSplit[0])
		{
			case "{endpoint":
			case "topic":
			case "timeStamp":
			case "numDataItems":
				return fSplit[1];
			case "metrics":
				return fSplit[3].substring(1, fSplit[3].length())+":"+fSplit[4];
			case "{nameValuePairs":
				return ",,"+fSplit[1].substring(1, fSplit[1].length())+":"+fSplit[2];
			default:
				String sDef = fSplit[0]+":"+fSplit[1];
				if(sDef.contains("}"))
					sDef = sDef.split("}")[0];
				return sDef;
		}
	}

	public MonitoringResourceKafka() {
	}
}
