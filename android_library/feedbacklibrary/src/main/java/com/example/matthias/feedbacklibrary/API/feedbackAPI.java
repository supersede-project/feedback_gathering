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
 * Created by Matthias on 28.03.2016.
 */
public interface feedbackAPI {
    // test servlets

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating.json
    @GET("FeedbackConfiguration/text_rating.json")
    Call<List<FeedbackConfigurationItem>> getTextRatingConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating_order.json
    @GET("FeedbackConfiguration/text_rating_order.json")
    Call<List<FeedbackConfigurationItem>> getTextRatingOrderConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_sc_rating.json
    @GET("FeedbackConfiguration/text_sc_rating.json")
    Call<List<FeedbackConfigurationItem>> getTextSCRatingConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_audio_sc_rating.json
    @GET("FeedbackConfiguration/text_audio_sc_rating.json")
    Call<List<FeedbackConfigurationItem>> getTextAudioSCRatingConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/rating_text.json
    @GET("FeedbackConfiguration/rating_text.json")
    Call<List<FeedbackConfigurationItem>> getRatingTextConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackRepository/FeedbackRepositoryFile
    @POST("FeedbackRepository/FeedbackRepositoryFile")
    Call<JsonObject> createFeedbackTest(@Body Feedback feedback);


    // actual backend

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_orchestrator/example/configuration
    @GET("feedback_orchestrator/example/configuration")
    Call<List<FeedbackConfigurationItem>> getConfiguration();

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedback(@Body Feedback feedback);

    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/example/feedback
    @Multipart
    @POST("feedback_repository/example/feedback")
    Call<JsonObject> createFeedbackMultipart(@Part("file") RequestBody file, @Part("json") RequestBody feedback);
}
