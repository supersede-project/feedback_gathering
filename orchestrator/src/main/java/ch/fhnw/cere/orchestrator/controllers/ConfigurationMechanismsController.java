package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.ConfigurationMechanism;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationMechanismRepository;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.ConfigurationService;
import ch.fhnw.cere.orchestrator.services.MechanismService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/configurations/{configurationId}/configurationMechanisms")
public class ConfigurationMechanismsController extends BaseController {

    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private MechanismService mechanismService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ConfigurationMechanism> getConfigurationMechanisms(@PathVariable long applicationId, @PathVariable long configurationId) {
        return configurationMechanismRepository.findByConfigurationId(configurationId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ConfigurationMechanism getConfigurationMechanism(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long id) {
        ConfigurationMechanism configurationMechanism = configurationMechanismRepository.findOne(id);
        if(configurationMechanism == null) {
            throw new NotFoundException();
        }
        return configurationMechanism;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/mechanisms/{mechanismId}")
    public ConfigurationMechanism createConfigurationMechanism(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long mechanismId, @RequestBody ConfigurationMechanism configurationMechanism) {
        Mechanism mechanism = mechanismService.find(mechanismId);
        configurationMechanism.setMechanism(mechanism);
        configurationMechanism.setConfiguration(getConfiguration());
        return configurationMechanismRepository.save(configurationMechanism);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteConfigurationMechanism(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long id) {
        configurationMechanismRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public ConfigurationMechanism updateConfigurationMechanism(@PathVariable long applicationId, @PathVariable long configurationId, @RequestBody ConfigurationMechanism configurationMechanism) {
        configurationMechanism.setConfiguration(getConfiguration());
        return configurationMechanismRepository.save(configurationMechanism);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }

    private Configuration getConfiguration() {
        return configurationService.find(configurationId());
    }
}