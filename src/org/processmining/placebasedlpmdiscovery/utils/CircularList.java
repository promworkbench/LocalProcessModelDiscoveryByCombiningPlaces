package org.processmining.placebasedlpmdiscovery.utils;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.exception.CircularListWrongPositionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CircularList<T> {

    private final ArrayList<Pair<Integer, T>> list;
    private final int size;

    public CircularList(int size) {
        this.list = new ArrayList<>(size);
        this.size = size;
        for (int i = 0; i < size; ++i) {
            this.list.add(null);
        }
    }

    public void set(int pos, T element) {
        list.set(pos % size, new Pair<>(pos, element));
    }

    public T get(int pos) throws CircularListWrongPositionException {
        Pair<Integer, T> pair = list.get(pos % size);
        if (pair == null)
            return null;

        if (pair.getKey() == pos)
            return pair.getValue();

        if (pair.getKey() + size == pos)
            return null;

        throw new CircularListWrongPositionException(pos, pair.getKey(), size);
    }

    public Collection<T> getAllNotNull() {
        final List<T> result = new LinkedList<>();
        for (Pair<Integer, T> t : list) {
            if (t != null) {
                result.add(t.getValue());
            }
        }
        return result;
    }

    public T getOrDefault(int pos, T defaultObj) throws CircularListWrongPositionException {
        T obj = get(pos);
        return obj == null ? defaultObj : obj;
    }

    public Collection<T> getAllUntil(int position) {
        List<T> res = new ArrayList<>();
        for (int i = Math.max(position - this.size + 1, 0); i <= position; ++i) {
            try {
                T el = this.get(i);
                if (el != null)
                    res.add(el);
            } catch (CircularListWrongPositionException ignored) {
            }
        }
        return res;
    }

    public void clear(int position) {
        list.set(position % size, null);
    }
}
