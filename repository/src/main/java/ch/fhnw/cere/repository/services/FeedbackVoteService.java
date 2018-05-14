package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackVote;

import java.util.List;

public interface FeedbackVoteService {
    public List<FeedbackVote> findAll();

    public FeedbackVote save(FeedbackVote feedbackVote);

    public FeedbackVote find(long id);

    public List<FeedbackVote> findByFeedbackId(long id);

    public List<FeedbackVote> findByVoterUserId(long id);

    public List<FeedbackVote> findByVotedUserId(long id);

    public void delete(long id);
}
