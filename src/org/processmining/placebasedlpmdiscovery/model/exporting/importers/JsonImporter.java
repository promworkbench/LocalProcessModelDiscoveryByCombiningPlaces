package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.exporting.deserializers.LPMAdditionalInfoDeserializer;
import org.processmining.placebasedlpmdiscovery.model.exporting.deserializers.LPMEvaluationResultIdDeserializer;
import org.processmining.placebasedlpmdiscovery.model.exporting.instancecreators.PairInstanceCreator;

import java.io.*;

public class JsonImporter<T> implements Importer<T> {

    @Override
    public T read(Class<T> tClass, InputStream is) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LPMAdditionalInfo.class, new LPMAdditionalInfoDeserializer())
                .registerTypeAdapter(LPMEvaluationResultId.class, new LPMEvaluationResultIdDeserializer())
                .registerTypeAdapter(Pair.class, new PairInstanceCreator());
        Gson gson = gsonBuilder.create();
        try(InputStreamReader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
