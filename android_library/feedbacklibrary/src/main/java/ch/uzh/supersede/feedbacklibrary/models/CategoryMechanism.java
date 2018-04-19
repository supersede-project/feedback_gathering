package ch.uzh.supersede.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.CATEGORY_TYPE;

public class CategoryMechanism extends AbstractMechanism {
    private boolean mandatory = false;
    private String mandatoryReminder;
    private boolean multiple = false;
    // Options the user can choose from
    private List<String> options = new ArrayList<>();
    private HashMap<String, Long> optionsIds = new HashMap<>();
    private boolean isOwnAllowed = true;
    // Selected options by the user
    private List<String> selectedOptions = new ArrayList<>();

    public CategoryMechanism(MechanismConfigurationItem item) {
        super(CATEGORY_TYPE, item);
    }

    @Override
    public void handleMechanismParameter(String key, Object value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("mandatory")) {
            if (isBool(value)) {
                setMandatory(Boolean.parseBoolean((String) value));
            } else {
                setMandatory(Utils.intToBool(Integer.parseInt((String) value)));
            }
        } else if (key.equals("mandatoryReminder")) {
            setMandatoryReminder((String) value);
        } else if (key.equals("multiple")) {
            if (isBool(value)) {
                setMultiple(Boolean.parseBoolean((String) value));
            } else {
                setMultiple(Utils.intToBool(Integer.parseInt((String) value)));
            }
        } else if (key.equals("ownAllowed")) {
            if (isBool(value)) {
                setOwnAllowed(Boolean.parseBoolean((String) value));
            } else {
                setOwnAllowed(Utils.intToBool(Integer.parseInt((String) value)));
            }
        } else if (key.equals("options")) {
            List<Map<String, Object>> opt = (List<Map<String, Object>>) value;
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

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && getSelectedOptions().isEmpty()) {
            errorMessage.add(getMandatoryReminder());
            return false;
        }
        return true;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, Long> getOptionsIds() {
        return optionsIds;
    }

    public void setOptionsIds(Map<String, Long> optionsIds) {
        this.optionsIds = new HashMap<>(optionsIds);
    }

    public boolean isOwnAllowed() {
        return isOwnAllowed;
    }

    public void setOwnAllowed(boolean isOwnAllowed) {
        this.isOwnAllowed = isOwnAllowed;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public Set<String> getSelectedOptionsSet() {
        return new HashSet<>(selectedOptions);
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    private boolean isBool(Object value) {
        return value != null && value instanceof Boolean;
    }
}
