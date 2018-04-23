package com.demo.service;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;

@Service
public class WordCountingServiceImpl implements WordCountingService{

    @Override
    public Map<String, LongAdder> countWords(String... files) {
        // TODO Auto-generated method stub
        return null;
    }
}