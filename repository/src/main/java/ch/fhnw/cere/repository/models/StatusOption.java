package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class StatusOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int order;
    private boolean userSpecific;

    public StatusOption() {
    }

    @Override
    public String toString() {
        return String.format(
                "StatusOption[id=%d, name='%s', order='%d', userSpecific='%b']",
                id, name, order, userSpecific);
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isUserSpecific() {
        return userSpecific;
    }

    public void setUserSpecific(boolean userSpecific) {
        this.userSpecific = userSpecific;
    }
}
