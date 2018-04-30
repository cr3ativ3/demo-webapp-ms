package com.demo.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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

/**
 * Main web application controller.
 */
@Controller
public class AppController {

    private static final Logger log = Logger.getLogger(AppController.class);

    /**
     * Service responsible for uploaded file processing.
     */
    @Autowired
    private FileProcessingService fileService;

    /**
     * @return mapping for the home page.
     */
    @GetMapping("/")
    public String homePage() {
        log.debug("Showing index page");
        return "index";
    }

    /**
     * Mapping for the uploaded file submission.
     * @param files the uploaded files
     * @param model the application view model
     * @return mapping to file processing details page
     */
    @PostMapping(path = "/upload-files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String uploadFiles(@RequestParam("files") MultipartFile[] files , Model model) {

        files = Optional.ofNullable(files).orElse(new MultipartFile[0]);
        logRequest(files);

        ProcessingResult details = fileService.processFiles(Arrays.asList(files));
        logMessages(details);

        model.addAttribute("details", details);
        return "fileDetails";
    }

    private void logRequest(MultipartFile[] files) {
        log.debug(String.format("Files uploaded: %s", Arrays.stream(files)
                .map(f -> f.getOriginalFilename()).collect(Collectors.toList())));
    }

    private void logMessages(ProcessingResult details) {
        if (!details.getErrors().isEmpty()) {
            log.debug("Files processed with warnings: " + details.getErrors());
        }
    }
}
