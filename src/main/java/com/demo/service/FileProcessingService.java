package com.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingDetails;

public interface FileProcessingService {

    ProcessingDetails processFiles(MultipartFile[] uploadedFiles);

}
