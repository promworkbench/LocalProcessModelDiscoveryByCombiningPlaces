package org.processmining.placebasedlpmdiscovery.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class CircularListWithMapping<T, M> {

    private final ArrayList<Integer> originalPos;
    private final ArrayList<T> list;
    private final ArrayList<M> mappingList;
    private final int size;

    public CircularListWithMapping(int size) {
        this.originalPos = new ArrayList<>(size);
        this.list = new ArrayList<>(size);
        this.mappingList = new ArrayList<>(size);
        this.size = size;
        for (int i = 0; i < size; ++i) {
            this.list.add(null);
            this.mappingList.add(null);
            this.originalPos.add(null);
        }
    }

    public void set(int pos, T element, M mapping) {
        originalPos.set(pos % size, pos);
        list.set(pos % size, element);
        mappingList.set(pos % size, mapping);
    }

    public boolean isPosOld(int pos) { // TODO: Maybe this can be improved - one is keeping only the smallest pos
        return originalPos.get(pos % size) != null && originalPos.get(pos % size) != pos;
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

    @Override
    public String toString() {
        return "CircularListWithMapping{" +
                "originalPos=" + originalPos +
                ", list=" + list +
                ", mappingList=" + mappingList +
                ", size=" + size +
                '}';
    }
}
