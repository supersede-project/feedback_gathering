package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.MonitorConfiguration;
import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorConfigurationRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorToolRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import eu.supersede.integration.api.monitoring.manager.proxies.MonitorManagerProxy;
import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.HttpMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "${supersede.base_path.monitoring}/MonitorTypes/{monitorTypeName}/Tools/{monitorToolName}/ToolConfigurations")
public class MonitorConfigurationController extends BaseController {

    @Autowired
    private MonitorConfigurationRepository monitorConfigurationRepository;
    @Autowired
    private MonitorTypeRepository monitorTypeRepository;
    @Autowired
    private MonitorToolRepository monitorToolRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public MonitorConfiguration getMonitorConfiguration(@PathVariable long id) {
    	
    	for (MonitorConfiguration cnf : monitorConfigurationRepository.findAll()) {
    		if (cnf.getMonitorTool().equals(getMonitoringTool()) && 
    				cnf.getMonitorManagerId() == id) return cnf;
    	}
    	
    	throw new NotFoundException();

    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public MonitorConfiguration createMonitorConfiguration(@RequestBody MonitorConfiguration monitorConfiguration) {
        monitorConfiguration.setMonitorTool(getMonitoringTool());
        try {
	        MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
			MonitorSpecificConfiguration configurationObj = generateMonitorConf(monitorConfiguration, getMonitoringTool());
			//MonitorSpecificConfiguration createConfiguration = proxy.createMonitorConfiguration(configurationObj);
			//FIXME setMonitorManagerId
			monitorConfiguration.setMonitorManagerId(100);
			MonitorConfiguration newMonitorConfiguration = monitorConfigurationRepository.save(monitorConfiguration);
			return newMonitorConfiguration;
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }        		
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteMonitorConfiguration(@PathVariable long id) {
    	
    	try {
    		for (MonitorConfiguration cnf : monitorConfigurationRepository.findAll()) {
	    		if (cnf.getMonitorTool().equals(getMonitoringTool()) && 
	    				cnf.getMonitorManagerId() == id) {
	    	    	MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
	    	    	MonitorSpecificConfiguration deleteConf = 
	    	    			generateMonitorConf(monitorConfigurationRepository.findOne(cnf.getId()), getMonitoringTool());
	    	    	deleteConf.setId((int) id);
	    	    	//proxy.deleteMonitorConfiguration(deleteConf);
	    			monitorConfigurationRepository.delete(cnf.getId());
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public MonitorConfiguration updateMonitorConfiguration(@RequestBody MonitorConfiguration monitorConfiguration,
    		@PathVariable long id) {
        monitorConfiguration.setMonitorTool(getMonitoringTool());
    	try {
    		for (MonitorConfiguration cnf : monitorConfigurationRepository.findAll()) {
	    		if (cnf.getMonitorTool().equals(getMonitoringTool()) && 
	    				cnf.getMonitorManagerId() == id) {
	    			MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
	    			MonitorSpecificConfiguration configurationObj = generateMonitorConf(monitorConfiguration, getMonitoringTool());
	    			configurationObj.setId((int) id);
	    			//proxy.updateMonitorConfiguration(configurationObj);
	    			monitorConfiguration.setMonitorManagerId(id);
	    			monitorConfiguration.setId(cnf.getId());
	    			monitorConfigurationRepository.save(monitorConfiguration);
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;
    }

    private MonitorType getMonitorType() {
        return monitorTypeRepository.findByName(monitorTypeName());
    }

    private MonitorTool getMonitoringTool() {
        return monitorToolRepository.findByMonitorTypeAndName(getMonitorType(), monitorToolName());
    }
    
    private MonitorSpecificConfiguration generateMonitorConf(MonitorConfiguration configuration, MonitorTool tool) throws Exception {
		MonitorSpecificConfiguration monitorManagerConf = null;
		if (tool.getMonitorName().equals("Twitter")) {
			monitorManagerConf = new TwitterMonitorConfiguration();
			((TwitterMonitorConfiguration) monitorManagerConf).setKeywordExpression(configuration.getKeywordExpression());
		}
		else if (tool.getMonitorName().equals("GooglePlay")) {
			monitorManagerConf = new GooglePlayMonitorConfiguration();
			((GooglePlayMonitorConfiguration) monitorManagerConf).setPackageName(configuration.getPackageName());
		}
		else if (tool.getMonitorName().equals("AppStore")) {
			monitorManagerConf = new AppStoreMonitorConfiguration();
			((AppStoreMonitorConfiguration) monitorManagerConf).setAppId(configuration.getAppId());
		}
		else if (tool.getMonitorName().equals("Http")) {
			monitorManagerConf = new HttpMonitorConfiguration();
			((HttpMonitorConfiguration) monitorManagerConf).setUrl(configuration.getUrl());
		}
		monitorManagerConf.setKafkaEndpoint(new URL(configuration.getKafkaEndpoint()));
		monitorManagerConf.setKafkaTopic(configuration.getKafkaTopic());
		monitorManagerConf.setTimeSlot(Integer.parseInt(configuration.getTimeSlot()));
		monitorManagerConf.setToolName(tool.getName());
		return monitorManagerConf;
}
}