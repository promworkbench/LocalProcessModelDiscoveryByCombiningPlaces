package org.processmining.placebasedlpmdiscovery.model.logs.activities;

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

}
