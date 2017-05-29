package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.ConfigurationService;
import ch.fhnw.cere.orchestrator.services.GeneralConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/{language}/applications/{applicationId}/configurations/{configurationId}")
public class ConfigurationGeneralConfigurationController extends BaseController {

    @Autowired
    private GeneralConfigurationService generalConfigurationService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ConfigurationService configurationService;

    @RequestMapping(method = RequestMethod.GET, value = "general_configuration")
    public GeneralConfiguration getConfigurationGeneralConfiguration() {
        GeneralConfiguration generalConfiguration = generalConfigurationService.findByConfigurationId(configurationId());
        if(generalConfiguration == null) {
            throw new NotFoundException();
        }
        generalConfiguration.filterByLanguage(language(), this.fallbackLanguage);
        return generalConfiguration;
    }

    @RequestMapping(method = RequestMethod.GET, value = "general_configuration/{id}")
    public GeneralConfiguration getConfigurationGeneralConfiguration(@PathVariable long id) {
        GeneralConfiguration generalConfiguration = generalConfigurationService.find(id);
        if(generalConfiguration == null) {
            throw new NotFoundException();
        }
        generalConfiguration.filterByLanguage(language(), this.fallbackLanguage);
        return generalConfiguration;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "general_configuration")
    public GeneralConfiguration createConfigurationGeneralConfiguration(@RequestBody GeneralConfiguration generalConfiguration) {
        generalConfiguration.setApplication(getApplication());
        generalConfiguration.setConfiguration(getConfiguration());
        return generalConfigurationService.save(generalConfiguration);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "general_configuration/{id}")
    public void deleteConfigurationGeneralConfiguration(@PathVariable long id) {
        generalConfigurationService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "general_configuration/")
    public GeneralConfiguration updateConfigurationGeneralConfiguration(@RequestBody GeneralConfiguration generalConfiguration) {
        return generalConfigurationService.save(generalConfiguration);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }

    private Configuration getConfiguration() {
        return configurationService.find(configurationId());
    }
}