package com.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.demo.data.Frequency;
import com.demo.data.Word;
import com.demo.data.WordGroup;

public class WritingServiceMockTest {

    private static final Logger log = Logger.getLogger(WritingServiceMockTest.class);

    List<Word> testWords = new ArrayList<>();

    WritingServiceImpl testObject = new WritingServiceImpl() {

        // not to actually print test data
        @Override
        protected void writeFile(WordGroup group, Map<Word, Frequency> words,
                java.util.function.Consumer<Throwable> handler) {
            log.debug("Mock writing " + words);
        };
    };

    @Before
    public void setUp() {

        testObject.setExecutor(task -> task.run());

        testWords.clear();
        testWords.add(new Word("One"));
        testWords.add(new Word("Two"));
        testWords.add(new Word("3"));
        testWords.add(new Word("Four"));
    }

    @Test
    public void writeSuccessTest() {

        Map<WordGroup, Map<Word, Frequency>> result = testObject
                .writeToFilesByGroup(Frequency.countToMap(testWords), RuntimeException::new);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey(WordGroup.A_G));
        assertTrue(result.containsKey(WordGroup.O_U));
        assertTrue(result.containsKey(WordGroup.UNKNOWN));
    }

    @Test
    public void writeEmptyOrNullTest() {
        Map<WordGroup, Map<Word, Frequency>> result = testObject.writeToFilesByGroup(null, RuntimeException::new);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        Map<Word, Frequency> emptyMap = Frequency.countToMap(new ArrayList<>());
        result = testObject.writeToFilesByGroup(emptyMap, RuntimeException::new);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
