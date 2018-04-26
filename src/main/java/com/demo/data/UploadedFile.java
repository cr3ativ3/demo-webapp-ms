package com.demo.data;

import java.io.InputStream;

public class UploadedFile {

    private String fileName;
    private InputStream inputStream;

    public UploadedFile(String name, InputStream stream) {
        this.fileName = name;
        this.inputStream = stream;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
