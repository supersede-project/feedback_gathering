package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}")
public class ApplicationParametersController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ApplicationService applicationService;


    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/mechanisms/{mechanismId}/parameters/{id}/switchOrder/{secondId}")
    public List<Parameter> switchParameterOrderForMechanism(@PathVariable long mechanismId, @PathVariable long id, @PathVariable long secondId) {
        return parameterService.switchParameterOrderForMechanism(mechanismId, id, secondId);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/mechanisms/{mechanismId}/parameters/{id}/reorder/{order}")
    public List<Parameter> reorderParameterForMechanism(@PathVariable long mechanismId, @PathVariable long id, @PathVariable int order) {
        return parameterService.reorderParameterForMechanism(mechanismId, id, order);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/general_configuration/{generalConfigurationId}/parameters/{id}/switchOrder/{secondId}")
    public List<Parameter> switchParameterOrderForGeneralConfiguration(@PathVariable long generalConfigurationId, @PathVariable long id, @PathVariable long secondId) {
        return parameterService.switchParameterOrderForGeneralConfiguration(generalConfigurationId, id, secondId);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/general_configuration/{generalConfigurationId}/parameters/{id}/reorder/{order}")
    public List<Parameter> reorderParameterForGeneralConfiguration(@PathVariable long generalConfigurationId, @PathVariable long id, @PathVariable int order) {
        return parameterService.reorderParameterForGeneralConfiguration(generalConfigurationId, id, order);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/parameter/{parameterId}/parameters/{id}/switchOrder/{secondId}")
    public List<Parameter> switchParameterOrderForParentParameter(@PathVariable long parameterId, @PathVariable long id, @PathVariable long secondId) {
        return parameterService.switchParameterOrderForParentParameter(parameterId, id, secondId);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "/parameter/{parameterId}/parameters/{id}/reorder/{order}")
    public List<Parameter> reorderParameterForParentParameter(@PathVariable long parameterId, @PathVariable long id, @PathVariable int order) {
        return parameterService.reorderParameterForParentParameter(parameterId, id, order);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }
}
