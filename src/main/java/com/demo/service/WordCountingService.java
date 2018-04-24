package com.demo.service;

import java.util.List;
import java.util.Map;

import com.demo.data.UploadedFile;

public interface WordCountingService {

    Map<String, ? extends Number> countWords(List<UploadedFile> uploadedFiles);

}
