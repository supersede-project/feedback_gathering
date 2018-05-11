package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.MechanismService;
import ch.fhnw.cere.orchestrator.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/mechanisms/{mechanismId}/parameters")
public class MechanismsParametersController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private MechanismService mechanismService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Parameter> getParameters() {
        return parameterService.findByMechanismIdAndLanguage(mechanismId(), language());
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Parameter createParameterForMechanism(@RequestBody Parameter parameter) {
        parameter.setMechanism(getMechanism());
        return parameterService.save(parameter);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteParameterForMechanism(@PathVariable long id) {
        parameterService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Parameter updateParameterForMechanism(@RequestBody Parameter parameter) {
        parameter.setMechanism(getMechanism());
        return parameterService.save(parameter);
    }

    protected long mechanismId() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(variables.get("mechanismId"));
    }

    protected Mechanism getMechanism() {
        return mechanismService.find(mechanismId());
    }
}