package com.demo.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingResult;
import com.demo.service.FileProcessingService;

@Controller
public class AppController {

    private static final Logger log = Logger.getLogger(AppController.class);

    @Autowired
    FileProcessingService fileService;

    @GetMapping("/")
    public String homePage() {
        log.debug("Showing index page");
        return "index";
    }

    @PostMapping(path = "/upload-files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String uploadFiles(@RequestParam("files") MultipartFile[] files , Model model, HttpServletRequest  request) {

        logRequest(files, request);

        ProcessingResult details = fileService.processFiles(Arrays.asList(files));
        logMessages(details);

        model.addAttribute("details", details);
        return "fileDetails";
    }

    private void logRequest(MultipartFile[] files, HttpServletRequest request) {
        log.debug("Upload file(s) request length: " + request.getContentLength());
        log.debug(String.format("Files uploaded: %s", Arrays.stream(files)
                .map(f -> f.getOriginalFilename()).collect(Collectors.toList())));
    }

    private void logMessages(ProcessingResult details) {
        if (!details.getErrors().isEmpty()) {
            log.debug("Files processed with warnings: " + details.getErrors());
        }
    }
}
