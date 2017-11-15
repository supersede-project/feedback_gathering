package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.services.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/api_users")
public class ApiUserController extends BaseController {

    @Autowired
    private ApiUserService apiUserService;

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ApiUser> getApiUsers() {
        return apiUserService.findAll();
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ApiUser getApiUser(@PathVariable long id) {
        ApiUser apiUser = apiUserService.find(id);
        if(apiUser == null) {
            throw new NotFoundException();
        }
        return apiUser;
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public ApiUser createApiUser(@RequestBody ApiUser apiUser) {
        return apiUserService.save(apiUser);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApiUser(@PathVariable long id) {
        apiUserService.delete(id);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public ApiUser updateApiUser(@RequestBody ApiUser apiUser) {
        return apiUserService.save(apiUser);
    }
}