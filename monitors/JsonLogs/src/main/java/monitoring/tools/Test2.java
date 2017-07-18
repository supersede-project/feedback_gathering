package monitoring.tools;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String filename = "C:/Users/Panagiotis/Downloads/example.json";
		String kafkaEndPoint = "localhost:9092";
		String kafkaTopic = "JSON_files";

		SendJSONs obj = new SendJSONs();
		String json = null;

		long start = System.currentTimeMillis();

		try {

			Scanner scanner = new Scanner(new File(filename));
			scanner.useDelimiter(Pattern.compile("}"));

			while (scanner.hasNext()) {
				json = scanner.next().trim();
				if (!json.equals("")) {
					json += "\n}";
				} else {
					continue;
				}
				
				obj.generateData(json, kafkaTopic);	
				

			}
			scanner.close();
			
			double elapsedTime = (System.currentTimeMillis() - start) / 1000.0;
			System.out.printf("Data sent successfully! Elapsed Time: %.2fsec", elapsedTime);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
