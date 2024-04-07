package org.processmining.placebasedlpmdiscovery.grouping.serialization;

import com.google.gson.InstanceCreator;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;

import java.lang.reflect.Type;

public class GroupingConfigInstanceCreator implements InstanceCreator<GroupingConfig> {
    @Override
    public GroupingConfig createInstance(Type type) {
        return new DefaultGroupingConfig();
    }
}
