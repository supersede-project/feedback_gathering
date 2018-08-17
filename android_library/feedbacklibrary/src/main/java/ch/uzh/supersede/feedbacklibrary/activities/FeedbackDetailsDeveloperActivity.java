package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsDeveloperActivity extends AbstractFeedbackDetailsActivity {
    private Button deleteButton;
    private Button awardKarmaButton;
    private Spinner statusSpinner;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_feedback_details_developer);
        setCallback(this);
        setResponseLayout(getView(R.id.details_developer_layout_scroll_layout, LinearLayout.class));
        setScrollContainer(getView(R.id.details_developer_layout_scroll_container, ScrollView.class));
        setVotesText(getView(R.id.details_developer_text_votes, TextView.class));
        setUserText(getView(R.id.details_developer_text_user, TextView.class));
        setTitleText(getView(R.id.details_developer_text_title, TextView.class));
        setDescriptionText(getView(R.id.details_developer_text_description, TextView.class));
        setImageButton(getView(R.id.details_developer_button_images, Button.class));
        setAudioButton(getView(R.id.details_developer_button_audio, Button.class));
        setTagButton(getView(R.id.details_developer_button_tags, Button.class));
        setSubscribeButton(getView(R.id.details_developer_button_subscribe, Button.class));
        setResponseButton(getView(R.id.details_developer_button_response, Button.class));

        deleteButton = getView(R.id.details_developer_button_delete, Button.class);
        awardKarmaButton = getView(R.id.details_developer_button_award_karma, Button.class);
        statusSpinner = getView(R.id.details_developer_spinner_status, Spinner.class);

        colorViews(1, getView(R.id.details_developer_root, ContentFrameLayout.class));
        colorViews(configuration.getLastColorIndex(), statusSpinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCallerClass(ObjectUtility.getCallerClass(getIntent()));

        initStatusSpinner();
        drawLayoutOutlines(R.id.details_developer_layout_up,R.id.details_developer_layout_mid,R.id.details_developer_layout_low,R.id.details_developer_layout_scroll_container,R.id.details_developer_layout_button);
        onPostCreate();
    }

    protected void initStatusSpinner() {
        if (getFeedbackDetailsBean() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.feedback_status_spinner_layout, Enums.getFeedbackStatusLabels());
            statusSpinner.setAdapter(adapter);
            statusSpinner.setSelection(adapter.getPosition(getFeedbackDetailsBean().getFeedbackStatus().getLabel()));
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Object feedbackStatus = ((AppCompatSpinner) parentView).getAdapter().getItem(position);
                    if (isStatusNew(feedbackStatus)) {
                        FeedbackService.getInstance(getApplicationContext()).editFeedbackStatus(getCallback(), getFeedbackDetailsBean(), (String) feedbackStatus);
                        Toast.makeText(getApplicationContext(), getString(R.string.details_developer_status_updated), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //NOP
                }
            });
        }
    }

    @Override
    public void onButtonClicked(View view) {
        super.onButtonClicked(view);

        if (view.getId() == awardKarmaButton.getId()) {
            final EditText karmaInputText = new EditText(this);
            karmaInputText.setSingleLine();
            karmaInputText.setMaxLines(1);
            karmaInputText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            karmaInputText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (karmaInputText.getText().length() > 0) {
                        Integer karma = Integer.parseInt(karmaInputText.getText().toString());
                        String userName = FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_NAME, null);
                        FeedbackService.getInstance(getApplicationContext()).createVote(getCallback(), getFeedbackDetailsBean(), karma, userName);
                        RepositoryStub.sendKarma(getFeedbackDetailsBean(), karma);
                        Toast
                                .makeText(FeedbackDetailsDeveloperActivity.this, getString(R.string.details_developer_karma_awarded, String.valueOf(karma), getFeedbackDetailsBean().getUserName()),
                                        Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this, getString(R.string.details_developer_karma_error), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_developer_award_karma_title))
                    .withInput(karmaInputText)
                    .withCustomOk("Confirm", okClickListener)
                    .withMessage(getString(R.string.details_developer_award_karma_content, getFeedbackDetailsBean().getUserName())).buildAndShow();
        } else if (view.getId() == deleteButton.getId()) {
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == Dialog.BUTTON_POSITIVE) {
                        FeedbackService.getInstance(getApplicationContext()).deleteFeedback(FeedbackDetailsDeveloperActivity.this, getFeedbackDetailsBean());
                        dialog.cancel();
                    }
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_developer_delete_confirm_title))
                    .withCustomOk("Confirm", okClickListener)
                    .withMessage(getString(R.string.details_developer_delete_confirm)).buildAndShow();
        }
        updateFeedbackState();
    }
}

