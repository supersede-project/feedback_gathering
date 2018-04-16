package ch.uzh.supersede.feedbacklibrary.configurations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

public class PullConfiguration {
    private boolean isActive;
    private double likelihood;
    private boolean showPopupDialog = true;  // If a popup dialog is shown asking the user if he wants to give feedback
    private List<AbstractMechanism> allPullMechanisms = null; // All mechanisms in the pull configuration

    public PullConfiguration(PullConfigurationItem item) {
        initPullConfiguration(item);
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
        Collections.sort(allPullMechanisms, new Comparator<AbstractMechanism>() {
            @Override
            public int compare(AbstractMechanism a, AbstractMechanism b) {
                return ((Integer) a.getOrder()).compareTo(b.getOrder());
            }
        });
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }

    public boolean isShowPopupDialog() {
        return showPopupDialog;
    }

    public void setShowPopupDialog(boolean showPopupDialog) {
        this.showPopupDialog = showPopupDialog;
    }

    public List<AbstractMechanism> getAllPullMechanisms() {
        return allPullMechanisms;
    }

    public void setAllPullMechanisms(List<AbstractMechanism> allPullMechanisms) {
        this.allPullMechanisms = allPullMechanisms;
    }
}
