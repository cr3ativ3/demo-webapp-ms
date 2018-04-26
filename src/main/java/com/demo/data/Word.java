package com.demo.data;

import java.util.Objects;

public class Word {

    private String original;
    private String text;
    private int hashCode;

    public Word(String original) {
        Objects.requireNonNull(original, "Word cannot be created from null");
        this.original = original;
        this.text = original.toLowerCase();
        this.hashCode = text.hashCode(); // optimization for later use in maps
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOriginal() {
        return original;
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
}
