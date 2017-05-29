package ch.fhnw.cere.orchestrator.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class MonitorType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="monitor_type_id")
    private Integer id;

    @NotNull
    @Column(unique=true)
    private String name;

    public MonitorType(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
