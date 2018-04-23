package com.demo.service;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public interface WordCountingService {

    Map<String, LongAdder> countWords(String... files);

}
