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
import com.example.matthias.feedbacklibrary.feedbacks.Feedback;
import com.example.matthias.feedbacklibrary.models.FeedbackConfiguration;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;
import com.example.matthias.feedbacklibrary.views.MechanismView;
import com.example.matthias.feedbacklibrary.views.RatingMechanismView;
import com.example.matthias.feedbacklibrary.views.TextMechanismView;
import com.google.gson.JsonObject;

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

    // List of feedback configuration items fetched from the orchestrator
    private List<FeedbackConfigurationItem> configuration;
    // Feedback configuration initialized from the previously fetched feedback configuration items
    private FeedbackConfiguration feedbackConfiguration;
    // All mechanisms (including inactive ones) --> models
    private List<Mechanism> allMechanisms;
    // All views representing active mechanisms --> view
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
        result = fbAPI.getConfiguration();

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
            allMechanisms = feedbackConfiguration.getAllMechanisms();
        }
    }

    /**
     * Initialize the view
     */
    public void initView() {
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

        // After successfully loading the model data and view, make the progress dialog disappear
        progressDialog.dismiss();
    }

    /*
     * User sends the feedback via a POST request to the feedback repository
     */
    public void sendButtonClicked(View view) {
        // The mechanism models are updated with the view values
        for(MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if(validateInput(allMechanisms, messages)) {
            Feedback feedback = new Feedback(allMechanisms);

            Call<JsonObject> result = null;
            if (feedback != null) {
                feedback.setApplication(feedbackConfiguration.getApplication());
                feedback.setUser(feedbackConfiguration.getUser());
                feedback.setConfigVersion(feedbackConfiguration.getConfigVersion());
                result = fbAPI.createFeedback(feedback);
                // result = fbAPI.createFeedbackTest(feedback);
            }

            if (result != null) {
                result.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 201) {
                            messages.add("Your feedback was successfully sent. Thank you");
                            DataDialog d = DataDialog.newInstance(messages);
                            d.show(getFragmentManager(), "dataDialog");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        messages.add("Oops. Something went wrong!");
                        DataDialog d = DataDialog.newInstance(messages);
                        d.show(getFragmentManager(), "dataDialog");
                    }
                });
            }
        } else {
            DataDialog d = DataDialog.newInstance(messages);
            d.show(getFragmentManager(), "dataDialog");
        }
    }

    // Move later on into a helper/utils class
    private boolean validateInput(List<Mechanism> allMechanisms, List<String> errorMessages) {
        boolean isValid = true;
        for(Mechanism mechanism : allMechanisms) {
            if(mechanism.getType().equals("TEXT_TYPE")) {
                int length = ((TextMechanism) mechanism).getInputText().length();
                int maxLength = ((TextMechanism) mechanism).getMaxLength();
                if(length > maxLength) {
                    isValid = false;
                    errorMessages.add("Text has " + length + " characters. Maximum allowed characters are " + maxLength);
                }
            }
        }

        return isValid;
    }

    // Move later on into a helper/utils class
    public static class DataDialog extends DialogFragment {
        static DataDialog newInstance(ArrayList<String> messages) {
            DataDialog f = new DataDialog();
            Bundle args = new Bundle();
            args.putStringArrayList("messages", messages);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            List<String> messages = getArguments().getStringArrayList("messages");
            StringBuilder message = new StringBuilder("");
            for(String s : messages) {
                message.append(s).append(".");
            }
            builder.setMessage(message.toString()).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }
}