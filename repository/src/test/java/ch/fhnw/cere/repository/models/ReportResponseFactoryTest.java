package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.reporting.RatingReport;
import ch.fhnw.cere.repository.models.reporting.ReportResponse;
import ch.fhnw.cere.repository.models.reporting.ReportResponseFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportResponseFactoryTest {

    @Test
    public void testReportResponseCreation() {
        ReportResponseFactory reportResponseFactory = new ReportResponseFactory();

        String ratingTitle = "Please rate this page";
        long mechanismId = 99;
        RatingFeedback ratingFeedback1 = new RatingFeedback(ratingTitle, 2, mechanismId);
        RatingFeedback ratingFeedback2 = new RatingFeedback(ratingTitle, 3, mechanismId);
        RatingFeedback ratingFeedback3 = new RatingFeedback(ratingTitle, 4, mechanismId);
        RatingFeedback ratingFeedback4 = new RatingFeedback(ratingTitle, 5, mechanismId);
        RatingFeedback ratingFeedback5 = new RatingFeedback(ratingTitle, 1, mechanismId);
        RatingFeedback ratingFeedback6 = new RatingFeedback("Another rating question", 28, 100);

        List<RatingFeedback> ratingFeedbacks = new ArrayList<RatingFeedback>() {{
            add(ratingFeedback1);
            add(ratingFeedback2);
            add(ratingFeedback3);
            add(ratingFeedback4);
            add(ratingFeedback5);
            add(ratingFeedback6);
        }};

        ReportResponse reportResponse = reportResponseFactory.createFromRatingFeedbacks(ratingFeedbacks);
        List<RatingReport> ratingReports = reportResponse.getRatingReports();
        assertEquals(2, ratingReports.size());

        assertEquals(ratingTitle, ratingReports.get(0).getRatingTitle());
        assertEquals(mechanismId, ratingReports.get(0).getRatingMechanismId());
        assertEquals(1, ratingReports.get(0).getMinRating(), 0.01);
        assertEquals(5, ratingReports.get(0).getMaxRating(), 0.01);
        assertEquals(3.0, ratingReports.get(0).getAverageRating(), 0.01);
        assertEquals(5, ratingReports.get(0).getNumberOfRatings());

        assertEquals("Another rating question", ratingReports.get(1).getRatingTitle());
        assertEquals(100, ratingReports.get(1).getRatingMechanismId());
        assertEquals(28, ratingReports.get(1).getMinRating(), 0.01);
        assertEquals(28, ratingReports.get(1).getMaxRating(), 0.01);
        assertEquals(28, ratingReports.get(1).getAverageRating(), 0.01);
        assertEquals(1, ratingReports.get(1).getNumberOfRatings());
    }
}
