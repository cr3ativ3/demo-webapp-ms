package com.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.demo.data.WordGroup;

/**
 * Implementation of {@link FileProcessingService}.
 */
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

        // Check for illegal words
        Map<Word, Frequency> unknownWords = frequencyMap.entrySet().stream()
                .filter(e -> e.getKey().getGroup() == WordGroup.UNKNOWN)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        if (!unknownWords.isEmpty()) {
            handleUnknown(unknownWords.keySet(), errors);
        }

        log.debug("Write word groups to files");
        Map<Word, Frequency> legalWords = frequencyMap.entrySet().stream()
                .filter(e -> e.getKey().getGroup() != WordGroup.UNKNOWN)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        writingService.writeToFilesByGroup(legalWords, ex -> errors.add(ex.getMessage()));

        result.setWordMap(new TreeMap<>(legalWords)); // add sorted
        return result;
    }

    private List<UploadedFile> toUploadedFiles(List<MultipartFile> files, List<String> errors) {
        if(files == null || files.isEmpty()) {
            log.debug("Array of uploaded files is null or empty");
            return new ArrayList<>();
        }
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

    /**
     * Handles words that fall into the {@link WordGroup#UNKNOWN} category that will not be written to files.
     * @param illegalWords the word's of {@link WordGroup#UNKNOWN} group
     * @param errors the error message holder
     */
    protected void handleUnknown(Collection<Word> illegalWords, List<String> errors) {
        String errorMsg = String.format(
                "Frequency map contains illegal words that will not be written: %s", illegalWords);
        log.error(errorMsg);
        errors.add(errorMsg);
    };

    public void setReadingService(ReadingService readingService) {
        this.readingService = readingService;
    }

    public void setWritingService(WritingService writingService) {
        this.writingService = writingService;
    }
}