package ua.edu.ukma.maps;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ShardedTreeMap<K, V> implements Map<K, V> {

    private static final short MAX_SHARDS = Short.MAX_VALUE;
    private static final short DEFAULT_CONCURRENCY_LEVEL = 16;

    private final ReadWriteLock[] locks;
    private final TreeMap<K, V>[] maps;
    private final int mask;

    public ShardedTreeMap() {
        this(DEFAULT_CONCURRENCY_LEVEL, null);
    }

    public ShardedTreeMap(Comparator<? super K> keyComparator) {
        this(DEFAULT_CONCURRENCY_LEVEL, keyComparator);
    }

    @SuppressWarnings("unchecked")
    public ShardedTreeMap(int concurrencyLevel, Comparator<? super K> keyComparator) {
        int shards = shardsForConcurrencyLevel(concurrencyLevel);
        this.locks = new ReadWriteLock[shards];
        this.maps = new TreeMap[shards];
        this.mask = shards - 1;
        for (int i = 0; i < shards; i++) {
            locks[i] = new ReentrantReadWriteLock();
            maps[i] = new TreeMap<>(keyComparator);
        }
    }

    private static int shardsForConcurrencyLevel(int c) {
        int n = -1 >>> Integer.numberOfLeadingZeros(c - 1);
        return (n < 0) ? 1 : (n >= MAX_SHARDS) ? MAX_SHARDS : n + 1;
    }

    private static int spread(int h) {
        return (h ^ (h >>> 16));
    }

    private int shardIndexFor(Object key) {
        return spread(key.hashCode()) & mask;
    }

    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].readLock();
            lock.lock();
            try {
                size += maps[i].size();
            } finally {
                lock.unlock();
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].readLock();
        lock.lock();
        try {
            return maps[shardIndex].containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].readLock();
            lock.lock();
            try {
                if (maps[i].containsValue(value)) {
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].readLock();
        lock.lock();
        try {
            return maps[shardIndex].get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].writeLock();
            lock.lock();
            try {
                maps[i].clear();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].readLock();
        lock.lock();
        try {
            return maps[shardIndex].getOrDefault(key, defaultValue);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].readLock();
            lock.lock();
            try {
                maps[i].forEach(action);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].writeLock();
            lock.lock();
            try {
                maps[i].replaceAll(function);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].putIfAbsent(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].remove(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].replace(key, oldValue, newValue);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].replace(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].computeIfAbsent(key, mappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].computeIfPresent(key, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].compute(key, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        int shardIndex = shardIndexFor(key);
        Lock lock = locks[shardIndex].writeLock();
        lock.lock();
        try {
            return maps[shardIndex].merge(key, value, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map<?, ?> other)) return false;
        if (other.size() != size()) return false;

        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].readLock();
            lock.lock();
            try {
                for (Entry<K, V> e : maps[i].entrySet()) {
                    K k = e.getKey();
                    V v = e.getValue();
                    Object ov = other.get(k);
                    if (!Objects.equals(v, ov)) {
                        return false;
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < maps.length; i++) {
            Lock lock = locks[i].readLock();
            lock.lock();
            try {
                h += maps[i].hashCode();
            } finally {
                lock.unlock();
            }
        }
        return h;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
