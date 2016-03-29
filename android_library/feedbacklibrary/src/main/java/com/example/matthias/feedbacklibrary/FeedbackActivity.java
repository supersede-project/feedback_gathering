package com.example.matthias.feedbacklibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity {
    private static final String endpoint = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";
    private feedbackAPI fbAPI;
    private String requestURL;
    private List<FeedbackConfigurationItem> configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Only for demo purposes, to show different configurations
        requestURL = getIntent().getStringExtra("requestURL");

        Retrofit rtf = new Retrofit.Builder().baseUrl(endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        fbAPI = rtf.create(feedbackAPI.class);

        // Get configuration file
        getConfigurationFile();

        // Initialize Models and Views
    }

    /**
     * GET request to feedback orchestrator to receive the configuration file (format JSON)
     */
    private void getConfigurationFile() {
        Call<List<FeedbackConfigurationItem>> result = null;
        if(requestURL.equals("text_rating.json")) {
            result = fbAPI.getTextRatingConfiguration();
        } else if(requestURL.equals("text_rating_order.json")) {
            result = fbAPI.getTextRatingOrderConfiguration();
        } else if(requestURL.equals("text_sc_rating.json")) {
            result = fbAPI.getTextSCRatingConfiguration();
        } else if(requestURL.equals("rating_text.json")) {
            result = fbAPI.getRatingTextConfiguration();
        }

        // asynchronous call
        if(result != null) {
            result.enqueue(new Callback<List<FeedbackConfigurationItem>>() {
                @Override
                public void onResponse(Call<List<FeedbackConfigurationItem>> call, Response<List<FeedbackConfigurationItem>> response) {
                    configuration = response.body();
/*                    for (FeedbackConfigurationItem config : configuration) {
                        System.out.println("type == " + config.getType());
                        for (Map<String, Object> param : config.getParameters()) {
                            for (String s : param.keySet()) {
                                System.out.println("key == " + s + "  and value == " + param.get(s));
                            }
                        }
                    }*/
                }

                @Override
                public void onFailure(Call<List<FeedbackConfigurationItem>> call, Throwable t) {
                    System.out.println("ASYNC call WAS NOT successful");
                }
            });
        } else {
            // should never happen!
        }
    }
}
