package monitoring.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.springframework.util.StopWatch;

import monitoring.model.HttpMonitoringData;

public class Test {
	
	 public static void main(String args[]) {

			List<HttpMonitoringData> data = new ArrayList<>();
			
			StopWatch watch = new StopWatch();
	        HttpClient client = new HttpClient();
	        HttpMethod method = new HeadMethod("http://lab-supersede.atos-sports.tv:8000/handshake_test.php");

	        try {
	            watch.start();
	            client.executeMethod(method);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            watch.stop();
	        }

	        data.add(new HttpMonitoringData(String.valueOf(watch.getTotalTimeMillis()), String.valueOf(method.getStatusCode())));
			System.out.println("Sent data: " + watch.getTotalTimeMillis() + "/" + method.getStatusCode());
	 }

}
