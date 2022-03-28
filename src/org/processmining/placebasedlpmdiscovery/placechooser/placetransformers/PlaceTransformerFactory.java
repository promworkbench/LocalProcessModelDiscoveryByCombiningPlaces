package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import java.util.Set;

public class PlaceTransformerFactory {

    PlaceTransformer createIncludedActivitiesPlaceTransformer(Set<String> activities) {
        return new IncludedActivitiesPlaceTransformer(activities);
    }
}
