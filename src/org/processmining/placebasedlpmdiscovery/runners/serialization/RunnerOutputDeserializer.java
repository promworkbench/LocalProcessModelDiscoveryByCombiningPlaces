package org.processmining.placebasedlpmdiscovery.runners.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.processmining.placebasedlpmdiscovery.runners.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.distancerunner.DistanceRunnerOutput;

import java.lang.reflect.Type;

public class RunnerOutputDeserializer implements JsonDeserializer<RunnerOutput> {
    @Override
    public RunnerOutput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, DistanceRunnerOutput.class);
    }
}
