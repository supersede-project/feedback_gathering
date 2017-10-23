package monitormanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.HttpMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;
import eu.supersede.integration.api.monitoring.monitors.proxies.AppStoreMonitorProxy;
import eu.supersede.integration.api.monitoring.monitors.proxies.GooglePlayMonitorProxy;
import eu.supersede.integration.api.monitoring.monitors.proxies.HttpMonitorProxy;
import eu.supersede.integration.api.monitoring.monitors.proxies.TwitterMonitorProxy;
import monitormanager.model.AppStoreMonitorManagerConfiguration;
import monitormanager.model.ConfigurationParser;
import monitormanager.model.GooglePlayMonitorManagerConfiguration;
import monitormanager.model.HttpMonitorManagerConfiguration;
import monitormanager.model.MonitorManagerSpecificConfiguration;
import monitormanager.model.MonitorManagerSpecificConfigurationResult;
import monitormanager.model.TwitterMonitorManagerConfiguration;

@RequestMapping(value = "/")
@RestController
public class MonitorManager implements IMonitorManager {
	private final Logger log = LoggerFactory.getLogger(this.getClass());	
	private ConfigurationParser parser = new ConfigurationParser();

	@Override
	@RequestMapping(value = "/{monitorName}/configuration", method = RequestMethod.POST, consumes="application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public MonitorManagerSpecificConfigurationResult addConfiguration(@PathVariable String monitorName, @RequestBody MonitorManagerSpecificConfiguration input) throws Exception {
		log.debug("Received new configuration for monitor: " + monitorName);
		System.out.println("Received new configuration for monitor: " + monitorName);
		switch (monitorName) {
			case "Twitter":
				TwitterMonitorProxy<?,?> proxyT = new TwitterMonitorProxy<>();
				TwitterMonitorConfiguration confT = parser.getTwitterConfiguration((TwitterMonitorManagerConfiguration) input);
				TwitterMonitorConfiguration resultT = proxyT.createMonitorConfiguration(confT);
				log.debug("Obtained TwitterMonitorConfiguration with id: " + resultT.getId());
				System.out.println("Obtained TwitterMonitorConfiguration with id: " + resultT.getId());
				return getResponse(resultT);
			case "GooglePlay":
				GooglePlayMonitorProxy<?,?> proxyG = new GooglePlayMonitorProxy<>();
				GooglePlayMonitorConfiguration confG = parser.getGooglePlayConfiguration((GooglePlayMonitorManagerConfiguration) input);
				GooglePlayMonitorConfiguration resultG = proxyG.createMonitorConfiguration(confG);
				return getResponse(resultG);
			case "AppStore":
				AppStoreMonitorProxy<?,?> proxyA = new AppStoreMonitorProxy<>();
				AppStoreMonitorConfiguration confA = parser.getAppStoreConfiguration((AppStoreMonitorManagerConfiguration) input);
				AppStoreMonitorConfiguration resultA = proxyA.createMonitorConfiguration(confA);
				return getResponse(resultA);
			case "Http":
				HttpMonitorProxy<?,?> proxyH = new HttpMonitorProxy<>();
				HttpMonitorConfiguration confH = parser.getHttpConfiguration((HttpMonitorManagerConfiguration) input);
				HttpMonitorConfiguration resultH = proxyH.createMonitorConfiguration(confH);
				return getResponse(resultH);
			default: throw new Exception("There is no monitor with this name");
		}
	}
	
	@Override
	@RequestMapping(value = "/{monitorName}/configuration/{confId}", method = RequestMethod.PUT, consumes="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public MonitorManagerSpecificConfigurationResult updateConfiguration(@PathVariable String monitorName,
			@PathVariable int confId, @RequestBody MonitorManagerSpecificConfiguration input) throws Exception {
		if (monitorName.equals("Twitter")) {
			TwitterMonitorProxy<?,?> proxy = new TwitterMonitorProxy<>();
			TwitterMonitorConfiguration conf = parser.getTwitterConfiguration((TwitterMonitorManagerConfiguration) input);
			conf.setId(confId);
			TwitterMonitorConfiguration result = proxy.updateMonitorConfiguration(conf);
			return getResponse(result);
		} else if (monitorName.equals("GooglePlay")) {
			GooglePlayMonitorProxy<?,?> proxy = new GooglePlayMonitorProxy<>();
			GooglePlayMonitorConfiguration conf = parser.getGooglePlayConfiguration((GooglePlayMonitorManagerConfiguration) input);
			conf.setId(confId);
			GooglePlayMonitorConfiguration result = proxy.updateMonitorConfiguration(conf);
			return getResponse(result);
		} else if (monitorName.equals("AppStore")) {
			AppStoreMonitorProxy<?,?> proxy = new AppStoreMonitorProxy<>();
			AppStoreMonitorConfiguration conf = parser.getAppStoreConfiguration((AppStoreMonitorManagerConfiguration) input);
			conf.setId(confId);
			AppStoreMonitorConfiguration result = proxy.updateMonitorConfiguration(conf);
			return getResponse(result);
		} else if (monitorName.contentEquals("Http")) {
			HttpMonitorProxy<?,?> proxy = new HttpMonitorProxy<>();
			HttpMonitorConfiguration conf = parser.getHttpConfiguration((HttpMonitorManagerConfiguration) input);
			HttpMonitorConfiguration result = proxy.updateMonitorConfiguration(conf);
			return getResponse(result);
		} else throw new Exception("There is no monitor with this name");
	}

	@Override
	@RequestMapping(value = "/{monitorName}/configuration/{confId}", method = RequestMethod.DELETE, consumes="application/json")
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
		} else if (monitorName.equals("Http")) {
			HttpMonitorProxy<?,?> proxy = new HttpMonitorProxy<>();
			HttpMonitorConfiguration conf = new HttpMonitorConfiguration();
			conf.setId(confId);
			proxy.deleteMonitorConfiguration(conf);
		} else throw new Exception("There is no monitor with this name");
	}
	
	private MonitorManagerSpecificConfigurationResult getResponse(MonitorSpecificConfiguration result) {
		MonitorManagerSpecificConfigurationResult res = new MonitorManagerSpecificConfigurationResult();
		res.setIdConf(result.getId());
		res.setStatus("success");
		log.debug("Created JsonObject response with id: " + result.getId());
		System.out.println("Created JsonObject response with id: " + result.getId());
		return res;
	}
	
}
