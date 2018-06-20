package ch.uzh.supersede.feedbacklibrary.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.nex3z.flowlayout.FlowLayout;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;

public class FeedbackIdentityActivity extends AbstractBaseActivity {
    private Map<String,String> loadedTags = new TreeMap<>();
    private List<String> createdTags = new ArrayList<>();
    private Button buttonNext;
    private Button buttonBack;
    private EditText editTitle;
    private EditText editTag;
    private FlowLayout tagContainer;
    private FlowLayout recommendationContainer;
    private LinearLayout focusSink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_identity);
        loadedTags = RepositoryStub.getFeedbackTags();
        buttonNext = getView(R.id.identity_button_next, Button.class);
        buttonBack = getView(R.id.identity_button_back, Button.class);
        tagContainer = getView(R.id.identity_container_tags, FlowLayout.class);
        recommendationContainer = getView(R.id.identity_container_recommendations, FlowLayout.class);
        colorViews(0,getView(R.id.identity_root,RelativeLayout.class));
        colorTextOnly(0,
                getView(R.id.identity_text_info_1, TextView.class),
                getView(R.id.identity_text_info_2, TextView.class),
                getView(R.id.identity_text_info_3, TextView.class),
                getView(R.id.identity_edit_tag, EditText.class),
                getView(R.id.identity_edit_title, EditText.class));
        colorViews(1, buttonBack, buttonNext);
        focusSink = getView(R.id.identity_focus_sink, LinearLayout.class);
        editTitle = getView(R.id.identity_edit_title, EditText.class);
        editTag = getView(R.id.identity_edit_tag, EditText.class);
        editTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && !"Input Feedback-Tags here...".equals(s.toString())){
                    if (s.toString().toCharArray()[s.length()-1]==' '){
                        if (s.toString().length()<4 /*TODO: KONFIGTHEMA*/ ){
                            Toast.makeText(getApplicationContext(),"Tag too short!",Toast.LENGTH_SHORT).show();
                            editTag.setText(s.toString().substring(0,s.length()-1));
                            editTag.setSelection(s.length()-1);
                        }else if (createdTags.size()==5 /*TODO: KONFIGTHEMA*/){
                            Toast.makeText(getApplicationContext(),"Maximum number of Tags! Delete some to continue!",Toast.LENGTH_SHORT).show();
                        }else{
                            addTag(s.toString().substring(0,s.length()-1));
                        }
                    }else{
                        fullTextSearch(s);
                    }
                }else{
                    recommendationContainer.removeAllViews();
                }
            }
        });
        editTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && "Input Feedback-Tags here...".equals(editTag.getText().toString())){
                    editTag.setText(null);
                }
            }
        });
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && "Input Feedback-Title here...".equals(editTitle.getText().toString())){
                    editTitle.setText(null);
                }
            }
        });
        focusSink.requestFocus();
    }

    private void fullTextSearch(Editable s) {
        recommendationContainer.removeAllViews();
        if (s == null || s.length() == 0){
            return;
        }
        String search = s.toString().toLowerCase();
        int found = 0;
        ArrayList<String> findings = new ArrayList<>();
        ArrayList<String> ignores = new ArrayList<>();
        for (Map.Entry<String, String> tag : loadedTags.entrySet()){
            if (tag.getKey().startsWith(search)){
                findings.add(tag.getValue());
                ignores.add(tag.getKey());
                found++;
            }
            if (found == 4){
                break;
            }
        }
        for (Map.Entry<String, String> tag : loadedTags.entrySet()){
            if (tag.getKey().contains(search) && !ignores.contains(tag.getKey())){
                findings.add(tag.getValue());
                found++;
            }
            if (found == 4){
                break;
            }
        }
        for (String matchingTag : findings){
            Button b = new Button(this);
            b.setText(matchingTag);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTag(((Button)v).getText().toString());
                }
            });
            recommendationContainer.addView(b);
        }
    }

    private void addTag(String s) {
        createdTags.add(s.toString());
        Button b = new Button(this);
        b.setText(s.toString());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTag.setText(((Button)v).getText().toString());
                editTag.setSelection(((Button)v).getText().length());
                tagContainer.removeView(v);
            }
        });
        tagContainer.addView(b);
        editTag.setText(null);
        recommendationContainer.removeAllViews();
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == buttonBack.getId()){
            onBackPressed();
        }else if (view.getId() == buttonNext.getId()){
            startActivity(this, FeedbackActivity.class);
        }
    }
}
