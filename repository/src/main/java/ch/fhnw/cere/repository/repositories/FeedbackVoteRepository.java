package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackVoteRepository extends JpaRepository<FeedbackVote, Long> {
    public List<FeedbackVote> findByFeedbackId(long id);
    public List<FeedbackVote> findByVoterUserId(long id);
    public List<FeedbackVote> findByVotedUserId(long id);
}
