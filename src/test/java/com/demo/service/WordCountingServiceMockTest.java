package com.demo.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.demo.data.Frequency;
import com.demo.data.UploadedFile;
import com.demo.data.Word;

public class WordCountingServiceMockTest {

    final long EXPECTED_COUNT = 9871;
    final BigInteger FREQUENCY_SUM = new BigInteger("667125");

    final String FILE_PATH = "src/test/resources/testbook.txt";
    final int SINGLE_FILE_ITERATIONS = 5;
    final int MULTIPLE_FILE_ITERATIONS = 1;
    final int MULTIPLE_FILE_COUNT = 3;

    ReadingServiceImpl testObject = new ReadingServiceImpl();

    @Test
    public void singleFileWordCountTest() throws IOException {
        for (int i = 0; i < SINGLE_FILE_ITERATIONS; i++) {
            System.out.println(
                    String.format("Single file iteration: %s/%s", i, SINGLE_FILE_ITERATIONS));
            countWordsAndAssert(1);
        }
    }

    @Test
    public void multipleFileWordCountTest() throws IOException {
        testObject.setExecutor(Executors.newCachedThreadPool());
        for (int i = 0; i < MULTIPLE_FILE_ITERATIONS; i++) {
            System.out.println(String.format("Multiple file (%s) iteration: %s/%s",
                    MULTIPLE_FILE_COUNT, i, MULTIPLE_FILE_ITERATIONS));
            countWordsAndAssert(MULTIPLE_FILE_COUNT);
        }
    }

    public void countWordsAndAssert(int fileCount) throws IOException {

        long start = Instant.now().toEpochMilli();

        List<Word> words = testObject.readAllWords(toUploadedFiles(fileCount), RuntimeException::new);
        Map<Word, Frequency> result = Frequency.countToMap(words);

        System.out.println(String.format("Completed in %d milliseconds", (Instant.now().toEpochMilli() - start)));

        assertEquals("Distinct words", EXPECTED_COUNT, result.keySet().size());
        assertEquals("Total words",
                FREQUENCY_SUM.multiply(new BigInteger(String.valueOf(fileCount))),
                valueSum(result));
    }

    private List<UploadedFile> toUploadedFiles(int fileCount) throws IOException {
        List<UploadedFile> streams = new ArrayList<>();
        for (int i = 0; i < fileCount; i++) {
            streams.add(new UploadedFile("file" + i, Files.newInputStream(Paths.get(FILE_PATH))));
        }
        return streams;
    }

    private BigInteger valueSum(Map<Word, Frequency> result) {
        return result.values().stream()
                .map(freq -> new BigInteger(String.valueOf(freq.longValue())))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
