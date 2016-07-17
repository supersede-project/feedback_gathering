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
 * Orchestrator: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration
 * Repository: // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
 */
public interface feedbackAPI {
    /**
     * This method retrieves the feedback configuration from the orchestrator
     *
     * @return the list of feedback configuration items
     */
    @GET("feedback_orchestrator/example/configuration")
    Call<List<FeedbackConfigurationItem>> getConfiguration();

    /**
     * This methods makes a POST request to the feedback repository without an image
     *
     * @param feedback the feedback
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedback(@Part("json") RequestBody feedback);

    /**
     * This methods makes a POST request to the feedback repository with an image
     *
     * @param file the image
     * @param feedback the feedback
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedbackMultipart(@Part("file") RequestBody file, @Part("json") RequestBody feedback);
}
