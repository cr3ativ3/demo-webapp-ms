package com.demo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingDetails {

    private List<String> messages;
    private Map<String, ? extends Number> sortedWords;

    public void addMessage(String msg) {
        getMessages().add(msg);
    }

    public List<String> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setSortedWords(Map<String, ? extends Number> sortedWords) {
        this.sortedWords = sortedWords;
    }

    public Map<String, ? extends Number> getSortedWords() {
        if (sortedWords == null) {
            return new HashMap<>();
        }
        return sortedWords;
    }
}
