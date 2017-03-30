package monitoring.model;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

public class Utils {

	
	public static String streamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(stream)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    sb.append(responseBody);
		}
		return sb.toString();
	}
	
	public static long getDateTimeInMillis(JSONObject obj) {
		return obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getInt("nanos")/1000000 + 
		obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getLong("seconds")*1000;
	}
	
}
