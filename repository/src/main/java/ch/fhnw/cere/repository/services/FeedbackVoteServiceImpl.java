package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackVote;
import ch.fhnw.cere.repository.repositories.FeedbackVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackVoteServiceImpl implements FeedbackVoteService {

    @Autowired
    private FeedbackVoteRepository feedbackVoteRepository;

    @Override
    public List<FeedbackVote> findAll() {
        return feedbackVoteRepository.findAll();
    }

    @Override
    public FeedbackVote save(FeedbackVote feedbackVote) {
        return feedbackVoteRepository.save(feedbackVote);
    }

    @Override
    public FeedbackVote find(long id) {
        return feedbackVoteRepository.findOne(id);
    }

    @Override
    public List<FeedbackVote> findByFeedbackId(long id) {
        return feedbackVoteRepository.findByFeedbackId(id);
    }

    @Override
    public List<FeedbackVote> findByVoterUserId(long id) {
        return feedbackVoteRepository.findByVoterUserId(id);
    }

    @Override
    public List<FeedbackVote> findByVotedUserName(String username) {
        return null;
    }

    @Override
    public void delete(long id) {
        feedbackVoteRepository.delete(id);
    }

    @Override
    public FeedbackVote findByFeedbackIdAndVoterUserId(long feedbackId, long voterUserId) {
       return feedbackVoteRepository.findByFeedbackIdAndVoterUserId(feedbackId, voterUserId);
    }
}
