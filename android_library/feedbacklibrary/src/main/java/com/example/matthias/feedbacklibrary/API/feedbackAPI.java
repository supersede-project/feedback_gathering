package com.example.matthias.feedbacklibrary.API;

import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Matthias on 28.03.2016.
 */
public interface feedbackAPI {
    //http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating.json
    //http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating_order.json
    //http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_sc_rating.json
    //http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/rating_text.json

    @GET("FeedbackConfiguration/text_rating.json")
    Call<List<FeedbackConfigurationItem>> getTextRatingConfiguration();

    @GET("FeedbackConfiguration/text_rating_order.json")
    Call<List<FeedbackConfigurationItem>> getTextRatingOrderConfiguration();

    @GET("FeedbackConfiguration/text_sc_rating.json")
    Call<List<FeedbackConfigurationItem>> getTextSCRatingConfiguration();

    @GET("FeedbackConfiguration/rating_text.json")
    Call<List<FeedbackConfigurationItem>> getRatingTextConfiguration();
}
