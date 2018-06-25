package ch.fhnw.cere.repository.models.reporting;

import ch.fhnw.cere.repository.models.RatingFeedback;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class ReportResponseFactory {

    public ReportResponse createFromRatingFeedbacks(List<RatingFeedback> ratingFeedbacks) {
        HashMap<Long, List<Integer>> ratingValuesMap = new HashMap<>();
        HashMap<Long, String> ratingTitlesMap = new HashMap<>();

        for(RatingFeedback ratingFeedback : ratingFeedbacks) {
            List<Integer> ratingsForMechanism = ratingValuesMap.getOrDefault(ratingFeedback.getMechanismId(), new ArrayList<>());
            ratingsForMechanism.add(ratingFeedback.getRating());
            ratingValuesMap.put(ratingFeedback.getMechanismId(), ratingsForMechanism);
            ratingTitlesMap.put(ratingFeedback.getMechanismId(), ratingFeedback.getTitle());
        }

        List<RatingReport> ratingReports = new ArrayList<>();

        for(Map.Entry<Long, List<Integer>> mapEntry : ratingValuesMap.entrySet()) {
            RatingReport ratingReport = new RatingReport();

            OptionalDouble average = mapEntry.getValue()
                    .stream()
                    .mapToDouble(a -> a)
                    .average();

            OptionalDouble min = mapEntry.getValue()
                    .stream()
                    .mapToDouble(a -> a)
                    .min();

            OptionalDouble max = mapEntry.getValue()
                    .stream()
                    .mapToDouble(a -> a)
                    .max();

            if(average.isPresent()) {
                double averageRating = average.getAsDouble();
                ratingReport.setAverageRating((float)averageRating);
            }
            if(min.isPresent()) {
                double minRating = min.getAsDouble();
                ratingReport.setMinRating((float)minRating);
            }
            if(max.isPresent()) {
                double maxRating = max.getAsDouble();
                ratingReport.setMaxRating((float)maxRating);
            }

            ratingReport.setRatingTitle(ratingTitlesMap.get(mapEntry.getKey()));
            ratingReport.setRatingMechanismId(mapEntry.getKey());
            ratingReport.setNumberOfRatings(mapEntry.getValue().size());

            ratingReports.add(ratingReport);
        }

        return new ReportResponse(ratingReports);
    }
}
