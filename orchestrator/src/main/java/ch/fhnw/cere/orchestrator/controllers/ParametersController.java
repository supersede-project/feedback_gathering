package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/{language}/parameters")
public class ParametersController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Parameter> getParameters() {
        return parameterService.findByLanguage(language());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Parameter getParameter(@PathVariable long id) {
        Parameter parameter = parameterService.find(id);
        if(parameter == null) {
            throw new NotFoundException();
        }
        return parameter;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Parameter createParameter(@RequestBody Parameter parameter) {
        return parameterService.save(parameter);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteParameter(@PathVariable long id) {
        parameterService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/")
    public Parameter updateParameter(@RequestBody Parameter parameter) {
        return parameterService.save(parameter);
    }
}