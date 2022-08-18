package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.PlaceAdditionalInfo;

import java.util.Set;

public class PlaceSet extends SerializableSet<Place> {

    private static final long serialVersionUID = 1645883969214312641L;

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
}
