package ch.uzh.supersede.feedbacklibrary.feedbacks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;

public class CategoryFeedback implements Serializable {
    private List<HashMap<String, Object>> categories;

    public CategoryFeedback(CategoryMechanism categoryMechanism) {
        initCategoryFeedback(categoryMechanism);
    }

    public List<HashMap<String, Object>> getCategories() {
        return categories;
    }

    private void initCategoryFeedback(CategoryMechanism categoryMechanism) {
        List<String> options = categoryMechanism.getOptions();
        HashMap<String, Long> optionsIds = new HashMap<>(categoryMechanism.getOptionsIds());
        Set<String> selectedOptionsSet = categoryMechanism.getSelectedOptionsSet();

        // Process all the not user created options
        for (String option : options) {
            if (optionsIds.containsKey(option)) {
                if (categories == null) {
                    categories = new ArrayList<>();
                }
                HashMap<String, Object> values = new HashMap<>();
                if (selectedOptionsSet.contains(option)) {
                    values.put("text", option);
                } else {
                    values.put("text", null);
                }
                values.put("parameterId", optionsIds.get(option));
                categories.add(values);
            }
        }

        // Process all the user created options
        Set<String> copySelectedOptionSet = new HashSet<>(selectedOptionsSet);
        copySelectedOptionSet.removeAll(new HashSet<>(options));
        for (String option : copySelectedOptionSet) {
            if (categories == null) {
                categories = new ArrayList<>();
            }
            HashMap<String, Object> values = new HashMap<>();
            values.put("text", option);
            values.put("parameterId", null);
            categories.add(values);
        }
    }
}
