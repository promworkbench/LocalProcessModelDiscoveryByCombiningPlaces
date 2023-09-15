package org.processmining.placebasedlpmdiscovery.model.exporting.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.*;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LPMAdditionalInfoDeserializer implements JsonDeserializer<LPMAdditionalInfo> {
    private static LPMEvaluationResult deserializeSingleEvaluationResult(JsonDeserializationContext context,
                                                                         String key,
                                                                         JsonElement evalRes) {
        LPMEvaluationResult lpmEvaluationResult;
        switch (key) {
            case "FittingWindowsEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, FittingWindowsEvaluationResult.class);
                break;
            case "EventCoverageEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, EventCoverageEvaluationResult.class);
                break;
            case "PassageCoverageEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, PassageCoverageEvaluationResult.class);
                break;
            case "PassageRepetitionEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, PassageRepetitionEvaluationResult.class);
                break;
            case "TransitionCoverageEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, TransitionCoverageEvaluationResult.class);
                break;
            case "TransitionOverlappingEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, TransitionsOverlappingEvaluationResult.class);
                break;
            case "TraceSupportEvaluationResult":
                lpmEvaluationResult = context.deserialize(evalRes, TraceSupportEvaluationResult.class);
                break;
            default:
                throw new IllegalArgumentException("No evaluation result exists with the following name: " + key);
        }
        return lpmEvaluationResult;
    }

    @Override
    public LPMAdditionalInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LPMAdditionalInfo lpmAdditionalInfo = new LPMAdditionalInfo();
        try {
            for (Map.Entry<String, JsonElement> kv : json.getAsJsonObject().entrySet()) {
                String name = kv.getKey();
                if (name.equals("evalResults")) {
                    Map<String, LPMEvaluationResult> resultMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> evalRes : kv.getValue().getAsJsonObject().entrySet()) {
                        LPMEvaluationResult evalResult = deserializeSingleEvaluationResult(context,evalRes.getKey(), evalRes.getValue());
                        resultMap.put(evalRes.getKey(), evalResult);
                    }

                    Field f = LPMAdditionalInfo.class.getDeclaredField(name);
                    f.setAccessible(true);
                    f.set(lpmAdditionalInfo, resultMap);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return lpmAdditionalInfo;
    }
}
