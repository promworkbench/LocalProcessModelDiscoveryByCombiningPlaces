package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public class ImporterFactory {

    public static JsonImporter<PlaceSet> createPlaceSetJsonImporter() {
        return new JsonImporter<>();
    }
}
