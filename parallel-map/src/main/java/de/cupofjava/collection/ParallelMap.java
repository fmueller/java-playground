package de.cupofjava.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO add javadoc
 * 
 * @author Felix Müller
 * 
 * @param <K> type of keys
 * @param <V> type of values
 */
public class ParallelMap<K, V> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Map<K, V> map;

    public ParallelMap() {
        this(new HashMap<K, V>());
    }

    /**
     * For testing purpose: to inject manipulated map.
     */
    protected ParallelMap(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this.map = map;
    }

    public V get(K key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public V put(K key, V value) {
        writeLock.lock();
        try {
            return map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public Map<K, V> asMap() {
        readLock.lock();
        try {
            return new HashMap<K, V>(map);
        } finally {
            readLock.unlock();
        }
    }
}