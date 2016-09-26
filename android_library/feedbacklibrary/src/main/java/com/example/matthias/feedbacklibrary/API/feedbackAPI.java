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
 * Orchestrator: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator
 * Repository: http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository
 */
public interface feedbackAPI {
    //String endpoint = "http://ec2-54-166-31-250.compute-1.amazonaws.com/"; --> PTV domain
    String endpoint = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";

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

    /**
     * This method retrieves the feedback configuration from the orchestrator.
     *
     * @return the configuration from the orchestrator
     */
    @GET("feedback_orchestrator/{language}/applications/{application_id}")
    Call<OrchestratorConfigurationItem> getConfiguration(@Path("language") String language, @Path("application_id") long application_id);

    /**
     * This method checks it the application is up and running.
     *
     * @return 'pong'
     */
    @GET("feedback_orchestrator/ping")
    Call<ResponseBody> pingOrchestrator();
    // TODO: Type of return value (JSON, Plain text)?
}
