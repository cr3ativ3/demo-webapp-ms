package com.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingDetails;

public interface FileProcessingService {

    ProcessingDetails processFiles(List<MultipartFile> uploadedFiles);

}
