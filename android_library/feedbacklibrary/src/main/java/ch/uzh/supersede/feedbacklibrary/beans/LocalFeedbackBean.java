package ch.uzh.supersede.feedbacklibrary.beans;

import android.database.Cursor;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import ch.uzh.supersede.feedbacklibrary.utils.Enums;

public class LocalFeedbackBean {
    private long creationDate;
    private long votedDate;
    private long subscribedDate;
    private long respondedDate;
    private int owner;
    private int voted;
    private int subscribed;
    private int votes;
    private int responses;
    private String feedbackStatusLabel;
    private int responded;
    private UUID feedbackUid;
    private String title;

    public LocalFeedbackBean(Cursor cursor) {
        this.feedbackUid = UUID.fromString(cursor.getString(0));
        this.title = cursor.getString(1);
        this.votes = cursor.getInt(2);
        this.responses = cursor.getInt(3);
        this.feedbackStatusLabel = cursor.getString(4);
        this.owner = cursor.getInt(5);
        this.creationDate = cursor.getLong(6);
        this.voted = cursor.getInt(7);
        this.votedDate = cursor.getLong(8);
        this.subscribed = cursor.getInt(9);
        this.subscribedDate = cursor.getLong(10);
        this.responded = cursor.getInt(11);
        this.respondedDate = cursor.getLong(12);
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getVotedDate() {
        return votedDate;
    }

    public long getSubscribedDate() {
        return subscribedDate;
    }

    public int getOwner() {
        return owner;
    }

    public int getVoted() {
        return voted;
    }

    public int getSubscribed() {
        return subscribed;
    }

    public int getVotes() {
        return votes;
    }

    public int getResponses() {
        return responses;
    }

    public UUID getFeedbackUid() {
        return feedbackUid;
    }

    public String getTitle() {
        return title;
    }

    public long getRespondedDate() {
        return respondedDate;
    }

    public int getResponded() {
        return responded;
    }

    public Enums.FEEDBACK_STATUS getFeedbackStatus(){
        return Enums.resolveFeedbackStatus(feedbackStatusLabel);
    }
}
