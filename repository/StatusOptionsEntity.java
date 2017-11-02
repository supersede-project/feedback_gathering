package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "status_options", schema = "monitor_feedback", catalog = "")
public class StatusOptionsEntity {
    private int id;
    private String name;
    private int order;
    private boolean userSpecific;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Basic
    @Column(name = "user_specific")
    public boolean isUserSpecific() {
        return userSpecific;
    }

    public void setUserSpecific(boolean userSpecific) {
        this.userSpecific = userSpecific;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusOptionsEntity that = (StatusOptionsEntity) o;

        if (id != that.id) return false;
        if (order != that.order) return false;
        if (userSpecific != that.userSpecific) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + order;
        result = 31 * result + (userSpecific ? 1 : 0);
        return result;
    }
}
