package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;
//
//import org.processmining.placebasedlpmdiscovery.model.Place;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class SubPlaceFilter implements PlaceFilter {
//    @Override
//    public Set<Place> filter(Set<Place> places) {
//        Set<Place> resPlaces = new HashSet<>(); // create the resulting set
//
//        for (Place queryPlace : places) { // for every place
//            boolean isContained = false; // assume that is not contained in any other place
//            for (Place place : places) { // then iterate again through all the lpms
//                // and check first that we are not querying the same place
//                if (queryPlace.equals(place))
//                    continue;
//                // if this is not the case, check whether the place is duplicate of the query place
//                if (queryPlace.getInputTransitions().equals(place.getInputTransitions())
//                        && queryPlace.getOutputTransitions().equals(place.getInputTransitions())) { // if they have the same set of places
//                    if (resPlaces.contains(place)) { // and the other place is already included in the resulting set
//                        isContained = true; // mark the place as contained
//                        break; // and stop the iteration
//                    }
//                } else if (place.containsLPM(queryPlace)) { // otherwise check whether the place is containing the query place
//                    isContained = true; // mark the query place as contained
//                    break; // stop the iteration since it is enough to be contained in only one place
//                }
//            }
//
//            if (!isContained) // if the lpm is not contained in any other lpm
//                resPlaces.add(queryPlace); // add it in the resulting set
//        }
//
//        return resPlaces; // return all lpms that are not contained in others
//    }
//}
