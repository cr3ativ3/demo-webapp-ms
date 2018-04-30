package com.demo.data;

/**
 * An indicator of words group based on the word's first letter.
 */
public enum WordGroup implements Comparable<WordGroup>{

    A_G('a', 'g'),
    H_N('h', 'n'),
    O_U('o', 'u'),
    V_Z('v', 'z'),
    UNKNOWN;

    private char from;
    private char to;

    private WordGroup(){}

    private WordGroup(char from, char to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Resolves to which group the words falls into. If none are matched the words group will be
     * {@link WordGroup#UNKNOWN}.
     *
     * @param string the word
     * @return the word's resolved group
     */
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
        char first = string.toLowerCase().toCharArray()[0];
        return from <= first && to >= first;
    }
}
