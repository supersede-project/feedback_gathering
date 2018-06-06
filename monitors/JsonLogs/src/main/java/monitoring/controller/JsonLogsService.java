package monitoring.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import monitoring.tools.SendJSONs;

@RestController
@RequestMapping(value = "/configuration")
public class JsonLogsService {
	
	@RequestMapping(method = RequestMethod.POST)
	public void sendJsonLog(@RequestBody String jsonLog, @RequestParam String kafkaTopic) throws Exception {
				
		SendJSONs tool = new SendJSONs();
		tool.generateData(jsonLog, kafkaTopic);
		
	}
	
}
