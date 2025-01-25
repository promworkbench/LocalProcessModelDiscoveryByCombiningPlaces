package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ImporterFactory;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class FromFilePlacesProvider implements PlacesProvider {

    private final String fileName;

    public FromFilePlacesProvider(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Set<Place> from(XLog log) {
        try {
            if (this.fileName.endsWith("pnml")) {
                Petrinet net = PlaceUtils.extractPetriNet(fileName);
                return PlaceUtils.getPlacesFromPetriNet(net);
            } else {
                JsonImporter<PlaceSet> importer = ImporterFactory.createPlaceSetJsonImporter();
                return importer.read(PlaceSet.class, Files.newInputStream(Paths.get(fileName))).getElements();
            }
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
