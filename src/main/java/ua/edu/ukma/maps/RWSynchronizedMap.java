package ua.edu.ukma.maps;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RWSynchronizedMap<K,V> implements Map<K,V> {

    private final Map<K, V> m;
    private final ReadWriteLock rwLock;

    public RWSynchronizedMap(Map<K, V> m) {
        this.m = Objects.requireNonNull(m);
        this.rwLock = new ReentrantReadWriteLock();
    }

    @Override
    public int size() {
        rwLock.readLock().lock();
        try {
            return m.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        rwLock.readLock().lock();
        try {
            return m.isEmpty();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        rwLock.readLock().lock();
        try {
            return m.containsKey(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        rwLock.readLock().lock();
        try {
            return m.containsValue(value);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        rwLock.readLock().lock();
        try {
            return m.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        rwLock.writeLock().lock();
        try {
            return m.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        rwLock.writeLock().lock();
        try {
            return m.remove(key);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        rwLock.writeLock().lock();
        try {
            m.putAll(map);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        rwLock.writeLock().lock();
        try {
            m.clear();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        rwLock.readLock().lock();
        try {
            return m.equals(o);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        rwLock.readLock().lock();
        try {
            return m.hashCode();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        rwLock.readLock().lock();
        try {
            return m.toString();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public V getOrDefault(Object k, V defaultValue) {
        rwLock.readLock().lock();
        try {
            return m.getOrDefault(k, defaultValue);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        rwLock.readLock().lock();
        try {
            m.forEach(action);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        rwLock.writeLock().lock();
        try {
            m.replaceAll(function);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        rwLock.writeLock().lock();
        try {
            return m.putIfAbsent(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        rwLock.writeLock().lock();
        try {
            return m.remove(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        rwLock.writeLock().lock();
        try {
            return m.replace(key, oldValue, newValue);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        rwLock.writeLock().lock();
        try {
            return m.replace(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key,
                             Function<? super K, ? extends V> mappingFunction) {
        rwLock.writeLock().lock();
        try {
            return m.computeIfAbsent(key, mappingFunction);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfPresent(K key,
                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        rwLock.writeLock().lock();
        try {
            return m.computeIfPresent(key, remappingFunction);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V compute(K key,
                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        rwLock.writeLock().lock();
        try {
            return m.compute(key, remappingFunction);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public V merge(K key, V value,
                   BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        rwLock.writeLock().lock();
        try {
            return m.merge(key, value, remappingFunction);
        } finally {
            rwLock.writeLock().unlock();
        }
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