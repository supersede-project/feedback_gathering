package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryMechanism extends AbstractMechanism {
    private boolean mandatory = false;
    private String mandatoryReminder;
    private boolean multiple = false;
    private List<String> options = new ArrayList<>();
    private boolean isOwnAllowed = true;
    private List<String> selectedOptions = new ArrayList<>();

    @Expose
    private List<String> categories = new ArrayList<>();

    public CategoryMechanism(long mechanismId, int order) {
        super(mechanismId, order);
        //TODO [jfo] set mandatoryReminder, multiple, ownAllowed, options
        initCategoryFeedback();
    }

    private void initCategoryFeedback() {
        categories.addAll(getSelectedOptionsSet());
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && getSelectedOptions().isEmpty()) {
            errorMessage.add(getMandatoryReminder());
            return false;
        }
        return true;
    }

    public List<String> getCategories() {
        return categories;
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

}
