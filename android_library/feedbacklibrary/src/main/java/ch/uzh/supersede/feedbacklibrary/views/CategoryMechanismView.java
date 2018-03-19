package ch.uzh.supersede.feedbacklibrary.views;

import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;

/**
 * Choice mechanism view
 */
public class CategoryMechanismView extends MechanismView {
    private CategoryMechanism categoryMechanism;
    private CustomSpinner customSpinner;

    public CategoryMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.categoryMechanism = (CategoryMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(ch.uzh.supersede.feedbacklibrary.R.layout.category_feedback_layout, null));
        initView();
    }

    private void initView() {
        customSpinner = (CustomSpinner) getEnclosingLayout().findViewById(ch.uzh.supersede.feedbacklibrary.R.id.supersede_feedbacklibrary_custom_spinner);
        List<String> items = new ArrayList<>(categoryMechanism.getOptions());
        if (categoryMechanism.isOwnAllowed()) {
            if (!categoryMechanism.isMultiple()) {
                items.add(getEnclosingLayout()
                        .getResources()
                        .getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_other_option_string));
            } else {
                items.add(getEnclosingLayout()
                        .getResources()
                        .getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_other_options_string));
            }
        }
        customSpinner.setOwnCategoryAllowed(categoryMechanism.isOwnAllowed());
        customSpinner.setItems(items, true);
        customSpinner.setMultipleChoice(categoryMechanism.isMultiple());
    }

    @Override
    public void updateModel() {
        categoryMechanism.setSelectedOptions(getCustomSpinner().getSelectedStrings());
    }

    public CustomSpinner getCustomSpinner() {
        return customSpinner;
    }
}
