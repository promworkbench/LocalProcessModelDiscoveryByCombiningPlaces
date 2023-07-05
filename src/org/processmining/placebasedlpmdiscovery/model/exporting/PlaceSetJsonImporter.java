package org.processmining.placebasedlpmdiscovery.model.exporting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlaceSetJsonImporter extends JsonImporter<PlaceSet> {

    @Override
    public PlaceSet read(InputStream is) {
        Gson gson = new GsonBuilder().create();
        try (InputStreamReader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, PlaceSet.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
