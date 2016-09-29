package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.CategoryMechanism;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Category feedback.
 */
public class CategoryFeedback implements Serializable {
    @Expose
    private long mechanismId;
    @Expose
    private List<HashMap<String, Object>> categories;

    public CategoryFeedback(CategoryMechanism categoryMechanism) {
        mechanismId = categoryMechanism.getId();
        initCategoryFeedback(categoryMechanism);
    }

    private void initCategoryFeedback(CategoryMechanism categoryMechanism) {
        List<String> options = categoryMechanism.getOptions();
        HashMap<String, Long> optionsIds = categoryMechanism.getOptionsIds();
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
                values.put("categoryTypesId", optionsIds.get(option));
                categories.add(values);
            }
        }

        // Process all the user created options
        Set<String> copySelectedOptionSet = new HashSet<>(selectedOptionsSet);
        copySelectedOptionSet.removeAll(new HashSet<String>(options));
        for (String option : copySelectedOptionSet) {
            if (categories == null) {
                categories = new ArrayList<>();
            }
            HashMap<String, Object> values = new HashMap<>();
            values.put("text", option);
            values.put("categoryTypesId", null);
            categories.add(values);
        }
    }
}
