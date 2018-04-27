package com.demo.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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

        Map<WordGroup, Map<Word, Frequency>> groupedWords = sortAndGroup(wordsFrequencies);
        if (log.isDebugEnabled()){
            log.debug(String.format("Finished grouping words into %s ", groupsAndSizes(groupedWords)));
        }

        // Check for illegal words
        if (groupedWords.containsKey(WordGroup.UNKNOWN)) {
            String errorMsg = String.format("Contains unknown words: %s",
                    groupedWords.get(WordGroup.UNKNOWN));
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // TODO trigger writing to files


        return groupedWords;
    };

    private Map<WordGroup, Integer> groupsAndSizes(Map<WordGroup, Map<Word, Frequency>> groupedWords) {
        return groupedWords.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, v -> v.getValue().size()));
    }

    private Map<WordGroup, Map<Word, Frequency>> sortAndGroup(Map<Word, Frequency> wordMap) {
        Map<WordGroup, Map<Word, Frequency>> groupedWords = new TreeMap<>();
        for(Entry<Word, Frequency> wordEntry : wordMap.entrySet()) {
            Map<Word, Frequency> groupMap = groupedWords.get(wordEntry.getKey());
            if (groupMap == null) {
                groupMap = new TreeMap<>();
                groupedWords.put(wordEntry.getKey().getGroup(), groupMap);
            }
            groupMap.put(wordEntry.getKey(), wordEntry.getValue());
        }
        return groupedWords;
    }

    @Autowired
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}