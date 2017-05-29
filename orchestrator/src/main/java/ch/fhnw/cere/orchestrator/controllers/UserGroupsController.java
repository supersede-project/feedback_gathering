package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.UserGroup;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/user_groups")
public class UserGroupsController extends BaseController {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<UserGroup> getApplicationUserGroups() {
        List<UserGroup> userGroups = userGroupService.findByApplicationId(applicationId());
        if(userGroups == null) {
            throw new NotFoundException();
        }
        return userGroups;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserGroup getApplicationUserGroup(@PathVariable long id) {
        UserGroup userGroup = userGroupService.find(id);
        if(userGroup == null) {
            throw new NotFoundException();
        }
        return userGroup;
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public UserGroup createApplicationUserGroup(@RequestBody UserGroup userGroup) {
        userGroup.setApplication(getApplication());
        return userGroupService.save(userGroup);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteApplicationUserGroup(@PathVariable long id) {
        userGroupService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public UserGroup updateApplicationUserGroup(@RequestBody UserGroup userGroup) {
        return userGroupService.save(userGroup);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }
}