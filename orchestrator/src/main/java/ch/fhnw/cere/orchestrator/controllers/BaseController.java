package ch.fhnw.cere.orchestrator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class BaseController {
    @Autowired
    HttpServletRequest req;

    protected String fallbackLanguage = "en";

    protected String language() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return variables.get("language");
    }

    protected long applicationId() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(variables.get("applicationId"));
    }

    protected long configurationId() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(variables.get("configurationId"));
    }

    protected String monitorTypeName() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return variables.get("monitorTypeName");
    }

    protected String monitorToolName() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return variables.get("monitorToolName");
    }

    protected long apiUserId() {
        Map<String, String> variables = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.parseLong(variables.get("apiUserId"));
    }
}
