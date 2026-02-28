package ua.edu.ukma.generators;

public abstract class AbstractPrecomputedKeyGenerator<K> implements KeyGenerator<K> {

    private final K[] keys;
    private int idx;

    public AbstractPrecomputedKeyGenerator(K[] keys) {
        this.keys = keys;
    }

    @Override
    public K nextKey() {
        K key = keys[idx++];
        if (idx == keys.length) idx = 0;
        return key;
    }
}
