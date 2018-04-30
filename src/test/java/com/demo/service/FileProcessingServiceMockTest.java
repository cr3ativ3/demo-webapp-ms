package com.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.demo.data.ProcessingResult;
import com.demo.data.Word;
import com.demo.data.WordGroup;

public class FileProcessingServiceMockTest {

    @Mock
    ReadingService readingServiceMock;

    @Mock
    WritingService writingServiceMock;

    FileProcessingServiceImpl testObject = new FileProcessingServiceImpl();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        testObject.setReadingService(readingServiceMock);
        testObject.setWritingService(writingServiceMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void processSuccessTest() {

        List<Word> testWords = new ArrayList<>();
        testWords.add(new Word("One"));
        testWords.add(new Word("Two"));
        testWords.add(new Word("3"));
        testWords.add(new Word("Four"));
        when(readingServiceMock.readAllWords(any(), any())).thenReturn(testWords);

        ProcessingResult result = testObject.processFiles(null);

        assertNotNull(result);
        assertNotNull(result.getWordMap());
        assertEquals(3, result.getWordMap().size());

        assertNotNull(result.getErrors());
        assertEquals(1, result.getErrors().size());
        assertEquals("Frequency map contains illegal words that will not be written: [3]",
                result.getErrors().get(0));

        @SuppressWarnings("rawtypes")
        ArgumentCaptor<Map> wordCaptor = ArgumentCaptor.forClass(Map.class);
        verify(writingServiceMock, times(1))
            .writeToFilesByGroup(wordCaptor.capture(), any());
        assertFalse(wordCaptor.getValue().containsKey(WordGroup.UNKNOWN));
    }

    @Test
    public void processEmptyOrNullTest() {
        ProcessingResult result = testObject.processFiles(null);
        assertNotNull(result);
        assertTrue(result.getWordMap().isEmpty());

        List<MultipartFile> emptyMap = new ArrayList<>();
        result = testObject.processFiles(emptyMap);

        assertNotNull(result);
        assertTrue(result.getWordMap().isEmpty());
    }
}
