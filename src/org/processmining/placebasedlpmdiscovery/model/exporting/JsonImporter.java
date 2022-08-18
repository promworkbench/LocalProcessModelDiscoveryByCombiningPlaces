package org.processmining.placebasedlpmdiscovery.model.exporting;

import com.google.gson.Gson;

import java.io.*;

public class JsonImporter<T> extends AbstractImporter<T> {

    public JsonImporter(InputStream is, Class<T> tClass) {
        super(is, tClass);
    }

    @Override
    public T read() {
        Gson gson = new Gson();
        try(InputStreamReader reader = new InputStreamReader(this.is)) {
            return gson.fromJson(reader, this.tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
