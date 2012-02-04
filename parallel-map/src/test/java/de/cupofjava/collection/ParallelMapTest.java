package de.cupofjava.collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class ParallelMapTest {

    private ParallelMap<String, String> parallelMap;

    @Before
    public void setUp() {
        parallelMap = new ParallelMap<String, String>();
    }

    @Test
    public void shouldBePossibleToAddAnEntry() {
        parallelMap.put("A", "valueOfA");
        assertThat(parallelMap.get("A"), is("valueOfA"));
    }

    @Test
    public void shouldBePossibleToAddManyEntriesAndRetrieveCorrectValueForGivenKey() {
        parallelMap.put("A", "valueOfA");
        parallelMap.put("B", "valueOfB");
        parallelMap.put("C", "valueOfC");

        assertThat(parallelMap.get("A"), is("valueOfA"));
        assertThat(parallelMap.get("B"), is("valueOfB"));
        assertThat(parallelMap.get("C"), is("valueOfC"));
    }

    @Test(timeout = 5000)
    public void shouldLockOnWritingEntriesSoThatReadOperationsHaveToWaitForFinishingOfWriteOperation() throws InterruptedException {
        final int numberOfReaderThreads = 10;
        final long waitForStartWriting = 1000L;

        final Map<String, String> map= new HashMap<String, String>();
        map.put("A", "valueOfA");
        map.put("B", "valueOfB");
        map.put("C", "valueOfC");

        parallelMap = new ParallelMap<String, String>(
                new MapWithSlowedPutOperation<String, String>(map, waitForStartWriting));

        final Map<String, String> newMap = new HashMap<String, String>();
        newMap.put("A", "newValueOfA");
        newMap.put("B", "newValueOfB");
        newMap.put("C", "newValueOfC");
        newMap.put("D", "valueOfD");

        for (final Map.Entry<String, String> entry : newMap.entrySet()) {

            final ExecutorService writerExecutor = startWriter(entry);
            final ScheduledExecutorService readerExecutor = Executors.newScheduledThreadPool(numberOfReaderThreads);

            /*
             * TODO explain this
             */
            final List<String> readValues = new ArrayList<String>();

            for (int i = 0; i < numberOfReaderThreads; i++) {
                scheduleReader(entry, readerExecutor, readValues, i);
            }

            readerExecutor.shutdown();
            writerExecutor.shutdown();
            readerExecutor.awaitTermination(2, TimeUnit.SECONDS);

            for (String value : readValues) {                
                assertThat(value, is(entry.getValue()));
            }
        }
    }

    private ExecutorService startWriter(final Map.Entry<String, String> entry) {
        final ExecutorService writerExecutor = Executors.newSingleThreadExecutor();
        writerExecutor.execute(new Runnable() {

            public void run() {
                parallelMap.put(entry.getKey(), entry.getValue());
            }
        });
        return writerExecutor;
    }

    private void scheduleReader(final Map.Entry<String, String> entry,
            final ScheduledExecutorService readerExecutor,
            final List<String> readValues,
            int executionDelayInMilliseconds) {

        readerExecutor.schedule(new Runnable() {

            public void run() {
                final String readValue = parallelMap.get(entry.getKey());
                readValues.add(readValue);
            }
        }, executionDelayInMilliseconds, TimeUnit.MILLISECONDS);
    }
}