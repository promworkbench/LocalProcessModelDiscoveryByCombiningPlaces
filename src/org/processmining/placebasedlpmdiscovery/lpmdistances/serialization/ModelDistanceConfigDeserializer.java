package org.processmining.placebasedlpmdiscovery.lpmdistances.serialization;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import java.lang.reflect.Type;

public class ModelDistanceConfigDeserializer implements JsonDeserializer<ModelDistanceConfig> {
    @Override
    public ModelDistanceConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        switch (jsonObject.get("distanceMethod").getAsString()) {
            case DataAttributeModelDistanceConfig.METHOD:
                return new DataAttributeModelDistanceConfig();
            case ProcessModelSimilarityDistanceConfig.METHOD:
                return new ProcessModelSimilarityDistanceConfig(ProcessModelSimilarityMeasure
                        .valueOf(jsonObject.get("processModelSimilarityMeasure").getAsString()));
            case MixedModelDistanceConfig.METHOD:
                return new MixedModelDistanceConfig();
            case PrecomputedFromFileModelDistanceConfig.METHOD:
                return context.deserialize(json, PrecomputedFromFileModelDistanceConfig.class);
        }
        throw new JsonParseException("The distance config " + jsonObject.get("distanceMethod").getAsString() + "does not exist");
    }
}
