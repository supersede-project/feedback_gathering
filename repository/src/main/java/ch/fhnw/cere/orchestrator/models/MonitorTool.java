package ch.fhnw.cere.orchestrator.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class MonitorTool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="monitor_tool_id")
    private Integer id;

    @NotNull
    @Column(unique=true)
    private String name;

    @Column(name="monitor_type_id")
    private Integer monitorTypeId;

    @Column(name="monitor_name")
    private String monitorName;

    public MonitorTool(String name, Integer monitorTypeId, String monitorName) {
        this.name = name;
        this.monitorTypeId = monitorTypeId;
        this.monitorName = monitorName;
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

    public Integer getMonitorTypeId() {
        return monitorTypeId;
    }

    public void setMonitorTypeId(Integer monitorTypeId) {
        this.monitorTypeId = monitorTypeId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }
}
