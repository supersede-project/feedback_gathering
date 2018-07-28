package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.*;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsDeveloperActivity extends AbstractFeedbackDetailsActivity {
    private Button deleteButton;
    private Button awardKarmaButton;

    @Override
    protected void initButtons() {
        setMode(READING);
        setCallback(this);
        setResponseLayout(getView(R.id.details_developer_layout_scroll_layout, LinearLayout.class));
        setScrollContainer(getView(R.id.details_developer_layout_scroll_container, ScrollView.class));
        setVotesText(getView(R.id.details_developer_text_votes, TextView.class));
        setUserText(getView(R.id.details_developer_text_user, TextView.class));
        setStatus(getView(R.id.details_developer_spinner_status, Spinner.class));
        setTitleText(getView(R.id.details_developer_text_title, TextView.class));
        setDescriptionText(getView(R.id.details_developer_text_description, TextView.class));
        setImageButton(getView(R.id.details_developer_button_images, Button.class));
        setAudioButton(getView(R.id.details_developer_button_audio, Button.class));
        setTagButton(getView(R.id.details_developer_button_tags, Button.class));
        setSubscribeButton(getView(R.id.details_developer_button_subscribe, Button.class));
        setResponseButton(getView(R.id.details_developer_button_response, Button.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details_developer);

        deleteButton = getView(R.id.details_developer_button_delete, Button.class);
        awardKarmaButton = getView(R.id.details_developer_button_award_karma, Button.class);

        onPostCreate();
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
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this, getString(R.string.details_developer_deleted), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        Intent intent = new Intent(getApplicationContext(), FeedbackListActivity.class);
                        intent.putExtra(EXTRA_KEY_FEEDBACK_DELETION, true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(FeedbackDetailsDeveloperActivity.this, FeedbackListActivity.class, true, intent);
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

