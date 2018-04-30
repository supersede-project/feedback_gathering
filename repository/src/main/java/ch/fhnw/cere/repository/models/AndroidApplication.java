package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class AndroidApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long applicationId;
    private String androidApplicationId;


    public AndroidApplication() {
    }

    public AndroidApplication(long applicationId, String androidApplicationId) {
        this.applicationId = applicationId;
        this.androidApplicationId = androidApplicationId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getAndroidApplicationId() {
        return androidApplicationId;
    }

    public void setAndroidApplicationId(String androidApplicationId) {
        this.androidApplicationId = androidApplicationId;
    }
}