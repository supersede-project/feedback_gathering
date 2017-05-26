package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import ch.fhnw.cere.orchestrator.services.GeneralConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/{language}/applications/{applicationId}/general_configuration")
public class ApplicationGeneralConfigurationController extends BaseController {

    @Autowired
    private GeneralConfigurationService generalConfigurationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public GeneralConfiguration getApplicationGeneralConfiguration() {
        GeneralConfiguration generalConfiguration = generalConfigurationService.findByApplicationId(applicationId());
        if(generalConfiguration == null) {
            throw new NotFoundException();
        }
        generalConfiguration.filterByLanguage(language(), this.fallbackLanguage);
        return generalConfiguration;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public GeneralConfiguration getApplicationGeneralConfiguration(@PathVariable long id) {
        GeneralConfiguration generalConfiguration = generalConfigurationService.find(id);
        if(generalConfiguration == null) {
            throw new NotFoundException();
        }
        generalConfiguration.filterByLanguage(language(), this.fallbackLanguage);
        return generalConfiguration;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public GeneralConfiguration createApplicationGeneralConfiguration(@RequestBody GeneralConfiguration generalConfiguration) {
        generalConfiguration.setApplication(getApplication());
        return generalConfigurationService.save(generalConfiguration);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApplicationGeneralConfiguration(@PathVariable long id) {
        generalConfigurationService.delete(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "")
    public GeneralConfiguration updateApplicationGeneralConfiguration(@RequestBody GeneralConfiguration generalConfiguration) {
        return generalConfigurationService.save(generalConfiguration);
    }

    private Application getApplication() {
        return applicationRepository.getOne(applicationId());
    }
}