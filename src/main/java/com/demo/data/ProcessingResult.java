package com.demo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingResult {

    private List<String> errors;
    private Map<Word, Frequency> wordMap;

    public void addErrorMessage(String msg) {
        getErrors().add(msg);
    }

    public List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
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
