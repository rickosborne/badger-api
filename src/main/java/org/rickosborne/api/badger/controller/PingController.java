package org.rickosborne.api.badger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping(value = "/ping")
public class PingController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String ping() {
        return "ping";
    }
}
