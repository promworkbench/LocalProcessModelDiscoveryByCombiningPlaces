package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import org.apache.commons.lang.NotImplementedException;

public class RemappedActivity<T> implements Activity{

    private final T mapping;

    public RemappedActivity(T mapping) {
        this.mapping = mapping;
    }

    @Override
    public String getName() {
        return this.mapping.toString();
    }

    @Override
    public ActivityId getId() {
        throw new NotImplementedException("This class should be deprecated.");
    }
}
