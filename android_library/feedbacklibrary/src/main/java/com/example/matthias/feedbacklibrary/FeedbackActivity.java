package com.example.matthias.feedbacklibrary;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.example.matthias.feedbacklibrary.models.FeedbackContainer;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

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
    private FeedbackContainer allFeedback;

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

        // Only for demo purposes, to show different configurations
        requestURL = getIntent().getStringExtra("requestURL");

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
            allFeedback = new FeedbackContainer();
            for(FeedbackConfigurationItem item : configuration) {
                if(item.getType().equals("TEXT_TYPE")) {
                    allFeedback.addFeedback(createText(item));
                } else if(item.getType().equals("RATING_TYPE")) {
                    allFeedback.addFeedback(createRating(item));
                } else if(item.getType().equals("AUDIO_TYPE")) {
                    // TODO
                } else if(item.getType().equals("SCREENSHOT_TYPE")) {
                    // TODO
                } else {
                    // should never happen!
                }
            }
            allFeedback.sortByOrder();
        }
    }

    public void initView() {
        List<Mechanism> all = allFeedback.getAllFeedback();
        //LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedback_activity_layout);
        for(int i = 0; i < all.size(); ++i) {
            Mechanism m = all.get(i);
            if (m.isActive()) {
                View view = null;

                if (m instanceof TextMechanism) {
                    view = layoutInflater.inflate(R.layout.text_feedback_layout, null);
                    ((TextMechanism) m).setEnclosingLayout(view);
                }
                if (m instanceof RatingMechanism) {
                    view = layoutInflater.inflate(R.layout.rating_feedback_layout, null);
                    ((RatingMechanism) m).setEnclosingLayout(view);
                }
                if (m instanceof ScreenshotMechanism) {
                    // TODO
                }
                if (m instanceof AudioMechanism) {
                    // TODO
                }

                if (view != null) {
                    linearLayout.addView(view);
                }
            }
        }
        View view = layoutInflater.inflate(R.layout.send_feedback_layout, null);
        if (view != null) {
            linearLayout.addView(view);
        }

        allFeedback.updateView();
        progressDialog.dismiss();
    }

    public void sendButtonClicked(View view) {
        allFeedback.updateModel();
        List<Mechanism> all = allFeedback.getAllFeedback();
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < all.size(); ++i) {
            Mechanism m = all.get(i);
            if (m.isActive()) {
                if (m instanceof TextMechanism) {
                    str.append("Input text sent == ").append(((TextMechanism) m).getInputText()).append("\n");
                }
                if (m instanceof RatingMechanism) {
                    str.append("Input rating sent == ").append(((RatingMechanism) m).getInputRating()).append("\n");
                }
                if (m instanceof ScreenshotMechanism) {
                    // TODO
                }
                if (m instanceof AudioMechanism) {
                    // TODO
                }
            }
        }
        DataDialog d = DataDialog.newInstance(str.toString());
        d.show(getFragmentManager(), "dataDialog");
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

    // TODO: move to a separate class?
    private TextMechanism createText(FeedbackConfigurationItem item) {
        TextMechanism t = new TextMechanism(item.canBeActivated(), item.isActive(), item.getOrder());
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if(key.equals("title")) {
                t.setTitle((String) param.get("value"));
            }
            // Hint
            if(key.equals("hint")) {
                t.setHint((String) param.get("value"));
            }
            // Maximum length
            if(key.equals("maxLength")) {
                t.setMaxLength(((Double) param.get("value")).intValue());
            }
        }
        return t;
    }

    // TODO: move to a separate class?
    private RatingMechanism createRating(FeedbackConfigurationItem item) {
        RatingMechanism r = new RatingMechanism(item.canBeActivated(), item.isActive(), item.getOrder());
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if(key.equals("title")) {
                r.setTitle((String) param.get("value"));
            }
            // Rating icon
            if(key.equals("ratingIcon")) {
                r.setRatingIcon((String) param.get("value"));
            }
            // Maximum rating
            if(key.equals("maxRating")) {
                r.setMaxRating(((Double) param.get("value")).intValue());
            }
            // Default rating
            if(key.equals("defaultRating")) {
                r.setDefaultRating(((Double) param.get("value")).floatValue());
            }
        }
        return r;
    }
}
