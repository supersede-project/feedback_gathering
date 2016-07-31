package com.example.matthias.feedbacklibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.matthias.feedbacklibrary.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom spinner class for spinner with single and multiple selection
 */
public class CustomSpinner extends Spinner implements OnClickListener, OnMultiChoiceClickListener {
    private int checkedIndex = -1;
    private boolean isMultiple;
    private String[] items = null;
    private String itemsAtStart = null;
    private boolean[] mSelection = null;
    private boolean[] mSelectionAtStart = null;
    private OnMultipleItemsSelectedListener listener;
    private ArrayAdapter<String> simpleAdapter;

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

    private String buildSelectedItemString() {
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

    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    public String getSelectedItemsAsString() {
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

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(items[i]);
            }
        }
        return selection;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (!isMultiple()) {
            if (mSelection != null && which < mSelection.length) {
                if (isChecked) {
                    for (int i = 0; i < mSelection.length; ++i) {
                        mSelection[i] = false;
                    }
                }
                mSelection[which] = isChecked;
                simpleAdapter.clear();
                simpleAdapter.add(buildSelectedItemString());
            } else {
                throw new IllegalArgumentException("Argument 'which' is out of bounds.");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which < items.length) {
            if (checkedIndex != -1 && checkedIndex != which) {
                mSelection[checkedIndex] = false;
            }
            checkedIndex = which;
            mSelection[which] = true;
            simpleAdapter.clear();
            simpleAdapter.add(items[which]);
            dialog.dismiss();
        } else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // TODO: Different texts for single/multiple selection?
        if (isMultiple()) {
            builder.setTitle("Please select one or multiple options.");
            builder.setMultiChoiceItems(items, mSelection, this);
            itemsAtStart = getSelectedItemsAsString();
            builder.setPositiveButton(R.string.supersede_feedbacklibrary_yes_string, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
            builder.setTitle("Please select an options.");
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

    public void setListener(OnMultipleItemsSelectedListener listener) {
        this.listener = listener;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

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
        simpleAdapter.add(buildSelectedItemString());
    }

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
        simpleAdapter.add(buildSelectedItemString());
    }

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
        simpleAdapter.add(buildSelectedItemString());
    }

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
        simpleAdapter.add(buildSelectedItemString());
    }

    public interface OnMultipleItemsSelectedListener {
        void selectedIndices(List<Integer> indices);

        void selectedStrings(List<String> strings);
    }
}
