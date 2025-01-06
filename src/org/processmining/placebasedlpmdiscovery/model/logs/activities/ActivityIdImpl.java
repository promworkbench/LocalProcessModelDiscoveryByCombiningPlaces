package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import java.util.Objects;
import java.util.UUID;

public class ActivityIdImpl implements ActivityId {

    private final UUID id;

    public ActivityIdImpl(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityIdImpl that = (ActivityIdImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ActivityIdImpl{" +
                "id=" + id +
                '}';
    }
}
