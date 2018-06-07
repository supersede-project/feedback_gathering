package ch.uzh.supersede.feedbacklibrary.services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryCallback<T> implements Callback<T> {
    private IFeedbackServiceEventListener callback;
    private IFeedbackServiceEventListener.EventType eventType;

    public RepositoryCallback(IFeedbackServiceEventListener callback, IFeedbackServiceEventListener.EventType eventType) {
        this.callback = callback;
        this.eventType = eventType;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            callback.onEventCompleted(eventType, response.body());
        } else {
            callback.onEventFailed(eventType, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onConnectionFailed(eventType);
    }
}
