package com.demo.service;

import java.io.InputStream;
import java.util.Map;

public interface WordCountingService {

    Map<String, ? extends Number> countWords(InputStream... streams);

}
