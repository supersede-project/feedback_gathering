package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackVote;
import ch.fhnw.cere.repository.models.VoteCount;
import ch.fhnw.cere.repository.repositories.AndroidUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AndroidUserServiceImpl implements AndroidUserService {

    @Autowired
    private AndroidUserRepository androidUserRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackVoteService feedbackVoteService;


    public List<AndroidUser> findAll(){

        List<AndroidUser> androidUsers = androidUserRepository.findAll();
        for(AndroidUser androidUser : androidUsers) {
            calculateStats(androidUser);
        }

        return androidUsers;
    }

    public AndroidUser save(AndroidUser androidUserRole){
        return androidUserRepository.save(androidUserRole);
    }

    public AndroidUser find(long id){
        AndroidUser androidUser = androidUserRepository.findOne(id);
        calculateStats(androidUser);
        return androidUser;
    }

    public void delete(long id){
        androidUserRepository.delete(id);
    }

    public AndroidUser findByName(String name) {
        AndroidUser androidUser = androidUserRepository.findByName(name);
        calculateStats(androidUser);
        return androidUser;
    }

    public List<AndroidUser> findByApplicationId(long id) {

        List<AndroidUser> androidUsers = androidUserRepository.findByApplicationId(id);
        for(AndroidUser androidUser : androidUsers) {
            calculateStats(androidUser);
        }

        return androidUsers;
    }


    private void calculateStats(AndroidUser androidUser) {
        androidUser.setKarma(calculateKarma(androidUser.getName()));
        androidUser.setFeedbackCount(countFeedback(androidUser.getName()));
        androidUser.setVoteCount(countVotes(androidUser.getId()));
    }

    private int calculateKarma(String userName) {
        int karma = 0;
        List<Feedback> userFeedback = feedbackService.findByUserIdentification(userName);

        for (Feedback feedback : userFeedback) {
            long feedbackId = feedback.getId();
            List<FeedbackVote> votes = feedbackVoteService.findByFeedbackId(feedbackId);
            for(FeedbackVote vote : votes) {
                karma += vote.getVote();
            }
        }
        return karma;
    }

    private VoteCount countVotes(long userId) {
        VoteCount voteCount = new VoteCount();
        int upVoteCount = 0;
        int downVoteCount = 0;
        List<FeedbackVote> votes = feedbackVoteService.findByVoterUserId(userId);
        for (FeedbackVote vote: votes) {
            if (vote.getVote() == 1) {
                upVoteCount++;
            } else if (vote.getVote() == -1) {
                downVoteCount++;
            }
        }

        voteCount.setUpVoteCount(upVoteCount);
        voteCount.setDownVoteCount(downVoteCount);
        return voteCount;
    }

    private long countFeedback(String userIdentification) {
        return feedbackService.countByUserIdentifictation(userIdentification);
    }

}