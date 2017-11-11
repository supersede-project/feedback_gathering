package ch.fhnw.cere.orchestrator.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
public class MonitorType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(unique=true)
    private String name;

    @OneToMany(mappedBy = "monitorType", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private List<MonitorTool> monitorTools;

    public MonitorType() {
    }

    public MonitorType(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MonitorTool> getMonitorTools() {
        return monitorTools;
    }

    public void setMonitorTools(List<MonitorTool> monitorTools) {
        this.monitorTools = monitorTools;
    }
}
