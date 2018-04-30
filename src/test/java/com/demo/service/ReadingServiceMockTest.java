package com.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.demo.data.Frequency;
import com.demo.data.UploadedFile;
import com.demo.data.Word;

public class ReadingServiceMockTest {

    private static final Logger log = Logger.getLogger(ReadingServiceMockTest.class);

    final long EXPECTED_COUNT = 9871;
    final BigInteger FREQUENCY_SUM = new BigInteger("667125");

    final String FILE_PATH = "src/test/resources/testbook.txt";
    final int SINGLE_FILE_ITERATIONS = 4;
    final int MULTIPLE_FILE_ITERATIONS = 2;
    final int MULTIPLE_FILE_COUNT = 2;

    ReadingServiceImpl testObject = new ReadingServiceImpl();

    @Test
    public void singleFileWordCountTest() throws IOException {
        for (int i = 0; i < SINGLE_FILE_ITERATIONS; i++) {
            log.debug(String.format("Single file iteration: %s/%s", i, SINGLE_FILE_ITERATIONS));
            countWordsAndAssert(1);
        }
    }

    @Test
    public void multipleFileWordCountTest() throws IOException {
        testObject.setExecutor(Executors.newCachedThreadPool());
        for (int i = 0; i < MULTIPLE_FILE_ITERATIONS; i++) {
            log.debug(String.format("Multiple file (%s) iteration: %s/%s",
                    MULTIPLE_FILE_COUNT, i, MULTIPLE_FILE_ITERATIONS));
            countWordsAndAssert(MULTIPLE_FILE_COUNT);
        }
    }

    @Test
    public void emptyOrNullFileTest() {
        List<Word> result = testObject.readAllWords(null, RuntimeException::new);
        assertNotNull("Should not be null", result);
        assertTrue(result.isEmpty());

        result = testObject.readAllWords(new ArrayList<>(), RuntimeException::new);
        assertNotNull("Should not be null", result);
        assertTrue(result.isEmpty());
    }

    public void countWordsAndAssert(int fileCount) throws IOException {

        long start = Instant.now().toEpochMilli();

        List<Word> words = testObject.readAllWords(toUploadedFiles(fileCount), RuntimeException::new);
        Map<Word, Frequency> result = Frequency.countToMap(words);

        log.debug(String.format("Completed in %d milliseconds", (Instant.now().toEpochMilli() - start)));

        assertEquals("Distinct words", EXPECTED_COUNT, result.keySet().size());
        assertEquals("Total words",
                FREQUENCY_SUM.multiply(new BigInteger(String.valueOf(fileCount))),
                valueSum(result));
    }

    private List<UploadedFile> toUploadedFiles(int fileCount) throws IOException {
        List<UploadedFile> streams = new ArrayList<>();
        for (int i = 0; i < fileCount; i++) {
            streams.add(new UploadedFile(String.format("testfile-%d.txt", i),
                    Files.newInputStream(Paths.get(FILE_PATH))));
        }
        return streams;
    }

    private BigInteger valueSum(Map<Word, Frequency> result) {
        return result.values().stream()
                .map(freq -> new BigInteger(String.valueOf(freq.longValue())))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
