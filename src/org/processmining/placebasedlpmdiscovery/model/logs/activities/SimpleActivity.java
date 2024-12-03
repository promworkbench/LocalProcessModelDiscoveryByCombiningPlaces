package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import java.util.Objects;

public class SimpleActivity implements Activity{

    private final String name;
    private final ActivityId activityId;

    public SimpleActivity(String name, ActivityId activityId) {
        this.name = name;
        this.activityId = activityId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ActivityId getId() {
        return this.activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleActivity that = (SimpleActivity) o;
        return Objects.equals(name, that.name) && Objects.equals(activityId, that.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId);
    }
}
