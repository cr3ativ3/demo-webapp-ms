package com.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.data.LetterGroup;
import com.demo.data.ProcessingDetails;
import com.demo.data.UploadedFile;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    private static final Logger log = Logger.getLogger(FileProcessingServiceImpl.class);

    @Autowired
    private WordCountingService wordCountingService;

    @Override
    public ProcessingDetails processFiles(MultipartFile[] uploadedFiles) {
        ProcessingDetails details = new ProcessingDetails();
        Map<String, ? extends Number> sortedWords = readAllFiles(details, uploadedFiles)
                 .entrySet().stream()
                 .sorted(Comparator.comparingLong(e -> e.getValue().longValue()))
                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                         (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        details.setSortedWords(sortedWords);

        Map<LetterGroup, ?> groupedWords = sortedWords.entrySet().stream().collect(Collectors.groupingBy(this::chooseLetterGroup));

        return details;
    }

    private LetterGroup chooseLetterGroup(Object obj) {
        log.debug(obj.getClass());
        return null;
    }

    private Map<String, ? extends Number> readAllFiles(ProcessingDetails details, MultipartFile[] uploadedFiles) {
        return wordCountingService.countWords(Arrays.stream(uploadedFiles).map(file -> {
            String name = file.getOriginalFilename();
            InputStream stream = null;
            try {
                stream = file.getInputStream();
            }
            catch (IOException e) {
                log.error("Faild to get input stream", e);
                details.addMessage("Could not get stream for " + name);
                stream = null;
            }
            return new UploadedFile(name, stream);
        }).collect(Collectors.toList()));
    }
}