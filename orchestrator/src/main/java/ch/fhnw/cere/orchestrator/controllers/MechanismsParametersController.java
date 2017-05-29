package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/mechanisms/{mechanismId}/parameters")
public class MechanismsParametersController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Parameter> getParameters() {
        return parameterService.findByMechanismIdAndLanguage(mechanismId(), language());
    }

    protected long mechanismId() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(variables.get("mechanismId"));
    }
}