package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackVote;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackVoteService feedbackVoteService;

    public List<Feedback> findAll() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        for (Feedback feedback : feedbackList) {
            calculateVotes(feedback);
        }

        return feedbackList;

    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback find(long id) {
        Feedback feedback = feedbackRepository.findOne(id);
        if (feedback != null) {
            calculateVotes(feedback);
        }
        return feedback;
    }

    public void delete(long id) {
        feedbackRepository.delete(id);
    }

    public List<Feedback> findByApplicationId(long applicationId) {
        List<Feedback> feedbackList = feedbackRepository.findByApplicationId(applicationId);

        for (Feedback feedback : feedbackList) {
            calculateVotes(feedback);
        }

        return feedbackList;

    }

    public List<Feedback> findByUserIdentification(String userIdentification) {
        List<Feedback> feedbackList = feedbackRepository.findByUserIdentification(userIdentification);

        for (Feedback feedback : feedbackList) {
            calculateVotes(feedback);
        }

        return feedbackList;
    }

    @Override
    public List<Feedback> findAllByFeedbackIdIn(long applicationId, List<Long> idList) {
        List<Feedback> feedbackList = feedbackRepository.findAllByFeedbackIdIn(applicationId, idList);

        for (Feedback feedback : feedbackList) {
            calculateVotes(feedback);
        }

        return feedbackList;
    }

    @Override
    public List<Feedback> findByUserIdentificationOrIsPublicAndApplicationId(long applicationId, String userIdentification, boolean isPublic) {
        return feedbackRepository.findByUserIdentificationOrIsPublicAndApplicationId(userIdentification, isPublic, applicationId);
    }

    @Override
    public List<Feedback> findByIsReported(long applicationId) {
        return feedbackRepository.findByIsReported(applicationId);
    }

    @Override
    public int countByUserIdentification(String userIdentification) {
        return feedbackRepository.countByUserIdentification(userIdentification);
    }


    public List<Feedback> findByApplicationIdAndIsPublic(long applicationId, boolean isPublic) {
        List<Feedback> feedbackList = feedbackRepository.findByApplicationIdAndIsPublic(applicationId, isPublic);

        for (Feedback feedback : feedbackList) {
            calculateVotes(feedback);
        }

        return feedbackList;
    }


    private void calculateVotes(Feedback feedback) {
        feedback.setVotes(countVotes(feedback.getId()));
    }


    private int countVotes(long feedbackId) {
        int voteCount = 0;
        List<FeedbackVote> votes = feedbackVoteService.findByFeedbackId(feedbackId);
        for (FeedbackVote vote : votes) {
            voteCount += vote.getVote();
        }
        return voteCount;
    }
}