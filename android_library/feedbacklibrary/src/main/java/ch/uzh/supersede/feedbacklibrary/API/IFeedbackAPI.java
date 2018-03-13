/*
 * Copyright [2016] [Matthias Scherrer]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.uzh.supersede.feedbacklibrary.API;

import com.google.gson.JsonObject;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * API calls to the feedback orchestrator and feedback repository
 * Orchestrator: baseURL/orchestrator/feedback (http://docs.supersedeorchestratorapi.apiary.io/#reference)
 * Repository: baseURL/feedback_repository (http://docs.supersederepositoryapi.apiary.io/#reference)
 */
public interface IFeedbackAPI {
    /**
     * This methods sends the feedback to the repository.
     *
     * @param language the language
     * @param feedback the feedback
     * @param files    the multipart files
     * @return the JSON object
     */
    @Multipart
    @POST("feedback_repository/{language}/applications/{application_id}/feedbacks")
    Call<JsonObject> createFeedbackVariant(@Path("language") String language, @Path("application_id") long application_id, @Part MultipartBody.Part feedback, @Part List<MultipartBody.Part> files);

    /**
     * This method retrieves the feedback configuration from the orchestrator.
     *
     * @return the configuration from the orchestrator
     */
    @GET("orchestrator/feedback/{language}/applications/{application_id}")
    Call<OrchestratorConfigurationItem> getConfiguration(@Path("language") String language, @Path("application_id") long application_id);

    /**
     * This method checks it the orchestrator is up and running.
     *
     * @return 'pong'
     */
    @GET("orchestrator/feedback/ping")
    Call<ResponseBody> pingOrchestrator();

    /**
     * This method checks it the repository is up and running.
     *
     * @return 'pong'
     */
    @GET("feedback_repository/ping")
    Call<ResponseBody> pingRepository();
}
