package org.processmining.placebasedlpmdiscovery.model.logs;

public class RemappedActivity<T> implements Activity{

    private final T mapping;

    public RemappedActivity(T mapping) {
        this.mapping = mapping;
    }

    @Override
    public String getName() {
        return this.mapping.toString();
    }
}
