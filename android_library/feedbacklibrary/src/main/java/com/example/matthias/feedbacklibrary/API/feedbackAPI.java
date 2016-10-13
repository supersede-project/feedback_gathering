package com.example.matthias.feedbacklibrary.API;

import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * API calls to the feedback orchestrator and feedback repository
 * Orchestrator: baseURL/feedback_orchestrator
 * Repository: baseURL/feedback_repository
 */
public interface feedbackAPI {
    /**
     * This methods sends the feedback to the repository.
     *
     * @param language the language
     * @param feedback the feedback
     * @param files    the multipart files
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/{language}/feedbacks")
    Call<JsonObject> createFeedbackVariant(@Path("language") String language, @Part("json") RequestBody feedback, @PartMap Map<String, RequestBody> files);

    // TODO: Change to updated URL when the new version of the backend is deployed
    /**
     * This method retrieves the feedback configuration from the orchestrator.
     *
     * @return the configuration from the orchestrator
     */
    @GET("feedback_orchestrator/{language}/applications/{application_id}")
    Call<OrchestratorConfigurationItem> getConfiguration(@Path("language") String language, @Path("application_id") long application_id);

    // TODO: Change to updated URL when the new version of the backend is deployed
    /**
     * This method checks it the orchestrator is up and running.
     *
     * @return 'pong'
     */
    @GET("feedback_orchestrator/ping")
    Call<ResponseBody> pingOrchestrator();

    /**
     * This method checks it the repository is up and running.
     *
     * @return 'pong'
     */
    @GET("feedback_repository/ping")
    Call<ResponseBody> pingRepository();
}
