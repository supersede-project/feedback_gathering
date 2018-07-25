package ch.uzh.supersede.feedbacklibrary.api;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.*;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * API calls to the feedback repository
 * Repository: baseURL/feedback_repository (http://docs.supersederepositoryapi.apiary.io/#reference)
 */
public interface IFeedbackAPI {

    @GET("feedback_repository/ping")
    Call<ResponseBody> pingRepository();

    @POST("feedback_repository/authenticate")
    Call<AuthenticateResponse> authenticate(@Body AuthenticateRequest body);

    @Multipart
    @POST("feedback_repository/{language}/applications/{applicationId}/feedbacks")
    Call<Feedback> createFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Part MultipartBody.Part feedback, @Part
            List<MultipartBody.Part> files);

    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}")
    Call<Feedback> getFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, long feedbackId);

    @DELETE("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}")
    Call<Feedback> deleteFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, long feedbackId);

    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks")
    Call<List<Feedback>> getFeedbackList(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Query("view") String view, @Query
            ("ids") String ids);

    @PUT("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}")
    Call<Feedback> editFeedback(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("feedbackId") long feedbackId, @Body Feedback body);

    @POST("feedback_repository/{language}/applications/{applicationId}/android_users")
    Call<AndroidUser> createUser(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Body AndroidUser body);

    @GET("feedback_repository/{language}/applications/{applicationId}/android_users")
    Call<AndroidUser> getUser(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Query("user") String user);

    @GET("feedback_repository/{language}/applications/{applicationId}/android_users/karma")
    Call<ResponseBody> getUserKarma(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Query("user") String user);

    @PUT("feedback_repository/{language}/applications/{applicationId}/android_users")
    Call<AndroidUser> editUser(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Query("user") String user, @Body AndroidUser body);

    @GET("feedback_repository/{language}/applications/{applicationId}/reports")
    Call<List<FeedbackReport>> getFeedbackReportList(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId);

    @POST("feedback_repository/{language}/applications/{applicationId}/reports")
    Call<FeedbackReport> createFeedbackReport(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Body FeedbackReport body);

    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}/responses")
    Call<List<FeedbackResponse>> getFeedbackResponseList(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("feedbackId")
            long feedbackId);

    @POST("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}/responses")
    Call<FeedbackResponse> createFeedbackResponse(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("feedbackId") long
            feedbackId, @Body FeedbackResponse body);

    @DELETE("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}/responses/{responseId}")
    void deleteFeedbackResponse(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("feedbackId")
            long feedbackId, @Path("responseId") long responseId);

    @GET("feedback_repository/{language}/applications/{applicationId}/tags")
    Call<List<String>> getTagList(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId);

    @PUT("feedback_repository/{language}/applications/{applicationId}/feedbacks/{feedbackId}/votes")
    Call<FeedbackVote> createVote(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("feedbackId") long feedbackId, @Body
            FeedbackVote body);

    //Retrieves a specific Screenshot/Image
    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks/screenshots/{file_name_ext}")
    Call<ResponseBody> getFeedbackImage(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("file_name_ext") String fileNameWithExtension);

    //Retrieves a specific Audiofile
    @GET("feedback_repository/{language}/applications/{applicationId}/feedbacks/audios/{file_name_ext}")
    Call<ResponseBody> getFeedbackAudio(@Header("Authorization") String token, @Path("language") String language, @Path("applicationId") long applicationId, @Path("file_name_ext") String fileNameWithExtension);

}
