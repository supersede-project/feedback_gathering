package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Choice mechanism model.
 */
public class CategoryMechanism extends Mechanism {
    private boolean mandatory;
    private String mandatoryReminder;
    private boolean multiple;
    // Options the user can choose from
    private List<String> options = new ArrayList<>();
    private HashMap<String, Long> optionsIds = new HashMap<>();
    private boolean ownAllowed;
    // Selected options by the user
    private List<String> selectedOptions = new ArrayList<>();
    private String title;

    public CategoryMechanism(MechanismConfigurationItem item) {
        super(CATEGORY_TYPE, item);
        initChoiceMechanism(item);
    }

    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    public List<String> getOptions() {
        return options;
    }

    public HashMap<String, Long> getOptionsIds() {
        return optionsIds;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public Set<String> getSelectedOptionsSet() {
        return new HashSet<>(selectedOptions);
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unchecked")
    private void initChoiceMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Mandatory
            if (key.equals("mandatory")) {
                setMandatory(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
            // Mandatory reminder
            if (key.equals("mandatoryReminder")) {
                setMandatoryReminder((String) param.get("value"));
            }
            // Multiple
            if (key.equals("multiple")) {
                setMultiple(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
            // Own category allowed
            if (key.equals("ownAllowed")) {
                setOwnAllowed(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
            // Options
            if (key.equals("options")) {
                List<Map<String, Object>> opt = (List<Map<String, Object>>) param.get("value");
                for (Map<String, Object> par : opt) {
                    for (Map.Entry<String, Object> entry : par.entrySet()) {
                        if (entry.getKey().equals("value")) {
                            options.add((String) entry.getValue());
                        }
                    }
                    for (Map.Entry<String, Object> entry : par.entrySet()) {
                        if (entry.getKey().equals("id")) {
                            optionsIds.put(options.get(options.size() - 1), ((Double) entry.getValue()).longValue());
                        }
                    }
                }
            }
        }
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public boolean isOwnAllowed() {
        return ownAllowed;
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && !(getSelectedOptions().size() > 0)) {
            errorMessage.add(getMandatoryReminder());
            return false;
        }
        return true;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setOwnAllowed(boolean ownAllowed) {
        this.ownAllowed = ownAllowed;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
