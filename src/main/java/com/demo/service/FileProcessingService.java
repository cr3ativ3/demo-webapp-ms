package com.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingResult;

/**
 * Service responsible for processing uploaded files.
 */
public interface FileProcessingService {

    /**
     * Processes uploaded files and outputs a processing result.
     *
     * @param uploadedFiles the uploaded files
     * @return the processing result
     */
    ProcessingResult processFiles(List<MultipartFile> uploadedFiles);

}
