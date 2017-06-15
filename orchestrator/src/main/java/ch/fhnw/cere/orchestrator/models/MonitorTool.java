package ch.fhnw.cere.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
public class MonitorTool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "monitorTool", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private List<MonitorConfiguration> monitorConfigurations;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "monitor_type_id")
    private MonitorType monitorType;

    @Column(name = "monitor_name")
    private String monitorName;

    public MonitorTool() {
    }

    public MonitorTool(String name, List<MonitorConfiguration> monitorConfigurations, MonitorType monitorType, String monitorName) {
        this.name = name;
        this.monitorConfigurations = monitorConfigurations;
        this.monitorType = monitorType;
        this.monitorName = monitorName;
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

    public List<MonitorConfiguration> getMonitorConfigurations() {
        return monitorConfigurations;
    }

    public void setMonitorConfigurations(List<MonitorConfiguration> monitorConfigurations) {
        this.monitorConfigurations = monitorConfigurations;
    }

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(MonitorType monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }
}
