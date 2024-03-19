package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.serialization.AttributeSummaryAdapter;
import org.python.google.common.reflect.TypeToken;

import java.io.*;

public class JsonExporter<T> implements Exporter<T> {

    @Override
    public void export(T object, OutputStream os) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(AttributeSummary.class, new AttributeSummaryAdapter())
                .create();
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(os))) {
            gson.toJson(object, object.getClass(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
