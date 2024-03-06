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
        JsonObject jsonObject = json.getAsJsonObject();

        Gson gson = new Gson();
        Map<String, Object> clusteringConfig = gson.fromJson(
                jsonObject.get("clusteringConfig"),
                new TypeToken<Map<String, Object>>(){}.getType());

        return new DefaultGroupingConfig(
                jsonObject.get("identifier").getAsString(),
                ClusteringAlgorithm.valueOf(jsonObject.get("clusteringAlgorithm").getAsString()),
                clusteringConfig,
                context.deserialize(jsonObject.get("modelDistanceConfig"), ModelDistanceConfig.class));
    }
}
