package com.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingResult;

public interface FileProcessingService {

    ProcessingResult processFiles(List<MultipartFile> uploadedFiles);

}
