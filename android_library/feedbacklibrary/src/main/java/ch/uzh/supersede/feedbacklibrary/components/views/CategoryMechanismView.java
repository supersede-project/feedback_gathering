package ch.uzh.supersede.feedbacklibrary.components.views;

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
    private CategorySpinner categorySpinner;

    public CategoryMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.categoryMechanism = (CategoryMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(ch.uzh.supersede.feedbacklibrary.R.layout.mechanism_category, null));
        initView();
    }

    private void initView() {
        categorySpinner = (CategorySpinner) getEnclosingLayout().findViewById(ch.uzh.supersede.feedbacklibrary.R.id.supersede_feedbacklibrary_custom_spinner);
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
        categorySpinner.setOwnCategoryAllowed(categoryMechanism.isOwnAllowed());
        categorySpinner.setItems(items, true);
        categorySpinner.setMultipleChoice(categoryMechanism.isMultiple());
    }

    @Override
    public void updateModel() {
        categoryMechanism.setSelectedOptions(getCategorySpinner().getSelectedStrings());
    }

    public CategorySpinner getCategorySpinner() {
        return categorySpinner;
    }
}
