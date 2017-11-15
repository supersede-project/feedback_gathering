package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.models.ApiUserPermission;
import ch.fhnw.cere.orchestrator.services.ApiUserPermissionService;
import ch.fhnw.cere.orchestrator.services.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/api_users/{apiUserId}/api_user_permissions")
public class ApiUserPermissionController extends BaseController {

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private ApiUserPermissionService apiUserPermissionService;

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ApiUserPermission> getApiUsers() {
        return apiUserPermissionService.findByApiUserId(apiUserId());
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ApiUserPermission getApiUserPermission(@PathVariable long id) {
        ApiUserPermission apiUserPermission = apiUserPermissionService.find(id);
        if(getApiUser() == null || apiUserPermission == null) {
            throw new NotFoundException();
        }
        return apiUserPermission;
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public ApiUserPermission createApiUserPermission(@RequestBody ApiUserPermission apiUserPermission) {
        return apiUserPermissionService.save(apiUserPermission);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApiUserPermission(@PathVariable long id) {
        apiUserPermissionService.delete(id);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public ApiUserPermission updateApiUserPermission(@RequestBody ApiUserPermission apiUserPermission) {
        return apiUserPermissionService.save(apiUserPermission);
    }

    private ApiUser getApiUser() {
        return apiUserService.find(apiUserId());
    }
}