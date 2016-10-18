package com.example.matthias.feedbacklibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.matthias.feedbacklibrary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom spinner class for spinner with single and multiple selection
 */
public class CustomSpinner extends Spinner implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    // General
    private int checkedIndex = -1;
    private boolean isMultiple;
    private String[] items = null;
    private String itemsAtStart = null;
    private boolean[] mSelection = null;
    private boolean[] mSelectionAtStart = null;
    private OnMultipleItemsSelectedListener listener;
    private ArrayAdapter<String> simpleAdapter;
    // Own category
    private boolean ownCategoryAllowed;

    public CustomSpinner(Context context) {
        super(context);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public CustomSpinner(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    /**
     * This method returns the indices of the selected items.
     *
     * @return the list of indices
     */
    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    /**
     * This method returns the selected items as a string.
     *
     * @return the concatenated string
     */
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

    /**
     * This method returns if the spinner is of single or multiple choice type.
     *
     * @return true if multiple choice, false otherwise
     */
    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * This method returns if creating an own category is allowed.
     *
     * @return true if allowed, false otherwise
     */
    public boolean isOwnCategoryAllowed() {
        return ownCategoryAllowed;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        final AlertDialog spinnerDialog = (AlertDialog) dialog;
        final int spinnerWhich = which;
        if (spinnerWhich < items.length) {
            if (spinnerWhich == items.length - 1 && isOwnCategoryAllowed()) {
                // Others was clicked
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                // Inflating the custom layout
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.own_category_dialog_layout, null);

                final TextInputLayout ownCategoryDialogInputLayout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_input_layout);
                final TextInputEditText ownCategoryDialogInputEditText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_text);

                // Set the hint and enable it
                ownCategoryDialogInputLayout.setHintEnabled(true);
                ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_hint));
                ownCategoryDialogInputEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0) {
                            ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_label));
                        } else if (s.length() == 0) {
                            ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_hint));
                        }
                    }
                });

                final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_empty_layout);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton(getResources().getString(R.string.supersede_feedbacklibrary_ok_string), new DialogInterface.OnClickListener() {
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
                            listener.selectedIndices(getSelectedIndices());
                            listener.selectedStrings(getSelectedStrings());

                            spinnerDialog.dismiss();
                        } else {
                            mSelection[spinnerWhich] = false;
                            spinnerDialog.getListView().setItemChecked(spinnerWhich, false);
                            ownCategoryDialogInputEditText.setText(null);
                            if (emptyLayout != null) {
                                emptyLayout.requestFocus();
                            }
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_cancel_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelection[spinnerWhich] = false;
                        spinnerDialog.getListView().setItemChecked(spinnerWhich, false);
                        ownCategoryDialogInputEditText.setText(null);
                        if (emptyLayout != null) {
                            emptyLayout.requestFocus();
                        }
                    }
                });
                builder.setView(linearLayout);
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        final AlertDialog spinnerDialog = (AlertDialog) dialog;
        final int spinnerWhich = which;
        if (spinnerWhich < items.length) {
            if (spinnerWhich == items.length - 1 && isOwnCategoryAllowed()) {
                // Other was clicked
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                // Inflating the custom layout
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.own_category_dialog_layout, null);

                final TextInputLayout ownCategoryDialogInputLayout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_input_layout);
                final TextInputEditText ownCategoryDialogInputEditText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_text);

                // Set the hint and enable it
                ownCategoryDialogInputLayout.setHintEnabled(true);
                ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_hint));
                ownCategoryDialogInputEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0) {
                            ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_label));
                        } else if (s.length() == 0) {
                            ownCategoryDialogInputLayout.setHint(getResources().getString(R.string.supersede_feedbacklibrary_own_category_dialog_hint));
                        }
                    }
                });

                final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_own_category_dialog_empty_layout);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton(getResources().getString(R.string.supersede_feedbacklibrary_ok_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ownCategoryDialogInputEditText.getText().toString().length() > 0) {
                            updateCategoryList(ownCategoryDialogInputEditText.getText().toString());

                            if (checkedIndex != -1 && checkedIndex != spinnerWhich) {
                                mSelection[checkedIndex] = false;
                            }
                            checkedIndex = spinnerWhich;
                            mSelection[spinnerWhich] = true;
                            simpleAdapter.clear();
                            simpleAdapter.add(items[spinnerWhich]);

                            ownCategoryDialogInputEditText.setText(null);

                            if (emptyLayout != null) {
                                emptyLayout.requestFocus();
                            }
                            spinnerDialog.dismiss();
                        } else {
                            mSelection[spinnerWhich] = false;
                            mSelection[checkedIndex] = true;
                            spinnerDialog.getListView().setItemChecked(spinnerWhich, false);
                            spinnerDialog.getListView().setItemChecked(checkedIndex, true);

                            ownCategoryDialogInputEditText.setText(null);

                            if (emptyLayout != null) {
                                emptyLayout.requestFocus();
                            }
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_cancel_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelection[spinnerWhich] = false;
                        mSelection[checkedIndex] = true;
                        spinnerDialog.getListView().setItemChecked(spinnerWhich, false);
                        spinnerDialog.getListView().setItemChecked(checkedIndex, true);

                        ownCategoryDialogInputEditText.setText(null);

                        if (emptyLayout != null) {
                            emptyLayout.requestFocus();
                        }
                    }
                });
                builder.setView(linearLayout);
                builder.setCancelable(false);
                builder.show();
            } else {
                if (checkedIndex != -1 && checkedIndex != spinnerWhich) {
                    mSelection[checkedIndex] = false;
                }
                checkedIndex = spinnerWhich;
                mSelection[spinnerWhich] = true;
                simpleAdapter.clear();
                simpleAdapter.add(items[spinnerWhich]);
                spinnerDialog.dismiss();
            }
        } else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        if (isMultiple()) {
            builder.setTitle(getResources().getString(R.string.supersede_feedbacklibrary_multiple_options_dialog_title));
            builder.setMultiChoiceItems(items, mSelection, this);
            itemsAtStart = getSelectedItemsAsString();
            builder.setPositiveButton(R.string.supersede_feedbacklibrary_yes_string, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAdapter.clear();
                    simpleAdapter.add(getSelectedItemsAsString());
                    System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
                    listener.selectedIndices(getSelectedIndices());
                    listener.selectedStrings(getSelectedStrings());
                }
            });
            builder.setNegativeButton(R.string.supersede_feedbacklibrary_cancel_string, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAdapter.clear();
                    simpleAdapter.add(itemsAtStart);
                    System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
                }
            });
        } else {
            builder.setTitle(getResources().getString(R.string.supersede_feedbacklibrary_single_option_dialog_title));
            builder.setSingleChoiceItems(items, checkedIndex, this);
            builder.setNegativeButton(R.string.supersede_feedbacklibrary_cancel_string, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by CustomSpinner.");
    }

    /**
     * This method sets the items for the selection.
     *
     * @param items                 the items to select
     * @param firstDefaultSelection the firstDefaultSelection
     */
    public void setItems(String[] items, boolean firstDefaultSelection) {
        this.items = items;
        mSelection = new boolean[this.items.length];
        mSelectionAtStart = new boolean[this.items.length];
        simpleAdapter.clear();
        if (firstDefaultSelection && this.items.length > 0) {
            simpleAdapter.add(this.items[0]);
            checkedIndex = 0;
        }
        Arrays.fill(mSelection, false);
        mSelection[0] = firstDefaultSelection;
        mSelectionAtStart[0] = firstDefaultSelection;
    }

    /**
     * This method sets the items for the selection.
     *
     * @param items                 the items to select
     * @param firstDefaultSelection the firstDefaultSelection
     */
    public void setItems(List<String> items, boolean firstDefaultSelection) {
        this.items = items.toArray(new String[items.size()]);
        mSelection = new boolean[this.items.length];
        mSelectionAtStart = new boolean[this.items.length];
        simpleAdapter.clear();
        if (firstDefaultSelection && this.items.length > 0) {
            simpleAdapter.add(this.items[0]);
            checkedIndex = 0;
        }
        Arrays.fill(mSelection, false);
        mSelection[0] = firstDefaultSelection;
        mSelectionAtStart[0] = firstDefaultSelection;
    }

    /**
     * This method sets the listener.
     *
     * @param listener the listener
     */
    public void setListener(OnMultipleItemsSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * This method sets if the spinner is of single or multiple choice type.
     *
     * @param multiple the multiple
     */
    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    /**
     * This method sets if own categories are allowed.
     *
     * @param ownCategoryAllowed the ownCategoryAllowed
     */
    public void setOwnCategoryAllowed(boolean ownCategoryAllowed) {
        this.ownCategoryAllowed = ownCategoryAllowed;
    }

    /**
     * This method sets the selection manually.
     *
     * @param selection the selection
     */
    public void setSelection(String[] selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String cell : selection) {
            for (int j = 0; j < items.length; ++j) {
                if (items[j].equals(cell)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simpleAdapter.clear();
        simpleAdapter.add(getSelectedItemsAsString());
    }

    /**
     * This method sets the selection manually.
     *
     * @param selection the selection
     */
    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < items.length; ++j) {
                if (items[j].equals(sel)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simpleAdapter.clear();
        simpleAdapter.add(getSelectedItemsAsString());
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
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simpleAdapter.clear();
        simpleAdapter.add(getSelectedItemsAsString());
    }

    /**
     * This method sets the selected indices manually.
     *
     * @param selectedIndices the selectedIndices
     */
    public void setSelection(int[] selectedIndices) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
                mSelectionAtStart[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simpleAdapter.clear();
        simpleAdapter.add(getSelectedItemsAsString());
    }

    /**
     * This method updates the category list after the user created an own category.
     *
     * @param newCategory the new category
     */
    private void updateCategoryList(String newCategory) {
        // Update items
        List<String> newItems = new ArrayList<>();
        for (int i = 0; i < items.length; ++i) {
            newItems.add(items[i]);
        }
        newItems.add(newItems.size() - 1, newCategory);
        items = newItems.toArray(new String[newItems.size()]);
        // Update mSelection
        boolean[] newMSelection = new boolean[items.length];
        for (int i = 0; i < items.length - 1; ++i) {
            newMSelection[i] = mSelection[i];
        }
        newMSelection[newMSelection.length - 1] = false;
        mSelection = newMSelection;
        // Update mSelectionAtStart
        boolean[] newMSelectionAtStart = new boolean[items.length];
        for (int i = 0; i < items.length - 1; ++i) {
            newMSelectionAtStart[i] = mSelectionAtStart[i];
        }
        newMSelectionAtStart[newMSelectionAtStart.length - 1] = false;
        mSelectionAtStart = newMSelectionAtStart;
    }

    public interface OnMultipleItemsSelectedListener {
        void selectedIndices(List<Integer> indices);

        void selectedStrings(List<String> strings);
    }
}
