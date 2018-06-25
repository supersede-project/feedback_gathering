package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.reporting.ReportResponse;
import ch.fhnw.cere.repository.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/reports")
public class ReportingController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ReportResponse getFeedbackReportOfApplication(@PathVariable long applicationId) {
        return reportService.findForApplicationId(applicationId);
    }
}
