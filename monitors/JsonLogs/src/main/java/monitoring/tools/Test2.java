package monitoring.tools;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String filename = "C:/Users/Panagiotis/Downloads/example.json";
		String kafkaEndPoint = "localhost:9092";
		String kafkaTopic = "JSON_files";
		
		SendJSONs obj = new SendJSONs();
		try{
			obj.generateData(filename, kafkaEndPoint, kafkaTopic);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
