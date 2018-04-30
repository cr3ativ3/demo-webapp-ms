package com.demo.service;

import java.util.Map;
import java.util.function.Consumer;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

/**
 * Service responsible for outputing words and their frequencies to separate files based on the
 * {@link Word}'s {@link WordGroup}.
 */
public interface WritingService {

    /**
     * Writes a map of words and their frequencies to separate files based on the words group.
     * Words of {@link WordGroup#UNKNOWN} are filtered out.
     *
     * @param wordsFrequencies the map of words together with their frequencies
     * @param handler the error handler
     * @return the map of words and their frequencies split based on word's group
     */
    Map<WordGroup, Map<Word, Frequency>> writeToFilesByGroup(Map<Word, Frequency> wordsFrequencies,
            Consumer<Throwable> handler);

}
