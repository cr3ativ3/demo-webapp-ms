package com.demo.service;

import java.util.List;
import java.util.function.Consumer;

import com.demo.data.UploadedFile;
import com.demo.data.Word;

public interface ReadingService {

    List<Word> readAllWords(List<UploadedFile> uploadedFiles, Consumer<Throwable> handler);

}
