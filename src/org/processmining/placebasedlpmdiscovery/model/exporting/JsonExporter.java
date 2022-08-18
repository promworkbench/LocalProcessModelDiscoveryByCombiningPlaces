package org.processmining.placebasedlpmdiscovery.model.exporting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonExporter<T> extends AbstractExporter<T> {
    public JsonExporter(File file) {
        super(file);
    }

    @Override
    public void export(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        try (FileWriter writer = new FileWriter(this.file)) {
            writer.write(json);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
