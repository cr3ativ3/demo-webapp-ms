package com.demo.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingDetails;
import com.demo.service.FileProcessingService;

@Controller
public class AppController {

    private static final Logger log = Logger.getLogger(AppController.class);

    @Autowired
    FileProcessingService fileService;

    @GetMapping("/")
    public String homePage() {
        log.debug("Show index page");
        return "index";
    }

    @PostMapping("upload-files")
    public String uploadFiles(@RequestParam("files") MultipartFile[] uploadedFiles, Model model) {

        log.debug(String.format("Files uploaded: %s", Arrays.stream(uploadedFiles)
                .map(f -> f.getOriginalFilename()).collect(Collectors.toList())));

        ProcessingDetails details = fileService.processFiles(uploadedFiles);
        logMessages(details);
        model.addAttribute("details", details);
        return "fileDetails";
    }

    private void logMessages(ProcessingDetails details) {
        if (details.getMessages().isEmpty()) {
            log.debug("Files processed with warnings: " + details.getMessages());
        }
    }
}
