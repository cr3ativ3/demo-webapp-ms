package com.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.data.Frequency;
import com.demo.data.ProcessingResult;
import com.demo.data.UploadedFile;
import com.demo.data.Word;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    private static final Logger log = Logger.getLogger(FileProcessingServiceImpl.class);

    @Autowired
    private ReadingService readingService;

    @Autowired
    private WritingService writingService;

    @Override
    public ProcessingResult processFiles(List<MultipartFile> files) {

        ProcessingResult result = new ProcessingResult();
        List<String> errors = result.getErrors();

        log.debug("Reading words from files");
        List<Word> allWords = readingService.readAllWords(toUploadedFiles(files, errors),
                ex -> errors.add(ex.getMessage()));

        Map<Word, Frequency> frequencyMap = Frequency.countToMap(allWords);

        log.debug("Write word groups to files");
        writingService.writeToFilesByGroup(frequencyMap, ex -> errors.add(ex.getMessage()));

        result.setWordMap(new TreeMap<>(frequencyMap)); // add sorted
        return result;
    }

    private List<UploadedFile> toUploadedFiles(List<MultipartFile> files, List<String> errors) {
        return files.stream().map(multipartFile -> {
            UploadedFile uploadedFile = null;
            try {
                uploadedFile = new UploadedFile(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            }
            catch (IOException ex) {
                errors.add(ex.getMessage());
            }
            return uploadedFile;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}