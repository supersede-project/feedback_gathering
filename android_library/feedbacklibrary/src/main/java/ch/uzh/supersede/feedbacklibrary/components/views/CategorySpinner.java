package ch.uzh.supersede.feedbacklibrary.components.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;


/**
 * Custom spinner class for spinner with single and multiple selection
 */

public class CategorySpinner extends AppCompatSpinner implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    // General
    private int checkedIndex = -1;
    private boolean isMultipleChoice;
    private String[] items = null;
    private boolean[] mSelection = null;
    private boolean[] mSelectionAtStart = null;
    private ArrayAdapter<String> simpleAdapter;
    // Own category
    private boolean ownCategoryAllowed;

    public CategorySpinner(Context context) {
        super(context);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public CategorySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public CategorySpinner(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public boolean isMultipleChoice() {
        return isMultipleChoice;
    }

    public void setMultipleChoice(boolean isMultipleChoice) {
        this.isMultipleChoice = isMultipleChoice;
    }

    public boolean isOwnCategoryAllowed() {
        return ownCategoryAllowed;
    }

    public void setOwnCategoryAllowed(boolean ownCategoryAllowed) {
        this.ownCategoryAllowed = ownCategoryAllowed;
    }

    @NonNull
    private String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(items[i]);
            }
        }
        return sb.toString();
    }

    /**
     * This method returns the strings of the selected items.
     *
     * @return the list of strings
     */
    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(items[i]);
            }
        }
        return selection;
    }

    private TextInputEditText createOwnCategoryDialog(LinearLayout linearLayout) {
        final TextInputLayout layout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_input_layout);
        final TextInputEditText editText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_text);

        layout.setHintEnabled(true);
        layout.setHint(getResources().getString(R.string.category_own_category_hint));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //nop
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    layout.setHint(getResources().getString(R.string.category_own_category));
                } else if (s.length() == 0) {
                    layout.setHint(getResources().getString(R.string.category_own_category_hint));
                }
            }
        });

        return editText;
    }

    @Override
    public void onClick(DialogInterface dialog, final int position, boolean isChecked) {
        if ((position >= items.length) || !(position == items.length - 1 && isOwnCategoryAllowed())) {
            return;
        }

        final AlertDialog spinnerDialog = (AlertDialog) dialog;
        // Others was clicked
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        // Inflating the custom layout
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.utility_category_spinner, null);
        final TextInputEditText ownCategoryDialogInputEditText = createOwnCategoryDialog(linearLayout);
        final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_empty_layout);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ownCategoryDialogInputEditText.getText().toString().length() > 0) {
                    updateCategoryList(ownCategoryDialogInputEditText.getText().toString());
                    ownCategoryDialogInputEditText.setText(null);
                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }

                    simpleAdapter.clear();
                    simpleAdapter.add(getSelectedItemsAsString());
                    System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);

                    spinnerDialog.dismiss();
                } else {
                    mSelection[position] = false;
                    spinnerDialog.getListView().setItemChecked(position, false);
                    ownCategoryDialogInputEditText.setText(null);
                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                }
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelection[position] = false;
                spinnerDialog.getListView().setItemChecked(position, false);
                ownCategoryDialogInputEditText.setText(null);
                if (emptyLayout != null) {
                    emptyLayout.requestFocus();
                }
            }
        });
        alertDialog.setView(linearLayout);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, final int position) {
        final AlertDialog spinnerDialog = (AlertDialog) dialog;
        if (position >= items.length) {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }

        if (!(position == items.length - 1 && isOwnCategoryAllowed())) {
            if (checkedIndex != -1 && checkedIndex != position) {
                mSelection[checkedIndex] = false;
            }
            checkedIndex = position;
            mSelection[position] = true;
            simpleAdapter.clear();
            simpleAdapter.add(items[position]);
            spinnerDialog.dismiss();
            return;
        }

        // Other was clicked
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        // Inflating the custom layout
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.utility_category_spinner, null);
        final TextInputEditText ownCategoryDialogInputEditText = createOwnCategoryDialog(linearLayout);
        final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_empty_layout);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ownCategoryDialogInputEditText.getText().toString().length() > 0) {
                    updateCategoryList(ownCategoryDialogInputEditText.getText().toString());

                    if (checkedIndex != -1 && checkedIndex != position) {
                        mSelection[checkedIndex] = false;
                    }
                    checkedIndex = position;
                    mSelection[position] = true;
                    simpleAdapter.clear();
                    simpleAdapter.add(items[position]);

                    ownCategoryDialogInputEditText.setText(null);

                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                    spinnerDialog.dismiss();
                } else if (!(mSelection.length - 1 > position || mSelection.length - 1 > checkedIndex)) {
                    mSelection[position] = false;
                    mSelection[checkedIndex] = true;
                    spinnerDialog.getListView().setItemChecked(position, false);
                    spinnerDialog.getListView().setItemChecked(checkedIndex, true);

                    ownCategoryDialogInputEditText.setText(null);

                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                }
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mSelection.length >= Math.max(position, checkedIndex) + 1) {
                    mSelection[position] = false;
                    mSelection[checkedIndex] = true;
                    spinnerDialog.getListView().setItemChecked(position, false);
                    spinnerDialog.getListView().setItemChecked(checkedIndex, true);

                    ownCategoryDialogInputEditText.setText(null);

                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                }
            }
        });
        alertDialog.setView(linearLayout);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        if (isMultipleChoice()) {
            builder.setTitle(getResources().getString(R.string.category_multiple_options));
            builder.setMultiChoiceItems(items, mSelection, this);
            final String itemsAtStart = getSelectedItemsAsString();
            builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAdapter.clear();
                    simpleAdapter.add(getSelectedItemsAsString());
                    System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAdapter.clear();
                    simpleAdapter.add(itemsAtStart);
                    System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
                }
            });
        } else {
            builder.setTitle(getResources().getString(R.string.category_single_option));
            builder.setSingleChoiceItems(items, checkedIndex, this);
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //nop
                }
            });
        }
        builder.show();
        return super.performClick();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported in CustomSpinner!");
    }

    /**
     * This method sets the items for the selection.
     *
     * @param items                 the items to select
     * @param firstDefaultSelection if the first item should be selected by default
     */
    public void setItems(List<String> items, boolean firstDefaultSelection) {
        this.items = items.toArray(new String[items.size()]);
        mSelection = new boolean[this.items.length];
        mSelectionAtStart = new boolean[this.items.length];
        simpleAdapter.clear();
        if (firstDefaultSelection && this.items.length > 0) {
            simpleAdapter.add("My feedback is about…");
            checkedIndex = -1;
        }
        Arrays.fill(mSelection, false);
        mSelection[0] = firstDefaultSelection;
        mSelectionAtStart[0] = firstDefaultSelection;
    }

    @Override
    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
            mSelectionAtStart[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index + " is out of bounds.");
        }
        simpleAdapter.clear();
        simpleAdapter.add(getSelectedItemsAsString());
    }

    private void updateCategoryList(String newCategory) {
        // Update items
        List<String> newItems = new ArrayList<>();
        Collections.addAll(newItems, items);
        newItems.add(newItems.size() - 1, newCategory);
        items = newItems.toArray(new String[newItems.size()]);

        // Update mSelection
        boolean[] newMSelection = new boolean[items.length];
        System.arraycopy(mSelection, 0, newMSelection, 0, mSelection.length);
        newMSelection[newMSelection.length - 1] = false;
        mSelection = newMSelection;

        // Update mSelectionAtStart
        boolean[] newMSelectionAtStart = new boolean[items.length];
        System.arraycopy(mSelectionAtStart, 0, newMSelectionAtStart, 0, mSelectionAtStart.length);
        newMSelectionAtStart[newMSelectionAtStart.length - 1] = false;
        mSelectionAtStart = newMSelectionAtStart;
    }
}
