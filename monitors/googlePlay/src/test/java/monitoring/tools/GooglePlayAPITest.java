package monitoring.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONObject;
import org.junit.Test;

public class GooglePlayAPITest {

	//OAuth credentials for API authentication
	private final String 	clientID = "785756325088-kes5s8om6n4o6sntvokqjt47tito7mla.apps.googleusercontent.com";
	private final String 	clientSecret = "hr5Fcj-Awp8XejMWbxBBI6ps";
	private final String 	refreshToken = "1/MKGHOAO4tTvO2kGECyG_EDUroJ07v55aIV_3EBbvKGA";
	
	//API Uri
	private final String 	apiUri = "https://www.googleapis.com/androidpublisher/v2/applications/";
	private final String	tokenUri = "https://accounts.google.com/o/oauth2/token";
	
	//Access token for API authentication
	private String 		 	accessToken = "ya29.Ci8WA5RBcItol_FE18VAHwHuueraVng6wdVi8kma8mDSRpQZtbX2XDM7cHbuMT0rnA";
		
	@Test
	public void test() throws MalformedURLException, IOException {
		
		String query = "grant_type=refresh_token"
				+ "&client_id=" + clientID 
				+ "&client_secret=" + clientSecret 
				+ "&refresh_token=" + refreshToken;
		
		URLConnection httpConnection = new URL(tokenUri 
				+ "?grant_type=refresh_token"
				+ "&client_id=" + clientID 
				+ "&client_secret=" + clientSecret 
				+ "&refresh_token=" + refreshToken)
				.openConnection();
		httpConnection.setDoOutput(true);
		//httpConnection.setFixedLengthStreamingMode(0);
		
		try (OutputStream output = httpConnection.getOutputStream()) {
		    output.write(query.getBytes("UTF-8"));
		}
		
		JSONObject res = new JSONObject(streamToString(httpConnection.getInputStream()));
		System.out.println(res.toString());
		
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
