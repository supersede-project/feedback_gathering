package ch.uzh.supersede.feedbacklibrary.components.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.models.LabelFeedback;

public class LabelFeedbackView extends AbstractFeedbackPartView {
    private LabelFeedback labelFeedback;
    private CategorySpinner categorySpinner;

    public LabelFeedbackView(LayoutInflater layoutInflater, LabelFeedback labelFeedback) {
        super(layoutInflater);
        this.viewOrder = labelFeedback.getOrder();
        this.labelFeedback = labelFeedback;
        setEnclosingLayout(getLayoutInflater().inflate(ch.uzh.supersede.feedbacklibrary.R.layout.category_feedback, null));
        initView();
    }

    private void initView() {
        categorySpinner = (CategorySpinner) getEnclosingLayout().findViewById(ch.uzh.supersede.feedbacklibrary.R.id.supersede_feedbacklibrary_custom_spinner);
        List<String> items = new ArrayList<>(labelFeedback.getLabels());
        items.add(getEnclosingLayout()
                .getResources()
                .getString(ch.uzh.supersede.feedbacklibrary.R.string.category_other_options));
        categorySpinner.setItems(items, true);
    }

    @Override
    public void updateModel() {
        labelFeedback.setSelectedLabels(getCategorySpinner().getSelectedStrings());
    }

    public CategorySpinner getCategorySpinner() {
        return categorySpinner;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractFeedbackPartView) {
            int comparedViewOrder = ((AbstractFeedbackPartView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
