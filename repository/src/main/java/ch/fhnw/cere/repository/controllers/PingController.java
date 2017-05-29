package ch.fhnw.cere.repository.controllers;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/ping")
public class PingController extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getPong() {
        return "pong";
    }
}