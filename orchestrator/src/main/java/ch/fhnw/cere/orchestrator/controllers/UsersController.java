package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.User;
import ch.fhnw.cere.orchestrator.services.ApplicationService;
import ch.fhnw.cere.orchestrator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/users")
public class UsersController extends BaseController {

    @Autowired
    private UserService userRepository;

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<User> getUser() {
        List<User> users = userRepository.findByApplicationId(applicationId());
        if(users == null) {
            throw new NotFoundException();
        }
        return users;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public User getUser(@PathVariable long id) {
        User user = userRepository.find(id);
        if(user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public User createUser(@PathVariable long applicationId, @RequestBody User user) {
        user.setApplication(getApplication());
        return userRepository.save(user);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteUser(@PathVariable long applicationId, @PathVariable long id) {
        userRepository.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public User updateUser(@PathVariable long applicationId, @RequestBody User user) {
        return userRepository.save(user);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public User updateUserById(@PathVariable long applicationId, @RequestBody User user) {
        return userRepository.save(user);
    }

    private Application getApplication() {
        return applicationService.find(applicationId());
    }
}