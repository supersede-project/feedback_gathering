package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.BadRequestException;
import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.services.AndroidUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/android_users")
public class AndroidUserController extends BaseController {

    @Autowired
    private AndroidUserService androidUserService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<AndroidUser> getApplicationAndroidUsers() {
        return androidUserService.findByApplicationId(applicationId());
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public AndroidUser getAndroidUser(@PathVariable long applicationId, @PathVariable long id) {
        AndroidUser androidUser = androidUserService.find(id);
        if (androidUser == null) {
            throw new NotFoundException();
        }
        return androidUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "")
    public AndroidUser createAndroidUser(@PathVariable long applicationId ,@RequestBody AndroidUser androidUser) {
        androidUser.setApplicationId(applicationId);
        return androidUserService.save(androidUser);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public AndroidUser blockedAndroidUser(@PathVariable("id") long id, HttpEntity<String> blockedState) {
        if(blockedState.getBody() != null){
            JSONObject obj = new JSONObject(blockedState.getBody());
            boolean blockedValue = obj.getBoolean("blocked");
            AndroidUser modifiedAndroidUser = androidUserService.find(id);
            if(modifiedAndroidUser != null) {
                modifiedAndroidUser.setBlocked(blockedValue);
                androidUserService.save(modifiedAndroidUser);
                return modifiedAndroidUser;
            } else {
                throw new NotFoundException();
            }
        }
        throw new BadRequestException();
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteAndroidUser(@PathVariable long id) {
        androidUserService.delete(id);
    }
}