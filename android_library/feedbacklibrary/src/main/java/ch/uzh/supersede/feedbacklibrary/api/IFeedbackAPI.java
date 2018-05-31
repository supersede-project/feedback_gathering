package ch.uzh.supersede.feedbacklibrary.api;

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
 * API calls to the feedback repository
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
    @POST("feedback_repository/{language}/applications/{applicationId}/feedbacks")
    Call<JsonObject> createFeedbackVariant(@Path("language") String language, @Path("applicationId") long applicationId, @Part MultipartBody.Part feedback, @Part List<MultipartBody.Part> files);
}
