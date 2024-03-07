package org.processmining.placebasedlpmdiscovery.grouping.serialization;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.python.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class GroupingConfigDeserializer implements JsonDeserializer<GroupingConfig> {
    @Override
    public GroupingConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, DefaultGroupingConfig.class);
    }
}
