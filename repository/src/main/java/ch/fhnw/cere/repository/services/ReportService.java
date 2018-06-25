package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.reporting.ReportResponse;


public interface ReportService {
    public ReportResponse findForApplicationId(long id);
}



