package org.processmining.placebasedlpmdiscovery.runners.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.processmining.placebasedlpmdiscovery.runners.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.distancerunner.DistanceRunnerInput;

import java.lang.reflect.Type;

public class RunnerInputDeserializer implements JsonDeserializer<RunnerInput> {
    @Override
    public RunnerInput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, DistanceRunnerInput.class);
    }
}
