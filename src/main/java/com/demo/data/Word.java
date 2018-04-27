package com.demo.data;

import java.util.Objects;

public class Word implements Comparable<Word> {

    private String original;
    private String text;
    private WordGroup group;
    private int hashCode;

    public Word(String original) {
        Objects.requireNonNull(original, "Word cannot be created from null");
        this.original = original;
        this.text = original.toLowerCase();
        this.group = WordGroup.resolve(this.text);
        this.hashCode = text.hashCode(); // optimization for later use in maps
    }

    public String getText() {
        return text;
    }

    public String getOriginal() {
        return original;
    }

    public WordGroup getGroup() {
        return group;
    }

    @Override
    public int compareTo(Word other) {
        return this.text.compareTo(other.text);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        // case insensitive equals
        return this == obj
                || (obj != null && obj instanceof Word && this.text.equals(((Word)obj).getText()));
    }

    @Override
    public String toString() {
        return this.text;
    }
}
