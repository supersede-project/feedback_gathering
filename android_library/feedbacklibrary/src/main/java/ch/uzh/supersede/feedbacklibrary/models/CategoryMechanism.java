package ch.uzh.supersede.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

/**
 * Choice mechanism model.
 */
public class CategoryMechanism extends Mechanism {
    private boolean mandatory = false;
    private String mandatoryReminder;
    private boolean multiple = false;
    // Options the user can choose from
    private List<String> options = new ArrayList<>();
    private HashMap<String, Long> optionsIds = new HashMap<>();
    private boolean ownAllowed = true;
    // Selected options by the user
    private List<String> selectedOptions = new ArrayList<>();
    private String title;

    public CategoryMechanism(MechanismConfigurationItem item) {
        super(CATEGORY_TYPE, item);
        initChoiceMechanism(item);
    }

    /**
     * This method returns the mandatory reminder.
     *
     * @return the mandatory reminder
     */
    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    /**
     * This method returns all the options.
     *
     * @return the options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * This method returns all the ids of the options.
     *
     * @return the option ids
     */
    public HashMap<String, Long> getOptionsIds() {
        return optionsIds;
    }

    /**
     * This method returns the selected options as a list.
     *
     * @return the selected options
     */
    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    /**
     * This method returns the selected options as a set.
     *
     * @return the selected options
     */
    public Set<String> getSelectedOptionsSet() {
        return new HashSet<>(selectedOptions);
    }

    /**
     * This method returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unchecked")
    private void initChoiceMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            if (key == null){
                return;
            }
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Mandatory
            if (key.equals("mandatory")) {
                if(param.get("value") instanceof Boolean) {
                    setMandatory((Boolean)param.get("value"));
                } else {
                    Double doubleValue = Double.parseDouble((String) param.get("value"));
                    setMandatory(Utils.intToBool(doubleValue.intValue()));
                }
            }
            // Mandatory reminder
            if (key.equals("mandatoryReminder")) {
                setMandatoryReminder((String) param.get("value"));
            }
            // Multiple
            if (key.equals("multiple")) {
                if(param.get("value") instanceof Boolean) {
                    setMultiple((Boolean)param.get("value"));
                } else {
                    Double doubleValue = Double.parseDouble((String) param.get("value"));
                    setMultiple(Utils.intToBool(doubleValue.intValue()));
                }
            }
            // Own category allowed
            if (key.equals("ownAllowed")) {
                if(param.get("value") instanceof Boolean) {
                    setOwnAllowed((Boolean)param.get("value"));
                } else {
                    Double doubleValue = Double.parseDouble((String) param.get("value"));
                    setOwnAllowed(Utils.intToBool(doubleValue.intValue()));
                }
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
                        if (entry
                                .getKey()
                                .equals("id")) {
                            optionsIds.put(options.get(options.size() - 1), ((Double) entry.getValue()).longValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * This method returns if selecting at least one category is mandatory.
     *
     * @return true if it is mandatory, false otherwise
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * This method returns if multiple options can be selected.
     *
     * @return true if multiple options can be selected, false otherwise (single option choice only)
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * This method returns if the user can create own categories.
     *
     * @return true if own categories are allowed, false otherwise
     */
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

    /**
     * This method sets if selecting at least one category is mandatory.
     *
     * @param mandatory true if it is mandatory, false otherwise
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * This method sets the mandatory reminder.
     *
     * @param mandatoryReminder the mandatory reminder
     */
    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    /**
     * This method sets if multiple options can be selected.
     *
     * @param multiple true if multiple options can be selected, false otherwise (single option choice only)
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * This method sets all the options.
     *
     * @param options the options
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }

    /**
     * This method sets if the user can create own categories.
     *
     * @param ownAllowed true if own categories are allowed, false otherwise
     */
    public void setOwnAllowed(boolean ownAllowed) {
        this.ownAllowed = ownAllowed;
    }

    /**
     * This method sets the selected options.
     *
     * @param selectedOptions the selected options
     */
    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    /**
     * This method sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
