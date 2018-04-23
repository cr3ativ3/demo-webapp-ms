package com.demo.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;

import org.junit.Test;

public class WordCountingServiceMockTest {

    final long EXPECTED_COUNT = 9880;
    final BigInteger FREQUENCY_SUM = new BigInteger("667136");

    final String FILE_PATH = "src/test/resources/testbook.txt";
    final int SINGLE_FILE_ITERATIONS = 5;
    final int MULTIPLE_FILE_ITERATIONS = 5;
    final int MULTIPLE_FILE_COUNT = 7;

    WordCountingService testObject = new WordCountingServiceImpl();

    @Test
    public void singleFileWordCountTest() throws IOException {
        for (int i=0; i < SINGLE_FILE_ITERATIONS; i++) {
            System.out.print("Single file iteration: " + i);
            long start = Instant.now().toEpochMilli();
            countWordsAndAssert(1);
            long end = Instant.now().toEpochMilli();
            System.out.println(String.format("\tCompleted in %d milliseconds", (end-start)));
        }
    }

    @Test
    public void multipleFileWordCountTest() throws IOException {
        for (int i=0; i < MULTIPLE_FILE_ITERATIONS; i++) {
            System.out.print(String.format("Multiple file (%s) iteration: %s", MULTIPLE_FILE_COUNT, i));
            long start = Instant.now().toEpochMilli();
            countWordsAndAssert(MULTIPLE_FILE_COUNT);
            long end = Instant.now().toEpochMilli();
            System.out.println(String.format("\tCompleted in %d milliseconds", (end-start)));
        }
    }

    public void countWordsAndAssert(int fileCount) throws IOException {

        InputStream[] streams = new InputStream[fileCount];
        for (int i = 0; i < fileCount; i++) {
            streams[i] = Files.newInputStream(Paths.get(FILE_PATH));
        }
        Map<String, ? extends Number> result = testObject.countWords(streams);

        assertEquals("Distinct words", EXPECTED_COUNT, result.keySet().size());
        assertEquals("Total words",
                FREQUENCY_SUM.multiply(new BigInteger(String.valueOf(fileCount))),
                valueSum(result));
    }

    private BigInteger valueSum(Map<String, ? extends Number> result) {
        return result.values().stream().map(freq -> new BigInteger(String.valueOf(freq.longValue())))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
