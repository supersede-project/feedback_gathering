package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "api_user_api_user_role", schema = "monitor_feedback", catalog = "")
public class ApiUserApiUserRoleEntity {
    private long id;
    private Integer apiUserRole;
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
    @Column(name = "api_user_role")
    public Integer getApiUserRole() {
        return apiUserRole;
    }

    public void setApiUserRole(Integer apiUserRole) {
        this.apiUserRole = apiUserRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiUserApiUserRoleEntity that = (ApiUserApiUserRoleEntity) o;

        if (id != that.id) return false;
        if (apiUserRole != null ? !apiUserRole.equals(that.apiUserRole) : that.apiUserRole != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (apiUserRole != null ? apiUserRole.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "api_user_id", referencedColumnName = "id")
    public ApiUserEntity getApiUserByApiUserId() {
        return apiUserByApiUserId;
    }

    public void setApiUserByApiUserId(ApiUserEntity apiUserByApiUserId) {
        this.apiUserByApiUserId = apiUserByApiUserId;
    }
}
