package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;
//
//import org.processmining.placebasedlpmdiscovery.model.Place;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Filters out all places whose paths are covered by other place
// */
//public class PathCoveragePlaceFilter implements PlaceFilter {
//    @Override
//    public Set<Place> filter(Set<Place> places) {
//        Set<Place> resPlaces = new HashSet<>();
//
//        for (Place place : places) { // for every place
//            boolean allCovered = true; // assume that all paths for that place are covered
//            for (String path : place.getAdditionalInfo().getPathInfo().keySet()) { // then for every path in the place
//                int pathFrequency = place.getAdditionalInfo().getPathInfo().get(path); // get the frequency of the path
//                boolean pathCovered = false; // and assume it is not covered
//
//                if (pathFrequency < 1) // if the path frequency is 0 or less
//                    continue; // just continue and ignore that path we don't need it to be covered
//
//                for (Place other : places) { // if the path is larger than 0, iterate through the other places
//                    if (place.getId().equals(other.getId())) { // we ignore the same place, since we don't want
//                        continue; // a place to be covered by itself
//                    }
//                    // if the place is not the same one, iterate through its paths
//                    for (String otherPath : other.getAdditionalInfo().getPathInfo().keySet()) {
//                        // check whether this path and the one we are trying to cover are the same
//                        // if they are, check whether this path is more frequent in the log or
//                        // if it has the same frequency check which place has less paths
//                        // (we are inclined towards places with less paths)
//                        if (path.equals(otherPath)
//                                && (pathFrequency < other.getAdditionalInfo().getPathInfo().get(otherPath)
//                                || pathFrequency == other.getAdditionalInfo().getPathInfo().get(otherPath)
//                                && place.getAdditionalInfo().getPathInfo().size() > other.getAdditionalInfo().getPathInfo().size())) {
//                            pathCovered = true; // mark the path as covered
//                            break; // and exit the loop
//                        }
//                    }
//                    if (pathCovered) // if the path is covered
//                        break; // no more other places need to be checked for this path
//                }
//
//                if (!pathCovered) { // if at least one path is not covered
//                    resPlaces.add(place); // we need to keep the place
//                    break; // no need to check the other paths if one is not covered
//                }
//            }
//        }
//
//        return resPlaces;
//    }
//}
