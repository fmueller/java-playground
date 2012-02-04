package de.cupofjava.collection;

import java.util.HashMap;
import java.util.Map;

public class MapWithSlowedPutOperation<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 2053940291870020777L;

    private final long waitForStartWriting;

    private boolean isInitialization = true;

    public MapWithSlowedPutOperation(Map<K, V> map, long waitForStartWriting) {
        this.waitForStartWriting = waitForStartWriting;
        putAll(map);
        isInitialization = false;
    }

    @Override
    public V put(K key, V value) {
        if (!isInitialization) {
            try {
                Thread.sleep(waitForStartWriting);
            } catch (InterruptedException ie) {
                throw new RuntimeException(
                        "waiting for start write operation was interrupted", ie);
            }
        }

        return super.put(key, value);
    }
}