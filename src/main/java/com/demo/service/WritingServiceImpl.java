package com.demo.service;

import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

@Service
public class WritingServiceImpl implements WritingService {

    private static final Logger log = Logger.getLogger(WritingServiceImpl.class);

    private Executor executor;

    @Override
    public Map<WordGroup, Map<Word, Frequency>> writeGroupedToFiles(Map<Word, Frequency> wordsFrequencies) {
        // TODO Auto-generated method stub

        Map<WordGroup, Map<Word, Frequency>> groupedWords = sortAndGroup(wordsFrequencies);
        // TODO split into groups
        // TODO trigger writing to files

        return null;
    };

    private Map<WordGroup, Map<Word, Frequency>> sortAndGroup(Map<Word, Frequency> wordMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}