package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "applications")
public class ApplicationsController {
    
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Application> getApplications() {
        return applicationService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Application getApplication(@PathVariable long id) {
        Application application = applicationService.find(id);
        if(application == null) {
            throw new NotFoundException();
        }
        return application;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Application createApplication(@RequestBody Application application) {
        return applicationService.save(application);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApplication(@PathVariable long id) {
        applicationService.delete(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/")
    public Application updateApplication(@RequestBody Application application) {
        return applicationService.save(application);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/name/{name}")
    public List<Application> getApplicationByName(@PathVariable String name) {
        List<Application> applications = applicationService.findByName(name);
        return applications;
    }
}