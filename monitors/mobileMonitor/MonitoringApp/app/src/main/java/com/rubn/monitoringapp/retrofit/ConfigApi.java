package com.rubn.monitoringapp.retrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Rubén on 04/10/2016.
 */

public interface ConfigApi {

    @POST("monitor/registerid")
    Call<JSONObject> registerId(@Body JSONObject id);

    @POST("monitor/kafka")
    Call<JSONObject> sendDataToKafka(@Body JSONObject id);
}
