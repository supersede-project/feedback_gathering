package monitormanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;
import eu.supersede.integration.api.monitoring.monitors.proxies.AppStoreMonitorProxy;
import eu.supersede.integration.api.monitoring.monitors.proxies.GooglePlayMonitorProxy;
import eu.supersede.integration.api.monitoring.monitors.proxies.TwitterMonitorProxy;
import monitormanager.model.ConfigurationParser;

@RequestMapping(value = "/")
@RestController
public class RESTController {
		
	private ConfigurationParser parser = new ConfigurationParser();

	@RequestMapping(value = "/{monitorName}/configuration", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public MonitorSpecificConfiguration addConfiguration(@PathVariable String monitorName, @RequestBody String input) throws Exception {
		JsonObject jsonObj = getJson(input);
		if (monitorName.equals("Twitter")) {
			TwitterMonitorProxy<?,?> proxy = new TwitterMonitorProxy<>();
			TwitterMonitorConfiguration conf = parser.getTwitterConfiguration(jsonObj);
			return proxy.createMonitorConfiguration(conf);
		} else if (monitorName.equals("GooglePlay")) {
			GooglePlayMonitorProxy<?,?> proxy = new GooglePlayMonitorProxy<>();
			GooglePlayMonitorConfiguration conf = parser.getGooglePlayConfiguration(jsonObj);
			return proxy.createMonitorConfiguration(conf);
		} else if (monitorName.equals("AppStore")) {
			AppStoreMonitorProxy<?,?> proxy = new AppStoreMonitorProxy<>();
			AppStoreMonitorConfiguration conf = parser.getAppStoreConfiguration(jsonObj);
			return proxy.createMonitorConfiguration(conf);
		} else throw new Exception("There is no monitor with this name");
	}
	
	@RequestMapping(value = "/{monitorName}/configuration/{confId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public MonitorSpecificConfiguration updateConfiguration(@PathVariable String monitorName,
			@PathVariable int confId, @RequestBody String input) throws Exception {
		JsonObject jsonObj = getJson(input);
		if (monitorName.equals("Twitter")) {
			TwitterMonitorProxy<?,?> proxy = new TwitterMonitorProxy<>();
			TwitterMonitorConfiguration conf = parser.getTwitterConfiguration(jsonObj);
			return proxy.updateMonitorConfiguration(conf);
		} else if (monitorName.equals("GooglePlay")) {
			GooglePlayMonitorProxy<?,?> proxy = new GooglePlayMonitorProxy<>();
			GooglePlayMonitorConfiguration conf = parser.getGooglePlayConfiguration(jsonObj);
			return proxy.updateMonitorConfiguration(conf);
		} else if (monitorName.equals("AppStore")) {
			AppStoreMonitorProxy<?,?> proxy = new AppStoreMonitorProxy<>();
			AppStoreMonitorConfiguration conf = parser.getAppStoreConfiguration(jsonObj);
			return proxy.updateMonitorConfiguration(conf);
		} else throw new Exception("There is no monitor with this name");
	}
	
	@RequestMapping(value = "/{monitorName}/configuration/{confId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteConfiguration(@PathVariable String monitorName,
			@PathVariable int confId) throws Exception {
		if (monitorName.equals("Twitter")) {
			TwitterMonitorProxy<?,?> proxy = new TwitterMonitorProxy<>();
			TwitterMonitorConfiguration conf = new TwitterMonitorConfiguration();
			conf.setId(confId);
			proxy.deleteMonitorConfiguration(conf);
		} else if (monitorName.equals("GooglePlay")) {
			GooglePlayMonitorProxy<?,?> proxy = new GooglePlayMonitorProxy<>();
			GooglePlayMonitorConfiguration conf = new GooglePlayMonitorConfiguration();
			conf.setId(confId);
			proxy.deleteMonitorConfiguration(conf);
		} else if (monitorName.equals("AppStore")) {
			AppStoreMonitorProxy<?,?> proxy = new AppStoreMonitorProxy<>();
			AppStoreMonitorConfiguration conf = new AppStoreMonitorConfiguration();
			conf.setId(confId);
			proxy.deleteMonitorConfiguration(conf);
		} else throw new Exception("There is no monitor with this name");
	}

	private JsonObject getJson(String configuration) {
		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject)jsonParser.parse(configuration);
		return json;
	}
	
}
