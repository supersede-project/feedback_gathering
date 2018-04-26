package ch.uzh.supersede.feedbacklibrary.beans;

import android.database.Cursor;

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
    private String feedbackStatusLabel;
    private int responded;
    private UUID feedbackUid;
    private String title;

    public LocalFeedbackBean(Cursor cursor) {
        this.feedbackUid = UUID.fromString(cursor.getString(0));
        this.title = cursor.getString(1);
        this.votes = cursor.getInt(2);
        this.feedbackStatusLabel = cursor.getString(3);
        this.owner = cursor.getInt(4);
        this.creationDate = cursor.getLong(5);
        this.voted = cursor.getInt(6);
        this.votedDate = cursor.getLong(7);
        this.subscribed = cursor.getInt(8);
        this.subscribedDate = cursor.getLong(9);
        this.responded = cursor.getInt(10);
        this.respondedDate = cursor.getLong(11);
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
