package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Pull configuration.
 */
public class PullConfiguration {
    private boolean isActive;
    private double likelihood;
    // If a popup dialog is shown asking the user if (s)he wants to give feedback
    private boolean showPopupDialog = true;

    // All mechanisms in the pull configuration
    private List<Mechanism> allPullMechanisms = null;

    public PullConfiguration(PullConfigurationItem item) {
        initPullConfiguration(item);
    }

    /**
     * This method returns all pull mechanisms.
     *
     * @return all mechanisms
     */
    public List<Mechanism> getAllPullMechanisms() {
        return allPullMechanisms;
    }

    /**
     * This method returns the likelihood.
     *
     * @return the likelihood
     */
    public double getLikelihood() {
        return likelihood;
    }

    private void initPullConfiguration(PullConfigurationItem item) {
        setActive(item.isActive());
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Likelihood
            if (key.equals("likelihood")) {
                setLikelihood(((Double) param.get("value")).floatValue());
            }
            // ShowPopupDialog
            if (key.equals("showPopupDialog")) {
                setShowPopupDialog(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
        }

        allPullMechanisms = new ArrayList<>();
        for (MechanismConfigurationItem mechanismConfigurationItem : item.getMechanisms()) {
            allPullMechanisms.add(mechanismConfigurationItem.createMechanism());
        }
        Collections.sort(allPullMechanisms, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo(b.getOrder());
            }
        });
    }

    /**
     * This method returns if the pull configuration is active.
     *
     * @return true if it is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * This method returns if a pop up dialog is shown.
     *
     * @return true if a pop up dialog is shown, false otherwise
     */
    public boolean isShowPopupDialog() {
        return showPopupDialog;
    }

    /**
     * This method sets if the pull configuration is active.
     *
     * @param active true if it is active, false otherwise
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * This method sets all pull mechanisms.
     *
     * @param allPullMechanisms all mechanisms
     */
    public void setAllPullMechanisms(List<Mechanism> allPullMechanisms) {
        this.allPullMechanisms = allPullMechanisms;
    }

    /**
     * This method sets the likelihood.
     *
     * @param likelihood the likelihood
     */
    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }

    /**
     * This method sets if a pop up dialog is shown.
     *
     * @param showPopupDialog true if a pop up dialog is shown, false otherwise
     */
    public void setShowPopupDialog(boolean showPopupDialog) {
        this.showPopupDialog = showPopupDialog;
    }
}
