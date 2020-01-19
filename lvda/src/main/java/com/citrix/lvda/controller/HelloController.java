package com.citrix.lvda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping("/greet")
    public String greeting() {
        return "StoreFront";
    }
}