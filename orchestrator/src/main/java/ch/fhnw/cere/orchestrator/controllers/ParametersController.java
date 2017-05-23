package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/{language}/parameters")
public class ParametersController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Parameter> getParameters() {
        return parameterService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Parameter getParameter(@PathVariable long id) {
        Parameter parameter = parameterService.find(id);
        if(parameter == null) {
            throw new NotFoundException();
        }
        return parameter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Parameter createParameter(@RequestBody Parameter parameter) {
        return parameterService.save(parameter);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteParameter(@PathVariable long id) {
        parameterService.delete(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/")
    public Parameter updateParameter(@RequestBody Parameter parameter) {
        return parameterService.save(parameter);
    }
}