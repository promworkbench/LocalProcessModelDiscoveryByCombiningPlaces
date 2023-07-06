package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public class ExporterFactory {

    public static Exporter<PlaceSet> createPlaceSetJsonExporter() {
        return new JsonExporter<>();
    }
}
