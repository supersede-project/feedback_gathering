package monitoring.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;
import org.junit.Test;

public class AppTweakTest {

	private final String token = "9IUCBJJb1GofSJJzHEg7gA4Hok0";
	
	private final String uri = "https://api.apptweak.com/android/applications/";
	private final String packageName = "com.shazam.android";
	
	
	@Test
	public void test() throws MalformedURLException, IOException {

		URLConnection connection = new URL(uri + packageName + "/reviews.json?country=es&language=es")
				.openConnection();
		connection.setRequestProperty("X-Apptweak-Key", token);
		connection.getInputStream();
		
		JSONObject data = new JSONObject(streamToString(connection.getInputStream()));
		System.out.println(data.toString());
		
	}
	
	private String streamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(stream)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    sb.append(responseBody);
		}
		return sb.toString();
	}
}
