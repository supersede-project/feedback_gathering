package com.example.matthias.feedbacklibrary.API;

import com.example.matthias.feedbacklibrary.feedbacks.Feedback;
import com.example.matthias.feedbacklibrary.feedbacks.ScreenshotFeedback;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * API calls to the feedback orchestrator and feedback repository
 */
public interface feedbackAPI {
    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration
    @GET("feedback_orchestrator/example/configuration")
    Call<List<FeedbackConfigurationItem>> getConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedback(@Part("json") RequestBody feedback);

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedbackMultipart(@Part("file") RequestBody file, @Part("json") RequestBody feedback);
}
