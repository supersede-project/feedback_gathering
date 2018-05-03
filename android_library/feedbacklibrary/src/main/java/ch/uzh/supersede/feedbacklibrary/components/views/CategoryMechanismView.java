package ch.uzh.supersede.feedbacklibrary.components.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;

public class CategoryMechanismView extends AbstractMechanismView {
    private CategoryMechanism categoryMechanism;
    private CategorySpinner categorySpinner;

    public CategoryMechanismView(LayoutInflater layoutInflater, AbstractMechanism mechanism) {
        super(layoutInflater);
        this.viewOrder = mechanism.getOrder();
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
                        .getString(ch.uzh.supersede.feedbacklibrary.R.string.category_other_option));
            } else {
                items.add(getEnclosingLayout()
                        .getResources()
                        .getString(ch.uzh.supersede.feedbacklibrary.R.string.category_other_options));
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

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractMechanismView){
            int comparedViewOrder = ((AbstractMechanismView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
