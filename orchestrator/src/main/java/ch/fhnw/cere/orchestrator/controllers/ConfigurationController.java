package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.ConfigurationService;
import ch.fhnw.cere.orchestrator.services.UserGroupService;
import ch.fhnw.cere.orchestrator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/configurations")
public class ConfigurationController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Configuration> getConfigurations() {
        List<Configuration> configurations = configurationService.findByApplicationId(applicationId());
        configurations.forEach(configuration -> configuration.filterByLanguage(language(), this.fallbackLanguage));
        return configurations;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Configuration getConfiguration(@PathVariable long id) {
        Configuration configuration = configurationService.find(id);
        if(configuration == null) {
            throw new NotFoundException();
        }
        configuration.filterByLanguage(language(), this.fallbackLanguage);
        return configuration;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public Configuration createConfiguration(@PathVariable long applicationId, @RequestBody Configuration configuration) {
        configuration.setApplication(getApplication());
        return configurationService.save(configuration);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteConfiguration(@PathVariable long id) {
        configurationService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission()")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public Configuration updateConfiguration(@RequestBody Configuration configuration) {
        return configurationService.save(configuration);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{userIdentification}/info")
    public Configuration createInfoPullConfigurationForUser(@PathVariable long applicationId, @PathVariable String userIdentification, @RequestBody UserInfoPullConfiguration userInfoPullConfiguration) {
        // TODO refactor this user group and user handling
        User user = userService.findByApplicationIdAndUserIdentification(applicationId, userIdentification);
        List<UserGroup> userGroups = userGroupService.findByApplicationId(applicationId);
        UserGroup userGroup = null;
        String userGroupName = "Group for " + userIdentification;

        if(user == null) {
            user = userService.save(new User(userIdentification, userIdentification, getApplication()));
        }

        if(userGroups == null || userGroups.size() == 0) {
            userGroup = createGroupAndAssignUser(userGroupName, user);
        } else {
            for(UserGroup foundUserGroup : userGroups) {
                if(foundUserGroup.getUsers().size() == 1 && foundUserGroup.getName().equals(userGroupName)) {
                    userGroup = foundUserGroup;
                }
            }

            if(userGroup == null) {
                userGroup = createGroupAndAssignUser(userGroupName, user);
            }
        }

        Configuration configuration = userInfoPullConfiguration.buildConfiguration(userIdentification, getApplication(), user, userGroup);
        return configurationService.save(configuration);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }

    private UserGroup createGroupAndAssignUser(String userGroupName, User user) {
        List<User> users = new ArrayList<User>();
        users.add(user);
        UserGroup userGroup = userGroupService.save(new UserGroup(userGroupName, users, getApplication()));
        user.setUserGroup(userGroup);
        userService.save(user);

        return userGroup;
    }
}