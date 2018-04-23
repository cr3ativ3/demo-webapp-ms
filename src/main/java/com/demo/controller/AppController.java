package com.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AppController {

    @GetMapping("/")
    public String homePage() {
        return "WEB-INF/views/index.jsp";
    }

    @PostMapping("uploadFiles")
    public ResponseEntity<?> uploadFiles(@RequestParam("fileUpload[]") MultipartFile fileUpload[]) {
        return null;
    }
}
