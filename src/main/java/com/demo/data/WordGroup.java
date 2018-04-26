package com.demo.data;

public enum WordGroup {

    A_Z('a', 'z'),
    UNKNOWN;


    private char from;
    private char to;

    private WordGroup(){}

    private WordGroup(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public static WordGroup resolve(String string) {
        for (WordGroup group : values()) {
            if (group.matches(string)) {
                return group;
            }
        }
        return WordGroup.UNKNOWN;
    }

    private boolean matches(String string) {
        if (string == null || string.length() < 1) {
            return false;
        }
        char first = string.toCharArray()[0];
        return from <= first && to >= first;
    }
}
