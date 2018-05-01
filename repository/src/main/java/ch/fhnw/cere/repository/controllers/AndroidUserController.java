package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.BaseController;
import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.services.AndroidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/android_users")
public class AndroidUserController extends BaseController {

    @Autowired
    private AndroidUserService androidUserService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<AndroidUser> getAndroidUsers() {
        return androidUserService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public AndroidUser getAndroidUser(@PathVariable long id) {
        AndroidUser androidUser = androidUserService.find(id);
        if (androidUser == null) {
            throw new NotFoundException();
        }
        return androidUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public AndroidUser createAndroidUser(@RequestBody AndroidUser androidUser) {
        return androidUserService.save(androidUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteAndroidUser(@PathVariable long id) {
        androidUserService.delete(id);
    }
}