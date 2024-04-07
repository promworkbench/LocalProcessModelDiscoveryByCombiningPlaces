package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import java.util.HashMap;
import java.util.Map;

public class GroupsInfo {

    private final Map<String, Integer> groupingProperties;

    public GroupsInfo() {
        this.groupingProperties = new HashMap<>();
    }

    public void addGroupingProperty(String groupingKey, int group) {
        this.groupingProperties.put(groupingKey, group);
    }

    public Integer getGroupingProperty(String groupingKey) {
        return this.groupingProperties.get(groupingKey);
    }
}
