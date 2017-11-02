package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "api_user_permission", schema = "monitor_feedback", catalog = "")
public class ApiUserPermissionEntity {
    private long id;
    private long applicationId;
    private boolean hasPermission;
    private ApiUserEntity apiUserByApiUserId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "application_id")
    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    @Basic
    @Column(name = "has_permission")
    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiUserPermissionEntity that = (ApiUserPermissionEntity) o;

        if (id != that.id) return false;
        if (applicationId != that.applicationId) return false;
        if (hasPermission != that.hasPermission) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (applicationId ^ (applicationId >>> 32));
        result = 31 * result + (hasPermission ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "api_user_id", referencedColumnName = "id", nullable = false)
    public ApiUserEntity getApiUserByApiUserId() {
        return apiUserByApiUserId;
    }

    public void setApiUserByApiUserId(ApiUserEntity apiUserByApiUserId) {
        this.apiUserByApiUserId = apiUserByApiUserId;
    }
}
