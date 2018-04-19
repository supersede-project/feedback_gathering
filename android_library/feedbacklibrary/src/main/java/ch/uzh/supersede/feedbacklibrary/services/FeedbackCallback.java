package ch.uzh.supersede.feedbacklibrary.services;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackWrapper<T> implements Callback<T> {
    private IFeedbackServiceEventListener callback;
    private IFeedbackServiceEventListener.EventType eventType;

    public CallbackWrapper(IFeedbackServiceEventListener callback, IFeedbackServiceEventListener.EventType eventType) {
        this.callback = callback;
        this.eventType = eventType;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            callback.onEventCompleted(eventType, response);
        } else {
            callback.onEventFailed(eventType, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onConnectionFailed(eventType);
    }
}
