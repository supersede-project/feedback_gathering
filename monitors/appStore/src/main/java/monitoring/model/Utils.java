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

}
