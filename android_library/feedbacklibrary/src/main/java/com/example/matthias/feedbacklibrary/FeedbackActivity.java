package com.example.matthias.feedbacklibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.models.FeedbackConfiguration;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.views.MechanismView;
import com.example.matthias.feedbacklibrary.views.RatingMechanismView;
import com.example.matthias.feedbacklibrary.views.TextMechanismView;

import java.util.ArrayList;
import java.util.List;

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
    private FeedbackConfiguration feedbackConfiguration;
    private List<Mechanism> allMechanisms;
    private List<MechanismView> allMechanismViews;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Make progress dialog visible
        progressDialog = new ProgressDialog((findViewById(R.id.feedback_activity_layout)).getContext());
        progressDialog.setTitle("Loading. Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit rtf = new Retrofit.Builder().baseUrl(endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        fbAPI = rtf.create(feedbackAPI.class);

        init();
    }

    /**
     * GET request to feedback orchestrator to receive the configuration file (format JSON).
     * If successful, initialize the model and view.
     */
    private void init() {
        Call<List<FeedbackConfigurationItem>> result = null;
        result = fbAPI.getTextAudioSCRatingConfiguration();

        // asynchronous call
        if(result != null) {
            result.enqueue(new Callback<List<FeedbackConfigurationItem>>() {
                @Override
                public void onResponse(Call<List<FeedbackConfigurationItem>> call, Response<List<FeedbackConfigurationItem>> response) {
                    configuration = response.body();
                    initModel();
                    initView();
                }

                @Override
                public void onFailure(Call<List<FeedbackConfigurationItem>> call, Throwable t) {
                }
            });
        } else {
            // should never happen!
        }
    }

    /**
     * Initialize the model data
     */
    private void initModel() {
        if(configuration != null) {
            feedbackConfiguration = new FeedbackConfiguration(configuration);
            // TODO: Save original configuration on the device --> probably SQLite database

        }
    }

    public void initView() {
        allMechanisms = feedbackConfiguration.getAllMechanisms();
        allMechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedback_activity_layout);

        for(int i = 0; i < allMechanisms.size(); ++i) {
            if(allMechanisms.get(i).isActive()) {
                MechanismView mechanismView = null;
                View view = null;
                String type = allMechanisms.get(i).getType();

                if (type.equals("TEXT_TYPE")) {
                    mechanismView = new TextMechanismView(layoutInflater, allMechanisms.get(i));
                    view = mechanismView.getEnclosingLayout();
                } else if (type.equals("RATING_TYPE")) {
                    mechanismView = new RatingMechanismView(layoutInflater, allMechanisms.get(i));
                    view = mechanismView.getEnclosingLayout();
                } else if (type.equals("AUDIO_TYPE")) {
                    // TODO
                } else if (type.equals("SCREENSHOT_TYPE")) {
                    // TODO
                } else {
                    // should never happen!
                }

                if (mechanismView != null && view != null) {
                    allMechanismViews.add(mechanismView);
                    linearLayout.addView(view);
                }
            }
        }

        View sendLayout = layoutInflater.inflate(R.layout.send_feedback_layout, null);
        if(sendLayout != null) {
            linearLayout.addView(sendLayout);
        }

        progressDialog.dismiss();
    }

    public void sendButtonClicked(View view) {
        for(MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
        }

        /*for(Mechanism mechanism : allMechanisms) {
            if(mechanism.getType().equals("TEXT_TYPE")) {
                System.out.println("Input text == " + ((TextMechanism) mechanism).getInputText());
            }
        }

        DataDialog d = DataDialog.newInstance("dummy");
        d.show(getFragmentManager(), "dataDialog");*/
    }

    // Only for demo purposes, to show the potential data to be sent
    public static class DataDialog extends DialogFragment {
        static DataDialog newInstance(String message) {
            DataDialog f = new DataDialog();
            Bundle args = new Bundle();
            args.putString("message", message);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getArguments().getString("message"))
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }
}