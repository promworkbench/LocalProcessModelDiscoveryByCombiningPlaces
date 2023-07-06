package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import com.google.gson.Gson;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.Importer;

import java.io.*;

public class JsonImporter<T> implements Importer<T> {

    @Override
    public T read(Class<T> tClass, InputStream is) {
        Gson gson = new Gson();
        try(InputStreamReader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
