package com.demo.service;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

/**
 * Basic implementation of a {@link ReadingService}.
 */
@Service
public class WritingServiceImpl implements WritingService {

    private static final Logger log = Logger.getLogger(WritingServiceImpl.class);

    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final Object FILE_EXTENSION = ".txt";

    @Value("${com.demo.app.output.dir}")
    private String fileOutputPath;

    private Executor executor;

    @Override
    public Map<WordGroup, Map<Word, Frequency>> writeToFilesByGroup(
            Map<Word, Frequency> wordsFrequencies, Consumer<Throwable> errorHandler) {

        Map<WordGroup, Map<Word, Frequency>> groupedWords = sortAndGroup(wordsFrequencies);
        if (log.isDebugEnabled()){
            log.debug(String.format("Finished grouping words into %s ", groupsAndSizes(groupedWords)));
        }

        // Start writing in the background
        for (Entry<WordGroup, Map<Word, Frequency>> groupEntry : groupedWords.entrySet()) {
            CompletableFuture.supplyAsync(() -> {
                writeFile(groupEntry.getKey(), groupEntry.getValue(), errorHandler);
                return null;
            }, executor);
        }
        return groupedWords;
    }

    /**
     * Sorts words and their frequencies and groups them under a common {@link WordGroup}.
     *
     * @param wordMap the map of words and their frequencies
     * @return a map of word groups, each containing a map of words and their frequencies
     */
    protected Map<WordGroup, Map<Word, Frequency>> sortAndGroup(Map<Word, Frequency> wordMap) {
        Map<WordGroup, Map<Word, Frequency>> groupedWords = new TreeMap<>();
        if (wordMap == null || wordMap.isEmpty()) {
            return groupedWords;
        }
        for(Entry<Word, Frequency> wordEntry : wordMap.entrySet()) {
            Map<Word, Frequency> groupMap = groupedWords.get(wordEntry.getKey().getGroup());
            if (groupMap == null) {
                groupMap = new TreeMap<>();
                groupedWords.put(wordEntry.getKey().getGroup(), groupMap);
            }
            groupMap.put(wordEntry.getKey(), wordEntry.getValue());
        }
        return groupedWords;
    }

    /**
     * Performs the writing of a group of words to a file.
     *
     * @param group the current group to print out
     * @param words the words of this group
     * @param handler the error handler
     */
    protected void writeFile(WordGroup group, Map<Word, Frequency> words,
            Consumer<Throwable> handler) {
        try {
            File outputFile = getFileForGroup(group);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            Files.write(outputFile.toPath(), toByteArray(words), WRITE, APPEND, CREATE);
        }
        catch (IOException ex) {
            String msg = "Error writing to file: %s";
            log.error(String.format(msg, ex.getMessage()), ex);
            handler.accept(new Throwable(String.format(msg, asFileName(group)), ex));
        } finally {
            log.debug("Finished writing to " + asFileName(group));
        }
    }

    private byte[] toByteArray(Map<Word, Frequency> valueMap) {
        return new TreeMap<>(valueMap).entrySet().stream()
                .map(e -> String.format("%s:%s\r\n", e.getKey(), e.getValue().longValue()))
                .collect(Collectors.joining())
                .getBytes();
    }

    private File getFileForGroup(WordGroup group) {
        String path = new File(new StringBuilder(ROOT_PATH)
                .append(File.separator).append(fileOutputPath)
                .append(File.separator).toString()).getAbsolutePath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file, asFileName(group));
    }

    private String asFileName(WordGroup group) {
        return new StringBuilder(group.name()).append(FILE_EXTENSION).toString();
    }

    private Map<WordGroup, Integer> groupsAndSizes(Map<WordGroup, Map<Word, Frequency>> groupedWords) {
        return groupedWords.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, v -> v.getValue().size()));
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}