package ch.uzh.supersede.feedbacklibrary.api;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateRequest;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * API calls to the feedback repository
 * Repository: baseURL/feedback_repository (http://docs.supersederepositoryapi.apiary.io/#reference)
 */
public interface IFeedbackAPI {

    @POST("feedback_repository/authenticate")
    Call<AuthenticateResponse> authenticate(@Body AuthenticateRequest body);

    @Multipart
    @POST("feedback_repository/{language}/applications/{applicationId}/feedbacks")
    Call<Feedback> createFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Part MultipartBody.Part feedback, @Part List<MultipartBody.Part> files);

    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}")
    Call<Feedback> getFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, long feedbackId);

    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks")
    Call<List<Feedback>> getFeedbackList(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId);

    @POST("feedback_repository/{language}/applications/{applicationId}/android_users")
    Call<AndroidUser> createUser(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Body AndroidUser body);

    @GET("feedback_repository/{language}/applications/{applicationId}/android_users?user={user}")
    Call<AndroidUser> getUser(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("user") String user);

    @GET("feedback_repository/ping")
    Call<ResponseBody> pingRepository();
}
