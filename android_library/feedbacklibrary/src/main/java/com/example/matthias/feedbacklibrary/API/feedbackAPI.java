package com.example.matthias.feedbacklibrary.API;

import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfiguration;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * API calls to the feedback orchestrator and feedback repository
 * Orchestrator: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration
 * Repository: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
 */
public interface feedbackAPI {
    String endpoint = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";

    // Test servlets:
    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/material_design_push.json
    @GET("FeedbackConfiguration/material_design_push.json")
    Call<OrchestratorConfiguration> getConfigurationPush();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/material_design_push_choice_active.json
    @GET("FeedbackConfiguration/material_design_push_choice_active.json")
    Call<OrchestratorConfiguration> getConfigurationPushChoiceActive();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/material_design_pull_0_text.json
    @GET("FeedbackConfiguration/material_design_pull_0_text.json")
    Call<OrchestratorConfiguration> getConfigurationPullText0();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/material_design_pull_1_text_rating.json
    @GET("FeedbackConfiguration/material_design_pull_1_text_rating.json")
    Call<OrchestratorConfiguration> getConfigurationPullTextRating1();

    // Actual backend
    /**
     * This method retrieves the feedback configuration from the orchestrator.
     *
     * @return the configuration from the orchestrator
     */
    @GET("feedback_orchestrator/example/configuration")
    Call<OrchestratorConfiguration> getConfiguration();

    /**
     * This methods makes a POST request to the feedback repository without an image.
     *
     * @param feedback the feedback
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedback(@Part("json") RequestBody feedback);

    /**
     * This methods makes a POST request to the feedback repository with an image.
     *
     * @param file the image
     * @param feedback the feedback
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedbackMultipart(@Part("file") RequestBody file, @Part("json") RequestBody feedback);
}
