//package org.processmining.placebasedlpmdiscovery.plugins.exports;
//
//import com.google.gson.*;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
//
//import java.lang.reflect.Type;
//
//public class SimpleEvaluationResultDeserializer implements JsonDeserializer<SimpleEvaluationResult> {
//    @Override
//    public SimpleEvaluationResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        LPMEvaluationResultId resultId = context.deserialize(jsonObject.get("id"), LPMEvaluationResultId.class);
//        if (resultId == LPMEvaluationResultId.FittingWindowsEvaluationResult) {
//            return new FittingWindowsEvaluationResult()
//        }
//        return null;
//    }
//}
