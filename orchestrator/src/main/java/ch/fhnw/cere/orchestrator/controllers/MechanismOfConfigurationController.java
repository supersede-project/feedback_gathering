package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MechanismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/configurations/{configurationId}/mechanisms")
public class ConfigurationMechanismController extends BaseController {

    @Autowired
    private MechanismRepository mechanismRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Mechanism> getMechanisms() {
        List<Mechanism> mechanisms = mechanismRepository.findAll();
        mechanisms.forEach(mechanism -> mechanism.filterByLanguage(language(), this.fallbackLanguage));
        return mechanisms;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Mechanism getMechanism(@PathVariable long id) {
        Mechanism mechanism = mechanismRepository.findOne(id);

        if(mechanism == null) {
            throw new NotFoundException();
        }
        return mechanism;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Mechanism createMechanism(@PathVariable long applicationId, @PathVariable long configurationId, @RequestBody Mechanism mechanism) {
        return mechanismRepository.save(mechanism);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteMechanism(@PathVariable long id) {
        mechanismRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Mechanism updateMechanism(@RequestBody Mechanism mechanism) {
        mechanism.setMonitorTool(getMonitoringTool());
        return mechanismRepository.save(mechanism);
    }

    private MonitorType getMonitorType() {
        return monitorTypeRepository.findByName(monitorTypeName());
    }

    private MonitorTool getMonitoringTool() {
        return monitorToolRepository.findByMonitorTypeAndName(getMonitorType(), monitorToolName());
    }
}