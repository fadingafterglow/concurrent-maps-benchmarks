package ua.edu.ukma.maps;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapsFactory {

    private  ConcurrentMapsFactory() {}

    public static <K extends Comparable<K>, V> Map<K, V> createConcurrentMap(ConcurrentMapType type, int concurrencyLevel) {
        return switch (type) {
            case SYNCHRONIZED_HASH_MAP -> Collections.synchronizedMap(new HashMap<>());
            case SYNCHRONIZED_TREE_MAP -> Collections.synchronizedMap(new TreeMap<>());
            case RW_SYNCHRONIZED_HASH_MAP -> new RWSynchronizedMap<>(new HashMap<>());
            case RW_SYNCHRONIZED_TREE_MAP -> new RWSynchronizedMap<>(new TreeMap<>());
            case CONCURRENT_HASH_MAP -> new ConcurrentHashMap<>();
            case NON_BLOCKING_HASH_MAP -> new NonBlockingHashMap<>();
            case SHARDED_TREE_MAP -> new ShardedTreeMap<>(concurrencyLevel, null);
            case LOCK_FREE_CHROMATIC_MAP -> new LockFreeChromaticMap<>(0);
        };
    }
}
