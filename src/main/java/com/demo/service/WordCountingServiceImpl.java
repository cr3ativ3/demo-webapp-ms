package com.demo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class WordCountingServiceImpl implements WordCountingService {

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final Object mutex = new Object();

    @Override
    public Map<String, ? extends Number> countWords(InputStream... files) {

        Map<String, BigInteger> wordCounts = Collections.synchronizedMap(new HashMap<>());
        List<CompletableFuture<Void>> fileReaders = Arrays.stream(files).parallel()
                .map(filePath -> CompletableFuture
                        .supplyAsync(() -> tallyWordsFor(wordCounts, filePath), executor))
                .collect(Collectors.toList());

        try {
            CompletableFuture.allOf(fileReaders.toArray(new CompletableFuture<?>[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return wordCounts;
    }

    private Void tallyWordsFor(Map<String, BigInteger> wordCounts, InputStream stream) {
        try {
            new BufferedReader(new InputStreamReader(stream))
                .lines().parallel()
                .map(line -> line.split("\\s+"))
                .flatMap(Arrays::stream)
                .filter(w -> w.matches("\\w+"))
                .map(String::toLowerCase)
                .forEach(word -> {
                    synchronized(mutex) {
                        BigInteger count = wordCounts.get(word);
                        wordCounts.put(word, count == null ? BigInteger.ONE : count.add(BigInteger.ONE));
                    }
                });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}