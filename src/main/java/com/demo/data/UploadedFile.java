package com.demo.data;

import java.io.InputStream;

public class UploadedFile {

    private String fileName;
    private InputStream stream;

    public UploadedFile(String name, InputStream stream) {
        this.fileName = name;
        this.stream = stream;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getStream() {
        return stream;
    }
}
