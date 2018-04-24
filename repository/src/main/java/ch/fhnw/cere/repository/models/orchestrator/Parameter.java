
package ch.fhnw.cere.repository.models.orchestrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter {
    @JsonProperty("createdAt")
    protected String createdAt;
    @JsonProperty("language")
    protected String language;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty("value")
    protected Object value;
    @JsonProperty("key")
    protected String key;
    @JsonProperty("order")
    protected int order;
    @JsonProperty("parameters")
    protected List<Parameter> parameters = null;

    public Parameter() {
    }

    public Parameter(String createdAt, String language, Long id, Object value, String key) {
        this.createdAt = createdAt;
        this.language = language;
        this.id = id;
        this.value = value;
        this.key = key;
    }

    public Parameter(String createdAt, String language, Long id, Object value, String key, int order) {
        this.createdAt = createdAt;
        this.language = language;
        this.id = id;
        this.value = value;
        this.key = key;
        this.order = order;
    }

    public Parameter(String createdAt, String language, Long id, Object value, String key, int order, List<Parameter> parameters) {
        this.createdAt = createdAt;
        this.language = language;
        this.id = id;
        this.value = value;
        this.key = key;
        this.order = order;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "createdAt='" + createdAt + '\'' +
                ", language='" + language + '\'' +
                ", id=" + id +
                ", value=" + value +
                ", key='" + key + '\'' +
                ", order=" + order +
                ", parameters=" + parameters +
                '}';
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
