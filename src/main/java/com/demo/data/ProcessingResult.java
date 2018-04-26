package com.demo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingResult {

    private List<String> messages;
    private Map<Word, Frequency> wordMap;

    public void addMessage(String msg) {
        getMessages().add(msg);
    }

    public List<String> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setWordMap(Map<Word, Frequency> wordsAndFrequencies) {
        this.wordMap = wordsAndFrequencies;
    }

    public Map<Word, Frequency> getWordMap() {
        if (wordMap == null) {
            return new HashMap<>();
        }
        return wordMap;
    }
}
