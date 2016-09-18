package com.example.matthias.feedbacklibrary.API;

import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * API calls to the feedback orchestrator and feedback repository
 * Orchestrator: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration
 * Repository: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
 */
public interface feedbackAPI {
    String endpoint = "http://ec2-54-166-31-250.compute-1.amazonaws.com/";

    /**
     * This methods makes a POST request to the feedback repository.
     *
     * @param language the language
     * @param feedback the feedback
     * @param files    the multipart files
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/{language}/feedbacks")
    Call<JsonObject> createFeedbackVariant1(@Path("language") String language, @Part("json") RequestBody feedback, @PartMap Map<String, RequestBody> files);

    /**
     * This methods makes a POST request to the feedback repository.
     *
     * @param language      the language
     * @param feedbackJSON  the feedback JSON
     * @param feedbackFiles the feedback files
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/{language}/feedbacks")
    Call<JsonObject> createFeedbackVariant2(@Path("language") String language, @Part("json") RequestBody feedbackJSON, @Part MultipartBody.Part feedbackFiles);

    /**
     * This method retrieves the feedback configuration from the orchestrator.
     *
     * @return the configuration from the orchestrator
     */
    @GET("feedback_orchestrator/{language}/applications/{application_id}")
    Call<OrchestratorConfigurationItem> getConfiguration(@Path("language") String language, @Path("application_id") long application_id);
}
