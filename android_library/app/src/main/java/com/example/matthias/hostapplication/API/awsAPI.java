package com.example.matthias.hostapplication.API;

import com.example.matthias.hostapplication.models.FeedbackConfiguration;
import com.example.matthias.hostapplication.models.FeedbackItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by Matthias on 20.03.2016.
 */
public interface awsAPI {
    public static final String toFetch = "http://ec2-54-175-37-30.compute-1.amazonaws.com/examples/api_test.json";
    // http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/FeedbackConfigTest

    @GET("examples/api_test.json")
    Call<List<FeedbackItem>> getFeedback();

    @GET("feedback_repository/FeedbackConfigTest")
    Call<List<FeedbackConfiguration>> getConfiguration();
}
