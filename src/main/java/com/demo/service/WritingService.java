package com.demo.service;

import java.util.Map;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

public interface WritingService {

    Map<WordGroup, Map<Word, Frequency>> writeGroupedToFiles(Map<Word, Frequency> wordsFrequencies);

}
