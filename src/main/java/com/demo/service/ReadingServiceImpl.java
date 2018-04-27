package com.demo.service;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.data.UploadedFile;
import com.demo.data.Word;

@Service
public class ReadingServiceImpl implements ReadingService {

    private static final Logger log = Logger.getLogger(ReadingServiceImpl.class);

    private Executor executor;

    @Override
    public List<Word> readAllWords(List<UploadedFile> uploadedFiles, Consumer<Throwable> errorHandler) {

        // Create futures that are reading from files
        List<CompletableFuture<List<Word>>> fileReaders = uploadedFiles.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> readFile(file, errorHandler),
                        getExecutor(uploadedFiles)))
                .collect(Collectors.toList());

        // Merge words lists from all complete futures
        List<Word> words = CompletableFuture.allOf(fileReaders.toArray(new CompletableFuture[0]))
                .thenApply(combined -> {
                    log.debug("Finished reading all files");
                    return fileReaders.stream().flatMap(reader -> reader.join().stream())
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }).join(); // let's wait to finish

        return words;
    }

    private List<Word> readFile(UploadedFile file, Consumer<Throwable> errorHandler) {
        try {
            return readInputStream(file.getInputStream());
        } catch (Throwable t) {
            String msg = "Error reading from file: %s";
            log.error(String.format(msg, t.getMessage()), t);
            errorHandler.accept(new Throwable(String.format(msg, file.getFileName())));
            return new ArrayList<Word>();
        } finally {
            log.debug("Finished reading file " + file.getFileName());
        }
    }

    private List<Word> readInputStream(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream)).lines()
                .map(line -> line.split("\\s+"))
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(w -> w.matches("\\w+") && !w.matches("\\d+"))
                .map(s -> new Word(s))
                .collect(toList());
    }

    private Executor getExecutor(List<UploadedFile> uploadedFiles) {
        // Avoid synchronizing for single file
        return (uploadedFiles.size() > 1 ? this.executor : task -> task.run());
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    };
}