package com.demo.service;

import java.util.List;
import java.util.function.Consumer;

import com.demo.data.UploadedFile;
import com.demo.data.Word;

/**
 * Service responsible for reading words from the given uploaded files.
 */
public interface ReadingService {

    /**
     * Reads list of uploaded files and outputs a list of English words found in all files. Performs
     * reading of all files in parallel. If there is only one file in the list reads it on the
     * current thread.
     *
     * @param files the list of files
     * @param handler the exception handler
     * @return a list of {@link Word}s
     */
    List<Word> readAllWords(List<UploadedFile> files, Consumer<Throwable> handler);
}
