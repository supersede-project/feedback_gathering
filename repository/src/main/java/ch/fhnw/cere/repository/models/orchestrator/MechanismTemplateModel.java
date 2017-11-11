
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MechanismTemplateModel extends Mechanism {
    private String title;
    private String label;

    public MechanismTemplateModel(Mechanism mechanism) {
        super(mechanism.getConfigurationsId(), mechanism.getCreatedAt(), mechanism.getActive(), mechanism.getId(), mechanism.getType(), mechanism.getParameters(), mechanism.getCanBeActivated(), mechanism.getOrder());

        title = (String)getParameterValueByParameterKey("title");
        label = (String)getParameterValueByParameterKey("label");
    }

    public Object getParameterValueByParameterId(long parameterId) {
        Optional<Parameter> parameterOptional = this.parameters.stream().filter(parameter -> parameter.getId() == parameterId).findFirst();
        return parameterOptional.map(Parameter::getValue).orElse(null);
    }

    public Object getParameterValueByParameterKey(String parameterKey) {
        Optional<Parameter> parameterOptional = this.parameters.stream().filter(parameter -> parameter.getKey().equals(parameterKey)).findFirst();
        return parameterOptional.map(Parameter::getValue).orElse(null);
    }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }
}
