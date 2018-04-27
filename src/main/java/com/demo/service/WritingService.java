package com.demo.service;

import java.util.Map;
import java.util.function.Consumer;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

public interface WritingService {

    Map<WordGroup, Map<Word, Frequency>> writeToFilesByGroup(Map<Word, Frequency> wordsFrequencies,
            Consumer<Throwable> handler);

}
