package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileApiController {

    @GetMapping("/")
    public String homePage() {
        return "WEB-INF/views/index.jsp";
    }
}
