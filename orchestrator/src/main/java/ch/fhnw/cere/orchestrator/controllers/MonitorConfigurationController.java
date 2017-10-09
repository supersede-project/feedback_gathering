package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.MonitorConfiguration;
import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorConfigurationRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorToolRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
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
        MonitorConfiguration monitorConfiguration = monitorConfigurationRepository.findOne(id);

        if(monitorConfiguration == null) {
            throw new NotFoundException();
        }
        return monitorConfiguration;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public MonitorConfiguration createMonitorConfiguration(@RequestBody MonitorConfiguration monitorConfiguration) {
        monitorConfiguration.setMonitorTool(getMonitoringTool());
        MonitorConfiguration newMonitorConfiguration = monitorConfigurationRepository.save(monitorConfiguration);
        //TODO add connection to MonitorManager
        return newMonitorConfiguration;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteMonitorConfiguration(@PathVariable long id) {
    	//TODO add connection to MonitorManager
        monitorConfigurationRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public MonitorConfiguration updateMonitorConfiguration(@RequestBody MonitorConfiguration monitorConfiguration) {
        monitorConfiguration.setMonitorTool(getMonitoringTool());
        MonitorConfiguration updatedMonitorConfiguration = monitorConfigurationRepository.save(monitorConfiguration);
        //TODO add connection to MonitorManager
        return updatedMonitorConfiguration;
    }

    private MonitorType getMonitorType() {
        return monitorTypeRepository.findByName(monitorTypeName());
    }

    private MonitorTool getMonitoringTool() {
        return monitorToolRepository.findByMonitorTypeAndName(getMonitorType(), monitorToolName());
    }
}