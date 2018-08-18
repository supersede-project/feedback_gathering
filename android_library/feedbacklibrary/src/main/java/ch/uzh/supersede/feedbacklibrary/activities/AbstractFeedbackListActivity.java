package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CompoundButtonCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.components.views.SpacerListItem;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_FEEDBACK_DELETION;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractFeedbackListActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {

    protected EditText searchText;
    protected LinearLayout scrollListLayout;
    private LinearLayout focusSink;
    private ScrollView scrollView;
    private final CheckBox[] filterCheckBoxArray = new CheckBox[Enums.FEEDBACK_STATUS.values().length];
    private List<Enums.FEEDBACK_STATUS> allowedStatuses = new ArrayList<>();
    private Map<Enums.FEEDBACK_SORTING, Button> buttons = new EnumMap<>(Enums.FEEDBACK_SORTING.class);
    private boolean returnedFromDeletion = false;
    private Enums.FEEDBACK_SORTING sorting;

    protected abstract void doSearch(String s);

    protected abstract void handleButtonToggle(View v);

    protected abstract List<FeedbackListItem> getActiveList();

    public List<Enums.FEEDBACK_STATUS> getAllowedStatuses() {
        return allowedStatuses;
    }

    public EditText getSearchText() {
        return searchText;
    }

    public void setSearchText(EditText searchText) {
        this.searchText = searchText;
    }

    public Map<Enums.FEEDBACK_SORTING, Button> getButtons() {
        return buttons;
    }

    public void setFocusSink(LinearLayout focusSink) {
        this.focusSink = focusSink;
    }

    public boolean isReturnedFromDeletion() {
        return returnedFromDeletion;
    }

    public void setScrollListLayout(LinearLayout scrollListLayout) {
        this.scrollListLayout = scrollListLayout;
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public void setSorting(Enums.FEEDBACK_SORTING sorting) {
        this.sorting = sorting;
    }

    public Enums.FEEDBACK_SORTING getSorting() {
        return sorting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnedFromDeletion = getIntent().getBooleanExtra(EXTRA_KEY_FEEDBACK_DELETION, false);
        Collections.addAll(allowedStatuses, Enums.FEEDBACK_STATUS.values());
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
    }

    protected void load() {
        scrollListLayout.removeAllViews();
        scrollView.scrollTo(0, 0);
        for (LinearLayout item : getActiveList()) {
            scrollListLayout.addView(item);
        }
    }

    protected void sort() {
        for (FeedbackListItem item : getActiveList()) {
            item.setSorting(sorting, getAllowedStatuses());
        }
        Collections.sort(getActiveList());
        load();
    }

    protected void setInactive(Button... buttons) {
        colorShape(0, buttons);
    }

    protected Button setOnClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtons(v);
            }
        });
        return button;
    }

    protected void toggleButtons(View v) {
        setInactive((getButtons().values()).toArray(new Button[0]));
        colorShape(1, v);

        handleButtonToggle(v);

        //handle focus and keyboard
        focusSink.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusSink.getWindowToken(), 0);
        }
    }

    protected EditText addTextChangedListener(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && searchText.getText() != null && searchText.getText().toString().equals(getString(R.string.list_edit_search))) {
                    searchText.setText(null);
                } else if (!hasFocus && !StringUtility.hasText(searchText.getText().toString())) {
                    searchText.setText(getString(R.string.list_edit_search));
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nop
            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(searchText.getText().toString());
            }
        });
        return editText;
    }

    protected void openFilteringOptions() {
        LinearLayout borderLayout = new LinearLayout(this);
        LinearLayout wrapperLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = 15;
        params.setMargins(margin, margin, margin, margin);
        wrapperLayout.setOrientation(LinearLayout.VERTICAL);
        wrapperLayout.setLayoutParams(params);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for (int s = 0; s < Enums.FEEDBACK_STATUS.values().length; s++) {
            Enums.FEEDBACK_STATUS status = Enums.FEEDBACK_STATUS.values()[s];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(status.getLabel());
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(getTopColor(1)));
            checkBox.setTextColor(ColorUtility.adjustColorToBackground(getTopColor(0), status.getColor(), 0.4));
            for (Enums.FEEDBACK_STATUS statusAllowed : allowedStatuses) {
                if (statusAllowed == status) {
                    checkBox.setChecked(true);
                }
            }
            filterCheckBoxArray[s] = checkBox;
            wrapperLayout.addView(checkBox);
        }
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        borderLayout.setBackgroundColor(getTopColor(1));
        wrapperLayout.setBackgroundColor(getTopColor(0));
        borderLayout.addView(wrapperLayout);
        builder.setView(borderLayout);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                allowedStatuses.clear();
                for (int s = 0; s < Enums.FEEDBACK_STATUS.values().length; s++) {
                    Enums.FEEDBACK_STATUS status = Enums.FEEDBACK_STATUS.values()[s];
                    if (filterCheckBoxArray[s].isChecked()) {
                        allowedStatuses.add(status);
                    }
                }
                sort();
            }
        });
        AlertDialog alertDialog = builder.show();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ColorUtility.adjustColorToBackground(getResources().getColor(R.color.white), getTopColor(1), 0.3));
    }
}
