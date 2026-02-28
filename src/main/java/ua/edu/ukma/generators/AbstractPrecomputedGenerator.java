package ua.edu.ukma.generators;

public abstract class AbstractPrecomputedGenerator<E> implements Generator<E> {

    private final E[] elements;
    private int idx;

    public AbstractPrecomputedGenerator(E[] elements) {
        this.elements = elements;
    }

    @Override
    public E next() {
        E key = elements[idx++];
        if (idx == elements.length) idx = 0;
        return key;
    }
}
