package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ActivityCacheImpl implements ActivityCache {

    private static ActivityCacheImpl activityCache;
    public static ActivityCacheImpl getInstance() {
        if (activityCache == null) {
            activityCache = new ActivityCacheImpl();
        }
        return activityCache;
    }

    private final Map<String, Activity> nameActivityMap;
    private final Map<ActivityId, Activity> idActivityMap;

    @Deprecated
    private final Map<ActivityId, Integer> activityIdToInt;
    private int nextId;

    public ActivityCacheImpl() {
        this.nameActivityMap = new HashMap<>();
        this.idActivityMap = new HashMap<>();

        this.activityIdToInt = new HashMap<>();
    }

    @Override
    public Activity getActivity(String name) {
        Activity res = this.nameActivityMap.get(name);
        return res == null ? addActivity(name) : res;
    }

    @Override
    public Activity getActivity(ActivityId id) {
        Activity res = this.idActivityMap.get(id);
        if (res != null) {
            return res;
        }
        throw new IllegalArgumentException("No activity with id " + id + " available.");
    }

    @Override
    public int getIntForActivityId(ActivityId id) {
        int intId = this.activityIdToInt.getOrDefault(id, this.nextId);
        if (intId == this.nextId) {
            this.activityIdToInt.put(id, intId);
            nextId++;
        }
        return intId;
    }

    @Override
    public ActivityId getActivityIdForInt(Integer tr) {
        Optional<ActivityId> res = this.activityIdToInt.entrySet().stream()
                .filter(e -> e.getValue().equals(tr)).map(Map.Entry::getKey).findFirst();
        if (res.isPresent()) {
            return res.get();
        }
        throw new IllegalArgumentException("There is no activity in the activity cache for the integer " + tr);
    }

    private Activity addActivity(String name) {
        Activity act = new SimpleActivity(name, generateNewId());
        this.nameActivityMap.put(act.getName(), act);
        this.idActivityMap.put(act.getId(), act);
        return act;
    }

    private ActivityId generateNewId() {
        return new ActivityIdImpl(UUID.randomUUID());
    }

}
