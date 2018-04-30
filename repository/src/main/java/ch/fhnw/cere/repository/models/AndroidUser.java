package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class AndroidUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private boolean isDeveloper;


    public AndroidUser() {
    }

    public AndroidUser(String name,  boolean isDeveloper) {
        this.name = name;
        this.isDeveloper = isDeveloper;
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

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
    }

}