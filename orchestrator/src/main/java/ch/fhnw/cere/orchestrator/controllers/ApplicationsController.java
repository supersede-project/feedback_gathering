package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.helpers.JsonHelper;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.UserGroupService;
import ch.fhnw.cere.orchestrator.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications")
public class ApplicationsController extends BaseController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private JsonHelper jsonHelper;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Application> getApplications() {
        List<Application> applications = applicationService.findAll();
        applications.stream().forEach(application -> application.filterByLanguage(language(), this.fallbackLanguage));
        return applications;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Application getAnonymousUserApplication(@PathVariable long id) {
        Application application = applicationService.find(id);
        if(application == null) {
            throw new NotFoundException();
        }
        application.filterByLanguage(language(), this.fallbackLanguage);
        application = application.filterForAnonymousUser();
        return application;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/full")
    public Application getFullApplication(@PathVariable long id) {
        Application application = applicationService.find(id);
        if(application == null) {
            throw new NotFoundException();
        }
        application.filterByLanguage(language(), this.fallbackLanguage);
        return application;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/full/polylang")
    public Application getFullApplicationWithAllLanguages(@PathVariable long id) {
        Application application = applicationService.find(id);
        if(application == null) {
            throw new NotFoundException();
        }
        return application;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/user_identification/{userIdentification}")
    public Application getApplicationByIdAndUserIdentification(@PathVariable long id, @PathVariable String userIdentification) {
        Application application;
        if(userService.findByApplicationIdAndUserIdentification(id, userIdentification) == null || userGroupService.findByUserIdentification(id, userIdentification).size() == 0) {
            application = applicationService.find(id);
            application = application.filterForAnonymousUser();
        } else {
            application = applicationService.findByIdAndUserIdentification(id, userIdentification);
        }

        if(application == null) {
            throw new NotFoundException();
        }
        application.filterByLanguage(language(), this.fallbackLanguage);
        return application;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/user_group/{userGroupId}")
    public Application getApplicationByIdAndUserGroupId(@PathVariable long id, @PathVariable long userGroupId) {
        Application application = applicationService.findByIdAndUserGroupId(id, userGroupId);
        if(application == null) {
            throw new NotFoundException();
        }
        application.filterByLanguage(language(), this.fallbackLanguage);
        return application;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Application createApplication(@RequestBody Application application) {
        application = setApplicationRelations(application);
        return applicationService.save(application);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/import")
    public List<Application> importApplications(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        payload = jsonHelper.stripOffIds(payload);
        payload = jsonHelper.stripOffTimestamps(payload);
        payload = jsonHelper.stripOffInvalidCommas(payload);

        ObjectMapper mapper = new ObjectMapper();
        List<Application> applications = mapper.readValue(payload, new TypeReference<List<Application>>(){});

        List<Application> createdApplications = new ArrayList<>();

        for(Application application : applications) {
            application = setApplicationRelations(application);
            Application createdApplication = applicationService.save(application);
            createdApplications.add(createdApplication);
        }

        return createdApplications;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#id)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApplication(@PathVariable long id) {
        applicationService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#application)")
    @RequestMapping(method = RequestMethod.PUT, value = "/")
    public Application updateApplication(@RequestBody Application application) {
        return applicationService.save(application);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/name/{name}")
    public List<Application> getApplicationByName(@PathVariable String name) {
        List<Application> applications = applicationService.findByName(name);
        return applications;
    }

    private Application setApplicationRelations(Application application) {
        String defaultLanguage = "en";

        if(application.getConfigurations() != null) {
            for(Configuration configuration : application.getConfigurations()) {
                configuration.setApplication(application);
                if(configuration.getGeneralConfiguration() != null) {
                    configuration.getGeneralConfiguration().setConfiguration(configuration);
                    if(configuration.getGeneralConfiguration().getParameters() != null) {
                        for(Parameter parameter : configuration.getGeneralConfiguration().getParameters()) {
                            parameter.setGeneralConfiguration(configuration.getGeneralConfiguration());
                            if(parameter.getLanguage() == null) {
                                parameter.setLanguage(defaultLanguage);
                            }
                            if(parameter.getParameters() != null) {
                                for(Parameter subParameter : parameter.getParameters()) {
                                    subParameter.setParentParameter(parameter);
                                    if(subParameter.getLanguage() == null) {
                                        subParameter.setLanguage(defaultLanguage);
                                    }
                                }
                            }
                        }
                    }
                }
                if(configuration.getMechanisms() != null) {
                    for(Mechanism mechanism : configuration.getMechanisms()) {
                        if(mechanism.getParameters() != null) {
                            for(Parameter parameter : mechanism.getParameters()) {
                                parameter.setMechanism(mechanism);
                                if(parameter.getLanguage() == null) {
                                    parameter.setLanguage(defaultLanguage);
                                }
                                if(parameter.getParameters() != null) {
                                    for(Parameter subParameter : parameter.getParameters()) {
                                        subParameter.setParentParameter(parameter);
                                        if(subParameter.getLanguage() == null) {
                                            subParameter.setLanguage(defaultLanguage);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(application.getGeneralConfiguration() != null) {
            application.getGeneralConfiguration().setApplication(application);
            if(application.getGeneralConfiguration().getParameters() != null) {
                for(Parameter parameter : application.getGeneralConfiguration().getParameters()) {
                    parameter.setGeneralConfiguration(application.getGeneralConfiguration());
                    if(parameter.getLanguage() == null) {
                        parameter.setLanguage(defaultLanguage);
                    }
                    if(parameter.getParameters() != null) {
                        for(Parameter subParameter : parameter.getParameters()) {
                            subParameter.setParentParameter(parameter);
                            if(subParameter.getLanguage() == null) {
                                subParameter.setLanguage(defaultLanguage);
                            }
                        }
                    }
                }
            }
        }
        return application;
    }
}