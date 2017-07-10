package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.ConflictException;
import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationMechanismRepository;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationRepository;
import ch.fhnw.cere.orchestrator.repositories.MechanismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}")
public class MechanismsController extends BaseController {

    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/mechanisms")
    public List<Mechanism> getMechanisms(@PathVariable long applicationId) {
        List<Configuration> configurationsOfApplication = configurationRepository.findByApplicationId(applicationId);
        if(configurationsOfApplication == null || configurationsOfApplication.size() == 0) {
            return new ArrayList<>();
        }
        List<ConfigurationMechanism> configurationMechanisms = new ArrayList<>();
        for(Configuration configuration : configurationsOfApplication) {
            configurationMechanisms.addAll(configuration.getConfigurationMechanisms());
        }

        List<Mechanism> mechanisms = configurationMechanisms.stream().map(ConfigurationMechanism::getMechanism).collect(Collectors.toList());
        mechanisms.forEach(mechanism -> mechanism.filterByLanguage(language(), this.fallbackLanguage));
        return mechanisms;
    }

    @RequestMapping(method = RequestMethod.GET, value = "mechanisms/{id}")
    public Mechanism getMechanism(@PathVariable long applicationId, @PathVariable long id) {
        Mechanism mechanism = mechanismRepository.findOne(id);

        if(mechanism == null) {
            throw new NotFoundException();
        }
        return mechanism;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "mechanisms")
    public Mechanism createMechanism(@PathVariable long applicationId, @RequestBody Mechanism mechanism) {
        return mechanismRepository.save(mechanism);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "mechanisms/{id}")
    public void deleteMechanism(@PathVariable long applicationId, @PathVariable long id) {
        if(configurationMechanismRepository.findByMechanismId(id).size() > 0) {
            throw new ConflictException();
        }
        mechanismRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "mechanisms/{id}")
    public Mechanism updateMechanism(@PathVariable long applicationId, @RequestBody Mechanism mechanism) {
        return mechanismRepository.save(mechanism);
    }

    @RequestMapping(method = RequestMethod.GET, value = "configurations/{configurationId}/mechanisms")
    public List<Mechanism> getMechanismsOfConfiguration(@PathVariable long applicationId, @PathVariable long configurationId) {
        List<ConfigurationMechanism> configurationMechanisms = configurationMechanismRepository.findByConfigurationId(configurationId);
        List<Mechanism> mechanisms = configurationMechanisms.stream().map(ConfigurationMechanism::getMechanism).collect(Collectors.toList());
        mechanisms.forEach(mechanism -> mechanism.filterByLanguage(language(), this.fallbackLanguage));
        return mechanisms;
    }

    @RequestMapping(method = RequestMethod.GET, value = "configurations/{configurationId}/mechanisms/{id}")
    public Mechanism getMechanismOfConfiguration(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long id) {
        if(configurationMechanismRepository.findByConfigurationIdAndMechanismId(configurationId, id).size() == 0) {
            throw new NotFoundException();
        }
        Mechanism mechanism = mechanismRepository.findOne(id);

        if(mechanism == null) {
            throw new NotFoundException();
        }
        return mechanism;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "configurations/{configurationId}/mechanisms")
    public Mechanism createMechanismForConfiguration(@PathVariable long applicationId, @PathVariable long configurationId, @RequestBody Mechanism mechanism) {
        return mechanismRepository.save(mechanism);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "configurations/{configurationId}/mechanisms/{id}")
    public void deleteMechanismForConfiguration(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long id) {
        if(configurationMechanismRepository.findByConfigurationIdAndMechanismId(configurationId, id).size() == 0) {
            throw new NotFoundException();
        }
        mechanismRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "configurations/{configurationId}/mechanisms/{id}")
    public Mechanism updateMechanismForConfiguration(@PathVariable long applicationId, @PathVariable long configurationId, @PathVariable long id, @RequestBody Mechanism mechanism) {
        if(configurationMechanismRepository.findByConfigurationIdAndMechanismId(configurationId, id).size() == 0) {
            throw new NotFoundException();
        }
        return mechanismRepository.save(mechanism);
    }
}