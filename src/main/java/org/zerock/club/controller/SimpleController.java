package org.zerock.club.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @RequestMapping("/")
    public String hello()
    {
        return "Hello World!";
    }

}
