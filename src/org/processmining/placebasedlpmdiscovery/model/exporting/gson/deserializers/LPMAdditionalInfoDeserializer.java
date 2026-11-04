package org.processmining.placebasedlpmdiscovery.model.exporting.gson.deserializers;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.*;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.GroupsInfo;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LPMAdditionalInfoDeserializer implements JsonDeserializer<LPMAdditionalInfo> {
    private static LPMCollectorResult deserializeSingleCollectorResult(JsonDeserializationContext context,
                                                                       String key,
                                                                       JsonElement evalRes) {
        // imported results are stored under the standard keys but only hold the final score and its normalized value
        if (evalRes.isJsonObject() && evalRes.getAsJsonObject().has(ImportedEvaluationResult.RESULT_FIELD_NAME)) {
            // saved by an older version without the normalized value, which can not be recovered, so it is dropped
            if (!evalRes.getAsJsonObject().has(ImportedEvaluationResult.NORMALIZED_RESULT_FIELD_NAME))
                return null;
            return context.deserialize(evalRes, ImportedEvaluationResult.class);
        }

        LPMCollectorResult lpmCollectorResult;
        switch (key) {
            case "FittingWindowsEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, FittingWindowsEvaluationResult.class);
                break;
            case "EventCoverageEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, EventCoverageEvaluationResult.class);
                break;
            case "PassageCoverageEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, PassageCoverageEvaluationResult.class);
                break;
            case "PassageRepetitionEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, PassageRepetitionEvaluationResult.class);
                break;
            case "TransitionCoverageEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, TransitionCoverageEvaluationResult.class);
                break;
            case "TransitionOverlappingEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, TransitionsOverlappingEvaluationResult.class);
                break;
            case "TraceSupportEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, TraceSupportEvaluationResult.class);
                break;
            case "LiftEvaluationResult":
                lpmCollectorResult = context.deserialize(evalRes, LiftEvaluationResult.class);
                break;
            case "EventAttributeCollectorResult":
                lpmCollectorResult = context.deserialize(evalRes, EventAttributeCollectorResult.class);
                break;
            case "CaseAttributeCollectorResult":
                lpmCollectorResult = context.deserialize(evalRes, CaseAttributeCollectorResult.class);
                break;
            default:
                throw new IllegalArgumentException("No evaluation result exists with the following name: " + key);
        }
        return lpmCollectorResult;
    }

    @Override
    public LPMAdditionalInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LPMAdditionalInfo lpmAdditionalInfo = new LPMAdditionalInfo();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject collectorResultsJson = jsonObject.get("collectorResults").getAsJsonObject();

        List<String> dropped = new ArrayList<>();
        for (Map.Entry<String, JsonElement> collRes : collectorResultsJson.entrySet()) {
            LPMCollectorResult result = deserializeSingleCollectorResult(context, collRes.getKey(), collRes.getValue());
            if (result == null)
                dropped.add(collRes.getKey());
            else
                lpmAdditionalInfo.addCollectorResult(collRes.getKey(), result);
        }
        if (!dropped.isEmpty())
            System.err.println("Imported scores without a normalized value are dropped: " + dropped);
        lpmAdditionalInfo.setGroupsInfo(context.deserialize(jsonObject.get("groupsInfo"), GroupsInfo.class));
        return lpmAdditionalInfo;
    }
}
