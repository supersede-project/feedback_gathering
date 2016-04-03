package com.example.matthias.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Matthias on 30.03.2016.
 */
public class FeedbackContainer {
    private List<Mechanism> allFeedback = new ArrayList<>();

    /**
     * Updates all models with the view data
     */
    public void updateModel() {
        for(Mechanism mechanism : allFeedback) {
            mechanism.updateModel();
        }
    }

    /**
     * Updates all views with the model data
     */
    public void updateView() {
        for(Mechanism mechanism : allFeedback) {
            if(mechanism.isActive()) {
                mechanism.updateView();
            }
        }
    }

    /**
     * Order list according to the order attribute in case if JSON list order is not representative
     */
    public void sortByOrder() {
        Collections.sort(allFeedback, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo((Integer) b.getOrder());
            }
        });
    }

    public void addFeedback(Mechanism mechanism) {
        allFeedback.add(mechanism);
    }

    public List<Mechanism> getAllFeedback() {
        return allFeedback;
    }
}
