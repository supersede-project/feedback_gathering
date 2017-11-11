package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.monitoring}/MonitorTypes")
public class MonitorTypeController extends BaseController {

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<MonitorType> getMonitorTypes() {
        return monitorTypeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public MonitorType getMonitorTypeByName(@PathVariable String name) {
        MonitorType monitorType = monitorTypeRepository.findByName(name);

        if (monitorType == null) {
            throw new NotFoundException();
        }
        return monitorType;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public MonitorType createMonitorType(@RequestBody MonitorType monitorType) {
        return monitorTypeRepository.save(monitorType);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public void deleteMonitorTypeByName(@PathVariable String name) {
        MonitorType monitorType = monitorTypeRepository.findByName(name);
        monitorTypeRepository.delete(monitorType.getId());
    }
}