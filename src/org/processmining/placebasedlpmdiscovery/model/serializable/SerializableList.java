package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.processmining.placebasedlpmdiscovery.utils.GeneralUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class SerializableList<T extends Serializable> extends SerializableCollection<T> {

    private static final long serialVersionUID = -1093078244499601417L;
    protected List<T> elements;
    private boolean sorted;

    public SerializableList() {
        this.elements = new ArrayList<>();
    }

    public SerializableList(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    @Override
    public boolean add(T element) {
        this.add(this.elements.size(), element);
        return true;
    }

    @Override
    public boolean contains(T element) {
        return this.elements.contains(element);
    }

    @Override
    public boolean remove(T element) {
        return this.elements.remove(element);
    }

    public T getElement(int index) {
        return this.elements.get(index);
    }

    public void add(int index, T element) {
        this.elements.add(index, element);
        sorted = false;
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public List<T> getElements() {
        return this.elements;
    }

    @Override
    public boolean addAll(Collection<T> elements) {
        return this.elements.addAll(elements);
    }

    public void sort(BiFunction<T, T, Integer> swapElementsFunction) {
        this.elements.sort(swapElementsFunction::apply);
        Collections.reverse(this.elements);
        sorted = true;
    }

    public void keep(int count) {
        if (sorted)
            this.elements = new ArrayList<>(this.elements.subList(0, Math.min(count, this.elements.size())));
        else
            throw new IllegalStateException("The list must be sorted first");
    }

    public void edit(Consumer<T> editFunction) {
        this.elements.forEach(editFunction::accept);
    }

    public T highestScoringElement(Function<T, Double> scoringFunction) {
        Optional<T> element = this.elements.stream().max(Comparator.comparingDouble(scoringFunction::apply));
        return element.orElse(null);
    }

    public SerializableSet<T> getSet() {
        return new SerializableSet<>(this.elements);
    }
}
