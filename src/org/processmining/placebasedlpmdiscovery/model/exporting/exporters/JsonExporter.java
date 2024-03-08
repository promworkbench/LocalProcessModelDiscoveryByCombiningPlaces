package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.serialization.AttributeSummaryAdapter;

import java.io.*;

public class JsonExporter<T> implements Exporter<T> {

    @Override
    public void export(T object, OutputStream os) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(AttributeSummary.class, new AttributeSummaryAdapter())
                .create();
        String json = gson.toJson(object);
        try (Writer writer = new OutputStreamWriter(os)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
