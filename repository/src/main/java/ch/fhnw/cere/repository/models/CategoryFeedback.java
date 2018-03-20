package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.orchestrator.*;
import ch.fhnw.cere.repository.models.orchestrator.Parameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@Entity
public class CategoryFeedback implements MechanismFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;

    private Long parameterId;

    private String text;

    @JsonIgnore
    @Transient
    private Mechanism mechanism;

    @JsonIgnore
    @Transient
    private String categoryValue;

    @Override
    public String toString() {
        return "CategoryFeedback{" +
                "id=" + id +
                ", feedback=" + feedback +
                ", mechanismId=" + mechanismId +
                ", parameterId=" + parameterId +
                ", text='" + text + '\'' +
                ", mechanism=" + mechanism +
                ", categoryValue='" + categoryValue + '\'' +
                '}';
    }

    public CategoryFeedback() {
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, Long parameterId, String text) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.parameterId = parameterId;
        this.text = text;
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, String text) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.text = text;
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, Long parameterId) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.parameterId = parameterId;
    }

    public void setCategoryValueThroughMechanism() {
        MechanismTemplateModel mechanismTemplateModel = new MechanismTemplateModel(this.mechanism);
        ArrayList<LinkedHashMap> options = (ArrayList<LinkedHashMap>)mechanismTemplateModel.getParameterValueByParameterKey("options");

        for(LinkedHashMap option : options) {
            int id = (int)option.get("id");
            if(this.parameterId != null && id == this.parameterId) {
                String value = (String)option.get("value");
                this.categoryValue = value;
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Mechanism getMechanism() {
        return mechanism;
    }

    @Override
    public void setMechanism(Mechanism mechanism) {
        this.mechanism = mechanism;
        setCategoryValueThroughMechanism();
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }
}
