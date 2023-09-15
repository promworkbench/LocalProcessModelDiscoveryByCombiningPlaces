package org.processmining.placebasedlpmdiscovery.model.exporting.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;

import java.lang.reflect.Type;

public class LPMEvaluationResultIdDeserializer implements JsonDeserializer<LPMEvaluationResultId> {
    @Override
    public LPMEvaluationResultId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        switch (json.getAsString()) {
            case "FittingWindowsEvaluationResult":
                return StandardLPMEvaluationResultId.FittingWindowsEvaluationResult;
            case "EventCoverageEvaluationResult":
                return StandardLPMEvaluationResultId.EventCoverageEvaluationResult;
            case "PassageCoverageEvaluationResult":
                return StandardLPMEvaluationResultId.PassageCoverageEvaluationResult;
            case "PassageRepetitionEvaluationResult":
                return StandardLPMEvaluationResultId.PassageRepetitionEvaluationResult;
            case "TransitionCoverageEvaluationResult":
                return StandardLPMEvaluationResultId.TransitionCoverageEvaluationResult;
            case "TransitionOverlappingEvaluationResult":
                return StandardLPMEvaluationResultId.TransitionOverlappingEvaluationResult;
            case "TraceSupportEvaluationResult":
                return StandardLPMEvaluationResultId.TraceSupportEvaluationResult;
            default:
                throw new IllegalArgumentException("No evaluation result id exists with the following name: " + json.getAsString());
        }
    }
}
