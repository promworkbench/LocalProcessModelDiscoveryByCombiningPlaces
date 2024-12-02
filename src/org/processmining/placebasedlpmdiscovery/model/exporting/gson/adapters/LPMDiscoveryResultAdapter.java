package org.processmining.placebasedlpmdiscovery.model.exporting.gson.adapters;

import com.google.gson.*;
import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.python.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

public class LPMDiscoveryResultAdapter implements JsonSerializer<LPMDiscoveryResult>, JsonDeserializer<LPMDiscoveryResult> {
    @Override
    public LPMDiscoveryResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        LPMDiscoveryResult result = null;
        switch (jsonObject.get("attributeClass").getAsString()) {
            case "LPMResult":
                result = context.deserialize(jsonObject.get("data"), new TypeToken<LPMResult>(){}.getType());
                break;
            case "StandardLPMDiscoveryResult":
                result = context.deserialize(jsonObject.get("data"), new TypeToken<StandardLPMDiscoveryResult>(){}.getType());
                break;
            default:
                throw new NotImplementedException("The deserialization for " + typeOfT.getTypeName() + " is not " +
                        "implemented.");
        }
        return result;
    }

    @Override
    public JsonElement serialize(LPMDiscoveryResult src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attributeClass", src.getClass().getSimpleName());
        jsonObject.add("data", jsonElement);
        return jsonObject;
    }
}
