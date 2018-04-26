package com.demo.service;

import java.util.Map;

import com.demo.data.Frequency;
import com.demo.data.Word;

public interface WritingService {

    void writeGroupedToFiles(Map<Word, Frequency> wordsFrequencies);

}
