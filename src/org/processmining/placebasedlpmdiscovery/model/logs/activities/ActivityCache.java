package org.processmining.placebasedlpmdiscovery.model.logs.activities;

public interface ActivityCache {

    static ActivityCache getInstance() {
        return ActivityCacheImpl.getInstance();
    }

    Activity getActivity(String name);

    Activity getActivity(ActivityId id);
}
