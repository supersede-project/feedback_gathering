package ch.uzh.supersede.feedbacklibrary.beans;

import android.database.Cursor;

import java.util.UUID;

public class LocalFeedbackBean {
    private long creationDate;
    private long votedDate;
    private long subscribedDate;
    private long respondedDate;
    private int owner;
    private int voted;
    private int subscribed;
    private int votes;
    private int responded;
    private UUID feedbackUid;
    private String title;

    public LocalFeedbackBean(Cursor cursor) {
        this.feedbackUid = UUID.fromString(cursor.getString(0));
        this.title = cursor.getString(1);
        this.votes = cursor.getInt(2);
        this.owner = cursor.getInt(3);
        this.creationDate = cursor.getLong(4);
        this.voted = cursor.getInt(5);
        this.votedDate = cursor.getLong(6);
        this.subscribed = cursor.getInt(7);
        this.subscribedDate = cursor.getLong(8);
        this.responded = cursor.getInt(9);
        this.respondedDate = cursor.getLong(10);
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
}
