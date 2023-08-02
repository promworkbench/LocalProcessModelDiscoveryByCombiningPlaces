package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.PlaceAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.exporting.Exportable;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;

import java.io.OutputStream;
import java.util.Set;

public class PlaceSet extends SerializableSet<Place> implements Exportable<PlaceSet>, PlaceDiscovery {

    private static final long serialVersionUID = 1645883969214312641L;

    public PlaceSet() {

    }

    public PlaceSet(Set<Place> places) {
        super(places);
    }

    public void writePassageUsage(LEFRMatrix lefrMatrix) { // TODO: Why is this here?
        for (Place place : getElements()) {
            if (place.getAdditionalInfo() == null)
                place.setAdditionalInfo(new PlaceAdditionalInfo());
            place.getAdditionalInfo().writePassageUsage(lefrMatrix, place);
        }
    }

    @Override
    public void export(Exporter<PlaceSet> exporter, OutputStream os) {
        exporter.export(this, os);
    }

    @Override
    public PlaceDiscoveryResult getPlaces() {
        return new PlaceDiscoveryResult(this.getElements());
    }
}
