package ch.uzh.supersede.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;

import java.util.ArrayList;
import java.util.List;

/**
 * Choice mechanism view
 */
public class CategoryMechanismView extends MechanismView implements CustomSpinner.OnMultipleItemsSelectedListener {
    private CategoryMechanism categoryMechanism = null;
    private CustomSpinner customSpinner = null;

    public CategoryMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.categoryMechanism = (CategoryMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(ch.uzh.supersede.feedbacklibrary.R.layout.category_feedback_layout, null));
        initView();
    }

    private void initView() {
        final TextView textView = (TextView) getEnclosingLayout().findViewById(ch.uzh.supersede.feedbacklibrary.R.id.supersede_feedbacklibrary_choice_feedback_title);
        textView.setText(categoryMechanism.getTitle());
        customSpinner = (CustomSpinner) getEnclosingLayout().findViewById(ch.uzh.supersede.feedbacklibrary.R.id.supersede_feedbacklibrary_custom_spinner);
        List<String> items = new ArrayList<>(categoryMechanism.getOptions());
        if (categoryMechanism.isOwnAllowed()) {
            if (!categoryMechanism.isMultiple()) {
                items.add(getEnclosingLayout().getResources().getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_other_option_string));
            } else {
                items.add(getEnclosingLayout().getResources().getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_other_options_string));
            }
        }
        customSpinner.setOwnCategoryAllowed(categoryMechanism.isOwnAllowed());
        customSpinner.setItems(items, true);
        customSpinner.setListener(this);
        customSpinner.setMultiple(categoryMechanism.isMultiple());
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
    }

    @Override
    public void updateModel() {
        categoryMechanism.setSelectedOptions(customSpinner.getSelectedStrings());
    }

    public CustomSpinner getCustomSpinner() {
        return customSpinner;
    }
}
