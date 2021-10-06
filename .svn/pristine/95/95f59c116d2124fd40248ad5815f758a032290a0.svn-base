package org.processmining.placebasedlpmdiscovery.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class CircularListWithMapping<T, M> {

    private final ArrayList<T> list;
    private final ArrayList<M> mappingList;
    private final int size;

    public CircularListWithMapping(int size) {
        this.list = new ArrayList<>(size);
        this.mappingList = new ArrayList<>(size);
        this.size = size;
        for (int i = 0; i < size; ++i) {
            this.list.add(null);
            this.mappingList.add(null);
        }
    }

    public void set(int pos, T element, M mapping) {
        list.set(pos % size, element);
        mappingList.set(pos % size, mapping);
    }

    public T get(int pos) {
        return list.get(pos % size);
    }

    public boolean isMapping(int pos, M mapping) {
        M posMapping = mappingList.get(pos % size);
        return posMapping != null && posMapping.equals(mapping);
    }

    public Collection<T> getAll() {
        return list.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public T getOrDefault(int pos, T defaultObj) {
        T obj = list.get(pos % size);
        return obj == null ? defaultObj : obj;
    }
}
