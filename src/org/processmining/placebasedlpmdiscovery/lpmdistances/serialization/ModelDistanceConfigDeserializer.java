package org.processmining.placebasedlpmdiscovery.lpmdistances.serialization;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;
import org.python.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

public class ModelDistanceConfigDeserializer implements JsonDeserializer<ModelDistanceConfig> {
    @Override
    public ModelDistanceConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        switch (jsonObject.get("distanceMethod").getAsString()) {
            case DataAttributeModelDistanceConfig.METHOD:
                DataAttributeModelDistanceConfig config = new DataAttributeModelDistanceConfig(
                        new HashSet<>(context.deserialize(jsonObject.get("attributes"),
                                new TypeToken<List<String>>(){}.getType())));
                return config;
            case ProcessModelSimilarityDistanceConfig.METHOD:
                return new ProcessModelSimilarityDistanceConfig(ProcessModelSimilarityMeasure
                        .valueOf(jsonObject.get("processModelSimilarityMeasure").getAsString()));
            case MixedModelDistanceConfig.METHOD:
                return context.deserialize(json, MixedModelDistanceConfig.class);
            case PrecomputedFromFileModelDistanceConfig.METHOD:
                return context.deserialize(json, PrecomputedFromFileModelDistanceConfig.class);
        }
        throw new JsonParseException("The distance config " + jsonObject.get("distanceMethod").getAsString() + "does not exist");
    }
}
