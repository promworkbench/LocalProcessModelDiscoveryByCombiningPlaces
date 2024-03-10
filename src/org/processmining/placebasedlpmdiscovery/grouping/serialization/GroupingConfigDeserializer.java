package org.processmining.placebasedlpmdiscovery.grouping.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;

import java.lang.reflect.Type;

public class GroupingConfigDeserializer implements JsonDeserializer<GroupingConfig> {
    @Override
    public GroupingConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, DefaultGroupingConfig.class);
    }
}
