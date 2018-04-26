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
import java.util.function.BiFunction;
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
    public List<Word> readAllWords(List<UploadedFile> uploadedFiles, Consumer<Throwable> handler) {

        // Create futures that are reading from files
        List<CompletableFuture<List<Word>>> fileReaders = uploadedFiles.stream()
                .map(file -> CompletableFuture
                        .supplyAsync(() -> readInputStream(file.getInputStream()), getExecutor(uploadedFiles)))
                .map(f -> f.handle(with(handler)))
                .collect(Collectors.toList());

        // Merge words lists from all complete futures
        List<Word> words = CompletableFuture.allOf(fileReaders.toArray(new CompletableFuture[0]))
                .thenApply(combined -> {
                    log.info("Finished reading all files");
                    return fileReaders.stream().flatMap(reader -> reader.join().stream())
                            .collect(Collectors.toList());
                }).join(); // let's wait to finish
        return words;
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

    private BiFunction<List<Word>, Throwable, List<Word>> with(Consumer<Throwable> handler) {
        return (result, t) -> {
            log.info("Reading process finished", t);
            if (t != null){
                log.error("Error while reading from file", t);
                handler.accept(t);
            }
            return result == null ? new ArrayList<Word>() : result;
        };
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    };
}