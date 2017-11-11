package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorToolRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "${supersede.base_path.monitoring}/MonitorTypes/{monitorTypeName}/Tools")
public class MonitorToolsController extends BaseController {

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;
    @Autowired
    private MonitorToolRepository monitorToolRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public MonitorTool getMonitorTool(@PathVariable String name) {
        MonitorTool monitorTool = monitorToolRepository.findByMonitorTypeAndName(getMonitorType(), name);

        if(monitorTool == null) {
            throw new NotFoundException();
        }
        return monitorTool;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public MonitorTool createMonitorTool(@RequestBody MonitorTool monitorTool) {
        monitorTool.setMonitorType(getMonitorType());
        return monitorToolRepository.save(monitorTool);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public void deleteMonitorToolByName(@PathVariable String name) {
        MonitorTool monitorTool = monitorToolRepository.findByMonitorTypeAndName(getMonitorType(), name);
        monitorToolRepository.delete(monitorTool.getId());
    }

    private MonitorType getMonitorType() {
        return monitorTypeRepository.findByName(monitorTypeName());
    }
}