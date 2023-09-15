package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public class ExporterFactory {

    public static Exporter<PlaceSet> createPlaceSetJsonExporter() {
        return new JsonExporter<>();
    }

    public static Exporter<LPMDiscoveryResult> createLPMDiscoveryResultExporter() {
        return new JsonExporter<>();
    }
}
