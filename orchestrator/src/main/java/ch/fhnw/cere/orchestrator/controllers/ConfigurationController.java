package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.User;
import ch.fhnw.cere.orchestrator.models.UserInfoPullConfiguration;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/configurations")
public class ConfigurationController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Configuration> getConfigurations() {
        List<Configuration> configurations = configurationService.findByApplicationId(applicationId());
        configurations.forEach(configuration -> configuration.filterByLanguage(language(), this.fallbackLanguage));
        return configurations;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Configuration getConfiguration(@PathVariable long id) {
        Configuration configuration = configurationService.find(id);
        if(configuration == null) {
            throw new NotFoundException();
        }
        configuration.filterByLanguage(language(), this.fallbackLanguage);
        return configuration;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Configuration createConfiguration(@PathVariable long applicationId, @RequestBody Configuration configuration) {
        configuration.setApplication(getApplication());
        return configurationService.save(configuration);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteConfiguration(@PathVariable long id) {
        configurationService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public Configuration updateConfiguration(@RequestBody Configuration configuration) {
        return configurationService.save(configuration);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{userIdentification}/info")
    public Configuration createInfoPullConfigurationForUser(@PathVariable long applicationId, @PathVariable String userIdentification, @RequestBody UserInfoPullConfiguration userInfoPullConfiguration) {
        Configuration configuration = userInfoPullConfiguration.buildConfiguration(userIdentification, getApplication());
        return configurationService.save(configuration);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }
}