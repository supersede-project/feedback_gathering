package com.example.matthias.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.ChoiceMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;

import java.util.List;

/**
 * Choice mechanism view
 */
public class ChoiceMechanismView extends MechanismView implements CustomSpinner.OnMultipleItemsSelectedListener {
    private ChoiceMechanism choiceMechanism = null;
    private CustomSpinner customSpinner = null;

    public ChoiceMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.choiceMechanism = (ChoiceMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.choice_feedback_layout, null));
        initView();
    }

    private void initView() {
        final TextView textView = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_choice_feedback_title);
        textView.setText(choiceMechanism.getTitle());
        customSpinner = (CustomSpinner) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_custom_spinner);
        customSpinner.setItems(choiceMechanism.getOptions(), false);
        customSpinner.setListener(this);
        customSpinner.setMultiple(choiceMechanism.isMultiple());
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
    }

    @Override
    public void updateModel() {
        choiceMechanism.setSelectedOptions(customSpinner.getSelectedStrings());
    }
}
