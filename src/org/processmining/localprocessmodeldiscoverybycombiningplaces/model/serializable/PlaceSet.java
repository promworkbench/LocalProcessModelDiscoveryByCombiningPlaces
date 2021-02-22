package org.processmining.localprocessmodeldiscoverybycombiningplaces.model.serializable;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LEFRMatrix;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.additionalinfo.PlaceAdditionalInfo;

import java.util.Set;

public class PlaceSet extends SerializableSet<Place> {

    private static final long serialVersionUID = 1645883969214312641L;

    public PlaceSet(Set<Place> places) {
        super(places);
    }

    public void writePassageUsage(LEFRMatrix lefrMatrix) {
        for (Place place : getElements()) {
            if (place.getAdditionalInfo() == null)
                place.setAdditionalInfo(new PlaceAdditionalInfo(place));
            place.getAdditionalInfo().writePassageUsage(lefrMatrix);
        }
    }
}
