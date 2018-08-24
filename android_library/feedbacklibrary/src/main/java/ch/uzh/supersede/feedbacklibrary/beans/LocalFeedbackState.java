package ch.uzh.supersede.feedbacklibrary.beans;

import android.database.Cursor;

public final class LocalFeedbackState {
    private int owner;
    private int voted;
    private int subscribed;
    private int responded;

    public LocalFeedbackState(int owner, int voted, int subscribed, int responded) {
        this.owner = owner;
        this.voted = voted;
        this.subscribed = subscribed;
        this.responded = responded;
    }

    public LocalFeedbackState(Cursor cursor) {
        this.owner = cursor.getInt(0);
        this.voted = cursor.getInt(1);
        this.subscribed = cursor.getInt(2);
        this.responded = cursor.getInt(3);
    }

    public boolean isOwner() {
        return owner == 1;
    }

    public boolean isDownVoted() {
        return voted == -1;
    }

    public boolean isUpVoted() {
        return voted == 1;
    }

    public boolean isEqualVoted() {
        return voted == 0;
    }

    public boolean isSubscribed() {
        return subscribed == 1;
    }

    public boolean isResponded() {
        return responded == 1;
    }
}
