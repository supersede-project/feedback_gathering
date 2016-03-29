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
    private feedbackAPI fbAPI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Retrofit rtf = new Retrofit.Builder().baseUrl(endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        fbAPI = rtf.create(feedbackAPI.class);

        // initialize the view
        init();

        // Add the views to the layout
    }

    /**
     * GET request to feedback orchestrator to receive the configuration file (format JSON)
     */
    private void init() {
        Call<List<FeedbackConfigurationItem>> result = fbAPI.getTextRatingConfiguration();

        // asynchronous call
        result.enqueue(new Callback<List<FeedbackConfigurationItem>>() {
            @Override
            public void onResponse(Call<List<FeedbackConfigurationItem>> call, Response<List<FeedbackConfigurationItem>> response) {
                System.out.println("ASYNC call WAS successful");
                List<FeedbackConfigurationItem> configuration = response.body();
                for (FeedbackConfigurationItem config : configuration) {
                    System.out.println("type == " + config.getType());
                    for(Map<String, Object> param : config.getParameters()) {
                        for(String s : param.keySet()) {
                            System.out.println("key == " + s + "  and value == " + param.get(s));
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<FeedbackConfigurationItem>> call, Throwable t) {
                System.out.println("ASYNC call WAS NOT successful");
            }
        });
    }
}
