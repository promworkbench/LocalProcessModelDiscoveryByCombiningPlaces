package org.processmining.placebasedlpmdiscovery.model.exporting;

import com.google.gson.Gson;

import java.io.*;

public abstract class JsonImporter<T> implements Importer<T> {

//    @Override
//    public T read() {
//        Gson gson = new Gson();
//        try(InputStreamReader reader = new InputStreamReader(this.is)) {
//            return gson.fromJson(reader, this.tClass);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public abstract T read(String json);
}
