package org.processmining.placebasedlpmdiscovery.runners.serialization;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.DefaultRunnerInput;

import java.lang.reflect.Type;

public class RunnerInputAdapter implements JsonSerializer<RunnerInput>, JsonDeserializer<RunnerInput> {
    @Override
    public RunnerInput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, DefaultRunnerInput.class);
    }

    @Override
    public JsonElement serialize(RunnerInput src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        switch (src.getClass().getSimpleName()) {
            case "LPMDiscoveryRunnerInput":
                jsonObject.addProperty("class", src.getClass().getSimpleName());
        }
        return null;
    }
}
